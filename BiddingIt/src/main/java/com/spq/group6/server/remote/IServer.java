package com.spq.group6.server.remote;

import com.spq.group6.server.data.Administrator;
import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;
import com.spq.group6.server.exceptions.AdministratorException;
import com.spq.group6.server.exceptions.AuctionException;
import com.spq.group6.server.exceptions.UserException;
import com.spq.group6.server.utils.observer.remote.IRemoteObserver;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;

public interface IServer extends Remote {

    // Account API
    User logIn(String username, String password, IRemoteObserver observer) throws RemoteException, UserException;

    void logOut(IRemoteObserver observer) throws RemoteException;

    User signIn(String username, String password, String country, IRemoteObserver observer) throws RemoteException, UserException;

    User updateUser(User user, String password, String country) throws RemoteException, UserException;

    User createProduct(User user, String name, String description) throws RemoteException;

    Product updateProduct(Product product, String name, String description) throws RemoteException;

    User deleteProduct(User user, Product product) throws RemoteException;

    // Auctions API
    Auction createPublicAuction(User owner, Product product, Timestamp dayLimit, float initialPrice) throws RemoteException;

    Auction bid(Auction auction, User user, float amount) throws RemoteException, AuctionException;

    ArrayList<Auction> searchAuctionByCountry(User requester, String country) throws RemoteException;

    ArrayList<Auction> searchAuctionByProductName(User requester, String name) throws RemoteException;

    ArrayList<Auction> getAllAuctions(User requester) throws RemoteException;

    // Administrator API
    Administrator adminLogIn(String username, String password) throws RemoteException, AdministratorException;

    void deleteAuction(Auction auction) throws RemoteException;

    void deleteUser(User user) throws RemoteException;

    ArrayList<User> getAllUsers() throws RemoteException;

    ArrayList<Auction> getAuctionByUser(User user) throws RemoteException;

    ArrayList<Auction> getAllAuctionsAdmin() throws RemoteException;
}
