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

public class AuctionService implements IAuctionService {
    private IBiddingDAO biddingDAO;
    public static HashMap<Long, Thread> countdownObservables;

    public AuctionService() {
        biddingDAO = new BiddingDAO();
        countdownObservables = new HashMap<Long, Thread>();

        for (Auction auction : biddingDAO.getAllAuctions()) {
            initAuction(auction);
        }
    }

    public Auction createPublicAuction(User owner, Product product, Timestamp dayLimit, float initialPrice) {
        Auction auction = new Auction(owner, product, dayLimit, initialPrice, null);
        biddingDAO.persistAuction(auction);
        // Remove product from Owner until auction is finished
        owner.getOwnedProducts().remove(product);
        biddingDAO.updateUser(owner);

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


    private void initAuction(Auction auction) {
        BiddingLocks.setAuctionLock(auction); // create lock for auction
        Thread auctionCountdown = new Thread(new AuctionCountdown(auction));
        auctionCountdown.start(); // Run thread for auction countdown
        countdownObservables.put(auction.getAuctionID(), auctionCountdown);
    }

}
