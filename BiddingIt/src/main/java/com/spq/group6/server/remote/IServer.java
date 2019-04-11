package com.spq.group6.server.remote;

import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;
import com.spq.group6.server.exceptions.BidException;
import com.spq.group6.server.exceptions.UserException;
import com.spq.group6.server.services.AuctionService;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Timestamp;

public interface IServer extends Remote {
    public User logIn(String username, String password) throws RemoteException, UserException;
    public User signIn(String username, String password, String country) throws RemoteException, UserException;
    public User updateUser(User user) throws RemoteException, UserException;
    public User createProduct(User user, String name, String description) throws RemoteException;
    public Product updateProduct(Product product, String name, String description) throws RemoteException;
    public User deleteProduct(User user, Product product) throws RemoteException;

    public Auction createPublicAuction(User owner, Product product, Timestamp dayLimit, float initialPrice) throws RemoteException;
    public Auction bid(Auction auction, User user, float amount) throws RemoteException, BidException;

}
