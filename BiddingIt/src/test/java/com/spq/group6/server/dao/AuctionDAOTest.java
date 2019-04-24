package com.spq.group6.server.dao;

import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.Bid;
import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AuctionDAOTest {
    private BiddingDAO biddingDAO;

    private User testUser;
    private Product testProduct;
    private Auction testAuction;
    private ArrayList<Auction> testArray;

    @Before
    public void setUp(){
        biddingDAO = new BiddingDAO();

        testUser = new User("test_user", "test_pass", "uk");
        testProduct = new Product("Mobile Phone", "Iphone X");
        int seconds = 10;
        Timestamp limit = new Timestamp(System.currentTimeMillis() + seconds*1000);
        testAuction = new Auction(testUser, testProduct, limit, 600, "123" );
        testArray = new ArrayList<Auction>();
        testArray.add(testAuction);
    }

    @Test
    public void getAuctionByCountry(){
        //Test
        assertNull(biddingDAO.getAuctionByCountry(testUser.getCountry()));
        biddingDAO.persistAuction(testAuction);
        for(int i = 0; i < testArray.size(); i++) {
            assertEquals(testArray.get(i), biddingDAO.getAuctionByCountry(testUser.getCountry()).get(i));
        }
    }

    @Test
    public void getAuctionByProductName(){
        //Test
        assertNull(biddingDAO.getAuctionByProductName(testProduct.getName()));
        biddingDAO.persistAuction(testAuction);
        for(int i = 0; i < testArray.size(); i++) {
            assertEquals(testArray.get(i), biddingDAO.getAuctionByProductName(testProduct.getName()).get(i));
        }
    }

    @Test
    public void getAuctionByUser(){
        //Test
        assertNull(biddingDAO.getAuctionByUser(testUser));
        biddingDAO.persistAuction(testAuction);
        for(int i = 0; i < testArray.size(); i++) {
            assertEquals(testArray.get(i), biddingDAO.getAuctionByUser(testUser).get(i));
        }
    }

    @Test
    public void getHighestBid(){
        // Setup
        User testUser2 = new User("test_user2", "test_pass2", "uk");
        Bid testBid = new Bid(testUser2,600);
        testAuction.setHighestBid(testBid);
        // Test
        assertNull(biddingDAO.getHighestBid(testAuction.getAuctionID()));
        biddingDAO.createUser(testUser2);
        biddingDAO.persistAuction(testAuction);
        assertEquals(testBid, biddingDAO.getHighestBid(testAuction.getAuctionID()));
        // Clean
        biddingDAO.deleteUser(testUser2);
    }

    @Test
    public void isOpen(){
        //Test
        assertNull(biddingDAO.isOpen(testAuction.getAuctionID()));
        biddingDAO.persistAuction(testAuction);
        assertEquals(testAuction.isOpen(), biddingDAO.isOpen(testAuction.getAuctionID()));
    }

    @After
    public void tearDown(){
        biddingDAO.deleteUser(testUser);
    }
}
