package com.spq.group6.server.services;

import com.spq.group6.server.data.Administrator;
import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;
import com.spq.group6.server.exceptions.AccountException;
import com.spq.group6.server.exceptions.AdministratorException;
import com.spq.group6.server.utils.observer.remote.IRemoteObserver;

/**
 * Accounts related service's interface
 * <p>
 * BiddingIt server's Application for account related services:
 * - Account creation
 * - Authentication
 * - Product creation
 * - Product removal
 * - Account details modification
 * - Product details modification
 */
public interface IAccountService {
    User logIn(String username, String password, IRemoteObserver observer) throws AccountException;

    void logOut(IRemoteObserver observer);

    User signIn(String username, String password, String country, IRemoteObserver observer) throws AccountException;

    User updateUser(User user, String country, String password) throws AccountException;

    Administrator createAdministrator(Administrator admin) throws AdministratorException;

    User createProduct(User user, String name, String description);

    Product updateProduct(Product product, String name, String description);

    User deleteProduct(User user, Product product);

}
