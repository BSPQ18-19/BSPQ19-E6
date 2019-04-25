package com.spq.group6.server.services;


import com.spq.group6.server.dao.BiddingDAO;
import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.Bid;
import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;
import com.spq.group6.server.exceptions.AuctionException;
import com.spq.group6.server.utils.AuctionLocks;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class AuctionServiceTest {
    private static AuctionService auctionService;
    private static BiddingDAO biddingDAO;
    private static User user;
    private static User fakeUser;
    private static Product product;
    private Auction auction;
    private int offsetSeconds;

    @Before
    public void setUp() throws RemoteException {
        auctionService = new AuctionService();
        product = new Product("test_product", "test_description");
        user = new User("test_user", "test_pass", "uk");
        fakeUser = new User("test_fake", "test_pass", "uk");
        user.getOwnedProducts().add(product);
        offsetSeconds = 1;
        Timestamp t1 = new Timestamp(System.currentTimeMillis() + offsetSeconds * 1000);
        auction = new Auction(user, product, t1, 12, null);
        biddingDAO = new BiddingDAO();
    }

    @Test
    public void testCreatePublicAuction() throws InterruptedException {
        Auction createdAuction = auctionService.createPublicAuction(auction.getOwner(), auction.getProduct(), auction.getDayLimit(), auction.getInitialPrice());
        assertEquals(auction, createdAuction);

        assertEquals(0, auction.getOwner().getOwnedProducts().size());
        auction = createdAuction;
        user = auction.getOwner();
        Thread.sleep((offsetSeconds +1) * 1000);

        user = biddingDAO.getUserByUsername(user.getUsername());
        assertEquals(1, user.getOwnedProducts().size());

    }


    @Test
    public void testBid() throws AuctionException {
        auction = auctionService.createPublicAuction(auction.getOwner(), auction.getProduct(), auction.getDayLimit(), auction.getInitialPrice());
        user = auction.getOwner();
        float initialPrice = auction.getInitialPrice();
        Bid bid = new Bid(user, initialPrice);
        // too low amount
        try{
            auctionService.bid(auction, user, bid.getAmount()-1);
            fail();
        }catch (AuctionException ignored){}
        // correct amount
        auction = auctionService.bid(auction, bid.getUser(), bid.getAmount());
        assertEquals(bid, auction.getHighestBid());
        // repeated amount
        try{
            auctionService.bid(auction, user, initialPrice);
            fail();
        }catch (AuctionException ignored){}
        // correct amount
        bid.setAmount(bid.getAmount()+1);
        auction = auctionService.bid(auction, bid.getUser(), bid.getAmount());
        assertEquals(bid, auction.getHighestBid());
    }

    @Test
    public void testSearchAuctionByCountry() throws InterruptedException {
        auction = auctionService.createPublicAuction(auction.getOwner(), auction.getProduct(), auction.getDayLimit(), auction.getInitialPrice());
        user = auction.getOwner();

        ArrayList<Auction> myAuctions = auctionService.searchAuctionByCountry(user, user.getCountry());
        assertEquals(0, myAuctions.size());

        ArrayList<Auction> auctions = auctionService.searchAuctionByCountry(fakeUser, user.getCountry());
        assertEquals(1, auctions.size());
        assertEquals(auction, auctions.get(0));

        Thread.sleep((offsetSeconds +1) * 1000);
        AuctionLocks.getLock(auction.getAuctionID()).lock();
        auctions = auctionService.searchAuctionByCountry(fakeUser, user.getCountry());
        AuctionLocks.getLock(auction.getAuctionID()).unlock();

        assertEquals(0, auctions.size());
    }

    @Test
    public void testSearchAuctionByProductName() throws InterruptedException {
        auction = auctionService.createPublicAuction(auction.getOwner(), auction.getProduct(), auction.getDayLimit(), auction.getInitialPrice());
        user = auction.getOwner();

        ArrayList<Auction> auctions = auctionService.searchAuctionByProductName(fakeUser, product.getName());
        assertEquals(1, auctions.size());
        assertEquals(auction, auctions.get(0));

        Thread.sleep((offsetSeconds +1) * 1000);
        AuctionLocks.getLock(auction.getAuctionID()).lock();
        auctions = auctionService.searchAuctionByProductName(fakeUser, product.getName());
        AuctionLocks.getLock(auction.getAuctionID()).unlock();

        assertEquals(0, auctions.size());

    }

    @After
    public void tearDown() throws InterruptedException {
        // Wait for any countdown left
        long countdownLeft = (auction.getDayLimit().getTime() - System.currentTimeMillis()) + 1000;
        Thread.sleep(Math.max(0, countdownLeft));

        if (auction != null){
            biddingDAO.deleteAuction(auction);
        }
        if (user != null){
            biddingDAO.deleteUser(user);
        }
    }
}
