package com.spq.group6.server.dao;

import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.Bid;
import com.spq.group6.server.data.User;

import java.util.ArrayList;

public interface IAuctionDAO {
    public void persistAuction(Auction auction);

    public void persistBid(Auction auction, Bid bid);

    public void deleteBid(Bid bid);

    public ArrayList<Auction> getAuctionByCountry(String country);

    public ArrayList<Auction> getAuctionByProductName(String name);

    public boolean isOpen(long auctionID);

    public Bid getHighestBid(long auctionID);

    public void closeAuction(long auctionID);

    public ArrayList<Auction> getAuctionByUser(User user);
}
