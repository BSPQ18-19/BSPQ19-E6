package com.spq.group6.server.remote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


import com.spq.group6.server.data.User;
import com.spq.group6.server.exceptions.UserException;
import com.spq.group6.server.services.Account;
import com.spq.group6.server.services.IAccount;


public class Server extends UnicastRemoteObject implements IServer {
    private IAccount accountService;

    public Server() throws RemoteException {
        accountService = new Account();
    }

    public User logIn(String username, String password) throws RemoteException, UserException {
        System.out.println("Received Log in petition");
        return accountService.logIn(username, password);
    }

    public User signIn(String username, String password, String country) throws RemoteException, UserException{
        System.out.println("Received Sign in petition");
        return accountService.signIn(username, password, country);
    }

    public void updateUser(User user) throws RemoteException, UserException {
        System.out.println("Received update petition");
        accountService.updateUser(user);
    }
}
