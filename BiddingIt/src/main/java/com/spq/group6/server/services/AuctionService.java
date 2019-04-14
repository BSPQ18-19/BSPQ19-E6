package com.spq.group6.server.services;

import com.spq.group6.server.dao.IAuctionDAO;
import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.Bid;
import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;
import com.spq.group6.server.exceptions.AuctionException;
import com.spq.group6.server.utils.AuctionCountdown;
import com.spq.group6.server.utils.AuctionLocks;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;

public class AuctionService implements IAuctionService {
    IAuctionDAO auctionDAO = null;

    public AuctionService() {
        // Initialize auctionDAO
    }

    public Auction createPublicAuction(User owner, Product product, Timestamp dayLimit, float initialPrice) {
        Auction auction = new Auction(owner, product, dayLimit, initialPrice, null);
        auctionDAO.persistAuction(auction);

        AuctionLocks.setLock(auction.getAuctionID()); // create lock for auction
        Thread auctionCountdown = new Thread(new AuctionCountdown(auction));
        auctionCountdown.start(); // Run thread for auction countdown
        
        return auction;
    }

    public Auction bid(Auction auction, User user, float amount) throws AuctionException {
        Lock auctionLock = AuctionLocks.getLock(auction.getAuctionID());
        auctionLock.lock();

        if (!auctionDAO.isOpen(auction.getAuctionID())){
            throw  new AuctionException("Auction is closed");
        }
        Bid oldBid = auctionDAO.getHighestBid(auction.getAuctionID());
        if (oldBid.getAmount() >= amount){
            throw  new AuctionException("Too low bid");
        }
        Bid newBid = new Bid(user, amount);
        auction.setHighestBid(newBid);
        auctionDAO.persistAuction(auction);
        auctionDAO.deleteBid(oldBid);

        auctionLock.unlock();
        return auction;
    }

    public ArrayList<Auction> searchAuctionByCountry(String country) {
        return auctionDAO.getAuctionByCountry(country);
    }

    public ArrayList<Auction> searchAuctionByProductName(String name) {
        return auctionDAO.getAuctionByProductName(name);
    }

    private void checkNewBid(Bid newBid, Bid oldBid) throws AuctionException {
        if (newBid.getAmount() <= oldBid.getAmount()) {
            throw new AuctionException("Invalid Bid value.");
        }
    }
}
