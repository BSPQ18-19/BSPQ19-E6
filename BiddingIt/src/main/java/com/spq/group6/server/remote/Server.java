package com.spq.group6.server.remote;

import com.spq.group6.server.data.Administrator;
import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;
import com.spq.group6.server.exceptions.AdministratorException;
import com.spq.group6.server.exceptions.AuctionException;
import com.spq.group6.server.exceptions.UserException;
import com.spq.group6.server.services.*;
import com.spq.group6.server.utils.logger.ServerLogger;
import com.spq.group6.server.utils.observer.remote.IRemoteObserver;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Timestamp;
import java.util.ArrayList;


/**
 * Remote Façade for BididngIt server
 */
public class Server extends UnicastRemoteObject implements IServer {
    private IAccountService accountService;
    private IAuctionService auctionService;
    private IAdminService adminService;

    public Server() throws RemoteException {
        accountService = new AccountService();
        auctionService = new AuctionService();
        adminService = new AdminService();
    }

    // Account API

    /**
     * Method for login and getting access to the system. Checks user credentials.
     *
     * @param username User's username
     * @param password User's password
     * @param observer Client's observer responsible for handling events sent by server
     * @return Logged in User
     * @throws RemoteException
     * @throws UserException   in case of invalid credentials
     */
    public User logIn(String username, String password, IRemoteObserver observer) throws RemoteException, UserException {
        ServerLogger.logger.debug("Received Log in petition");
        User user = accountService.logIn(username, password, observer);
        ServerLogger.logger.debug("User " + username + " has logged in.");
        return user;
    }

    /**
     * Method for letting know the server a Client has logged out, so it can delete its observer
     *
     * @param observer Client's observer that will not listen more for Events
     * @throws RemoteException
     * @throws UserException
     */
    public void logOut(IRemoteObserver observer) throws RemoteException {
        accountService.logOut(observer);
    }

    /**
     * Method for creating a new User.
     *
     * @param username User's username
     * @param password User's password
     * @param country  User's country. Useful for auctions search
     * @param observer Client's observer responsible for handling events sent by server
     * @return Created User
     * @throws RemoteException
     * @throws UserException   in case of invalid credentials
     */
    public User signIn(String username, String password, String country, IRemoteObserver observer) throws RemoteException, UserException {
        ServerLogger.logger.debug("Received Sign in petition");
        return accountService.signIn(username, password, country, observer);
    }

    /**
     * Method for updating User's password or country.
     *
     * @param user     Old User that will be updated
     * @param password New(or same old) password for updating User
     * @param country  New (or same old) country for updating User
     * @return Updated User
     * @throws RemoteException
     * @throws UserException
     */
    public User updateUser(User user, String password, String country) throws RemoteException, UserException {
        ServerLogger.logger.debug("Received update petition");
        return accountService.updateUser(user, password, country);
    }

    /**
     * Method for creating a new Product.
     *
     * @param user        Product's owner User
     * @param name        Product's name
     * @param description Product's description
     * @return User with its new Product on 'ownedProducts' ArrayList
     * @throws RemoteException
     */
    public User createProduct(User user, String name, String description) throws RemoteException {
        ServerLogger.logger.debug("Received product create petition");
        user = accountService.createProduct(user, name, description);
        ServerLogger.logger.debug("Product created.");
        return user;
    }

    /**
     * Method for updating Product's name or description.
     *
     * @param product     Product that will be updated
     * @param name        Product's new (or same old) name for updating product
     * @param description Product's new (or same old) description for updating product
     * @return Updated Product
     * @throws RemoteException
     */
    public Product updateProduct(Product product, String name, String description) throws RemoteException {
        ServerLogger.logger.debug("Received product update petition");
        Product newProduct = accountService.updateProduct(product, name, description);
        ServerLogger.logger.debug("Product '" + newProduct.getName() + "' updated.");
        return newProduct;
    }

    /**
     * Method for deleting a product
     *
     * @param user    Product's owner User
     * @param product Product that will be deleted
     * @return User without Product. Product is deleted from 'ownedProducts' ArrayList
     * @throws RemoteException
     */
    public User deleteProduct(User user, Product product) throws RemoteException {
        ServerLogger.logger.debug("Received product delete petition");
        user = accountService.deleteProduct(user, product);
        ServerLogger.logger.debug("Product deleted");
        return user;
    }

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
    public Auction createPublicAuction(User owner, Product product, Timestamp dayLimit, float initialPrice) throws RemoteException {
        ServerLogger.logger.debug("Received public auction creation petition");
        Auction auction = auctionService.createPublicAuction(owner, product, dayLimit, initialPrice);
        ServerLogger.logger.debug("Public auction created " + auction.getAuctionID());
        return auction;
    }

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
    public Auction bid(Auction auction, User user, float amount) throws RemoteException, AuctionException {
        ServerLogger.logger.debug("Received bid petition for " + auction.getAuctionID());
        auction = auctionService.bid(auction, user, amount);
        ServerLogger.logger.debug("Bid created for " + auction.getAuctionID());
        return auction;
    }

