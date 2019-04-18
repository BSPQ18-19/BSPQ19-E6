package com.spq.group6.admin.controller;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.spq.group6.admin.remote.AdminServiceLocator;
import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.Product;
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

	public void exit(){
    	System.exit(0);
    }

}