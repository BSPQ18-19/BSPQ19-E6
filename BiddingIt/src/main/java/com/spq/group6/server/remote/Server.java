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


public class Server extends UnicastRemoteObject implements IServer {
    private IAccountService accountService;
    private IAuctionService auctionService;
    private IAdminService adminService;

    public Server() throws RemoteException {
        accountService = new AccountService();
        auctionService = new AuctionService();
        adminService = new AdminService();
    }

    public User logIn(String username, String password, IRemoteObserver observer) throws RemoteException, UserException {
        ServerLogger.logger.debug("Received Log in petition");
        User user = accountService.logIn(username, password, observer);
        ServerLogger.logger.debug("User " + username + " has logged in.");
        return user;
    }

    public void logOut(String username, IRemoteObserver observer) throws RemoteException, UserException {
        accountService.logOut(username, observer);
    }

    public User signIn(String username, String password, String country, IRemoteObserver observer) throws RemoteException, UserException {
        ServerLogger.logger.debug("Received Sign in petition");
        return accountService.signIn(username, password, country, observer);
    }

    public User updateUser(User user) throws RemoteException, UserException {
        ServerLogger.logger.debug("Received update petition");
        return accountService.updateUser(user);
    }
    
    public Administrator createAdministrator(Administrator admin) throws RemoteException, AdministratorException {
        ServerLogger.logger.debug("Received admin creation petition");
        return accountService.createAdministrator(admin);
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
        ServerLogger.logger.debug("Received public auction creation petition");
        Auction auction = auctionService.createPublicAuction(owner, product, dayLimit, initialPrice);
        ServerLogger.logger.debug("Public auction created "+ auction.getAuctionID());
        return auction;
    }

    public Auction bid(Auction auction, User user, float amount) throws RemoteException, AuctionException {
        ServerLogger.logger.debug("Received bid petition for " + auction.getAuctionID());
        return auctionService.bid(auction, user, amount);
    }

    public ArrayList<Auction> searchAuctionByCountry(User requester, String country) throws RemoteException {
        ServerLogger.logger.debug("Received auction search petition by country: " + country);
        return auctionService.searchAuctionByCountry(requester, country);
    }

    public ArrayList<Auction> searchAuctionByProductName(User requester, String name) throws RemoteException {
        ServerLogger.logger.debug("Received auction search petition by product name: " + name);
        return auctionService.searchAuctionByProductName(requester, name);
    }

    public ArrayList<Auction> getAllAuctions(User requester) throws RemoteException {
        ServerLogger.logger.debug("Received all auctions retrieval petition");
        return auctionService.getAllAuctions(requester);
    }

    public Administrator adminLogIn(String username, String password) throws RemoteException, AdministratorException {
        ServerLogger.logger.debug("Received log in petition for " + username);
        return adminService.logIn(username, password);
    }

    public void deleteAuction(Auction auction) throws RemoteException {
        ServerLogger.logger.debug("Received auction removal petition for " + auction.getAuctionID());
        adminService.deleteAuction(auction);
    }

    public void deleteUser(User user) throws RemoteException {
        ServerLogger.logger.debug("Received user removal petition for " + user.getUsername());
        adminService.deleteUser(user);
    }

    public ArrayList<User> getAllUsers() throws RemoteException {
        ServerLogger.logger.debug("Received all users retrieval petition");
        return adminService.getAllUsers();
    }

    public ArrayList<Auction> getAuctionByUser(User user) throws RemoteException {
        ServerLogger.logger.debug("Received auction search petition by user: " + user.getUsername());
        return adminService.getAuctionByUser(user);
    }

    public ArrayList<Auction> getAllAuctionsAdmin() throws RemoteException {
        return adminService.getAllAuctions();
    }

    // Observer calls
    public void addRemoteObserver(Auction auction, IRemoteObserver observer) throws RemoteException {
        ServerLogger.logger.debug("Received obersver addition petition");
        auctionService.addRemoteObserver(auction, observer);
    }

    public void deleteRemoteObserver(Auction auction, IRemoteObserver observer) throws RemoteException {
        ServerLogger.logger.debug("Received obersver removal petition");
        auctionService.deleteRemoteObserver(auction, observer);
    }
}
