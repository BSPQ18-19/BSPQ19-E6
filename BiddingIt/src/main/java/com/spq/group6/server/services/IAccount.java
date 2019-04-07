package com.spq.group6.server.services;

import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;
import com.spq.group6.server.exceptions.UserException;

import java.rmi.RemoteException;

public interface IAccount {
    public User logIn(String username, String password) throws UserException;
    public User signIn(String username, String password, String country) throws UserException;
    public void updateUser(User user) throws UserException;
    public Product createProduct(User user, String name, String description);
    public Product updateProduct(User user, Product product, String name, String description);
    public void deleteProduct(User user, Product product);
}
