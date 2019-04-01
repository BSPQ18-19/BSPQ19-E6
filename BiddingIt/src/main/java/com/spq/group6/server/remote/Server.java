package com.spq.group6.server.remote;

import com.spq.group6.server.data.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Server extends UnicastRemoteObject implements IServer {
    public Server() throws RemoteException {
    }

    @Override
    public User logIn(String username, String password) throws RemoteException {
        return null;
    }

    @Override
    public User signIn(String username, String password, String country) throws RemoteException {
        return new User(username, password, "SPAIN", 100, new Product[10]);
    }
}
