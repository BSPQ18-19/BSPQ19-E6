package com.spq.group6.server.services;

import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;

import java.sql.Timestamp;

public class AuctionService implements IAuctionService {

    public Auction createPublicAuction(User owner, Product product, Timestamp dayLimit, float initialPrice) {
        return null;
    }

    public Auction bid(Auction auction, User user, float amount) {
        return null;
    }
}
