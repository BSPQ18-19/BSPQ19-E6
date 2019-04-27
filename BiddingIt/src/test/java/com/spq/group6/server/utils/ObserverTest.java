package com.spq.group6.server.utils;

import com.spq.group6.server.dao.BiddingDAO;
import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;
import com.spq.group6.server.exceptions.AuctionException;
import com.spq.group6.server.exceptions.UserException;
import com.spq.group6.server.remote.IServer;
import com.spq.group6.server.remote.Server;
import com.spq.group6.server.services.AccountService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.rmi.RemoteException;
import java.sql.Timestamp;

import static org.junit.Assert.*;

public class ObserverTest {
    private User seller, buyer;
    private Product product;
    private Auction auction;
    private BiddingDAO biddingDAO = new BiddingDAO();
    private IServer server;
    private int offset;
    private ObserverDemo observerDemo;

    @Before
    public void setUp() throws RemoteException {
        server = new Server();
        String usernameSeller = "seller", usernameBuyer = "buyer", pass = "test_pass", country = "uk";
        String prodctName = "product", productDescription = "Description";
        offset = 1000;
        Timestamp limit = new Timestamp(System.currentTimeMillis() + offset);

        seller = new User(usernameSeller, pass, country);
        buyer = new User(usernameBuyer, pass, country);
        product = new Product(prodctName, productDescription);
        auction = new Auction(seller, product, limit, 12, null);

        biddingDAO.createUser(seller);
        biddingDAO.createUser(buyer);
        seller.getOwnedProducts().add(product);
        biddingDAO.updateUser(seller);
        observerDemo = new ObserverDemo();
        AccountService.observable.addRemoteObserver(observerDemo);
    }

    @Test
    public void testUserDeleted() throws InterruptedException, RemoteException, UserException {
        server.logIn(buyer.getUsername(), buyer.getPassword(),observerDemo);
        server.deleteUser(buyer);
        buyer = null;

        assertFalse(observerDemo.auctionClosed);
        assertFalse(observerDemo.auctionDeleted);
        assertFalse(observerDemo.newBid);
        assertTrue(observerDemo.userDeleted);
    }

    @Test
    public void testBidObserver() throws InterruptedException, RemoteException, AuctionException, UserException {
        auction = server.createPublicAuction(auction.getOwner(), auction.getProduct(), auction.getDayLimit(), auction.getInitialPrice());
        // Add observer
        assertFalse(observerDemo.auctionClosed);
        server.logIn(buyer.getUsername(), buyer.getPassword(), observerDemo);
        server.bid(auction,buyer, auction.getInitialPrice()+1);

        Thread.sleep(offset + 1000);

        assertTrue(observerDemo.auctionClosed);
        assertFalse(observerDemo.auctionDeleted);
        assertTrue(observerDemo.newBid);
        assertFalse(observerDemo.userDeleted);
        auction = BiddingLocks.lockAndGetAuction(auction);
        assertFalse(auction.isOpen());
        BiddingLocks.unlockAuction(auction);
    }

    @Test
    public void testAuctionDeletedObserver() throws InterruptedException, RemoteException, AuctionException, UserException {
        auction = server.createPublicAuction(auction.getOwner(), auction.getProduct(), auction.getDayLimit(), auction.getInitialPrice());
        // Add observer
        assertFalse(observerDemo.auctionClosed);
        server.logIn(buyer.getUsername(), buyer.getPassword(), observerDemo);
        server.bid(auction,buyer, auction.getInitialPrice()+1);
        server.deleteAuction(auction);

        Thread.sleep(offset + 1000);

        assertFalse(observerDemo.auctionClosed);
        assertTrue(observerDemo.auctionDeleted);
        assertTrue(observerDemo.newBid);
        assertFalse(observerDemo.userDeleted);
        auction = biddingDAO.getAuctionByID(auction.getAuctionID());
        assertNull(auction);
    }

    @Test
    public void testNewBidAndAuctionDeletedObserver() throws InterruptedException, RemoteException, AuctionException, UserException {
        auction = server.createPublicAuction(auction.getOwner(), auction.getProduct(), auction.getDayLimit(), auction.getInitialPrice());
        // Add observer
        assertFalse(observerDemo.auctionClosed);
        server.deleteAuction(auction);

        Thread.sleep(offset + 1000);

        assertFalse(observerDemo.auctionClosed);
        assertTrue(observerDemo.auctionDeleted);
        assertFalse(observerDemo.newBid);
        assertFalse(observerDemo.userDeleted);
        auction = biddingDAO.getAuctionByID(auction.getAuctionID());
        assertNull(auction);
    }

    @After
    public void tearDown() throws InterruptedException {
        if(seller!=null){
            biddingDAO.deleteUser(seller);
        }
        if(buyer != null) {
            biddingDAO.deleteUser(buyer);
        }

    }
}
