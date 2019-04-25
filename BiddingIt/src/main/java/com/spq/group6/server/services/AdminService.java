package com.spq.group6.server.services;

import com.spq.group6.server.dao.BiddingDAO;
import com.spq.group6.server.dao.IBiddingDAO;
import com.spq.group6.server.data.Administrator;
import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.User;
import com.spq.group6.server.exceptions.AdministratorException;

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
        biddingDAO.deleteAuction(auction);
    }

    public void deleteUser(User user) {
        biddingDAO.deleteUser(user);
    }

    public ArrayList<User> getAllUsers() {
        return biddingDAO.getAllUsers();
    }

    public ArrayList<Auction> getAuctionByUser(User user) {
        return biddingDAO.getAuctionByUser(user);
    }
}
