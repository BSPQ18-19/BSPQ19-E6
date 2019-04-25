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
import java.util.List;

import static org.junit.Assert.*;

public class AuctionDAOTest {
    private BiddingDAO biddingDAO;

    private User testUser;
    private Product testProduct;
    private Auction testAuction;
    private List<Auction> testArray;
    private List<Auction> auctions;

    @Before
    public void setUp(){
        biddingDAO = new BiddingDAO();

        testUser = new User("test_user", "test_pass", "uk");
        testProduct = new Product("Mobile Phone", "Iphone X");
        int seconds = 10;
        Timestamp limit = new Timestamp(System.currentTimeMillis() + seconds*1000);
        testAuction = new Auction(testUser, testProduct, limit, 600, "123" );
        testArray.add(testAuction);
    }

    @Test
    public void getAuctionByCountry(){
        //Test
        assertEquals(0, biddingDAO.getAuctionByCountry(testUser.getCountry()).size());
        biddingDAO.persistAuction(testAuction);
        auctions = biddingDAO.getAuctionByCountry(testUser.getCountry());
        assertTrue(testArray.size() == auctions.size() &&
                testArray.containsAll(auctions) && auctions.containsAll(testArray));
    }

    @Test
    public void getAuctionByProductName(){
        //Test
        assertEquals(0, biddingDAO.getAuctionByProductName(testProduct.getName()).size());
        biddingDAO.persistAuction(testAuction);
        auctions = biddingDAO.getAuctionByProductName(testProduct.getName());
        assertTrue(testArray.size() == auctions.size() &&
                testArray.containsAll(auctions) && auctions.containsAll(testArray));
    }

    @Test
    public void getAuctionByUser(){
        //Test
        assertEquals(0, biddingDAO.getAuctionByUser(testUser).size());
        biddingDAO.persistAuction(testAuction);
        auctions = biddingDAO.getAuctionByUser(testUser);
        assertTrue(testArray.size() == auctions.size() &&
                testArray.containsAll(auctions) && auctions.containsAll(testArray));
    }

    @Test
    public void getAuctionByID(){
        assertNull(biddingDAO.getAuctionByID(testAuction.getAuctionID()));
        biddingDAO.persistAuction(testAuction);
        assertEquals(testAuction, biddingDAO.getAuctionByID(testAuction.getAuctionID()));
    }

    @Test
    public void getAllAuctions(){
        assertEquals(0, biddingDAO.getAllAuctions().size());
        biddingDAO.persistAuction(testAuction);
        auctions = biddingDAO.getAllAuctions();
        assertTrue(testArray.size() == auctions.size() &&
                testArray.containsAll(auctions) && auctions.containsAll(testArray));
    }
    
    @After
    public void tearDown(){
        biddingDAO.deleteUser(testAuction.getOwner());
    }
}
