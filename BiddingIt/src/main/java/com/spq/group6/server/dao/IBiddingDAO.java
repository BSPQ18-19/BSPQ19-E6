package com.spq.group6.server.dao;

import com.spq.group6.server.data.*;

import java.util.ArrayList;

public interface IBiddingDAO {
    void createUser(User user);

    User getUserByUsername(String username);

    void updateUser(User user);

    void deleteUser(User user);

    void updateProduct(Product product);

    void deleteProduct(Product product);

    Administrator getAdministratorByUsername(String username);

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
