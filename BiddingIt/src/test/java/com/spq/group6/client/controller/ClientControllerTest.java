package com.spq.group6.client.controller;

import com.spq.group6.client.remote.ServiceLocator;
import com.spq.group6.server.dao.BiddingDAO;
import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;
import com.spq.group6.server.remote.IServer;
import com.spq.group6.server.remote.Server;
import com.spq.group6.server.utils.logger.ServerLogger;
import org.junit.*;

import static org.junit.Assert.*;


import java.rmi.Naming;
import java.rmi.RemoteException;

public class ClientControllerTest {
    private static Thread rmiRegistryThread = null;
    private static Thread rmiServerThread = null;

    private static ClientController clientController;
    private static BiddingDAO biddingDAO;
    private static User user;
    private static Product product;

    @BeforeClass
    static public void setUpClass() {
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
    }

    @Before
    public void setUp() throws RemoteException {
        clientController = new ClientController();
        user = new User("test_user", "test_pass", "uk");
        product = new Product("test_product", "test_description");
        biddingDAO = new BiddingDAO();
    }

    @Test
    public void signInTest() throws RemoteException {
        assertTrue(clientController.signIn(user.getUsername(), user.getPassword(), user.getCountry()));
        assertEquals(user, clientController.getCurrentUser());
        assertFalse(clientController.signIn(user.getUsername(), user.getPassword(), user.getCountry()));
        System.out.println(clientController.getCurrentUser());
    }


    @Test
    public void logInInTest() throws RemoteException {
        assertFalse(clientController.logIn(user.getUsername(), user.getPassword()));
        // Creates a new user for trying to login
        assertTrue(clientController.signIn(user.getUsername(), user.getPassword(), user.getCountry()));

        assertTrue(clientController.logIn(user.getUsername(), user.getPassword()));
        assertEquals(user, clientController.getCurrentUser());
    }

    @Test
    public void logOutInTest() throws RemoteException {
        assertNull(clientController.getCurrentUser());
        // Creates a new user and logs in
        assertTrue(clientController.signIn(user.getUsername(), user.getPassword(), user.getCountry()));
        assertTrue(clientController.logIn(user.getUsername(), user.getPassword()));
        User user = clientController.getCurrentUser(); // We save it for deleting it later
        assertNotNull(user);

        clientController.logOut();
        assertNull(clientController.getCurrentUser());

        biddingDAO.deleteUser(user);
    }

    @Test
    public void createProductTest() throws RemoteException {
        assertTrue(clientController.signIn(user.getUsername(), user.getPassword(), user.getCountry()));
        assertTrue(clientController.createProduct(product.getName(), product.getDescription()));

        assertEquals(1, clientController.getCurrentUser().getOwnedProducts().size());
        Product createdProduct = clientController.getCurrentUser().getOwnedProducts().get(0);
        assertEquals(product, createdProduct);
    }

    @Test
    public void updateProductTest() throws RemoteException {
        assertTrue(clientController.signIn(user.getUsername(), user.getPassword(), user.getCountry()));
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
        assertTrue(clientController.signIn(user.getUsername(), user.getPassword(), user.getCountry()));
        assertTrue(clientController.createProduct(product.getName(), product.getDescription()));
        product = clientController.getCurrentUser().getOwnedProducts().get(0);

        assertTrue(clientController.deleteProduct(product));
    }



    @After
    public void tearDown(){
        User user = clientController.getCurrentUser();
        if (user != null){
            biddingDAO.deleteUser(clientController.getCurrentUser());
        }
    }

    @AfterClass
    static public void tearDownClass() {
        try	{
            rmiServerThread.join();
            rmiRegistryThread.join();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }
}
