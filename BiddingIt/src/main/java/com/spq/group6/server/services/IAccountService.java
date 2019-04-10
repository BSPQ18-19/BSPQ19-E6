package com.spq.group6.server.services;

import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;
import com.spq.group6.server.exceptions.UserException;

import java.rmi.RemoteException;

public interface IAccountService {
    public User logIn(String username, String password) throws UserException;
    public User signIn(String username, String password, String country) throws UserException;
    public User updateUser(User user) throws UserException;
    public User createProduct(User user, String name, String description);
    public Product updateProduct(Product product, String name, String description);
    public User deleteProduct(User user, Product product);
}
