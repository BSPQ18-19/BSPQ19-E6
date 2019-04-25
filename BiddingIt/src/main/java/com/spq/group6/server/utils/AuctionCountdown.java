package com.spq.group6.server.utils;

import com.spq.group6.server.dao.BiddingDAO;
import com.spq.group6.server.dao.IBiddingDAO;
import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.Bid;
import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;
import com.spq.group6.server.utils.logger.ServerLogger;
import com.spq.group6.server.utils.observer.events.AuctionClosedEvent;
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
        ServerLogger.logger.debug("Countdown started for auction: " + auction.getAuctionID());
        Timestamp now = new Timestamp(System.currentTimeMillis());
        long remainingMilis = auction.getDayLimit().getTime() - now.getTime();
        try {
            Thread.sleep(remainingMilis);
            ServerLogger.logger.debug("Countdown ended for auction: " + auction.getAuctionID());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Lock auctionLock = AuctionLocks.getLock(auction.getAuctionID());
        auctionLock.lock();

        auction = biddingDAO.getAuctionByID(auction.getAuctionID());
        auction.setOpen(false);
        biddingDAO.persistAuction(auction);

        auctionLock.unlock();

        Bid bid = auction.getHighestBid();
        if (bid != null) {
            User buyer = bid.getUser();
            // Check if there was any bid and if the bid maker has enough money
            if (buyer != null && buyer.getMoney() >= bid.getAmount()) {
                Product product = auction.getProduct();
                User seller = auction.getOwner();

                seller.getOwnedProducts().remove(product);
                buyer.getOwnedProducts().add(product);
                biddingDAO.updateUser(seller);
                biddingDAO.updateUser(buyer);
            }
            else {
                endUnsoldAuction();
            }
        } else{
            endUnsoldAuction();
        }
        // notify remote observers
        observable.notifyRemoteObservers(new AuctionClosedEvent(auction));
    }

    private void endUnsoldAuction(){
        ServerLogger.logger.debug("Eneded auction without exchange: " + auction.getAuctionID());
        Lock auctionLock = AuctionLocks.getLock(auction.getAuctionID());

        auctionLock.lock();
        auction.setHighestBid(null);
        auction.getOwner().getOwnedProducts().add(auction.getProduct());
        biddingDAO.persistAuction(auction);
        biddingDAO.updateUser(auction.getOwner());
        auctionLock.unlock();
    }
}
