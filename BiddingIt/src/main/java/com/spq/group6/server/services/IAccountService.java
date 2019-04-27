package com.spq.group6.server.services;

import com.spq.group6.server.data.Administrator;
import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;
import com.spq.group6.server.exceptions.AdministratorException;
import com.spq.group6.server.exceptions.UserException;
import com.spq.group6.server.utils.observer.remote.RemoteObserver;

public interface IAccountService {
    public User logIn(String username, String password, RemoteObserver observer) throws UserException;

    void logOut(String username, RemoteObserver observer);

    public User signIn(String username, String password, String country, RemoteObserver observer) throws UserException;

    public User updateUser(User user) throws UserException;
    
    public Administrator createAdministrator(Administrator admin) throws AdministratorException;

    public User createProduct(User user, String name, String description);

    public Product updateProduct(Product product, String name, String description);

    public User deleteProduct(User user, Product product);

}
