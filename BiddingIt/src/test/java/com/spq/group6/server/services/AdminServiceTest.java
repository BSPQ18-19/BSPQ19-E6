package com.spq.group6.server.services;

import com.spq.group6.server.dao.BiddingDAO;
import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class AdminServiceTest {
    private static AdminService adminService;
    private static BiddingDAO biddingDAO;
    private static User user;
    private static Product product;
    private Auction auction;
    private int offsetSeconds;

    @Before
    public void setUp() throws RemoteException {
        adminService = new AdminService();
        product = new Product("test_product", "test_description");
        user = new User("test_user", "test_pass", "uk");
        user.getOwnedProducts().add(product);
        offsetSeconds = 1;
        Timestamp t1 = new Timestamp(System.currentTimeMillis() + offsetSeconds * 1000);
        auction = new Auction(user, product, t1, 12, null);
        biddingDAO = new BiddingDAO();
    }

    @Test
    public void testSearchAuctionByUser() throws InterruptedException {
        biddingDAO.persistAuction(auction);
        user = auction.getOwner();

        ArrayList<Auction> auctions = adminService.getAuctionByUser(user);
        assertEquals(1, auctions.size());
        assertEquals(auction, auctions.get(0));
    }

    @After
    public void tearDown() throws InterruptedException {
        if (auction != null){
            biddingDAO.deleteAuction(auction);
        }
        if (user != null){
            biddingDAO.deleteUser(user);
        }
    }
}
