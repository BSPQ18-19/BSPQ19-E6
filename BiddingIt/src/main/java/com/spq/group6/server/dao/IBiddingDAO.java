package com.spq.group6.server.dao;

import com.spq.group6.server.data.*;

import java.util.ArrayList;

public interface IBiddingDAO {
    // Account DAO
    void createUser(User user);

    User getUserByUsername(String username);

    void updateUser(User user);

    void updateProduct(Product product);

    void deleteProduct(Product product);

    // Admin DAO

    void deleteUser(User user);

    Administrator getAdministratorByUsername(String username);

    void deleteAuction(Auction auction);

    // Auction DAO

    void persistAuction(Auction auction);

    void deleteBid(Bid bid);

    ArrayList<Auction> getAuctionByCountry(String country);

    ArrayList<Auction> getAuctionByProductName(String name);

    Auction getAuctionByID(long auctionID);

    ArrayList<Auction> getAuctionByUser(User user);

}