    /**
     * Method for searching open auctions by Auction owner's country
     *
     * @param requester User that requests Auctions. Needed for not sending User's Auctions
     * @param country   Country that Auction's owner is from
     * @return Auctions matching owner's country
     * @throws RemoteException
     */
    public ArrayList<Auction> searchAuctionByCountry(User requester, String country) throws RemoteException {
        ServerLogger.logger.debug("Received auction search petition by country: " + country);
        ArrayList<Auction> auctions = auctionService.searchAuctionByCountry(requester, country);
        ServerLogger.logger.debug("Retrieved all Auctions by country: " + country);
        return auctions;
    }

    /**
     * Method for searching open auctions by Auction product's name
     *
     * @param requester User that requests Auctions. Needed for not sending User's Auctions
     * @param name      Name that Auction's product have
     * @return Auctions matching product's name
     * @throws RemoteException
     */
    public ArrayList<Auction> searchAuctionByProductName(User requester, String name) throws RemoteException {
        ServerLogger.logger.debug("Received auction search petition by product name: " + name);
        ArrayList<Auction> auctions = auctionService.searchAuctionByProductName(requester, name);
        ServerLogger.logger.debug("Retrieved auctions by product name: " + name);
        return auctions;
    }

    /**
     * Method for getting all open Auctions, without needing to search by a condition
     *
     * @param requester User that requests Auctions. Needed for not sending User's Auctions
     * @return All open auctions
     * @throws RemoteException
     */
    public ArrayList<Auction> getAllAuctions(User requester) throws RemoteException {
        ServerLogger.logger.debug("Received all auctions retrieval petition");
        ArrayList<Auction> auctions = auctionService.getAllAuctions(requester);
        ServerLogger.logger.debug("Retrieved all auctions");
        return auctions;
    }

    // Admin API

    /**
     * Method for login an Administrator client
     *
     * @param username Administrator's username
     * @param password Administrator's password
     * @return Logged in Administrator
     * @throws RemoteException
     * @throws AdministratorException in case of invalid credentials
     */
    public Administrator adminLogIn(String username, String password) throws RemoteException, AdministratorException {
        ServerLogger.logger.debug("Received log in petition for " + username);
        Administrator admin = adminService.logIn(username, password);
        ServerLogger.logger.debug("Logged in Administrator " + username);
        return admin;
    }

    /**
     * Method for deleting an Auction
     *
     * @param auction Auction that will be deleted
     * @throws RemoteException
     */
    public void deleteAuction(Auction auction) throws RemoteException {
        ServerLogger.logger.debug("Received auction removal petition for " + auction.getAuctionID());
        adminService.deleteAuction(auction);
        ServerLogger.logger.debug("Removed Auction " + auction.getAuctionID());
    }

    /**
     * Method for deleting a User
     *
     * @param user User that will be deleted
     * @throws RemoteException
     */
    public void deleteUser(User user) throws RemoteException {
        ServerLogger.logger.debug("Received user removal petition for " + user.getUsername());
        adminService.deleteUser(user);
        ServerLogger.logger.debug("Removed user " + user.getUsername());
    }

    /**
     * Method for getting all Users
     *
     * @return All Users
     * @throws RemoteException
     */
    public ArrayList<User> getAllUsers() throws RemoteException {
        ServerLogger.logger.debug("Received all users retrieval petition");
        ArrayList<User> users = adminService.getAllUsers();
        ServerLogger.logger.debug("Retrieved all users");
        return users;
    }

    /**
     * Method for getting specific User's Auctions
     *
     * @param user Auctions owner User
     * @return All Acutions the User is owner of
     * @throws RemoteException
     */
    public ArrayList<Auction> getAuctionByUser(User user) throws RemoteException {
        ServerLogger.logger.debug("Received auction search petition by user: " + user.getUsername());
        ArrayList<Auction> auctions = adminService.getAuctionByUser(user);
        ServerLogger.logger.debug("Retrieved auctions by user: " + user.getUsername());
        return auctions;
    }

    /**
     * Method for getting all Auctions, opened and closed
     *
     * @return All opened and closed Auctions
     * @throws RemoteException
     */
    public ArrayList<Auction> getAllAuctionsAdmin() throws RemoteException {
        ServerLogger.logger.debug("Received all Auctions (opened and closed) retrieval petition");
        ArrayList<Auction> auctions = adminService.getAllAuctions();
        ServerLogger.logger.debug("Retrieved all Auctions (opened and closed)");
        return auctions;
    }

}
