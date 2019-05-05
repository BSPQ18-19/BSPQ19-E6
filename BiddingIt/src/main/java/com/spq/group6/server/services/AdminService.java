package com.spq.group6.server.services;

import com.spq.group6.server.dao.BiddingDAO;
import com.spq.group6.server.dao.IBiddingDAO;
import com.spq.group6.server.data.Administrator;
import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.User;
import com.spq.group6.server.exceptions.AdministratorException;
import com.spq.group6.server.utils.BiddingLocks;
import com.spq.group6.server.utils.observer.events.AuctionDeletedEvent;
import com.spq.group6.server.utils.observer.events.UserDeletedEvent;

import java.rmi.RemoteException;
import java.util.ArrayList;


/**
 * BiddingIt server's Application for Administrator related services:
 * - Authentication
 * - Auction removal
 * - User removal
 * - Auction and User retrieval
 */
public class AdminService implements IAdminService {
    private IBiddingDAO biddingDAO;

    public AdminService() {
        // Initializze AdminMain DAO
        biddingDAO = new BiddingDAO();
    }

    public Administrator logIn(String username, String password) throws AdministratorException {
        Administrator admin = biddingDAO.getAdministratorByUsername(username);
        if (admin == null) throw new AdministratorException("Administrator does not exist");
        if (!password.equals(admin.getPassword())) throw new AdministratorException("Invalid username or password");
        return admin;
    }

    public void deleteAuction(Auction auction) {
        auction = BiddingLocks.lockAndGetAuction(auction);
        try {
            auction = biddingDAO.getAuctionByID(auction.getAuctionID());
            biddingDAO.deleteAuction(auction);
            AccountService.observable.notifyRemoteObservers(new AuctionDeletedEvent(auction));
            AuctionService.countdownObservables.get(auction.getAuctionID()).interrupt();
        } finally {
            BiddingLocks.unlockAuction(auction);
        }
    }

    public void deleteUser(User user) {
        for (Auction auction : biddingDAO.getAuctionByUser(user)) {
            deleteAuction(auction);
        }
        biddingDAO.deleteUser(user);
        AccountService.observable.notifyRemoteObservers(new UserDeletedEvent(user));
    }

    public ArrayList<User> getAllUsers() {
        return biddingDAO.getAllUsers();
    }

    public ArrayList<Auction> getAuctionByUser(User user) {
        return biddingDAO.getAuctionByUser(user);
    }

    public ArrayList<Auction> getAllAuctions() throws RemoteException {
        return biddingDAO.getAllAuctions();
    }
}
