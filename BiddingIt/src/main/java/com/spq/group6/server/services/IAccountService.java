package com.spq.group6.server.services;

import com.spq.group6.server.data.Administrator;
import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;
import com.spq.group6.server.exceptions.AdministratorException;
import com.spq.group6.server.exceptions.UserException;
import com.spq.group6.server.utils.observer.remote.IRemoteObserver;

public interface IAccountService {
    User logIn(String username, String password, IRemoteObserver observer) throws UserException;

    void logOut(IRemoteObserver observer);

    User signIn(String username, String password, String country, IRemoteObserver observer) throws UserException;

    User updateUser(User user, String password, String country) throws UserException;

    Administrator createAdministrator(Administrator admin) throws AdministratorException;

    User createProduct(User user, String name, String description);

    Product updateProduct(Product product, String name, String description);

    User deleteProduct(User user, Product product);

}
