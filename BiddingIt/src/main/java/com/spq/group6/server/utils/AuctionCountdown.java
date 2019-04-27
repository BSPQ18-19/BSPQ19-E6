package com.spq.group6.server.utils;

import com.spq.group6.server.dao.BiddingDAO;
import com.spq.group6.server.dao.IBiddingDAO;
import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.Bid;
import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;
import com.spq.group6.server.services.AccountService;
import com.spq.group6.server.services.AuctionService;
import com.spq.group6.server.utils.logger.ServerLogger;
import com.spq.group6.server.utils.observer.events.AuctionClosedEvent;

import java.sql.Timestamp;
import java.util.concurrent.locks.Lock;

public class AuctionCountdown implements Runnable {
    private Auction auction;
    private long auctionID;
    private static IBiddingDAO biddingDAO = new BiddingDAO();

    public AuctionCountdown(Auction auction){
        this.auction = auction;
        auctionID = auction.getAuctionID();
    }

    public void run() {
        ServerLogger.logger.debug("Countdown started for auction: " + auction.getAuctionID());
        Timestamp now = new Timestamp(System.currentTimeMillis());
        long remainingMilis = auction.getDayLimit().getTime() - now.getTime();
        try {
            Thread.sleep(Math.max(remainingMilis, 0));
            ServerLogger.logger.debug("Countdown ended for auction: " + auction.getAuctionID());
        } catch (InterruptedException e) {
            ServerLogger.logger.debug("Admin has deleted auction: " + auctionID);
        }
        Auction oldAuction = auction;
        auction = BiddingLocks.lockAndGetAuction(auction);
        if (auction == null){ // This means it has been deleted by Admin
            AuctionService.countdownObservables.remove(auctionID);
            BiddingLocks.unlockAuction(oldAuction);
            return;
        }

        auction.setOpen(false);
        biddingDAO.persistAuction(auction);

        Bid bid = auction.getHighestBid();
        User buyer = null;
        if (bid != null){
            buyer = bid.getUser();
        }
        if (bid != null && bid.getUser() != null && bid.getUser().getMoney() >= bid.getAmount()) {
            User seller = BiddingLocks.lockAndGetUser(auction.getOwner());
            buyer = BiddingLocks.lockAndGetUser(buyer);

            try{
                Product product = auction.getProduct();
                seller.getOwnedProducts().remove(product);
                buyer.getOwnedProducts().add(product);
                biddingDAO.updateUser(seller);
                biddingDAO.updateUser(buyer);
                ServerLogger.logger.debug("Ended auction with exchange: " + auction.getAuctionID());
            }catch (Exception e){
                BiddingLocks.unlockUser(seller);
                BiddingLocks.unlockUser(buyer);
                throw e;
            }

        } else{
            endUnsoldAuction();
        }
        // notify remote observers
        AccountService.observable.notifyRemoteObservers(new AuctionClosedEvent(auction));
        BiddingLocks.unlockAuction(auction);
    }

    private void endUnsoldAuction(){
        ServerLogger.logger.debug("Ended auction without exchange: " + auction.getAuctionID());
        User seller = BiddingLocks.lockAndGetUser(auction.getOwner());
        try {
            Bid highestBid = auction.getHighestBid();

            auction.setHighestBid(null);
            seller.getOwnedProducts().add(auction.getProduct());
            biddingDAO.persistAuction(auction);
            biddingDAO.updateUser(seller);
            if (highestBid != null) {
                biddingDAO.deleteBid(highestBid);
            }
        }catch (Exception e){
            BiddingLocks.unlockAuction(auction);
            BiddingLocks.unlockUser(seller);
            throw e;
        }
        BiddingLocks.unlockUser(seller);
    }

}
