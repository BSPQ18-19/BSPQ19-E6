package com.spq.group6.admin.controller;

import com.spq.group6.admin.gui.AdminWindow;
import com.spq.group6.admin.remote.AdminServiceLocator;
import com.spq.group6.admin.utils.logger.AdminLogger;
import com.spq.group6.server.data.Administrator;
import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.User;
import com.spq.group6.server.exceptions.AdministratorException;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Controller of the Administration service
 * <p>
 * This is the API that describes functionality offered by BiddingIt Administration service.
 */

public class AdminController {

    private AdminServiceLocator adminServiceLocator;
    private Administrator currentAdmin;

    public AdminController() throws RemoteException {
        super();
        adminServiceLocator = AdminServiceLocator.getServiceLocator();
        AdminWindow.getAdminWindow(this).setVisible(true);
    }

    /**
     * Method for login and getting access to the system.
     * <p>
     * Checks if Admin credentials are valid,
     * and returns a Admin object that matched.
     *
     * @param email    Admin's email
     * @param password Admin's password
     * @return Logged in Admin
     * @throws RemoteException is raised in case of Error on RMI connection
     */
    public boolean logIn(String email, String password) throws RemoteException {
        String info = "Log in with email " + email + " and password " + password;
        try {
            AdminLogger.logger.debug("Trying to " + info + ".");
            Administrator admin = adminServiceLocator.getService().adminLogIn(email, password);
            if (admin != null) {
                AdminLogger.logger.debug(info + " correct.");
                this.currentAdmin = admin;
                return true;
            } else
                AdminLogger.logger.warn(info + " incorrect. Server returned null.");
        } catch (AdministratorException re) {
            AdminLogger.logger.error(info + ". Exception found in server: " + re.getMessage());
        }
        return false;
    }

    /**
     * Method for logOut and stop getting access to the system.
     */
    public boolean logOut() {
        this.currentAdmin = null;
        return true;
    }

    public Administrator getCurrentAdmin() {
        return currentAdmin;
    }

    /**
     * Method to get all the existing users in the BiddingIt database
     */
    public ArrayList<User> getAllUsers() {
        ArrayList<User> users = new ArrayList<>();
        String info = "Get all users";
        try {
            AdminLogger.logger.debug("Trying to " + info + ".");
            users = adminServiceLocator.getService().getAllUsers();
            AdminLogger.logger.debug(info + " correct.");
        } catch (RemoteException e) {
            AdminLogger.logger.error("Error serching for users: " + e.getMessage());
        }
        return users;
    }

    /**
     * Method to get all the existing auctions in the BiddingIt database
     */
    public ArrayList<Auction> getAllAuctions() {
        ArrayList<Auction> auctions = new ArrayList<>();

        String info = "Get all auctions";
        try {
            AdminLogger.logger.debug("Trying to " + info + ".");
            auctions = adminServiceLocator.getService().getAllAuctionsAdmin();
            AdminLogger.logger.debug(info + " correct.");
        } catch (RemoteException e) {
            AdminLogger.logger.error("Error searching for auctions: " + e.getMessage());
        }

        return auctions;
    }

    /**
     * Method to delete a User from the BiddingIt database
     */
    public boolean deleteUser(User user) {
        String info = "Delete the user " + user.getUsername() + " with country " + user.getCountry();
        try {
            AdminLogger.logger.debug("Trying to " + info + ".");
            adminServiceLocator.getService().deleteUser(user);
            AdminLogger.logger.debug(info + " correct.");
        } catch (RemoteException e) {
            AdminLogger.logger.error("Error deleting user: " + e.getMessage());
        }
        return true;
    }

    /**
     * Method to delete an Auction from the BiddingIt database
     */
    public boolean deleteAuction(Auction auction) {
        String info = "Delete the auction of the product " + auction.getProduct().getName() + " with description " + auction.getProduct().getDescription();
        try {
            AdminLogger.logger.debug("Trying to " + info + ".");
            adminServiceLocator.getService().deleteAuction(auction);
            AdminLogger.logger.debug(info + " correct.");
        } catch (RemoteException e) {
            AdminLogger.logger.error("Error deleting auction: " + e.getMessage());
        }
        return true;
    }

    public void startUncheckedAuctions() {
        String info = "Start unchecked Auctions";
        try {
            AdminLogger.logger.debug("Trying to " + info + ".");
            adminServiceLocator.getService().startUncheckedAuctions();
            AdminLogger.logger.debug(info + " correct.");
        } catch (RemoteException e) {
            AdminLogger.logger.error("Error starting unchecked Auctions: " + e.getMessage());
        }
    }

    public void exit() {
        System.exit(0);
    }

}