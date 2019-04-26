package com.spq.group6.server.dao;

import com.spq.group6.server.data.*;

import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;

public interface IBiddingDAO {
    // Account DAO
    void createUser(User user);

    User getUserByUsername(String username);

    void updateUser(User user);

    void updateProduct(Product product);

    void deleteProduct(Product product);

    // Admin DAO
    void createAdministrator(Administrator administrator);
    
    Administrator getAdministratorByUsername(String username);

    void deleteUser(User user);

    void deleteAuction(Auction auction);

    ArrayList<User> getAllUsers();

    ArrayList<Auction> getAuctionByUser(User user);

    // Auction DAO

    void persistAuction(Auction auction);

    void deleteBid(Bid bid);

    ArrayList<Auction> getAuctionByCountry(User requester, String country);

    ArrayList<Auction> getAuctionByProductName(User requester, String name);

    Auction getAuctionByID(long auctionID);

    ArrayList<Auction> getAllAuctionsExceptRequester(User requester);

    ArrayList<Auction> getAllAuctions();

}
