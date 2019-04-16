package com.spq.group6.server.dao;

import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.Bid;

import java.util.ArrayList;

public interface IAuctionDAO {
    public void persistAuction(Auction auction);

    public void deleteAuction(Auction auction);

    public void persistBid(long auctionID, Bid bid);

    public void deleteBid(Bid bid);

    public ArrayList<Auction> getAuctionByCountry(String country);

    public ArrayList<Auction> getAuctionByProductName(String name);

    public boolean isOpen(long auctionID);

    public Bid getHighestBid(long auctionID);

    public void closeAuction(long auctionID);
}
