package com.spq.group6.server.utils.observer.remote;

import com.spq.group6.server.utils.logger.ServerLogger;

import java.util.ArrayList;
import java.util.List;

/*
 * INSTRUCTIONS: - The remote server will keep a reference to a RemoteObservable
 * object (this class) to which will delegate every subscription and call when
 * updates are needed.
 *
 * The RemoteObservable object will not inherit any remote interface because
 * it's not a remote object.
 */
public class RemoteObservable {

    /**
     * List for storing remote observers
     */
    private List<IRemoteObserver> remoteObservers;
    private List<IRemoteObserver> newRemoteObservers;

    public RemoteObservable() {
        this.remoteObservers = new ArrayList<>();
        this.newRemoteObservers = new ArrayList<>();
    }

    public void addRemoteObserver(IRemoteObserver observer) {
        if (observer != null && this.remoteObservers.indexOf(observer) == -1) {
            this.newRemoteObservers.add(observer);
        }
    }

    public void deleteRemoteObserver(IRemoteObserver observer) {
        if (observer != null) {
            this.newRemoteObservers.remove(observer);
        }
    }

    public synchronized void deleteRemoteObservers() {
        this.remoteObservers.clear();
    }

    public synchronized int countRemoteObservers() {
        return this.remoteObservers.size();
    }

    public synchronized void notifyRemoteObservers(Object arg) {
        remoteObservers = new ArrayList<>(newRemoteObservers);
        for (IRemoteObserver observer : remoteObservers) {
            try {
                if (observer != null) {
                    observer.update(arg);
                }
            } catch (Exception ex) {
                ServerLogger.logger.error("$ Error notifying remote observers: " + ex, ex);
                deleteRemoteObserver(observer);
            }
        }
    }
}