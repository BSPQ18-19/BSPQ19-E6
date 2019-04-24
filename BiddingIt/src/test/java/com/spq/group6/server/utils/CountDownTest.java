package com.spq.group6.server.utils;

import com.spq.group6.server.dao.AccountDAO;
import com.spq.group6.server.dao.AuctionDAO;
import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;
import com.spq.group6.server.utils.observer.remote.RemoteObservable;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;

public class CountDownTest {
    User seller, buyer;
    Product product;
    Auction auction;
    AccountDAO accountDAO = new AccountDAO();
    AuctionDAO auctionDAO = new AuctionDAO();
    @Before
    public void setUp(){
        String usernameSeller = "seller", usernameBuyer = "buyer", pass = "test_pass", country = "uk";
        String prodctName = "product", productDescription = "Description";
        Timestamp limit = new Timestamp(System.currentTimeMillis() + 10000);
        float initialPrice = 12;

        seller = new User(usernameSeller, pass, country);
        buyer = new User(usernameBuyer, pass, country);
        product = new Product(prodctName, productDescription);
        auction = new Auction(seller, product, limit, initialPrice, null);
    }

    @Test
    public void testCountDown() throws InterruptedException {
        accountDAO.createUser(seller);
        accountDAO.createUser(buyer);
        accountDAO.updateProduct(product);
        auctionDAO.persistAuction(auction);

        AuctionLocks.setLock(auction.getAuctionID()); // create lock for auction
        RemoteObservable observable = new RemoteObservable();
        Thread auctionCountdown = new Thread(new AuctionCountdown(auction, observable));

        Thread.sleep(2000);

        auctionDAO.isOpen(auction.getAuctionID());
    }
}
