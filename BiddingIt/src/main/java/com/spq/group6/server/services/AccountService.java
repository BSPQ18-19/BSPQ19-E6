package com.spq.group6.server.services;

import com.spq.group6.server.dao.BiddingDAO;
import com.spq.group6.server.dao.IBiddingDAO;
import com.spq.group6.server.data.Administrator;
import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;
import com.spq.group6.server.exceptions.AdministratorException;
import com.spq.group6.server.exceptions.AccountException;
import com.spq.group6.server.utils.BiddingLocks;
import com.spq.group6.server.utils.logger.ServerLogger;
import com.spq.group6.server.utils.observer.remote.IRemoteObserver;
import com.spq.group6.server.utils.observer.remote.RemoteObservable;

/**
 * BiddingIt server's Application for account related services:
 * - Account creation
 * - Authentication
 * - Product creation
 * - Product removal
 * - Account details modification
 * - Product details modification
 */
public class AccountService implements IAccountService {
    public static RemoteObservable observable = new RemoteObservable();
    private IBiddingDAO biddingDAO;

    public AccountService() {
        biddingDAO = new BiddingDAO();
        for (User user : biddingDAO.getAllUsers()) {
            init_user(user);
        }
    }


    public User logIn(String username, String password, IRemoteObserver observer) throws AccountException {
        User user = biddingDAO.getUserByUsername(username);
        if (user == null) throw new AccountException("User does not exist");
        if (!password.equals(user.getPassword())) throw new AccountException("Invalid username or password");
        observable.addRemoteObserver(observer);
        return user;
    }

    public void logOut(IRemoteObserver observer) {
        observable.deleteRemoteObserver(observer);
    }

    public User signIn(String username, String password, String country, IRemoteObserver observer) throws AccountException {
        User user = new User(username, password, country);
        user.setMoney(1000);
        checkDuplicatedUser(user);
        biddingDAO.createUser(user);
        init_user(user);
        ServerLogger.logger.debug("User '" + username + "' has signed in.");
        return user;
    }

    public User updateUser(User user, String password, String country) throws AccountException {
        user = BiddingLocks.lockAndGetUser(user);
        user.setPassword(password);
        user.setCountry(country);
        try {
            biddingDAO.updateUser(user);
        } catch (Exception e) {
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

        try {
            Product newProduct = new Product(name, description);
            user.getOwnedProducts().add(newProduct);
            biddingDAO.updateUser(user);
        } catch (Exception e) {
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

        try {
            user.getOwnedProducts().remove(product);
            biddingDAO.updateUser(user);
            biddingDAO.deleteProduct(product);
        } catch (Exception e) {
            BiddingLocks.unlockUser(user);
            throw e;
        }
        BiddingLocks.unlockUser(user);
        return user;
    }

    /**
     * Method for checking if user already exists
     *
     * @param user User that will be checked
     * @throws AccountException in case of User previous existance
     */
    private void checkDuplicatedUser(User user) throws AccountException {
        String username = user.getUsername();
        if (biddingDAO.getUserByUsername(username) != null) throw new AccountException("Username already in use.");
    }

    /**
     * Method for creating Lock for User
     *
     * @param user User that a Lock will be created for
     */
    void init_user(User user) {
        BiddingLocks.setUserLock(user);
    }

}
