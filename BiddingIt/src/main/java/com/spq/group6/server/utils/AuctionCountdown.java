package com.spq.group6.server.utils;

import com.spq.group6.server.dao.IAuctionDAO;
import com.spq.group6.server.data.Auction;

import java.sql.Timestamp;
import java.util.concurrent.locks.Lock;

public class AuctionCountdown implements Runnable {
    private Auction auction;

    public AuctionCountdown(Auction auction){
        this.auction = auction;
    }

    public void run() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        long remainingMilis = auction.getDayLimit().getTime() - now.getTime();
        try {
            Thread.sleep(remainingMilis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // TODO: decide what to do after finishing countdown
        Lock auctionLock = AuctionLocks.getLock(auction.getAuctionID());
        auctionLock.lock();
        IAuctionDAO auctionDAO = null; // TODO: get actual object
        auctionDAO.closeAuction(auction.getAuctionID());
        auctionLock.unlock();
    }
}
