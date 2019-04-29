package com.spq.group6.server.services;

import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;
import com.spq.group6.server.exceptions.AuctionException;

import java.sql.Timestamp;
import java.util.ArrayList;

public interface IAuctionService {
    Auction createPublicAuction(User owner, Product product, Timestamp dayLimit, float initialPrice);

    Auction bid(Auction auction, User user, float amount) throws AuctionException;

    ArrayList<Auction> searchAuctionByCountry(User requester, String country);

    ArrayList<Auction> searchAuctionByProductName(User requester, String name);

    ArrayList<Auction> getAllAuctions(User requester);
}
