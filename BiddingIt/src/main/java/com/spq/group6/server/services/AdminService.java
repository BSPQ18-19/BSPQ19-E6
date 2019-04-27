package com.spq.group6.server.services;

import com.spq.group6.server.dao.BiddingDAO;
import com.spq.group6.server.dao.IBiddingDAO;
import com.spq.group6.server.data.Administrator;
import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.User;
import com.spq.group6.server.exceptions.AdministratorException;
import com.spq.group6.server.utils.AuctionLocks;
import com.spq.group6.server.utils.observer.events.AuctionDeletedEvent;
import com.spq.group6.server.utils.observer.events.UserDeletedEvent;

import java.rmi.RemoteException;
import java.util.ArrayList;


public class AdminService implements IAdminService {
    private IBiddingDAO biddingDAO;

    public AdminService() {
        // Initializze Admin DAO
        biddingDAO = new BiddingDAO();
    }

    public Administrator logIn(String username, String password) throws AdministratorException {
        Administrator admin = biddingDAO.getAdministratorByUsername(username);
        if (admin == null) throw new AdministratorException("Administrator does not exist");
        if (!password.equals(admin.getPassword())) throw new AdministratorException("Invalid username or password");
        return admin;
    }

    public void deleteAuction(Auction auction) {
        AuctionLocks.getLock(auction.getAuctionID()).lock();
        auction = biddingDAO.getAuctionByID(auction.getAuctionID());
        biddingDAO.deleteAuction(auction);
        AuctionService.auctionObservables.get(auction.getAuctionID()).notifyRemoteObservers(new AuctionDeletedEvent(auction));
        AuctionService.countdownObservables.get(auction.getAuctionID()).interrupt();
        AuctionLocks.getLock(auction.getAuctionID()).unlock();
    }

    public void deleteUser(User user) {
        for(Auction auction: biddingDAO.getAuctionByUser(user)){
            deleteAuction(auction);
        }
        biddingDAO.deleteUser(user);
        AccountService.userObservables.get(user.getUsername()).notifyRemoteObservers(new UserDeletedEvent(user));
        AccountService.userObservables.remove(user.getUsername());
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
