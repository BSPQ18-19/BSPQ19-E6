package com.spq.group6.server.utils.observer.remote;

import com.spq.group6.server.data.Auction;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRemoteObservable extends Remote {
	public void addRemoteObserver(Auction auction, IRemoteObserver observer) throws RemoteException;
	public void deleteRemoteObserver(Auction auction, IRemoteObserver observer) throws RemoteException;
}