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
import junit.framework.JUnit4TestAdapter;
import org.databene.contiperf.PerfTest;
import org.databene.contiperf.Required;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.*;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class ConcurrentClientControllerTest {
    private static Thread rmiRegistryThread = null;
    private static Thread rmiServerThread = null;

    private static ClientController buyerClientController, sellerClientController;
    private static BiddingDAO biddingDao;
    private static User seller, buyer;
    private static Product product, freeProduct;
    private static Auction auction, notPersistedAuction;
    private static Bid bid;

    @Rule public ContiPerfRule rule = new ContiPerfRule();
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ConcurrentClientControllerTest.class);
    }

    @BeforeClass
    static public void setUpClass() throws RemoteException {
        // Launch the RMI registry
        class RMIRegistryRunnable implements Runnable {

            public void run() {
                try {
                    java.rmi.registry.LocateRegistry.createRegistry(1099);
                    System.out.println("RMI registry ready.");
                } catch (Exception e) {
                    System.out.println("Exception starting RMI registry:");
                    e.printStackTrace();
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
        String serverName = "//"+ip+":"+port+"/"+ name;
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
            Thread.sleep(2000);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }

        ServiceLocator.getServiceLocator().setService(ip, port, name);
        seller = new User("test_seller", "test_pass", "uk");
        buyer = new User("test_buyer", "test_pass", "uk");
        biddingDao = new BiddingDAO();
        biddingDao.createUser(seller);
        biddingDao.createUser(buyer);
        product = new Product("test_product", "test_description");
        freeProduct = new Product("test_product", "test_description");
        seller.getOwnedProducts().add(product);
        seller.getOwnedProducts().add(freeProduct);
        biddingDao.updateUser(seller);

        int offset = 20;
        Timestamp limit = new Timestamp(System.currentTimeMillis() +offset*1000);
        notPersistedAuction = new Auction(seller, product, limit, 12, null);
        auction = new Auction(seller, product, limit, 12, null);
        sellerClientController = new ClientController();
        sellerClientController.logIn(seller.getUsername(), seller.getPassword());
        sellerClientController.createPublicAuction( auction.getProduct(), auction.getDayLimit(), auction.getInitialPrice());
        auction = biddingDao.getAllAuctions().get(0);
    }

    @Before
    public void setUp() throws RemoteException {
        buyerClientController = new ClientController();
        sellerClientController = new ClientController();
        assertTrue(buyerClientController.logIn(buyer.getUsername(), buyer.getPassword()));
        assertTrue(sellerClientController.logIn(seller.getUsername(), seller.getPassword()));
    }


    @Test
    @PerfTest(invocations = 1000, threads = 20)
    @Required(max = 150, average = 50)
    public void logInInTest() throws RemoteException {
        assertTrue(buyerClientController.logIn(buyer.getUsername(), buyer.getPassword()));
        assertEquals(buyer, buyerClientController.getCurrentUser());
    }

    @Test
    @PerfTest(invocations = 1000, threads = 20)
    @Required(max = 350, average = 180)
    public void createProductTest() throws RemoteException {
        assertTrue(buyerClientController.logIn(seller.getUsername(), seller.getPassword())); // If we do not use unique user objects, JDO throw error
        assertTrue(buyerClientController.createProduct(product.getName(), product.getDescription()));
    }

    @Test
    @PerfTest(invocations = 1000, threads = 20)
    @Required(max = 95, average = 35)
    public void updateProductTest() throws RemoteException {
        product = sellerClientController.getCurrentUser().getOwnedProducts().get(0);
        assertTrue(sellerClientController.updateProduct(product, product.getName(), product.getDescription()));
    }

    @Test
    @PerfTest(invocations = 1000, threads = 20)
    @Required(max = 800, average = 300)
    public void createPublicAuctionTest() throws RemoteException {
        assertTrue(sellerClientController.createPublicAuction(notPersistedAuction.getProduct(), notPersistedAuction.getDayLimit(), notPersistedAuction.getInitialPrice()));
    }

    @Test
    @PerfTest(invocations = 1000, threads = 20)
    @Required(max = 500, average = 300)
    public void bidTest() throws RemoteException {
        buyerClientController.bid(auction, System.nanoTime());
    }

    @Test
    @PerfTest(invocations = 1000, threads = 20)
    @Required(max = 500, average = 300)
    public void searchAuctionByCountryTest() throws RemoteException {
        ArrayList<Auction> auctions = buyerClientController.searchAuctionByCountry(seller.getCountry());
        assertEquals(1, auctions.size() );
    }

    @After
    public void tearDown(){

    }

    @AfterClass
    static public void tearDownClass() {
        biddingDao.deleteUser(seller);
        biddingDao.deleteUser(buyer);
        try	{
            rmiServerThread.join();
            rmiRegistryThread.join();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }
}
