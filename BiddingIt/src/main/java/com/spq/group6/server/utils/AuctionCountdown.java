package com.spq.group6.server.utils;

import com.spq.group6.server.dao.IAuctionDAO;
import com.spq.group6.server.data.Auction;
import com.spq.group6.server.utils.observer.remote.RemoteObservable;

import java.sql.Timestamp;
import java.util.Observable;
import java.util.concurrent.locks.Lock;

public class AuctionCountdown implements Runnable {
    private Auction auction;
    private RemoteObservable observable;

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
        IAuctionDAO auctionDAO = null; // TODO: get actual object
        auctionDAO.closeAuction(auction.getAuctionID());
        auctionLock.unlock();
        // notify remote observers
        observable.notifyRemoteObservers(auction);
    }
}
