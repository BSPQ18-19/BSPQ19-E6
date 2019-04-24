package com.spq.group6.server.utils;

import com.spq.group6.server.dao.BiddingDAO;
import com.spq.group6.server.dao.IBiddingDAO;
import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.Bid;
import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;
import com.spq.group6.server.utils.observer.remote.RemoteObservable;

import java.sql.Timestamp;
import java.util.concurrent.locks.Lock;

public class AuctionCountdown implements Runnable {
    private Auction auction;
    private RemoteObservable observable;
    private static IBiddingDAO biddingDAO = new BiddingDAO();

    public AuctionCountdown(Auction auction, RemoteObservable observable){
        this.auction = auction;
        this.observable = observable;
    }

    public void run() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        long remainingMilis = auction.getDayLimit().getTime() - now.getTime();
        try {
            Thread.sleep(remainingMilis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Set as 'closed' the auction
        Lock auctionLock = AuctionLocks.getLock(auction.getAuctionID());
        auctionLock.lock();
        biddingDAO.closeAuction(auction.getAuctionID());
        Bid bid = biddingDAO.getHighestBid(auction.getAuctionID());
        auctionLock.unlock();

        Product product = auction.getProduct();
        User seller = auction.getOwner();
        User buyer = bid.getUser();
        // Check if there was any bid and if the bid maker has enough money
        if (buyer != null && buyer.getMoney()>=bid.getAmount()){
            seller.getOwnedProducts().remove(product);
            buyer.getOwnedProducts().add(product);
            biddingDAO.updateUser(seller);
            biddingDAO.updateUser(buyer);
        }
        // TODO: what to do if user has not enough money

        // notify remote observers
        observable.notifyRemoteObservers(auction);
    }
}
