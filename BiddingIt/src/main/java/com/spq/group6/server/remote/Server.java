package com.spq.group6.server.remote;

import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;
import com.spq.group6.server.exceptions.AuctionException;
import com.spq.group6.server.exceptions.UserException;
import com.spq.group6.server.services.*;
<<<<<<< HEAD
import com.spq.group6.server.utils.observer.remote.IRemoteObserver;
import com.spq.group6.server.utils.observer.remote.RemoteObservable;
=======
import com.spq.group6.server.utils.logger.ServerLogger;
>>>>>>> server_db

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;


public class Server extends UnicastRemoteObject implements IServer {
    private IAccountService accountService;
    private IAuctionService auctionService;
    private IAdminService adminService;

    public Server() throws RemoteException {
        accountService = new AccountService();
        auctionService = new AuctionService();
        adminService = new AdminService();
    }

    public User logIn(String username, String password) throws RemoteException, UserException {
        ServerLogger.logger.debug("Received Log in petition");
        User user = accountService.logIn(username, password);
        ServerLogger.logger.debug("User " + username + " has logged in.");
        return user;
    }

    public User signIn(String username, String password, String country) throws RemoteException, UserException {
        ServerLogger.logger.debug("Received Sign in petition");
        return accountService.signIn(username, password, country);
    }

    public User updateUser(User user) throws RemoteException, UserException {
        ServerLogger.logger.debug("Received update petition");
        return accountService.updateUser(user);
    }

    public User createProduct(User user, String name, String description) throws RemoteException {
        ServerLogger.logger.debug("Received product create petition");
        return accountService.createProduct(user, name, description);
    }

    public Product updateProduct(Product product, String name, String description) throws RemoteException {
        ServerLogger.logger.debug("Received product update petition");
        Product newProduct = accountService.updateProduct(product, name, description);
        ServerLogger.logger.debug("Product '" + newProduct.getName() + "' updated.");
        return newProduct;
    }

    public User deleteProduct(User user, Product product) throws RemoteException {
        ServerLogger.logger.debug("Received product delete petition");
        return accountService.deleteProduct(user, product);
    }

    public Auction createPublicAuction(User owner, Product product, Timestamp dayLimit, float initialPrice) throws RemoteException {
        Auction auction = auctionService.createPublicAuction(owner, product, dayLimit, initialPrice);
        return auction;
    }

    public Auction bid(Auction auction, User user, float amount) throws RemoteException, AuctionException {
        return auctionService.bid(auction, user, amount);
    }

    public ArrayList<Auction> searchAuctionByCountry(String country) throws RemoteException {
        return auctionService.searchAuctionByCountry(country);
    }

    public ArrayList<Auction> searchAuctionByProductName(String name) throws RemoteException {
        return auctionService.searchAuctionByProductName(name);
    }

    public void deleteAuction(Auction auction) throws RemoteException {
        adminService.deleteAuction(auction);
    }

    public void deleteUser(User user) throws RemoteException {
        adminService.deleteUser(user);
    }

    // Observer calls
    public void addRemoteObserver(Auction auction, IRemoteObserver observer) throws RemoteException {
        auctionService.addRemoteObserver(auction, observer);
    }

    public void deleteRemoteObserver(Auction auction, IRemoteObserver observer) throws RemoteException {
        auctionService.deleteRemoteObserver(auction, observer);
    }
}
