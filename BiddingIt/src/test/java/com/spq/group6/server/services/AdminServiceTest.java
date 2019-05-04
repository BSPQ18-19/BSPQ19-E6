package com.spq.group6.server.services;

import com.spq.group6.server.dao.BiddingDAO;
import com.spq.group6.server.data.Administrator;
import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;
import com.spq.group6.server.exceptions.AdministratorException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class AdminServiceTest {
    private static AdminService adminService;
    private static AuctionService auctionService;
    private static BiddingDAO biddingDAO;
    private static User user;
    private static Product product;
    private Auction auction;
    private Administrator admin;
    private int offsetSeconds;

    @Before
    public void setUp() throws RemoteException {
        auctionService = new AuctionService();
        adminService = new AdminService();
        admin = new Administrator("test_admin", "admin_pass");
        product = new Product("test_product", "test_description");
        user = new User("test_user", "test_pass", "uk");
        user.getOwnedProducts().add(product);
        offsetSeconds = 1;
        Timestamp t1 = new Timestamp(System.currentTimeMillis() + offsetSeconds * 1000);
        auction = new Auction(user, product, t1, 12, null);
        biddingDAO = new BiddingDAO();

        biddingDAO.persistAuction(auction);
        auctionService.initAuction(auction); // Initializes countdown. Needed for removal test.
        biddingDAO.persistAdministrator(admin);
    }

    @Test
    public void testLogin() throws AdministratorException {
        // Wrong username & password
        try {
            adminService.logIn("anyUser", "anyPass");
            fail();
        } catch (AdministratorException err) {
        }
        // Correct username & wrong password
        try {
            adminService.logIn(admin.getUsername(), "anyPass");
            fail();
        } catch (AdministratorException err) {
        }
        // Correct username and password
        assertEquals(admin, adminService.logIn(admin.getUsername(), admin.getPassword()));
    }

    @Test
    public void testDeleteAuction() {
        adminService.deleteAuction(auction);
        assertNull(biddingDAO.getAuctionByID(auction.getAuctionID()));
    }

    @Test
    public void testDeleteUser() {
        adminService.deleteUser(user);
        assertNull(biddingDAO.getUserByUsername(user.getUsername()));

        user = null; // This is needed for not deleting it again in TearDown
    }

    @Test
    public void testGetAllUsers() {
        ArrayList<User> users = adminService.getAllUsers();
        assertEquals(1, users.size());
        assertEquals(user, users.get(0));
    }

    @Test
    public void testGetAuctionByUser() {
        user = auction.getOwner();
        ArrayList<Auction> auctions = adminService.getAuctionByUser(user);
        assertEquals(1, auctions.size());
        assertEquals(auction, auctions.get(0));
    }

    @Test
    public void testGetAllAuctions() throws InterruptedException, RemoteException {
        ArrayList<Auction> auctions = adminService.getAllAuctions();
        assertEquals(1, auctions.size());
        assertEquals(auction, auctions.get(0));
    }

    @After
    public void tearDown() throws InterruptedException {
        // Wait for any countdown left
        long countdownLeft = (auction.getDayLimit().getTime() - System.currentTimeMillis()) + 1000;
        Thread.sleep(Math.max(0, countdownLeft));

        biddingDAO.deleteAdministrator(admin);
        if (user != null) { // This is needed when the removal test is run
            biddingDAO.deleteUser(user);
        }
    }
}
