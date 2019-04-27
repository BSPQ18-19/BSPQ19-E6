package com.spq.group6.server.utils;

import com.spq.group6.server.dao.BiddingDAO;
import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;
import com.spq.group6.server.remote.IServer;
import com.spq.group6.server.remote.Server;
import com.spq.group6.server.services.AccountService;
import com.spq.group6.server.utils.observer.remote.RemoteObservable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.rmi.RemoteException;
import java.sql.Timestamp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CountDownTest {
    private User seller, buyer;
    private Product product;
    private Auction auction;
    private BiddingDAO biddingDAO = new BiddingDAO();
    private int offset;
    private Thread auctionCountdown;
    private ObserverDemo observerDemo;

    @Before
    public void setUp() throws RemoteException {
        String usernameSeller = "seller", usernameBuyer = "buyer", pass = "test_pass", country = "uk";
        String prodctName = "product", productDescription = "Description";
        offset = 1000;
        Timestamp limit = new Timestamp(System.currentTimeMillis() + offset);

        seller = new User(usernameSeller, pass, country);
        buyer = new User(usernameBuyer, pass, country);
        product = new Product(prodctName, productDescription);
        auction = new Auction(seller, product, limit, 12, null);

        biddingDAO.createUser(buyer);
        biddingDAO.updateProduct(product);
        biddingDAO.persistAuction(auction);
        BiddingLocks.setAuctionLock(auction); // create lock for auction
        observerDemo = new ObserverDemo();
        AccountService.observable.addRemoteObserver(observerDemo);
    }

    @Test
    public void testNormalCountDown() throws InterruptedException {
        RemoteObservable observable = new RemoteObservable();
        auctionCountdown = new Thread(new AuctionCountdown(auction));
        auctionCountdown.start();
        // Add observer

        observable.addRemoteObserver(observerDemo);
        assertFalse(observerDemo.auctionClosed);

        Thread.sleep(offset + 1000);

        assertTrue(observerDemo.auctionClosed);
        assertFalse(observerDemo.auctionDeleted);
        assertFalse(observerDemo.newBid);
        assertFalse(observerDemo.userDeleted);
        auction = BiddingLocks.lockAndGetAuction(auction);
        assertFalse(auction.isOpen());
        BiddingLocks.unlockAuction(auction);
    }

    @After
    public void tearDown() throws InterruptedException {
        auctionCountdown.join();
        biddingDAO.deleteUser(buyer);
        biddingDAO.deleteUser(seller);
    }
}
