package com.spq.group6.server.dao;

import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.Bid;
import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;
import org.junit.Test;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AuctionDAOTest {
    private AuctionDAO auctionDao = new AuctionDAO();
    private AccountDAO accountDao = new AccountDAO();

    @Test
    public void getAuctionByCountry(){
        //Setup
        User testUser = new User("test_user", "test_pass", "uk");
        Product testProduct = new Product("Mobile Phone", "Iphone X");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = dateFormat.parse("23/09/2019");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert date != null;
        long time = date.getTime();
        Timestamp timestamp = new Timestamp(time);
        Auction testAuction = new Auction(testUser, testProduct, timestamp, 600, "123" );
        ArrayList<Auction> testArray = new ArrayList();
        testArray.add(testAuction);
        //Test
        assertNull(auctionDao.getAuctionByCountry(testUser.getCountry()));
        auctionDao.persistAuction(testAuction);
        for(int i = 0; i < testArray.size(); i++) {
            assertEquals(testArray.get(i), auctionDao.getAuctionByCountry(testUser.getCountry()).get(i));
        }
        //Clean
        accountDao.deleteUser(testUser);
    }

    @Test
    public void getAuctionByProductName(){
        //Setup
        User testUser = new User("test_user", "test_pass", "uk");
        Product testProduct = new Product("Mobile Phone", "Iphone X");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = dateFormat.parse("23/09/2019");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert date != null;
        long time = date.getTime();
        Timestamp timestamp = new Timestamp(time);
        Auction testAuction = new Auction(testUser, testProduct, timestamp, 600, "123" );
        ArrayList<Auction> testArray = new ArrayList();
        testArray.add(testAuction);
        //Test
        assertNull(auctionDao.getAuctionByProductName(testProduct.getName()));
        auctionDao.persistAuction(testAuction);
        for(int i = 0; i < testArray.size(); i++) {
            assertEquals(testArray.get(i), auctionDao.getAuctionByProductName(testProduct.getName()).get(i));
        }
        //Clean
        accountDao.deleteUser(testUser);
    }

    @Test
    public void getAuctionByUser(){
        //Setup
        User testUser = new User("test_user", "test_pass", "uk");
        Product testProduct = new Product("Mobile Phone", "Iphone X");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = dateFormat.parse("23/09/2019");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert date != null;
        long time = date.getTime();
        Timestamp timestamp = new Timestamp(time);
        Auction testAuction = new Auction(testUser, testProduct, timestamp, 600, "123" );
        ArrayList<Auction> testArray = new ArrayList();
        testArray.add(testAuction);
        //Test
        assertNull(auctionDao.getAuctionByUser(testUser));
        auctionDao.persistAuction(testAuction);
        for(int i = 0; i < testArray.size(); i++) {
            assertEquals(testArray.get(i), auctionDao.getAuctionByUser(testUser).get(i));
        }
        //Clean
        accountDao.deleteUser(testUser);
    }

    @Test
    public void getHighestBid(){
        // Setup
        User testUser = new User("test_user", "test_pass", "uk");
        User testUser2 = new User("test_user2", "test_pass2", "uk");
        Product testProduct = new Product("Mobile Phone", "Iphone X");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = dateFormat.parse("23/09/2019");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert date != null;
        long time = date.getTime();
        Timestamp timestamp = new Timestamp(time);
        Auction testAuction = new Auction(testUser, testProduct, timestamp, 600, "123" );
        Bid testBid = new Bid(testUser2,600);
        testAuction.setHighestBid(testBid);
        // Test
        assertNull(auctionDao.getHighestBid(testAuction.getAuctionID()));
        accountDao.createUser(testUser2);
        auctionDao.persistAuction(testAuction);
        assertEquals(testBid, auctionDao.getHighestBid(testAuction.getAuctionID()));
        // Clean
        accountDao.deleteUser(testUser);
        accountDao.deleteUser(testUser2);
    }

    @Test
    public void isOpen(){
        //Setup
        User testUser = new User("test_user", "test_pass", "uk");
        Product testProduct = new Product("Mobile Phone", "Iphone X");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = dateFormat.parse("23/09/2019");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert date != null;
        long time = date.getTime();
        Timestamp timestamp = new Timestamp(time);
        Auction testAuction = new Auction(testUser, testProduct, timestamp, 600, "123" );
        //Test
        assertNull(auctionDao.isOpen(testAuction.getAuctionID()));
        auctionDao.persistAuction(testAuction);
        assertEquals(testAuction.isOpen(), auctionDao.isOpen(testAuction.getAuctionID()));
        //Clean
        accountDao.deleteUser(testUser);
    }
}
