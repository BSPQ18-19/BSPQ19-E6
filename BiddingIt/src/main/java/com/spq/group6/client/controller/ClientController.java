package com.spq.group6.client.controller;

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

/**
 * Controller of the Client service
 * <p>
 * This is the API that describes functionality offered by BiddingIt Client service.
 */
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

    public void setClientRemoteObserver() {
        observer.setClientWindow();
    }

    /**
     * Method for login and getting access to the system.
     * <p>
     * Checks if Admin credentials are valid,
     * and returns a Admin object that matched.
     *
     * @param email    user's email
     * @param password user's password
     * @return Logged in User
     * @throws RemoteException is raised in case of Error on RMI connection
     */
    public boolean logIn(String email, String password) throws RemoteException {
        String info = "Log in with username " + email + " and password " + password;
        try {
            ClientLogger.logger.debug("Trying to " + info + ".");
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

    /**
     * Method for sign int BiddingIt database, login and getting access to the system.
     * <p>
     * Checks if User credentials are valid,
     * and returns a User object that matched.
     *
     * @param email    user's email
     * @param password user's password
     * @param country  user's country
     * @return Logged in User
     */
    public boolean signIn(String email, String password, String country) {
        String info = "Sign in with username " + email + " and password " + password + " and country " + country;
        try {
            ClientLogger.logger.debug("Trying to " + info + ".");
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

    /**
     * Method to create a new product.
     * <p>
     *
     * @param name        name of the product
     * @param description description of the product
     * @return product created boolean
     */
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

    /**
     * Method to update a product.
     * <p>
     *
     * @param product     product-type object that will be updated
     * @param name        new name of the product
     * @param description new description of the product
     * @return product updated boolean
     */
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

    /**
     * Method to update a product.
     * <p>
     *
     * @param product product-type object that will be deleted
     * @return product deleted boolean
     */
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

    /**
     * Method to create a public auction.
     * <p>
     *
     * @param product      product-type object that will be auctioned
     * @param dayLimit     temporal limit of the auction
     * @param initialPrice initial price of the auction
     * @return auction created boolean
     */
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

    /**
     * Method to create a private auction.
     * <p>
     *
     * @param product      product-type object that will be auctioned
     * @param password     password that makes the auction private
     * @param dayLimit     temporal limit of the auction
     * @param initialPrice initial price of the auction
     * @return auction created boolean
     */
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


    /**
     * Method to bid in an auction.
     * <p>
     *
     * @param auction auction that will be bided
     * @param amount  amount of money that is bided
     * @return auction bided boolean
     */
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


    public ArrayList<Auction> getAllAuctions() {
        ArrayList<Auction> allAuctions = new ArrayList<>();
        String info = "Get all auctions";
        try {
            ClientLogger.logger.debug("Trying to " + info + ".");
            allAuctions = serviceLocator.getService().getAllAuctions(currentUser);
            ClientLogger.logger.debug(info + " correct.");
        } catch (RemoteException e) {
            ClientLogger.logger.error("Error getting all auctions: " + e.getMessage());
        }
        return allAuctions;
    }

    /**
     * Method to request auctions located in a specific country
     * <p>
     *
     * @param country country of which auctions are requested
     * @return arraylist of auctions
     */
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

    /**
     * Method to request auctions depending its Product Name
     * <p>
     *
     * @param name name of the product requested
     * @return arraylist of auctions
     */
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

    /**
     * Method to update a User.
     * <p>
     *
     * @param country  country of the updated user
     * @param password password of the updated user
     * @return user updated boolean
     */
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