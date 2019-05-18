package com.spq.group6.server.remote;

import com.spq.group6.server.data.Administrator;
import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;
import com.spq.group6.server.exceptions.AccountException;
import com.spq.group6.server.exceptions.AdministratorException;
import com.spq.group6.server.exceptions.AuctionException;
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
    public User logIn(String username, String password, IRemoteObserver observer) throws RemoteException, AccountException {
        ServerLogger.logger.debug("Received Log in petition");
        User user = accountService.logIn(username, password, observer);
        ServerLogger.logger.debug("User " + username + " has logged in.");
        return user;
    }

    public void logOut(IRemoteObserver observer) throws RemoteException {
        accountService.logOut(observer);
    }

    public User signIn(String username, String password, String country, IRemoteObserver observer) throws RemoteException, AccountException {
        ServerLogger.logger.debug("Received Sign in petition");
        return accountService.signIn(username, password, country, observer);
    }

    public User updateUser(User user, String country, String password) throws RemoteException, AccountException {
        ServerLogger.logger.debug("Received update petition");
        return accountService.updateUser(user, country, password);
    }

    public User createProduct(User user, String name, String description) throws RemoteException {
        ServerLogger.logger.debug("Received product create petition");
        user = accountService.createProduct(user, name, description);
        ServerLogger.logger.debug("Product created.");
        return user;
    }

    public Product updateProduct(Product product, String name, String description) throws RemoteException {
        ServerLogger.logger.debug("Received product update petition");
        Product newProduct = accountService.updateProduct(product, name, description);
        ServerLogger.logger.debug("Product '" + newProduct.getName() + "' updated.");
        return newProduct;
    }


    public User deleteProduct(User user, Product product) throws RemoteException {
        ServerLogger.logger.debug("Received product delete petition");
        user = accountService.deleteProduct(user, product);
        ServerLogger.logger.debug("Product deleted");
        return user;
    }

    // Auctions API

    public Auction createPublicAuction(User owner, Product product, Timestamp dayLimit, float initialPrice) throws RemoteException, AuctionException {
        ServerLogger.logger.debug("Received public auction creation petition");
        Auction auction = auctionService.createAuction(owner, product, dayLimit, initialPrice, null);
        ServerLogger.logger.debug("Public auction created " + auction.getAuctionID());
        return auction;
    }

    public Auction createPrivateAuction(User owner, Product product, Timestamp dayLimit, float initialPrice, String password) throws RemoteException, AuctionException {
        ServerLogger.logger.debug("Received public auction creation petition");
        if (password == null) {
            throw new AuctionException("Invalid null password");
        }
        Auction auction = auctionService.createAuction(owner, product, dayLimit, initialPrice, password);
        ServerLogger.logger.debug("Public auction created " + auction.getAuctionID());
        return auction;
    }

    public Auction bid(Auction auction, User user, float amount) throws RemoteException, AuctionException {
        ServerLogger.logger.debug("Received bid petition for " + auction.getAuctionID());
        auction = auctionService.bid(auction, user, amount);
        ServerLogger.logger.debug("Bid created for " + auction.getAuctionID());
        return auction;
    }

    public ArrayList<Auction> searchAuctionByCountry(User requester, String country) throws RemoteException {
        ServerLogger.logger.debug("Received auction search petition by country: " + country);
        ArrayList<Auction> auctions = auctionService.searchAuctionByCountry(requester, country);
        ServerLogger.logger.debug("Retrieved all Auctions by country: " + country);
        return auctions;
    }

    public ArrayList<Auction> searchAuctionByProductName(User requester, String name) throws RemoteException {
        ServerLogger.logger.debug("Received auction search petition by product name: " + name);
        ArrayList<Auction> auctions = auctionService.searchAuctionByProductName(requester, name);
        ServerLogger.logger.debug("Retrieved auctions by product name: " + name);
        return auctions;
    }

    public ArrayList<Auction> getAllAuctions(User requester) throws RemoteException {
        ServerLogger.logger.debug("Received all auctions retrieval petition");
        ArrayList<Auction> auctions = auctionService.getAllAuctions(requester);
        ServerLogger.logger.debug("Retrieved all auctions");
        return auctions;
    }

    // AdminMain API

    public Administrator adminLogIn(String username, String password) throws RemoteException, AdministratorException {
        ServerLogger.logger.debug("Received log in petition for " + username);
        Administrator admin = adminService.logIn(username, password);
        ServerLogger.logger.debug("Logged in Administrator " + username);
        return admin;
    }

    public void deleteAuction(Auction auction) throws RemoteException {
        ServerLogger.logger.debug("Received auction removal petition for " + auction.getAuctionID());
        adminService.deleteAuction(auction);
        ServerLogger.logger.debug("Removed Auction " + auction.getAuctionID());
    }

    public void deleteUser(User user) throws RemoteException {
        ServerLogger.logger.debug("Received user removal petition for " + user.getUsername());
        adminService.deleteUser(user);
        ServerLogger.logger.debug("Removed user " + user.getUsername());
    }

    public ArrayList<User> getAllUsers() throws RemoteException {
        ServerLogger.logger.debug("Received all users retrieval petition");
        ArrayList<User> users = adminService.getAllUsers();
        ServerLogger.logger.debug("Retrieved all users");
        return users;
    }

    public ArrayList<Auction> getAuctionByUser(User user) throws RemoteException {
        ServerLogger.logger.debug("Received auction search petition by user: " + user.getUsername());
        ArrayList<Auction> auctions = adminService.getAuctionByUser(user);
        ServerLogger.logger.debug("Retrieved auctions by user: " + user.getUsername());
        return auctions;
    }

    public ArrayList<Auction> getAllAuctionsAdmin() throws RemoteException {
        ServerLogger.logger.debug("Received all Auctions (opened and closed) retrieval petition");
        ArrayList<Auction> auctions = adminService.getAllAuctions();
        ServerLogger.logger.debug("Retrieved all Auctions (opened and closed)");
        return auctions;
    }

    public void startUncheckedAuctions() throws RemoteException {
        auctionService.startUncheckedAuctions();
    }

    public Administrator createAdministrator(Administrator admin) throws RemoteException, AdministratorException {
        ServerLogger.logger.debug("Received admin creation petition");
        return accountService.createAdministrator(admin);
}
}
