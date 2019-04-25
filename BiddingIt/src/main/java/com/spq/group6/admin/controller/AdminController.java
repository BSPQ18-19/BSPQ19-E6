package com.spq.group6.admin.controller;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.spq.group6.admin.remote.AdminServiceLocator;
import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;
import com.spq.group6.server.data.Administrator;
import com.spq.group6.server.exceptions.AdministratorException;
import com.spq.group6.admin.Admin;
import com.spq.group6.admin.gui.AdminWindow;

public class AdminController {

	private AdminServiceLocator adminServiceLocator;
	private Administrator currentAdmin;
	
    public AdminController() throws RemoteException {
		super();
		adminServiceLocator = AdminServiceLocator.getServiceLocator();
		AdminWindow.getAdminWindow(this).setVisible(true);
	}
    
    public boolean logIn(String email, String password) throws RemoteException {
    	String info = "Log in with email " + email + " and password " + password;
        try {
        	System.out.println("Trying to " + info + ".");
            Administrator admin = adminServiceLocator.getService().adminLogIn(email, password);
        	if (admin != null) {
            	System.out.println(info + " correct.");
            	this.currentAdmin = admin;
            	return true;
            } else
            	System.out.println(info + " incorrect. Server returned null.");
        } catch (AdministratorException re) {
        	System.out.println(info + ". Exception found in server: " + re);
        }
        return false;
     }
    
    public boolean logOut() {
    	this.currentAdmin = null;
    	return true;
    }
    
    public Administrator getCurrentAdmin() {
		return currentAdmin;
	}

    public boolean deleteAuction(Auction auction) {
    	String info = "Delete the auction of the product " + auction.getProduct().getName() + " with description " + auction.getProduct().getDescription();
        try {
        	System.out.println("Trying to " + info + ".");
			adminServiceLocator.getService().deleteAuction(auction);
			System.out.println(info + " correct.");
		} catch (RemoteException e) {
			e.printStackTrace();
		}
    	return true;
    }
    
    public boolean deleteUser(User user) {
    	String info = "Delete the user " + user.getUsername() + " with country " + user.getCountry();
        try {
        	System.out.println("Trying to " + info + ".");
			adminServiceLocator.getService().deleteUser(user);
			System.out.println(info + " correct.");
		} catch (RemoteException e) {
			e.printStackTrace();
		}
    	return true;
    }
    
    public ArrayList<Auction> searchAuctionByCountry(String country){
    	ArrayList<Auction> countryAuctions = new ArrayList<>();
    	String info = "Get auctions from country " + country;
        try {
        	System.out.println("Trying to " + info + ".");
			countryAuctions = adminServiceLocator.getService().searchAuctionByCountry(country);
			System.out.println(info + " correct.");
		} catch (RemoteException e) {
			e.printStackTrace();
		}
    	return countryAuctions;
    }
    
    public ArrayList<Auction> searchAuctionByProductName(String name) {
    	ArrayList<Auction> prodNameAuctions = new ArrayList<>();
    	String info = "Get auctions with prod. name " + name;
        try {
        	System.out.println("Trying to " + info + ".");
			prodNameAuctions = adminServiceLocator.getService().searchAuctionByProductName(name);
			System.out.println(info + " correct.");
		} catch (RemoteException e) {
			e.printStackTrace();
		}
    	return prodNameAuctions;
    }
    
	public void exit(){
    	System.exit(0);
    }

}