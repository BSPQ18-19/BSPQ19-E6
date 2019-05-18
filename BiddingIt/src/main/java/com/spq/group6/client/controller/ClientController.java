package com.spq.group6.client.controller;

import com.spq.group6.client.gui.ClientWindow;
import com.spq.group6.client.remote.ServiceLocator;
import com.spq.group6.client.utils.logger.ClientLogger;
import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;
import com.spq.group6.server.exceptions.AccountException;
import com.spq.group6.server.exceptions.AuctionException;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ClientController {
    private static ClientController controller;

    private ServiceLocator serviceLocator;
    private User currentUser;
    private ClientRemoteObserver observer;

    private ClientController() {
        super();
        try {
            observer = new ClientRemoteObserver();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        serviceLocator = ServiceLocator.getServiceLocator();
        ClientWindow.getClientWindow().setVisible(true);
    }

    public static ClientController getClientController() {
        if (controller == null) {
            controller = new ClientController();
        }
        return controller;
    }

    public static ClientController getNewClientController() {
        return new ClientController();
    }

    public boolean logIn(String email, String password) throws RemoteException {
        String info = "Log in with username " + email + " and password " + password;
        try {
            ClientLogger.logger.debug("Trying to " + info + ".");
            // TODO: create a real Observer for login
            User user = serviceLocator.getService().logIn(email, password, observer);
            if (user != null) {
                ClientLogger.logger.debug(info + " correct.");
                this.currentUser = user;
                return true;
            } else
                ClientLogger.logger.warn(info + " incorrect. Server returned null.");
        } catch (AccountException re) {
            ClientLogger.logger.error(info + ". Exception found in server: " + re.getMessage());
        }
        return false;
    }

    public boolean signIn(String email, String password, String country) {
        String info = "Sign in with username " + email + " and password " + password + " and country " + country;
        try {
            ClientLogger.logger.debug("Trying to " + info + ".");
            System.out.println(serviceLocator.getService());
            User user = serviceLocator.getService().signIn(email, password, country, observer);
            if (user != null) {
                ClientLogger.logger.debug(info + " correct.");
                this.currentUser = user;
                return true;
            } else
                ClientLogger.logger.warn(info + " incorrect. Server returned null.");
        } catch (AccountException | RemoteException re) {
            ClientLogger.logger.error(info + ". Exception found in server: " + re.getMessage());
        }
        return false;
    }

    public boolean logOut() {
        String info = "Log in with username " + currentUser.getUsername();
        try {
            serviceLocator.getService().logOut(observer);
        } catch (Exception e) {
            ClientLogger.logger.error(info + ". Exception found in server: " + e.getMessage());
            e.printStackTrace();
        }
        this.currentUser = null;
        return true;
    }
    
    /*
    public boolean existsUsername(String username) {
    	try {
        	ClientLogger.logger.debug("Checking if user with username " + username + " exists in easy booking server.");
            if (serviceLocator.getService().existsUsername(username)) {
            	ClientLogger.logger.debug("User with username " + username + " already exists.");
            	return true;
            } else
            	ClientLogger.logger.warn("User with username " + username + " does not exist.");
        } catch (Exception e) {
        	ClientLogger.logger.error("Error finding user: " + e.getMessage());
        }
        return false;
    }
    
    public boolean existsEmail(String email) {
    	try {
        	ClientLogger.logger.debug("Checking if user with email " + email + " exists in easy booking server.");
            if (serviceLocator.getService().existsEmail(email)) {
            	ClientLogger.logger.debug("User with email " + email + " already exists.");
            	return true;
            } else
            	ClientLogger.logger.warn("User with email " + email + " does not exist.");
        } catch (Exception e) {
        	ClientLogger.logger.error("Error finding user: " + e.getMessage());
        }
        return false;
    }
    */


    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public List<Product> getCurrentUserProducts() {
        return currentUser.getOwnedProducts();
    }

    public boolean createProduct(String name, String description) {
        String info = "Create the product " + name + " with description " + description;
        try {
            ClientLogger.logger.debug("Trying to " + info + ".");
            currentUser = serviceLocator.getService().createProduct(currentUser, name, description);
            Product newProduct = currentUser.getOwnedProducts().get(currentUser.getOwnedProducts().size() - 1);
            if (newProduct != null) {
                ClientLogger.logger.debug(info + " correct.");
            } else {
                ClientLogger.logger.warn(info + " incorrect. Server returned null.");
            }
        } catch (RemoteException e) {
            ClientLogger.logger.error("Error creating the product: " + e.getMessage());
        }
        return true;
    }

    public boolean updateProduct(Product product, String name, String description) {
        String info = "Update the product " + name + " with description " + description;
        try {
            ClientLogger.logger.debug("Trying to " + info + ".");
            Product updatedProduct = serviceLocator.getService().updateProduct(product, name, description);
            if (updatedProduct != null) {
                ClientLogger.logger.debug(info + " correct.");
                int index = currentUser.getOwnedProducts().indexOf(product);
                currentUser.getOwnedProducts().set(index, updatedProduct);
            } else {
                ClientLogger.logger.warn(info + " incorrect. Server returned null.");
            }
        } catch (RemoteException e) {
            ClientLogger.logger.error("Error updating the product: " + e.getMessage());
        }
        return true;
    }

    public boolean deleteProduct(Product product) {
        String info = "Delete the product " + product.getName() + " with description " + product.getDescription();
        try {
            ClientLogger.logger.debug("Trying to " + info + ".");
            currentUser = serviceLocator.getService().deleteProduct(currentUser, product);
            ClientLogger.logger.debug(info + " correct.");
        } catch (RemoteException e) {
            ClientLogger.logger.error("Error deleting the product: " + e.getMessage());
        }
        return true;
    }


    public List<Auction> getCurrentUserAuctions() {
        ArrayList<Auction> userAuctions = new ArrayList<>();
        String info = "Get auctions from user " + currentUser.getUsername();
        try {
            ClientLogger.logger.debug("Trying to " + info + ".");
            userAuctions = serviceLocator.getService().getAuctionByUser(currentUser);
            ClientLogger.logger.debug(info + " correct.");
        } catch (RemoteException e) {
            ClientLogger.logger.error("Error getting user auctions: " + e.getMessage());
        }
        return userAuctions;
    }

    public boolean createPublicAuction(Product product, Timestamp dayLimit, float initialPrice) {
        String info = "Create the public auction for product " + product.getName() + " with day limit " + dayLimit
                + " and initial price " + initialPrice;
        try {
            ClientLogger.logger.debug("Trying to " + info + ".");
            Auction auction = serviceLocator.getService().createPublicAuction(currentUser, product, dayLimit, initialPrice);
            if (auction != null) {
                currentUser = auction.getOwner();
                ClientLogger.logger.debug(info + " correct.");
            } else {
                ClientLogger.logger.warn(info + " incorrect. Server returned null.");
                return false;
            }
        } catch (RemoteException | AuctionException e) {
            ClientLogger.logger.error("Error creating a public auction: " + e.getMessage());
        }
        return true;
    }

    public boolean createPrivateAuction(Product product, String password, Timestamp dayLimit, float initialPrice) {
        String info = "Create the private auction for product " + product.getName() + " with password " + password +
                ", day limit " + dayLimit + " and initial price " + initialPrice;
        try {
            ClientLogger.logger.debug("Trying to " + info + ".");
            Auction auction = serviceLocator.getService().createPrivateAuction(currentUser, product, dayLimit, initialPrice, password);
            if (auction != null) {
                currentUser = auction.getOwner();
                ClientLogger.logger.debug(info + " correct.");
            } else {
                ClientLogger.logger.warn(info + " incorrect. Server returned null.");
                return false;
            }
        } catch (RemoteException | AuctionException e) {
            ClientLogger.logger.error("Error creating a public auction: " + e.getMessage());
        }
        return true;
    }

    public boolean bid(Auction auction, float amount) {
        String info = "Bid " + amount + " for " + auction.getProduct().getName();
        try {
            ClientLogger.logger.debug("Trying to " + info + ".");
            Auction auctionReturn = serviceLocator.getService().bid(auction, currentUser, amount);
            if (auctionReturn != null) {
                ClientLogger.logger.debug(info + " correct.");
            } else {
                ClientLogger.logger.warn(info + " incorrect. Server returned null.");
                return false;
            }
        } catch (RemoteException | AuctionException e) {
            ClientLogger.logger.error("Error bidding an auction: " + e.getMessage());
            return false;
        }
        return true;
    }

    public ArrayList<Auction> searchAuctionByCountry(String country) {
        ArrayList<Auction> countryAuctions = new ArrayList<>();
        String info = "Get auctions from country " + country;
        try {
            ClientLogger.logger.debug("Trying to " + info + ".");
            countryAuctions = serviceLocator.getService().searchAuctionByCountry(currentUser, country);
            ClientLogger.logger.debug(info + " correct.");
        } catch (RemoteException e) {
            ClientLogger.logger.error("Error searching auctions by country: " + e.getMessage());
        }
        return countryAuctions;
    }

    public ArrayList<Auction> searchAuctionByProductName(String name) {
        ArrayList<Auction> prodNameAuctions = new ArrayList<>();
        String info = "Get auctions with prod. name " + name;
        try {
            ClientLogger.logger.debug("Trying to " + info + ".");
            prodNameAuctions = serviceLocator.getService().searchAuctionByProductName(currentUser, name);
            ClientLogger.logger.debug(info + " correct.");
        } catch (RemoteException e) {
            ClientLogger.logger.error("Error searching auctions by prod name: " + e.getMessage());
        }
        return prodNameAuctions;
    }

    public boolean updateUser(String country, String password) {
        String info = "Update user: " + currentUser.getUsername();
        try {
            ClientLogger.logger.debug("Trying to " + info + ".");
            currentUser = serviceLocator.getService().updateUser(getCurrentUser(), country, password);
            ClientLogger.logger.debug(info + " correct.");
        } catch (RemoteException | AccountException e) {
            ClientLogger.logger.error("Error searching auctions by prod name: " + e.getMessage());
            return false;
        }
        return true;
    }

    public void exit() {
        System.exit(0);
    }

}