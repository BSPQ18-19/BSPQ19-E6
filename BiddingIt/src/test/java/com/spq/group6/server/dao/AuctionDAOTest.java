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
    private AuctionDAO auctionDao = new AuctionDAO();
    private AccountDAO accountDao = new AccountDAO();

    private User testUser;
    private Product testProduct;
    private Auction testAuction;
    private ArrayList<Auction> testArray;

    @Before
    public void setUp(){
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
        assertNull(auctionDao.getAuctionByCountry(testUser.getCountry()));
        auctionDao.persistAuction(testAuction);
        for(int i = 0; i < testArray.size(); i++) {
            assertEquals(testArray.get(i), auctionDao.getAuctionByCountry(testUser.getCountry()).get(i));
        }
    }

    @Test
    public void getAuctionByProductName(){
        //Test
        assertNull(auctionDao.getAuctionByProductName(testProduct.getName()));
        auctionDao.persistAuction(testAuction);
        for(int i = 0; i < testArray.size(); i++) {
            assertEquals(testArray.get(i), auctionDao.getAuctionByProductName(testProduct.getName()).get(i));
        }
    }

    @Test
    public void getAuctionByUser(){
        //Test
        assertNull(auctionDao.getAuctionByUser(testUser));
        auctionDao.persistAuction(testAuction);
        for(int i = 0; i < testArray.size(); i++) {
            assertEquals(testArray.get(i), auctionDao.getAuctionByUser(testUser).get(i));
        }
    }

    @Test
    public void getHighestBid(){
        // Setup
        User testUser2 = new User("test_user2", "test_pass2", "uk");
        Bid testBid = new Bid(testUser2,600);
        testAuction.setHighestBid(testBid);
        // Test
        assertNull(auctionDao.getHighestBid(testAuction.getAuctionID()));
        accountDao.createUser(testUser2);
        auctionDao.persistAuction(testAuction);
        assertEquals(testBid, auctionDao.getHighestBid(testAuction.getAuctionID()));
        // Clean
        accountDao.deleteUser(testUser2);
    }

    @Test
    public void isOpen(){
        //Test
        assertNull(auctionDao.isOpen(testAuction.getAuctionID()));
        auctionDao.persistAuction(testAuction);
        assertEquals(testAuction.isOpen(), auctionDao.isOpen(testAuction.getAuctionID()));
    }

    @After
    public void tearDown(){
        accountDao.deleteUser(testUser);
    }
}
