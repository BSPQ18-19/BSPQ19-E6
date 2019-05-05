package com.spq.group6.server.utils.observer.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface implemented by ClientMain's RemotObserver
 * needed for receiving events from the Server.
 */
public interface IRemoteObserver extends Remote {
    void update(Object arg) throws RemoteException;
}