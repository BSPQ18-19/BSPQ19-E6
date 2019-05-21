package com.spq.group6.server.remote;

import com.spq.group6.server.data.Administrator;
import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;
import com.spq.group6.server.exceptions.AccountException;
import com.spq.group6.server.exceptions.AdministratorException;
import com.spq.group6.server.exceptions.AuctionException;
import com.spq.group6.server.utils.observer.remote.IRemoteObserver;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Remote Façade Interface for BididngIt server
 * <p>
 * This is the API that describes functionality offered by BiddingIt Server.
 * It is used on the User application.
 */
public interface IServer extends Remote {

    // Account API

    /**
     * Method for login and getting access to the system.
     * <p>
     * Checks if User credentials are valid,
     * and returns a User object that matched.
     *
     * @param username User's username
     * @param password User's password
     * @param observer User's observer responsible for handling events sent by server
     * @return Logged in User
     * @throws RemoteException  is raised in case of Error on RMI connection
     * @throws AccountException is raised in case of invalid credentials
     */
    User logIn(String username, String password, IRemoteObserver observer) throws RemoteException, AccountException;

    /**
     * Method for letting know the server a User has logged out, so it can delete its observer
     *
     * @param observer User's observer that will not listen more for Events
     * @throws RemoteException is raised in case of Error on RMI connection
     */
    void logOut(IRemoteObserver observer) throws RemoteException;

    /**
     * Method for creating a new User.
     * <p>
     * This is the method used for creating an Account on BiddingIt
     * server.
     *
     * @param username User's username
     * @param password User's password
     * @param country  User's country. Useful for auctions search
     * @param observer User's observer responsible for handling events sent by server
     * @return Created User
     * @throws RemoteException  is raised in case of Error on RMI connection
     * @throws AccountException is raised in case of invalid credentials
     */
    User signIn(String username, String password, String country, IRemoteObserver observer) throws RemoteException, AccountException;

    /**
     * Method for updating User's password or country.
     * <p>
     * Whenever a User wants to update any of its detail's aacount,
     * this method is called passing its new values. Even if only one
     * value have changed, it is needed to pass both of them.
     *
     * @param user     Old User that will be updated
     * @param password New (or same old) password for updating User
     * @param country  New (or same old) country for updating User
     * @return Updated User
     * @throws RemoteException  is raised in case of Error on RMI connection
     * @throws AccountException is raised in case of invalid values for update
     */
    User updateUser(User user, String country, String password) throws RemoteException, AccountException;

    /**
     * Method for creating a new Product.
     * <p>
     * Whenever a User wants to add a Product to the system,
     * in order to create an Auction for it, this method is called.
     * As every Product is related to a User,
     * it is necessary to pass the User as argument.
     *
     * @param user        Product's owner User
     * @param name        Product's name
     * @param description Product's description
     * @return User with its new Product on 'ownedProducts' ArrayList
     * @throws RemoteException is raised in case of Error on RMI connection
     */
    User createProduct(User user, String name, String description) throws RemoteException;

    /**
     * Method for updating Product's name or description.
     * <p>
     * Whenever a User wants to change details of one of its Products,
     * this method is called.
     *
     * @param product     Product that will be updated
     * @param name        Product's new (or same old) name for updating product
     * @param description Product's new (or same old) description for updating product
     * @return Updated Product
     * @throws RemoteException is raised in case of Error on RMI connection
     */
    Product updateProduct(Product product, String name, String description) throws RemoteException;

    /**
     * Method for deleting a product
     * <p>
     * Whenever a User wants to delete a Product from the system,
     * this method is called. Thi may happen when a User buys a Product
     * in a Bid, and it is not interested on selling it.
     *
     * @param user    Product's owner User
     * @param product Product that will be deleted
     * @return User without Product. Product is deleted from 'ownedProducts' ArrayList
     * @throws RemoteException is raised in case of Error on RMI connection
     */
    User deleteProduct(User user, Product product) throws RemoteException;

    // Auctions API

    /**
     * Method for creating an Auction
     * <p>
     * Whenever a User wants to create a public Auction for
     * selling a product, this method is called. A public Auction
     * accepts Bids from any User.
     * As every Auction is related to a User,
     * it is necessary to pass the User as argument.
     *
     * @param owner        Auction creator or Product's owner
     * @param product      Product that will be sold on Auction
     * @param dayLimit     Timestamp when Auction will be closed
     * @param initialPrice Initial proce for Product
     * @return Created Auction
     * @throws RemoteException  is raised in case of Error on RMI connection
     * @throws AuctionException is raised in case of invalid values for Auction
     */
    Auction createPublicAuction(User owner, Product product, Timestamp dayLimit, float initialPrice) throws RemoteException, AuctionException;

    /**
     * Method for creating a private Auction.
     * <p>
     * Whenever a User wants to create a private Auction for
     * selling a product, this method is called.
     * In contrast to a public Auction, private Auctions require a Password
     * in order to be able to bid Auction's Product.
     *
     * @param owner        Auction creator or Product's owner
     * @param product      Product that will be sold on Auction
     * @param dayLimit     Timestamp when Auction will be closed
     * @param initialPrice Initial proce for Product
     * @param password     Password that the Auction will be protected with the Auction.
     *                     It needs to be different from null.
     * @return Created Auction
     * @throws RemoteException  is raised in case of Error on RMI
     * @throws AuctionException is raised in case of invalid values for Auction
     */
    Auction createPrivateAuction(User owner, Product product, Timestamp dayLimit, float initialPrice, String password) throws RemoteException, AuctionException;

