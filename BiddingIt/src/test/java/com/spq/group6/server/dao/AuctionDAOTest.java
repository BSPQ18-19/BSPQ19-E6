package com.spq.group6.server.dao;

import com.spq.group6.server.data.Auction;
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
    private static User fakeUser;
    private BiddingDAO biddingDAO;
    private User testUser;
    private Product product;
    private Auction auction;
    private List<Auction> databaseAuctions;
    private List<Auction> auctions;

    @Before
    public void setUp() {
        biddingDAO = new BiddingDAO();
        testUser = new User("test_user", "test_pass", "uk");
        product = new Product("Mobile Phone", "Iphone X");
        fakeUser = new User("test_fake", "test_pass", "uk");
        int seconds = 10;
        Timestamp limit = new Timestamp(System.currentTimeMillis() + seconds * 1000);
        auction = new Auction(testUser, product, limit, 600, "123");
        databaseAuctions = new ArrayList<>();
        databaseAuctions.add(auction);

        biddingDAO.persistAuction(auction); // Persists User and Product too
    }

    @Test
    public void getAuctionByCountry() {
        //Test
        assertEquals(0, biddingDAO.getAuctionByCountry(testUser, testUser.getCountry()).size());
        auctions = biddingDAO.getAuctionByCountry(fakeUser, testUser.getCountry());
        assertEquals(databaseAuctions.size(), auctions.size());
        assertTrue(databaseAuctions.containsAll(auctions) && auctions.containsAll(databaseAuctions));
    }

    @Test
    public void getAuctionByProductName() {
        //Test
        assertEquals(0, biddingDAO.getAuctionByProductName(testUser, product.getName()).size());
        auctions = biddingDAO.getAuctionByProductName(fakeUser, product.getName());
        assertTrue(databaseAuctions.size() == auctions.size() &&
                databaseAuctions.containsAll(auctions) && auctions.containsAll(databaseAuctions));
    }

    @Test
    public void getAuctionByUser() {
        //Test
        assertEquals(0, biddingDAO.getAuctionByUser(fakeUser).size());
        auctions = biddingDAO.getAuctionByUser(testUser);
        assertTrue(databaseAuctions.size() == auctions.size() &&
                databaseAuctions.containsAll(auctions) && auctions.containsAll(databaseAuctions));
    }

    @Test
    public void getAuctionByID() {
        assertEquals(auction, biddingDAO.getAuctionByID(auction.getAuctionID()));
    }

    @Test
    public void getAllAuctionsExceptRequester() {
        assertEquals(0, biddingDAO.getAllAuctionsExceptRequester(testUser).size());
        auctions = biddingDAO.getAllAuctionsExceptRequester(fakeUser);
        assertTrue(databaseAuctions.size() == auctions.size() &&
                databaseAuctions.containsAll(auctions) && auctions.containsAll(databaseAuctions));
    }

    @After
    public void tearDown() {
        biddingDAO.deleteUser(auction.getOwner()); // Deletes Auctions, Products and Bids too
    }
}
