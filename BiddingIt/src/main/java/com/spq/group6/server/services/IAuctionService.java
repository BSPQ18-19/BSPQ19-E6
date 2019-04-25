package com.spq.group6.server.services;

import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.Bid;
import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;
import com.spq.group6.server.exceptions.AuctionException;

import com.spq.group6.server.utils.observer.remote.IRemoteObservable;

import java.sql.Timestamp;
import java.util.ArrayList;

public interface IAuctionService extends IRemoteObservable {
    public Auction createPublicAuction(User owner, Product product, Timestamp dayLimit, float initialPrice);

    public Auction bid(Auction auction, User user, float amount) throws AuctionException;

    public ArrayList<Auction> searchAuctionByCountry(User requester, String country);

    public ArrayList<Auction> searchAuctionByProductName(User requester, String name);

    public ArrayList<Auction> getAllAuctions(User requester);
}
