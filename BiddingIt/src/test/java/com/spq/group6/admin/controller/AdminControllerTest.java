package com.spq.group6.admin.controller;

import com.spq.group6.client.controller.ClientController;
import com.spq.group6.client.remote.ServiceLocator;
import com.spq.group6.server.dao.BiddingDAO;
import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.Bid;
import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;
import com.spq.group6.server.remote.IServer;
import com.spq.group6.server.remote.Server;
import com.spq.group6.server.utils.logger.ServerLogger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.sql.Timestamp;

public class AdminControllerTest {
    private static Thread rmiRegistryThread = null;
    private static Thread rmiServerThread = null;

    private static ClientController clientController;
    private static BiddingDAO biddingDao;
    private static User seller, buyer;
    private static Product product;
    private static Auction auction;
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
            Thread.sleep(2000);
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
        clientController = ClientController.getClientController();
        seller = new User("test_seller", "test_pass", "uk");
        buyer = new User("test_buyer", "test_pass", "uk");
        product = new Product("test_product", "test_description");
        biddingDao = new BiddingDAO();
        int offset = 10;
        Timestamp limit = new Timestamp(System.currentTimeMillis() + offset * 1000);
        auction = new Auction(seller, product, limit, 12, null);
        bid = new Bid(buyer, auction.getInitialPrice() + 1);
    }

    @After
    public void tearDown() {
        User user = clientController.getCurrentUser();
        if (user != null) {
            biddingDao.deleteUser(clientController.getCurrentUser());
        }
    }
}
