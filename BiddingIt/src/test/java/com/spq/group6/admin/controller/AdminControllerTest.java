package com.spq.group6.admin.controller;

import com.spq.group6.admin.remote.AdminServiceLocator;
import com.spq.group6.client.controller.ClientController;
import com.spq.group6.client.remote.ServiceLocator;
import com.spq.group6.server.dao.BiddingDAO;
import com.spq.group6.server.data.*;
import com.spq.group6.server.remote.IServer;
import com.spq.group6.server.remote.Server;
import com.spq.group6.server.utils.logger.ServerLogger;
import org.junit.*;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class AdminControllerTest {
    private static Thread rmiRegistryThread = null;
    private static Thread rmiServerThread = null;

    private static AdminController adminController;
    private static BiddingDAO biddingDao;
    private static User seller;
    private static Product product;
    private static Auction auction;
    private static Administrator admin;

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
            Thread.sleep(2000);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }

        AdminServiceLocator.getServiceLocator().setService(ip, port, name);


        biddingDao = new BiddingDAO();
        admin = new Administrator("admin", "pass");
        biddingDao.persistAdministrator(admin);
    }

    @Before
    public void setUp() throws RemoteException {
        adminController = new AdminController();
        seller = new User("test_seller", "test_pass", "uk");
        product = new Product("test_product", "test_description");
        int offset = 10;
        Timestamp limit = new Timestamp(System.currentTimeMillis() + offset * 1000);
        auction = new Auction(seller, product, limit, 12, null);

        biddingDao.persistAuction(auction);
    }


    @Test
    public void logInInTest() throws RemoteException {
        // Wrong username & password
        assertFalse(adminController.logIn("any_user", "any_pass"));
        // Correct username & wrong password
        assertFalse(adminController.logIn(admin.getUsername(), "any_pass"));
        // Correct username & password
        assertTrue(adminController.logIn(admin.getUsername(), admin.getPassword()));
    }

    @Test
    public void logOutInTest() throws RemoteException {
        assertNull(adminController.getCurrentAdmin());
        assertTrue(adminController.logIn(admin.getUsername(), admin.getPassword()));
        assertNotNull(adminController.getCurrentAdmin());

        adminController.logOut();
        assertNull(adminController.getCurrentAdmin());
    }

    @Test
    public void getAllUsersTest() {
        ArrayList<User> users = adminController.getAllUsers();
        assertEquals(1, users.size());

        assertEquals(seller, users.get(0));
    }

    @Test(expected = NullPointerException.class)
    public void testFailStartuUcheckedAuctions(){
        // It should fail because it tries to delet a countdown that does not exist
        adminController.deleteAuction(auction);
    }

    @Test
    public void testCorrectStartuUcheckedAuctions(){
        adminController.startUncheckedAuctions();
        adminController.deleteAuction(auction);
    }

    @Test
    public void testGetAllAuctions() {
        ArrayList<Auction> auctions = adminController.getAllAuctions();
        assertEquals(1, auctions.size());

        assertEquals(auction, auctions.get(0));
    }

    @Test
    public void testDeleteUser() {
        // First start countDown for auction, otherwise it throws an Error
        adminController.startUncheckedAuctions();
        // Test
        adminController.deleteUser(seller);
        assertEquals(0, adminController.getAllUsers().size());

        seller = null; // Set to null for not trying to delete it in 'tearDown'
    }

    @Test
    public void testDeleteAuction() {
        // First start countDown for auction, otherwise it throws an Error
        adminController.startUncheckedAuctions();
        // Test
        adminController.deleteAuction(auction);
        assertEquals(0, adminController.getAllAuctions().size());
    }

    @After
    public void tearDown() {
        if (seller!= null) {
            biddingDao.deleteUser(seller);
        }
    }

    @AfterClass
    static public void tearDownClass() {
        biddingDao.deleteAdministrator(admin);
        try {
            rmiServerThread.join();
            rmiRegistryThread.join();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }
}
