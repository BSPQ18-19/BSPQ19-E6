package com.spq.group6.server.services;

import com.spq.group6.client.controller.ClientController;
import com.spq.group6.server.dao.AdminDAO;
import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;
import com.spq.group6.server.exceptions.AuctionException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.rmi.RemoteException;
import java.sql.Time;
import java.sql.Timestamp;

public class AuctionServiceTest {
    private static AuctionService auctionService;
    private static AdminDAO adminDao;
    private static User user;
    private static Product product;
    private Auction auction;

    @Before
    public void setUp() throws RemoteException {
        auctionService = new AuctionService();
        user = new User("test_user", "test_pass", "uk");
        product = new Product("test_product", "test_description");
        Timestamp t1 = new Timestamp(System.currentTimeMillis() + 2000);
        auction = new Auction(user, product, t1, 12, null);
        adminDao = new AdminDAO();
    }

    @Test
    public void testCreatePublicAuction() {
        Auction createdAuction = auctionService.createPublicAuction(auction.getOwner(), auction.getProduct(), auction.getDayLimit(), auction.getInitialPrice());
        assertEquals(auction, createdAuction);
    }

    public void testBid() throws AuctionException {

    }

    public void testSearchAuctionByCountry(){

    }

    public void testSearchAuctionByProductName() {

    }

    public void testSearchAuctionByUser() {

    }

    @After
    public void tearDown(){
        if (user != null){
            adminDao.deleteUser(user);
        }
        if (auction != null){
            adminDao.deleteAuction(auction);
        }
    }
}
