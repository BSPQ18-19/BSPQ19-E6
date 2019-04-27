package com.spq.group6.server.services;

import com.spq.group6.server.dao.BiddingDAO;
import com.spq.group6.server.dao.IBiddingDAO;
import com.spq.group6.server.data.Administrator;
import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;
import com.spq.group6.server.exceptions.AdministratorException;
import com.spq.group6.server.exceptions.UserException;
import com.spq.group6.server.utils.BiddingLocks;
import com.spq.group6.server.utils.logger.ServerLogger;
import com.spq.group6.server.utils.observer.remote.RemoteObservable;
import com.spq.group6.server.utils.observer.remote.IRemoteObserver;
import com.spq.group6.server.utils.observer.remote.RemoteObserver;

import java.util.HashMap;

public class AccountService implements IAccountService {
    private IBiddingDAO biddingDAO;
    public static RemoteObservable observable = new RemoteObservable();

    public AccountService() {
        biddingDAO = new BiddingDAO();
        for(User user: biddingDAO.getAllUsers()){
            init_user(user);
        }
    }


    public User logIn(String username, String password, IRemoteObserver observer) throws UserException {
        User user = biddingDAO.getUserByUsername(username);
        if (user == null) throw new UserException("User does not exist");
        if (!password.equals(user.getPassword())) throw new UserException("Invalid username or password");
        observable.addRemoteObserver(observer);
        return user;
    }

    public void logOut(String username, IRemoteObserver observer) {
        observable.deleteRemoteObserver(observer);
    }

    public User signIn(String username, String password, String country, IRemoteObserver observer) throws UserException {
        User user = new User(username, password, country);
        checkDuplicatedUser(user);
        biddingDAO.createUser(user);
        init_user(user);
        ServerLogger.logger.debug("User '" + username + "' has signed in.");
        return user;
    }

    public User updateUser(User user) throws UserException {
        user = BiddingLocks.lockAndGetUser(user);

        try {
            checkDuplicatedUser(user);
            biddingDAO.updateUser(user);
        }catch (Exception e){
            BiddingLocks.unlockUser(user);
            throw e;
        }

        BiddingLocks.unlockUser(user);
        return user;
    }
    
    public Administrator createAdministrator(Administrator admin) throws AdministratorException {
        biddingDAO.createAdministrator(admin);
        return admin;
    }

    public User createProduct(User user, String name, String description) {
        user = BiddingLocks.lockAndGetUser(user);

        try{
            Product newProduct = new Product(name, description);
            user.getOwnedProducts().add(newProduct);
            biddingDAO.updateUser(user);
        }catch (Exception e){
            BiddingLocks.unlockUser(user);
            throw e;
        }
        BiddingLocks.unlockUser(user);
        return user;
    }

    public Product updateProduct(Product product, String name, String description) {
        product.setName(name);
        product.setDescription(description);
        biddingDAO.updateProduct(product);
        return product;
    }

    public User deleteProduct(User user, Product product) {
        user = BiddingLocks.lockAndGetUser(user);

        try{
            user.getOwnedProducts().remove(product);
            biddingDAO.updateUser(user);
            biddingDAO.deleteProduct(product);
        }catch (Exception e){
            BiddingLocks.unlockUser(user);
            throw e;
        }
        BiddingLocks.unlockUser(user);
        return user;
    }

    private void checkDuplicatedUser(User user) throws UserException {
        String username = user.getUsername();
        if (biddingDAO.getUserByUsername(username) != null) throw new UserException("Username already in use.");
    }

    void init_user(User user) {
        BiddingLocks.setUserLock(user);
    }

}
