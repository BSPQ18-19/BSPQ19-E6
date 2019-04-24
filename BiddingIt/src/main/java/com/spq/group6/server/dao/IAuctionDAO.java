package com.spq.group6.server.dao;

import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.Bid;
import com.spq.group6.server.data.User;

import java.util.ArrayList;

public interface IAuctionDAO {
    void persistAuction(Auction auction);

    void persistBid(Auction auction, Bid bid);

    void deleteBid(Bid bid);

    void deleteAuction(Auction auction);

    ArrayList<Auction> getAuctionByCountry(String country);

    ArrayList<Auction> getAuctionByProductName(String name);

    boolean isOpen(long auctionID);

    Bid getHighestBid(long auctionID);

    void closeAuction(long auctionID);

    ArrayList<Auction> getAuctionByUser(User user);
}