    /**
     * Method for bidding an Auction
     * <p>
     * Whenever a User wants to Bid in an Auction,
     * this method is called.
     * As every Bid is related to a User and an Auction,
     * it is necessary to pass the User and the Auction as arguments.
     *
     * @param auction Auction will be bidded to
     * @param user    Bid creator User
     * @param amount  Bidded amount (money)
     * @return Auction with the new Bid
     * @throws RemoteException  is raised in case of Error on RMI connection
     * @throws AuctionException is raised in case of invalid Bid amount
     */
    Auction bid(Auction auction, User user, float amount) throws RemoteException, AuctionException;

    /**
     * Method for searching auctions by Auction owner's country.
     * <p>
     * Whenever a User wants to browse Auctions and search them by Country,
     * this method is called.
     * The result does not include Auctions created by the User,
     * nor Auction already closed. That is why the User needs to be passed as an argument.
     * A notification is sent to IRemoteObservers informing about a new Bid.
     * Users that have Bidded on the Auction or the one that created may react to it.
     *
     * @param requester User that requests Auctions. Needed for not sending User's Auctions
     * @param country   Country that Auction's owner is from
     * @return Auctions matching owner's country
     * @throws RemoteException is raised in case of Error on RMI connection
     */
    ArrayList<Auction> searchAuctionByCountry(User requester, String country) throws RemoteException;

    /**
     * Method for searching auctions by Auction product's name
     * <p>
     * Whenever a User wants to browse Auctions and search them by Product name,
     * this method is called.
     * The result does not include Auctions created by the User,
     * nor Auction already closed. That is why the User needs to be passed as an argument.
     *
     * @param requester User that requests Auctions. Needed for not sending User's Auctions
     * @param name      Name that Auction's product have
     * @return Auctions matching product's name
     * @throws RemoteException is raised in case of Error on RMI connection
     */
    ArrayList<Auction> searchAuctionByProductName(User requester, String name) throws RemoteException;

    /**
     * Method for getting all Auctions, without needing to search by a condition.
     * <p>
     * Whenever a User wants to browse Auctions and is not interested
     * in a specific Auction, this method is called.
     * The result does not include Auctions created by the User,
     * nor Auction already closed. That is why the User needs to be passed as an argument.
     *
     * @param requester User that requests Auctions. Needed for not sending User's Auctions
     * @return All open auctions
     * @throws RemoteException is raised in case of Error on RMI connection
     */
    ArrayList<Auction> getAllAuctions(User requester) throws RemoteException;

    // Administrator API

    /**
     * Method for login an Administrator User.
     * <p>
     * Checks if Administrator credentials are valid,
     * and returns an Administrator object that matched.
     *
     * @param username Administrator's username
     * @param password Administrator's password
     * @return Logged in Administrator
     * @throws RemoteException        is raised in case of Error on RMI connection
     * @throws AdministratorException is raised in case of invalid credentials
     */
    Administrator adminLogIn(String username, String password) throws RemoteException, AdministratorException;

    /**
     * Method for deleting an Auction.
     * <p>
     * This method is used by Administrators in order to delete an Auction
     * from the system. A notification is sent to IRemoteObservers.
     *
     * @param auction Auction that will be deleted
     * @throws RemoteException is raised in case of Error on RMI connection
     */
    void deleteAuction(Auction auction) throws RemoteException;

    /**
     * Method for deleting a User.
     * <p>
     * This method is used by Administrators in order to delete a User
     * from the system. A notification is sent to IRemoteObservers
     * informing about a deleted Auction. Users that have Bidded Auction' Product
     * or the one that created it may react to it.
     *
     * @param user User that will be deleted
     * @throws RemoteException is raised in case of Error on RMI connection
     */
    void deleteUser(User user) throws RemoteException;

    /**
     * Method for getting all Users.
     * <p>
     * This method is used by Administrators in order to retrieve all
     * Users that have an account on the system. A notification is sent to IRemoteObservers
     * informing about a deleted User.
     * The deleted User may react to it.
     *
     * @return All Users that have an account on BiddingIt
     * @throws RemoteException is raised in case of Error on RMI connection
     */
    ArrayList<User> getAllUsers() throws RemoteException;

    /**
     * Method for getting specific User's Auctions.
     * <p>
     * This method is used by Administrators in order to retrieve all Auctions,
     * opened or closed, created by a specific User.
     *
     * @param user Auctions owner User
     * @return All Acutions the User is owner of
     * @throws RemoteException is raised in case of Error on RMI connection
     */
    ArrayList<Auction> getAuctionByUser(User user) throws RemoteException;

    /**
     * Method for getting all Auctions
     * <p>
     * This method is used by Administrators in order to retrieve all
     * opened Auctions on the system.
     *
     * @return All opened Auctions on the system
     * @throws RemoteException is raised in case of Error on RMI connection
     */
    ArrayList<Auction> getAllAuctionsAdmin() throws RemoteException;

    /**
     * Method for starting Auctions' Countdown threads.
     * <p>
     * This is only used when testing. It is needed when an Auction
     * have been created manually on the database after initializing the Server.
     *
     * @throws RemoteException is raised in case of Error on RMI connection
     */
    void startUncheckedAuctions() throws RemoteException;

    public Administrator createAdministrator(Administrator admin) throws RemoteException, AdministratorException;
}
