package com.spq.group6.server.services;

import com.spq.group6.server.data.Administrator;
import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.User;
import com.spq.group6.server.exceptions.AdministratorException;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Administrator related service's interface.
 * <p>
 * BiddingIt server's Application for Administrator related services:
 * - Authentication
 * - Auction removal
 * - User removal
 * - Auction and User retrieval
 */
public interface IAdminService {
    Administrator logIn(String username, String password) throws AdministratorException;

    void deleteAuction(Auction auction);

    void deleteUser(User user);

    ArrayList<User> getAllUsers();

    ArrayList<Auction> getAuctionByUser(User user);

    ArrayList<Auction> getAllAuctions() throws RemoteException;

}
