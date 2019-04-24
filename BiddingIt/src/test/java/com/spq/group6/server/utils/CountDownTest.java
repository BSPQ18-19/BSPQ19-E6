package com.spq.group6.server.utils;

import com.spq.group6.server.dao.BiddingDAO;
import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;
import com.spq.group6.server.utils.observer.remote.RemoteObservable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;

import static org.junit.Assert.assertFalse;

public class CountDownTest {
    private User seller, buyer;
    private Product product;
    private Auction auction;
    private BiddingDAO biddingDAO = new BiddingDAO();
    private int offset;
    private Thread auctionCountdown;

    @Before
    public void setUp(){
        String usernameSeller = "seller", usernameBuyer = "buyer", pass = "test_pass", country = "uk";
        String prodctName = "product", productDescription = "Description";
        offset = 1000;
        Timestamp limit = new Timestamp(System.currentTimeMillis() + offset);

        seller = new User(usernameSeller, pass, country);
        buyer = new User(usernameBuyer, pass, country);
        product = new Product(prodctName, productDescription);
        auction = new Auction(seller, product, limit, 12, null);
    }

    @Test
    public void testCountDown() throws InterruptedException {
        biddingDAO.createUser(seller);
        biddingDAO.createUser(buyer);
        biddingDAO.updateProduct(product);
        biddingDAO.persistAuction(auction);
        AuctionLocks.setLock(auction.getAuctionID()); // create lock for auction
        RemoteObservable observable = new RemoteObservable();
        auctionCountdown = new Thread(new AuctionCountdown(auction, observable));
        auctionCountdown.start();

        Thread.sleep(offset + 1000);

        AuctionLocks.getLock(auction.getAuctionID()).lock();
        auction = biddingDAO.getAuctionByID(auction.getAuctionID());
        assertFalse(auction.isOpen());
        AuctionLocks.getLock(auction.getAuctionID()).unlock();
    }

    @After
    public void tearDown() throws InterruptedException {
        auctionCountdown.join();
        biddingDAO.deleteUser(buyer);
        biddingDAO.deleteUser(seller);
    }
}
