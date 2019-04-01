package com.spq.group6.server.remote;

import com.spq.group6.server.data.User;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IServer extends Remote {
    User logIn(String username, String password) throws RemoteException;

    User signIn(String username, String password, String country) throws RemoteException;
}
