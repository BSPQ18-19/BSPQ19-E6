package com.spq.group6.server.dao;

import com.spq.group6.server.data.*;

import java.util.ArrayList;

/**
 * Interface for BiddingIt server's DAO (data persistance)
 */
public interface IBiddingDAO {
    // Account DAO

    /**
     * Method for creating and updating a User on the database
     *
     * @param user User that will be persisted
     */
    void persistUser(User user);

    /**
     * Method for getting a User by its username
     *
     * @param username Username that will be matched
     * @return User that matches the username
     */
    User getUserByUsername(String username);

    /**
     * Method for creating and updating a Product on the database
     *
     * @param product Product that will be persisted
     */
    void persistUser(Product product);

    /**
     * Method for deleting a Product from the database
     *
     * @param product Product that will be deleted
     */
    void deleteProduct(Product product);

    // Admin DAO

    /**
     * Method for creating or updating an Administrator on the database, needed for tests
     *
     * @param administrator Administrator that will be persisted
     */
    void persistAdministrator(Administrator administrator);

    /**
     * Method for deleting and Administrator from database, needed for tests
     * @param admin Administrator that will be deleted
     */
    void deleteAdministrator(Administrator admin);

    /**
     * Method for getting an Admisnitrator by its username from the database
     *
     * @param username Username that will be matched
     * @return Administrator that matches the username
     */
    Administrator getAdministratorByUsername(String username);

    /**
     * Method for deleting a User from the database.
     * It deletes User's Auctions, Products and Bids
     *
     * @param user User that will be deleted
     */
    void deleteUser(User user);

    /**
     * Method for deleting an Auction from the database
     *
     * @param auction Auction that will be deleted
     */
    void deleteAuction(Auction auction);

    /**
     * Method for getting all User's from the database
     *
     * @return ArrayList containing all the Users from the database
     */
    ArrayList<User> getAllUsers();

    /**
     * Method for getting User's Auctions from the database
     *
     * @param user User that is Auction's owner
     * @return ArrayList containing User's Auctions
     */
    ArrayList<Auction> getAuctionByUser(User user);

    // Auction DAO

    /**
     * Method for creating and updating an Auction on the database
     *
     * @param auction Auction that will be persisted
     */
    void persistAuction(Auction auction);

    /**
     * Method for deleting a Bid from the database
     *
     * @param bid Bid that will be deleted
     */
    void deleteBid(Bid bid);

    /**
     * Method for getting all Auctions that matches
     * owner's country, that it is opened and not owned by the requester
     *
     * @param requester User that requests Auctions
     * @param country   Country that will be matched
     * @return ArrayList containing Auctions that match conditions
     */
    ArrayList<Auction> getAuctionByCountry(User requester, String country);

    /**
     * Method for getting all Auctions that matches
     * product's name, that it is opened and not owned by the requester
     *
     * @param requester User that requests Auctions
     * @param name      Product name that will be matched
     * @return ArrayList containing Auctions that match conditions
     */
    ArrayList<Auction> getAuctionByProductName(User requester, String name);

    /**
     * Method for getting an Auction by its ID
     *
     * @param auctionID ID that will be matched
     * @return Auction that matched the ID
     */
    Auction getAuctionByID(long auctionID);

    /**
     * Method for getting all opened auctions except
     * ones owned by the requester
     *
     * @param requester User that requests Auctions
     * @return ArrayList containing Auctions not owned by the requester
     */
    ArrayList<Auction> getAllAuctionsExceptRequester(User requester);

    /**
     * Method for getting all opened auctions
     *
     * @return ArrayList containing all opened Auctions
     */
    ArrayList<Auction> getAllAuctions();
}
