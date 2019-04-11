package com.spq.group6.server.services;

import com.spq.group6.server.dao.IAuctionDAO;
import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.Bid;
import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;
import com.spq.group6.server.exceptions.BidException;

import java.sql.Timestamp;

public class AuctionService implements IAuctionService {
    IAuctionDAO auctionDAO = null;

    public AuctionService() {
        // Initialize auctionDAO
    }

    public Auction createPublicAuction(User owner, Product product, Timestamp dayLimit, float initialPrice) {
        Auction auction = new Auction(owner, product, dayLimit, initialPrice, null);
        auctionDAO.persistAuction(auction);
        return auction;
    }

    public Auction bid(Auction auction, User user, float amount) throws BidException{
        Bid newBid = new Bid(user, amount);
        Bid oldBid = auction.getHighestBid();

        auction.setHighestBid(newBid);
        auctionDAO.persistAuction(auction);
        auctionDAO.deleteBid(oldBid);
        return auction;
    }

    private void checkNewBid(Bid newBid, Bid oldBid) throws BidException{
        if (newBid.getAmount()<= oldBid.getAmount()) {
            throw new BidException("Invalid Bid value.");
        }
    }
}
