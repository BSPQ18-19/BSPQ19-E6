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
        assertEquals(0, biddingDAO.getAuctionByCountry(testUser.getCountry()).size());
        biddingDAO.persistAuction(testAuction);
        for(int i = 0; i < testArray.size(); i++) {

            assertEquals(testArray.get(i), biddingDAO.getAuctionByCountry(testUser.getCountry()).get(i));
        }
    }

    @Test
    public void getAuctionByProductName(){
        //Test
        assertEquals(0, biddingDAO.getAuctionByProductName(testProduct.getName()).size());
        biddingDAO.persistAuction(testAuction);
        for(int i = 0; i < testArray.size(); i++) {
            assertEquals(testArray.get(i), biddingDAO.getAuctionByProductName(testProduct.getName()).get(i));
        }
    }

    @Test
    public void getAuctionByUser(){
        //Test
        assertEquals(0, biddingDAO.getAuctionByUser(testUser).size());
        biddingDAO.persistAuction(testAuction);
        for(int i = 0; i < testArray.size(); i++) {
            assertEquals(testArray.get(i), biddingDAO.getAuctionByUser(testUser).get(i));
        }
    }
    
    @After
    public void tearDown(){
        biddingDAO.deleteUser(testAuction.getOwner());
    }
}