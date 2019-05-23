package com.spq.group6.server.services;

import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;
import com.spq.group6.server.exceptions.AuctionException;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Auctions related service's interface.
 * <p>
 * BiddingIt server's Application for Auction related services:
 * - Auction creation
 * - Bid
 * - Auctions's retrieval
 */
public interface IAuctionService {
    Auction createAuction(User owner, Product product, Timestamp dayLimit, float initialPrice, String password) throws AuctionException;

    Auction bid(Auction auction, User user, float amount) throws AuctionException;

    ArrayList<Auction> searchAuctionByCountry(User requester, String country);

    ArrayList<Auction> searchAuctionByProductName(User requester, String name);

    ArrayList<Auction> getAllAuctions(User requester);

    void startUncheckedAuctions();
}
