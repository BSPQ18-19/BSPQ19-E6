package com.spq.group6.server.remote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Timestamp;


import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;
import com.spq.group6.server.exceptions.BidException;
import com.spq.group6.server.exceptions.UserException;
import com.spq.group6.server.services.AccountService;
import com.spq.group6.server.services.AuctionService;
import com.spq.group6.server.services.IAccountService;
import com.spq.group6.server.services.IAuctionService;


public class Server extends UnicastRemoteObject implements IServer {
    private IAccountService accountService;
    private IAuctionService auctionService;

    public Server() throws RemoteException {
        accountService = new AccountService();
        auctionService = new AuctionService();
    }

    public User logIn(String username, String password) throws RemoteException, UserException {
        System.out.println("Received Log in petition");
        User user = accountService.logIn(username, password);
        System.out.println("User " + username + " has logged in.");
        return user;
    }

    public User signIn(String username, String password, String country) throws RemoteException, UserException{
        System.out.println("Received Sign in petition");
        return accountService.signIn(username, password, country);
    }

    public User updateUser(User user) throws RemoteException, UserException {
        System.out.println("Received update petition");
        return accountService.updateUser(user);
    }

    public User createProduct(User user, String name, String description) throws RemoteException{
        System.out.println("Received product create petition");
        return accountService.createProduct(user, name, description);
    }

    public Product updateProduct(Product product, String name, String description) throws RemoteException{
        System.out.println("Received product update petition");
        return accountService.updateProduct(product, name, description);
        //System.out.println("User '" + user.getUsername() + "' updated.");
    }

    public User deleteProduct(User user, Product product) throws RemoteException{
        System.out.println("Received product delete petition");
        return accountService.deleteProduct(user, product);
    }

    public Auction createPublicAuction(User owner, Product product, Timestamp dayLimit, float initialPrice) throws RemoteException {
        return auctionService.createPublicAuction(owner, product, dayLimit, initialPrice);
    }

    public Auction bid(Auction auction, User user, float amount) throws RemoteException, BidException {
        return auctionService.bid(auction, user, amount);
    }

}
