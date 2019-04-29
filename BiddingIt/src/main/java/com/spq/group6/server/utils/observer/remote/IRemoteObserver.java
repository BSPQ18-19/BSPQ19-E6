package com.spq.group6.server.utils.observer.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRemoteObserver extends Remote {
    void update(Object arg) throws RemoteException;
}