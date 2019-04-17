package com.spq.group6.client.controller;

import java.rmi.RemoteException;
import java.util.List;

import com.spq.group6.client.remote.ServiceLocator;
import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;
import com.spq.group6.server.exceptions.UserException;
import com.spq.group6.client.gui.ClientWindow;

public class ClientController {

	private ServiceLocator serviceLocator;
	private User currentUser;
	
    public ClientController() throws RemoteException {
		super();
		serviceLocator = ServiceLocator.getServiceLocator();
		ClientWindow.getClientWindow(this).setVisible(true);
	}
    
    public boolean logIn(String email, String password) throws RemoteException {
    	String info = "Log in with email " + email + " and password " + password;
        try {
        	System.out.println("Trying to " + info + ".");
            User user = serviceLocator.getService().logIn(email, password);
        	if (user != null) {
            	System.out.println(info + " correct.");
            	this.currentUser = user;
            	return true;
            } else
            	System.out.println(info + " incorrect. Server returned null.");
        } catch (UserException re) {
        	System.out.println(info + ". Exception found in server: " + re);
        }
        return false;
     }
    
    public boolean signIn(String email, String password, String country) throws RemoteException {
    	String info = "Sign in with email " + email + " and password " + password + " and country " + country;
        try {
        	System.out.println("Trying to " + info + ".");
			System.out.println(serviceLocator);
            User user = serviceLocator.getService().signIn(email, password, country);
        	if (user != null) {
            	System.out.println(info + " correct.");
            	this.currentUser = user;
            	return true;
            } else
            	System.out.println(info + " incorrect. Server returned null.");
        } catch (UserException re) {
        	System.out.println(info + ". Exception found in server: " + re);
        }
        return false;
     }
    
    public boolean logOut() {
    	this.currentUser = null;
    	return true;
    }
    
    /*
    public boolean existsUsername(String username) {
    	try {
        	System.out.println("Checking if user with username " + username + " exists in easy booking server.");
            if (serviceLocator.getService().existsUsername(username)) {
            	System.out.println("User with username " + username + " already exists.");
            	return true;
            } else
            	System.out.println("User with username " + username + " does not exist.");
        } catch (Exception e) {
        	System.out.println("Error finding user: " + e.getMessage());
        }
        return false;
    }
    
    public boolean existsEmail(String email) {
    	try {
        	System.out.println("Checking if user with email " + email + " exists in easy booking server.");
            if (serviceLocator.getService().existsEmail(email)) {
            	System.out.println("User with email " + email + " already exists.");
            	return true;
            } else
            	System.out.println("User with email " + email + " does not exist.");
        } catch (Exception e) {
        	System.out.println("Error finding user: " + e.getMessage());
        }
        return false;
    }
    */

    
    public User getCurrentUser() {
		return currentUser;
	}
    
    public List<Product> getCurrentUserProducts() {
    	return currentUser.getOwnedProducts();
    }
    
    public boolean createProduct(String name, String description) {
    	String info = "Create the product " + name + " with description " + description;
        try {
        	System.out.println("Trying to " + info + ".");
        	currentUser = serviceLocator.getService().createProduct(currentUser, name, description);
			Product newProduct = currentUser.getOwnedProducts().get(currentUser.getOwnedProducts().size()-1);
        	if (newProduct != null) {
				System.out.println(info + " correct.");
			}
			else
            	System.out.println(info + " incorrect. Server returned null.");
        } catch (RemoteException e) {
			e.printStackTrace();
		}
    	return true;
    }
    
    public boolean updateProduct(Product product, String name, String description) {
    	String info = "Update the product " + name + " with description " + description;
        try {
        	System.out.println("Trying to " + info + ".");
			product = serviceLocator.getService().updateProduct(product, name, description);
			if (product != null)
				System.out.println(info + " correct.");
			else
            	System.out.println(info + " incorrect. Server returned null.");
		} catch (RemoteException e) {
			e.printStackTrace();
		}
    	return true;
    }
    
    public boolean deleteProduct(Product product) {
    	String info = "Delete the product " + product.getName() + " with description " + product.getDescription();
        try {
        	System.out.println("Trying to " + info + ".");
			currentUser = serviceLocator.getService().deleteProduct(currentUser, product);
			System.out.println(info + " correct.");
		} catch (RemoteException e) {
			e.printStackTrace();
		}
    	return true;
    }

	public void exit(){
    	System.exit(0);
    }

}