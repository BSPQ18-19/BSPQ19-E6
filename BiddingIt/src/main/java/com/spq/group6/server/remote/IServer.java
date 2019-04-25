package com.spq.group6.server.remote;

import com.spq.group6.server.data.Administrator;
import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;
import com.spq.group6.server.exceptions.AdministratorException;
import com.spq.group6.server.exceptions.AuctionException;
import com.spq.group6.server.exceptions.UserException;
import com.spq.group6.server.utils.observer.remote.IRemoteObservable;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;

public interface IServer extends Remote, IRemoteObservable {
    // User account management API
    public User logIn(String username, String password) throws RemoteException, UserException;

    public User signIn(String username, String password, String country) throws RemoteException, UserException;

    public User updateUser(User user) throws RemoteException, UserException;

    public User createProduct(User user, String name, String description) throws RemoteException;

    public Product updateProduct(Product product, String name, String description) throws RemoteException;

    public User deleteProduct(User user, Product product) throws RemoteException;

    // Auctions API
    public Auction createPublicAuction(User owner, Product product, Timestamp dayLimit, float initialPrice) throws RemoteException;

    public Auction bid(Auction auction, User user, float amount) throws RemoteException, AuctionException;

    public ArrayList<Auction> searchAuctionByCountry(User requester, String country) throws RemoteException;

    public ArrayList<Auction> searchAuctionByProductName(User requester, String name) throws RemoteException;

    public ArrayList<Auction> getAllAuctions(User requester) throws RemoteException;

    // Administrator API
    public Administrator adminLogIn(String username, String password) throws RemoteException, AdministratorException;

    public void deleteAuction(Auction auction) throws RemoteException;

    public void deleteUser(User user) throws RemoteException;

    public ArrayList<User> getAllUsers() throws RemoteException;

    public ArrayList<Auction> getAuctionByUser(User user) throws RemoteException;
}
