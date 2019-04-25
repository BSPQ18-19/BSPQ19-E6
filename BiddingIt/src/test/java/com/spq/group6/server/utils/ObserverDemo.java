package com.spq.group6.server.utils;

import com.spq.group6.server.utils.observer.remote.IRemoteObserver;

import java.rmi.RemoteException;

public class ObserverDemo implements IRemoteObserver {
    public boolean updated = false;

    public void update(Object arg) throws RemoteException {
        updated = true;
    }
}
