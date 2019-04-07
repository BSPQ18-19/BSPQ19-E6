package com.spq.group6.server.remote;

import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;
import com.spq.group6.server.exceptions.UserException;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IServer extends Remote {
    public User logIn(String username, String password) throws RemoteException, UserException;
    public User signIn(String username, String password, String country) throws RemoteException, UserException;
    public void updateUser(User user) throws RemoteException, UserException;
    public void createProduct(User user, Product product) throws RemoteException;
    public void updateProduct(User user, Product product) throws RemoteException;
    public void deleteProduct(User user, Product product) throws RemoteException;

}
