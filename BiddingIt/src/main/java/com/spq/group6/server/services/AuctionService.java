package com.spq.group6.server.services;

import com.spq.group6.server.dao.BiddingDAO;
import com.spq.group6.server.dao.IBiddingDAO;
import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.Bid;
import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;
import com.spq.group6.server.exceptions.AuctionException;
import com.spq.group6.server.utils.AuctionCountdown;
import com.spq.group6.server.utils.BiddingLocks;
import com.spq.group6.server.utils.observer.events.NewBidEvent;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * BiddingIt server's Application for Auction related services:
 * - Auction creation
 * - Bid
 * - Auctions's retrieval
 */
public class AuctionService implements IAuctionService {
    public static HashMap<Long, Thread> countdownObservables;
    /**
     * HashMap containing all AuctionCountdowns that are
     * running.  Countdowns are Threads, and they are identified by its Auction's ID (long)
     */
    private IBiddingDAO biddingDAO;

    public AuctionService() {
        biddingDAO = new BiddingDAO();
        countdownObservables = new HashMap<Long, Thread>();

        for (Auction auction : biddingDAO.getAllAuctions()) {
            initAuction(auction);
        }
    }

    public Auction createAuction(User owner, Product product, Timestamp dayLimit, float initialPrice, String password) throws AuctionException {
        if (initialPrice < 0) throw new AuctionException("Invalid <0 initial price");
        Auction auction = new Auction(owner, product, dayLimit, initialPrice, password);
        biddingDAO.persistAuction(auction);
        // Remove product from Owner until auction is finished
        owner.getOwnedProducts().remove(product);
        biddingDAO.persistUser(owner);

        initAuction(auction);
        return auction;
    }

    public Auction bid(Auction auction, User user, float amount) throws AuctionException {
        auction = BiddingLocks.lockAndGetAuction(auction);

        try {
            if (!auction.isOpen()) {
                throw new AuctionException("Auction is closed");
            }
            Bid oldBid = auction.getHighestBid();
            if (amount < auction.getInitialPrice() || (oldBid != null && oldBid.getAmount() >= amount)) {
                throw new AuctionException("Too low bid");
            }
            Bid newBid = new Bid(user, amount);
            auction.setHighestBid(newBid);
            biddingDAO.persistAuction(auction);
            biddingDAO.deleteBid(oldBid);
            // Notify about new Bid
            NewBidEvent newBidEvent = new NewBidEvent(auction);
            AccountService.observable.notifyRemoteObservers(newBidEvent);
        } catch (Exception e) {
            BiddingLocks.unlockAuction(auction);
            throw e;
        }
        BiddingLocks.unlockAuction(auction);
        return auction;
    }

    public ArrayList<Auction> searchAuctionByCountry(User requester, String country) {
        return biddingDAO.getAuctionByCountry(requester, country);
    }

    public ArrayList<Auction> searchAuctionByProductName(User requester, String name) {
        return biddingDAO.getAuctionByProductName(requester, name);
    }

    public ArrayList<Auction> getAllAuctions(User requester) {
        return biddingDAO.getAllAuctionsExceptRequester(requester);
    }

    public void startUncheckedAuctions() {
        for (Auction auction : biddingDAO.getAllAuctions()) {
            if (!countdownObservables.containsKey(auction.getAuctionID())) {
                initAuction(auction);
            }
        }
    }


    /**
     * Method for creating a Lock for an Auction and a CountDown thread implementing
     * the logic when an Auction finishes
     *
     * @param auction Auction that will be initalized
     */
    void initAuction(Auction auction) {
        BiddingLocks.setAuctionLock(auction); // create lock for auction
        Thread auctionCountdown = new Thread(new AuctionCountdown(auction));
        auctionCountdown.start(); // Run thread for auction countdown
        countdownObservables.put(auction.getAuctionID(), auctionCountdown);
    }

}
