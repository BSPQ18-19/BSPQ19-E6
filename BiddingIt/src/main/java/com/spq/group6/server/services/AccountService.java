package com.spq.group6.server.services;

import com.spq.group6.server.dao.BiddingDAO;
import com.spq.group6.server.dao.IBiddingDAO;
import com.spq.group6.server.data.Administrator;
import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;
import com.spq.group6.server.exceptions.AccountException;
import com.spq.group6.server.exceptions.AdministratorException;
import com.spq.group6.server.utils.BiddingLocks;
import com.spq.group6.server.utils.observer.remote.IRemoteObserver;
import com.spq.group6.server.utils.observer.remote.RemoteObservable;

public class AccountService implements IAccountService {
    public static RemoteObservable observable = new RemoteObservable();
    /**
     * Remote observable responsible for
     * notifying Clients. Contains all User RemoteObservers.
     */
    private IBiddingDAO biddingDAO;

    public AccountService() {
        biddingDAO = new BiddingDAO();
    }


    public User logIn(String username, String password, IRemoteObserver observer) throws AccountException {
        User user = biddingDAO.getUserByUsername(username);
        if (user == null) throw new AccountException("User does not exist");
        if (!password.equals(user.getPassword())) throw new AccountException("Invalid username or password");
        initUser(user, observer);
        return user;
    }

    public void logOut(IRemoteObserver observer) {
        observable.deleteRemoteObserver(observer);
    }

    public User signIn(String username, String password, String country, IRemoteObserver observer) throws AccountException {
        User user = new User(username, password, country);
        // Added 1000 as initial money, otherwise a Payment service would be needed.
        user.setMoney(1000);
        checkDuplicatedUser(user);
        biddingDAO.persistUser(user);
        initUser(user, observer);
        return user;
    }

    public User updateUser(User user, String country, String password) throws AccountException {
        user = BiddingLocks.lockAndGetUser(user);
        user.setPassword(password);
        user.setCountry(country);
        try {
            biddingDAO.persistUser(user);
        } catch (Exception e) {
            BiddingLocks.unlockUser(user);
            throw e;
        }

        BiddingLocks.unlockUser(user);
        return user;
    }

    public Administrator createAdministrator(Administrator admin) throws AdministratorException {
        biddingDAO.persistAdministrator(admin);
        return admin;
    }

    public User createProduct(User user, String name, String description) {
        user = BiddingLocks.lockAndGetUser(user);

        try {
            Product newProduct = new Product(name, description);
            user.getOwnedProducts().add(newProduct);
            biddingDAO.persistUser(user);
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
        biddingDAO.persistUser(product);
        return product;
    }

    public User deleteProduct(User user, Product product) {
        user = BiddingLocks.lockAndGetUser(user);
        try {
            user.getOwnedProducts().remove(product);
            biddingDAO.persistUser(user);
            biddingDAO.deleteProduct(product);
        } catch (Exception e) {
            BiddingLocks.unlockUser(user);
            throw e;
        }
        BiddingLocks.unlockUser(user);
        return user;
    }

    /**
     * Method for checking if a User already exists.
     * If alreay exists, throws an Exception.
     *
     * @param user User that will be checked.
     * @throws AccountException in case of User previous existance.
     */
    private void checkDuplicatedUser(User user) throws AccountException {
        String username = user.getUsername();
        if (biddingDAO.getUserByUsername(username) != null) throw new AccountException("Username already in use.");
    }

    /**
     * Method for creating Lock and adding a RemoteObserver for a User.
     *
     * @param user User that a Lock will be created for.
     */
    void initUser(User user, IRemoteObserver observer) {
        BiddingLocks.setUserLock(user);
        observable.addRemoteObserver(observer);
    }

}
