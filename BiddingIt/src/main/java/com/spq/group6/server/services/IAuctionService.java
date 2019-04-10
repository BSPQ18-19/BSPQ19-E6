package com.spq.group6.server.services;

import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;

import java.rmi.RemoteException;
import java.sql.Timestamp;

public interface IAuctionService {
    public Auction createPublicAuction(User owner, Product product, Timestamp dayLimit, float initialPrice);
    public Auction bid(Auction auction, User user, float amount);
}
