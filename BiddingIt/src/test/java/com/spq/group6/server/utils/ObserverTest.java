package com.spq.group6.server.utils;

import com.spq.group6.server.dao.BiddingDAO;
import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;
import com.spq.group6.server.exceptions.AccountException;
import com.spq.group6.server.exceptions.AuctionException;
import com.spq.group6.server.remote.IServer;
import com.spq.group6.server.remote.Server;
import com.spq.group6.server.services.AccountService;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
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
    private FakeRemoteObserver fakeRemoteObserver;

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

        biddingDAO.persistUser(seller);
        biddingDAO.persistUser(buyer);
        seller.getOwnedProducts().add(product);
        biddingDAO.persistUser(seller);
        fakeRemoteObserver = new FakeRemoteObserver();
        AccountService.observable.addRemoteObserver(fakeRemoteObserver);
    }

    @Ignore
    @Test
    public void testUserDeleted() throws InterruptedException, RemoteException, AccountException {
        server.deleteUser(buyer);
        buyer = null;

        assertFalse(fakeRemoteObserver.auctionClosed);
        assertFalse(fakeRemoteObserver.auctionDeleted);
        assertFalse(fakeRemoteObserver.newBid);
        assertTrue(fakeRemoteObserver.userDeleted);
    }

    @Test
    public void testBidObserver() throws InterruptedException, RemoteException, AuctionException, AccountException {
        auction = server.createPublicAuction(auction.getOwner(), auction.getProduct(), auction.getDayLimit(), auction.getInitialPrice());
        // Add observer
        assertFalse(fakeRemoteObserver.auctionClosed);
        server.logIn(buyer.getUsername(), buyer.getPassword(), fakeRemoteObserver);
        server.bid(auction, buyer, auction.getInitialPrice() + 1);

        Thread.sleep(offset + 1000);

        assertTrue(fakeRemoteObserver.auctionClosed);
        assertFalse(fakeRemoteObserver.auctionDeleted);
        assertTrue(fakeRemoteObserver.newBid);
        assertFalse(fakeRemoteObserver.userDeleted);
        auction = BiddingLocks.lockAndGetAuction(auction);
        assertFalse(auction.isOpen());
        BiddingLocks.unlockAuction(auction);
    }

    @Test
    public void testAuctionDeletedObserver() throws InterruptedException, RemoteException, AuctionException, AccountException {
        auction = server.createPublicAuction(auction.getOwner(), auction.getProduct(), auction.getDayLimit(), auction.getInitialPrice());
        // Add observer
        assertFalse(fakeRemoteObserver.auctionClosed);
        server.logIn(buyer.getUsername(), buyer.getPassword(), fakeRemoteObserver);
        server.bid(auction, buyer, auction.getInitialPrice() + 1);
        server.deleteAuction(auction);

        Thread.sleep(offset + 1000);

        assertFalse(fakeRemoteObserver.auctionClosed);
        assertTrue(fakeRemoteObserver.auctionDeleted);
        assertTrue(fakeRemoteObserver.newBid);
        assertFalse(fakeRemoteObserver.userDeleted);
        auction = biddingDAO.getAuctionByID(auction.getAuctionID());
        assertNull(auction);
    }

    @Test
    public void testNewBidAndAuctionDeletedObserver() throws InterruptedException, RemoteException, AuctionException, AccountException {
        auction = server.createPublicAuction(auction.getOwner(), auction.getProduct(), auction.getDayLimit(), auction.getInitialPrice());
        // Add observer
        assertFalse(fakeRemoteObserver.auctionClosed);
        server.deleteAuction(auction);

        Thread.sleep(offset + 1000);

        assertFalse(fakeRemoteObserver.auctionClosed);
        assertTrue(fakeRemoteObserver.auctionDeleted);
        assertFalse(fakeRemoteObserver.newBid);
        assertFalse(fakeRemoteObserver.userDeleted);
        auction = biddingDAO.getAuctionByID(auction.getAuctionID());
        assertNull(auction);
    }

    @After
    public void tearDown() throws InterruptedException {
        if (seller != null) {
            biddingDAO.deleteUser(seller);
        }
        if (buyer != null) {
            biddingDAO.deleteUser(buyer);
        }

    }
}
