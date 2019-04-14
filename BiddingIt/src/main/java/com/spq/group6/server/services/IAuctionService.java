package com.spq.group6.server.services;

import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;
import com.spq.group6.server.exceptions.AuctionException;
import com.spq.group6.server.exceptions.BidException;

import java.sql.Timestamp;
import java.util.ArrayList;

public interface IAuctionService {
    public Auction createPublicAuction(User owner, Product product, Timestamp dayLimit, float initialPrice);

    public Auction bid(Auction auction, User user, float amount) throws BidException, AuctionException;

    public ArrayList<Auction> searchAuctionByCountry(String country);

    public ArrayList<Auction> searchAuctionByProductName(String name);
}
