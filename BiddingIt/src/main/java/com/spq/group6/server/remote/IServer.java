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
 */
public interface IServer extends Remote {

    // Account API

    /**
     * Method for login and getting access to the system. Checks user credentials.
     *
     * @param username User's username
     * @param password User's password
     * @param observer Client's observer responsible for handling events sent by server
     * @return Logged in User
     * @throws RemoteException
     * @throws AccountException in case of invalid credentials
     */
    User logIn(String username, String password, IRemoteObserver observer) throws RemoteException, AccountException;

    /**
     * Method for letting know the server a Client has logged out, so it can delete its observer
     *
     * @param observer Client's observer that will not listen more for Events
     * @throws RemoteException
     * @throws AccountException
     */
    void logOut(IRemoteObserver observer) throws RemoteException;

    /**
     * Method for creating a new User.
     *
     * @param username User's username
     * @param password User's password
     * @param country  User's country. Useful for auctions search
     * @param observer Client's observer responsible for handling events sent by server
     * @return Created User
     * @throws RemoteException
     * @throws AccountException in case of invalid credentials
     */
    User signIn(String username, String password, String country, IRemoteObserver observer) throws RemoteException, AccountException;

    /**
     * Method for updating User's password or country.
     *
     * @param user     Old User that will be updated
     * @param password New(or same old) password for updating User
     * @param country  New (or same old) country for updating User
     * @return Updated User
     * @throws RemoteException
     * @throws AccountException
     */
    User updateUser(User user, String password, String country) throws RemoteException, AccountException;

    /**
     * Method for creating a new Product.
     *
     * @param user        Product's owner User
     * @param name        Product's name
     * @param description Product's description
     * @return User with its new Product on 'ownedProducts' ArrayList
     * @throws RemoteException
     */
    User createProduct(User user, String name, String description) throws RemoteException;

    /**
     * Method for updating Product's name or description.
     *
     * @param product     Product that will be updated
     * @param name        Product's new (or same old) name for updating product
     * @param description Product's new (or same old) description for updating product
     * @return Updated Product
     * @throws RemoteException
     */
    Product updateProduct(Product product, String name, String description) throws RemoteException;

    /**
     * Method for deleting a product
     *
     * @param user    Product's owner User
     * @param product Product that will be deleted
     * @return User without Product. Product is deleted from 'ownedProducts' ArrayList
     * @throws RemoteException
     */
    User deleteProduct(User user, Product product) throws RemoteException;

    // Auctions API

    /**
     * Method for creating an Auction
     *
     * @param owner        Auction creator or Product's owner
     * @param product      Product that will be sold on Auction
     * @param dayLimit     Timestamp when Auction will be closed
     * @param initialPrice Initial proce for Product
     * @return Created Auction
     * @throws RemoteException
     */
    Auction createPublicAuction(User owner, Product product, Timestamp dayLimit, float initialPrice) throws RemoteException;

    /**
     * Method for creating an Auction
     *
     * @param owner        Auction creator or Product's owner
     * @param product      Product that will be sold on Auction
     * @param dayLimit     Timestamp when Auction will be closed
     * @param initialPrice Initial proce for Product
     * @param password     Password that the Auction will be protected with
     * @return Created Auction
     * @throws RemoteException
     */
    public Auction createPrivateAuction(User owner, Product product, Timestamp dayLimit, float initialPrice, String password) throws RemoteException, AuctionException;

    /**
     * Method for bidding an Auction
     *
     * @param auction Auction will be bidded to
     * @param user    Bid creator User
     * @param amount  Bidded amount (money)
     * @return Auction with the new Bid
     * @throws RemoteException
     * @throws AuctionException
     */
    Auction bid(Auction auction, User user, float amount) throws RemoteException, AuctionException;

    /**
     * Method for searching open auctions by Auction owner's country
     *
     * @param requester User that requests Auctions. Needed for not sending User's Auctions
     * @param country   Country that Auction's owner is from
     * @return Auctions matching owner's country
     * @throws RemoteException
     */
    ArrayList<Auction> searchAuctionByCountry(User requester, String country) throws RemoteException;

    /**
     * Method for searching open auctions by Auction product's name
     *
     * @param requester User that requests Auctions. Needed for not sending User's Auctions
     * @param name      Name that Auction's product have
     * @return Auctions matching product's name
     * @throws RemoteException
     */
    ArrayList<Auction> searchAuctionByProductName(User requester, String name) throws RemoteException;

    /**
     * Method for getting all open Auctions, without needing to search by a condition
     *
     * @param requester User that requests Auctions. Needed for not sending User's Auctions
     * @return All open auctions
     * @throws RemoteException
     */
    ArrayList<Auction> getAllAuctions(User requester) throws RemoteException;

    // Administrator API

    /**
     * Method for login an Administrator client
     *
     * @param username Administrator's username
     * @param password Administrator's password
     * @return Logged in Administrator
     * @throws RemoteException
     * @throws AdministratorException in case of invalid credentials
     */
    Administrator adminLogIn(String username, String password) throws RemoteException, AdministratorException;

    /**
     * Method for deleting an Auction
     *
     * @param auction Auction that will be deleted
     * @throws RemoteException
     */
    void deleteAuction(Auction auction) throws RemoteException;

    /**
     * Method for deleting a User
     *
     * @param user User that will be deleted
     * @throws RemoteException
     */
    void deleteUser(User user) throws RemoteException;

    /**
     * Method for getting all Users
     *
     * @return All Users
     * @throws RemoteException
     */
    ArrayList<User> getAllUsers() throws RemoteException;

    /**
     * Method for getting specific User's Auctions
     *
     * @param user Auctions owner User
     * @return All Acutions the User is owner of
     * @throws RemoteException
     */
    ArrayList<Auction> getAuctionByUser(User user) throws RemoteException;

    /**
     * Method for getting all Auctions
     *
     * @return All opened Auctions
     * @throws RemoteException
     */
    ArrayList<Auction> getAllAuctionsAdmin() throws RemoteException;
}
