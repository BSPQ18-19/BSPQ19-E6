package com.spq.group6.client.controller;

import com.spq.group6.client.remote.ServiceLocator;
import com.spq.group6.server.dao.BiddingDAO;
import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.Bid;
import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;
import com.spq.group6.server.remote.IServer;
import com.spq.group6.server.remote.Server;
import com.spq.group6.server.utils.logger.ServerLogger;
import org.junit.*;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class ClientControllerTest {
    private static Thread rmiRegistryThread = null;
    private static Thread rmiServerThread = null;

    private static ClientController clientController;
    private static BiddingDAO biddingDao;
    private static User seller, buyer;
    private static Product product, product2;
    private static Auction auction, privateAuction;
    private static Bid bid;

    @BeforeClass
    static public void setUpClass() {
        // Launch the RMI registry
        class RMIRegistryRunnable implements Runnable {

            public void run() {
                try {
                    java.rmi.registry.LocateRegistry.createRegistry(1099);
                    System.out.println("RMI registry ready.");
                } catch (Exception e) {
                    System.out.println("RMI registry already started");
                }
            }
        }

        rmiRegistryThread = new Thread(new RMIRegistryRunnable());
        rmiRegistryThread.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }

        String ip = "127.0.0.1", port = "1099", name = "BiddingItServer";
        String serverName = "//" + ip + ":" + port + "/" + name;
        class RMIServerRunnable implements Runnable {

            public void run() {

                try {
                    IServer objServer = new Server();
                    Naming.rebind(serverName, objServer);
                    ServerLogger.logger.info("BiddingIt active and listening...");
                } catch (Exception e) {
                    ServerLogger.logger.error("Exception ocurred: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        rmiServerThread = new Thread(new RMIServerRunnable());
        rmiServerThread.start();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }

        ServiceLocator.getServiceLocator().setService(ip, port, name);
    }

    @AfterClass
    static public void tearDownClass() {
        try {
            rmiServerThread.join();
            rmiRegistryThread.join();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }

    @Before
    public void setUp() throws RemoteException {
        clientController = ClientController.getNewClientController();
        seller = new User("test_seller", "test_pass", "uk");
        buyer = new User("test_buyer", "test_pass", "uk");
        product = new Product("test_product", "test_description");
        product2 = new Product("test_product2", "test_description2");
        biddingDao = new BiddingDAO();
        int offset = 10;
        Timestamp limit = new Timestamp(System.currentTimeMillis() + offset * 1000);
        auction = new Auction(seller, product, limit, 12, null);
        privateAuction = new Auction(seller, product2, limit, 14, "pass1");
        bid = new Bid(buyer, auction.getInitialPrice() + 1);
    }

    @Test
    public void signInTest() throws RemoteException {
        assertTrue(clientController.signIn(seller.getUsername(), seller.getPassword(), seller.getCountry()));
        assertEquals(seller, clientController.getCurrentUser());
        assertFalse(clientController.signIn(seller.getUsername(), seller.getPassword(), seller.getCountry()));
    }

    @Test
    public void logInInTest() throws RemoteException {
        assertFalse(clientController.logIn(seller.getUsername(), seller.getPassword()));
        // Creates a new seller for trying to login
        assertTrue(clientController.signIn(seller.getUsername(), seller.getPassword(), seller.getCountry()));

        assertTrue(clientController.logIn(seller.getUsername(), seller.getPassword()));
        assertEquals(seller, clientController.getCurrentUser());
    }

    @Test
    public void logOutInTest() throws RemoteException {
        assertNull(clientController.getCurrentUser());
        // Creates a new seller and logs in
        assertTrue(clientController.signIn(seller.getUsername(), seller.getPassword(), seller.getCountry()));
        assertTrue(clientController.logIn(seller.getUsername(), seller.getPassword()));
        User user = clientController.getCurrentUser(); // We save it for deleting it later
        assertNotNull(user);

        clientController.logOut();
        assertNull(clientController.getCurrentUser());
        biddingDao.deleteUser(user);
    }

    @Test
    public void createProductTest() throws RemoteException {
        assertTrue(clientController.signIn(seller.getUsername(), seller.getPassword(), seller.getCountry()));
        assertTrue(clientController.createProduct(product.getName(), product.getDescription()));

        assertEquals(1, clientController.getCurrentUser().getOwnedProducts().size());
        Product createdProduct = clientController.getCurrentUser().getOwnedProducts().get(0);
        assertEquals(product, createdProduct);
    }

    @Test
    public void updateProductTest() throws RemoteException {
        assertTrue(clientController.signIn(seller.getUsername(), seller.getPassword(), seller.getCountry()));
        assertTrue(clientController.createProduct(product.getName(), product.getDescription()));
        product = clientController.getCurrentUser().getOwnedProducts().get(0);

        String newName = "new_name", newDescription = "new_description";
        assertTrue(clientController.updateProduct(product, newName, newDescription));

        Product createdProduct = clientController.getCurrentUser().getOwnedProducts().get(0);
        assertEquals(newName, createdProduct.getName());
        assertEquals(newDescription, createdProduct.getDescription());
    }

    @Test
    public void deleteProductTest() throws RemoteException {
        assertTrue(clientController.signIn(seller.getUsername(), seller.getPassword(), seller.getCountry()));
        assertTrue(clientController.createProduct(product.getName(), product.getDescription()));
        product = clientController.getCurrentUser().getOwnedProducts().get(0);

        assertTrue(clientController.deleteProduct(product));
    }

    @Test
    public void createPublicAuctionTest() throws RemoteException {
        assertTrue(clientController.signIn(seller.getUsername(), seller.getPassword(), seller.getCountry()));
        seller = clientController.getCurrentUser();

        assertTrue(clientController.createPublicAuction(auction.getProduct(), auction.getDayLimit(), auction.getInitialPrice()));
        assertTrue(clientController.signIn(buyer.getUsername(), buyer.getPassword(), buyer.getCountry()));
        ArrayList<Auction> auctions = clientController.searchAuctionByProductName(product.getName());
        assertEquals(1, auctions.size());
        auction.setAuctionID(auctions.get(0).getAuctionID());
        assertEquals(auction, auctions.get(0));
        // Clean up
        biddingDao.deleteUser(seller);
    }

    @Test
    public void createPrivateAuctionTest() throws RemoteException {
        assertTrue(clientController.signIn(seller.getUsername(), seller.getPassword(), seller.getCountry()));
        seller = clientController.getCurrentUser();

        assertTrue(clientController.createPrivateAuction(privateAuction.getProduct(), "pass1", privateAuction.getDayLimit(), privateAuction.getInitialPrice()));
        assertTrue(clientController.signIn(buyer.getUsername(), buyer.getPassword(), buyer.getCountry()));
        ArrayList<Auction> auctions = clientController.searchAuctionByProductName(product2.getName());
        assertEquals(1, auctions.size());
        privateAuction.setAuctionID(auctions.get(0).getAuctionID());
        assertEquals(privateAuction, auctions.get(0));
        // Clean up
        biddingDao.deleteUser(seller);
    }

    @Test
    public void bidTest() throws RemoteException {
        assertTrue(clientController.signIn(seller.getUsername(), seller.getPassword(), seller.getCountry()));
        seller = clientController.getCurrentUser();
        assertTrue(clientController.createPublicAuction(auction.getProduct(), auction.getDayLimit(), auction.getInitialPrice()));
        assertTrue(clientController.signIn(buyer.getUsername(), buyer.getPassword(), buyer.getCountry()));
        auction = clientController.searchAuctionByProductName(product.getName()).get(0);

        assertTrue(clientController.bid(auction, auction.getInitialPrice() + 1));
        auction = clientController.searchAuctionByProductName(product.getName()).get(0);
        assertEquals(bid, auction.getHighestBid());
        // Clean up
        biddingDao.deleteUser(seller);
    }

    @Test
    public void searchAuctionByCountryTest() throws RemoteException {
        assertTrue(clientController.signIn(seller.getUsername(), seller.getPassword(), seller.getCountry()));
        seller = clientController.getCurrentUser();
        assertTrue(clientController.createPublicAuction(auction.getProduct(), auction.getDayLimit(), auction.getInitialPrice()));
        assertEquals(0, clientController.searchAuctionByCountry(seller.getCountry()).size());

        assertTrue(clientController.signIn(buyer.getUsername(), buyer.getPassword(), buyer.getCountry()));
        ArrayList<Auction> auctions = clientController.searchAuctionByCountry(seller.getCountry());
        assertEquals(1, auctions.size());
        auction.setAuctionID(auctions.get(0).getAuctionID());
        assertEquals(auction, auctions.get(0));

        // Clean up
        biddingDao.deleteUser(seller);
    }

    @Test
    public void searchAuctionByProductNameTest() throws RemoteException {
        assertTrue(clientController.signIn(seller.getUsername(), seller.getPassword(), seller.getCountry()));
        seller = clientController.getCurrentUser();
        assertTrue(clientController.createPublicAuction(auction.getProduct(), auction.getDayLimit(), auction.getInitialPrice()));
        assertEquals(0, clientController.searchAuctionByProductName(product.getName()).size());

        assertTrue(clientController.signIn(buyer.getUsername(), buyer.getPassword(), buyer.getCountry()));
        ArrayList<Auction> auctions = clientController.searchAuctionByProductName(product.getName());
        assertEquals(1, auctions.size());
        auction.setAuctionID(auctions.get(0).getAuctionID());
        assertEquals(auction, auctions.get(0));
        // Clean up
        biddingDao.deleteUser(seller);
    }

    @Test
    public void updateUserTest() throws RemoteException {
        assertTrue(clientController.signIn(seller.getUsername(), seller.getPassword(), seller.getCountry()));
        assertEquals(seller.getCountry(), clientController.getCurrentUser().getCountry());

        String newCountry = "Thailand";
        assertTrue(clientController.updateUser(newCountry, seller.getPassword()));

        assertEquals(newCountry, clientController.getCurrentUser().getCountry());
    }

    @After
    public void tearDown() {
        User user = clientController.getCurrentUser();
        if (user != null) {
            biddingDao.deleteUser(clientController.getCurrentUser());
        }
    }
}
