package com.spq.group6.server.utils;

import com.spq.group6.server.utils.observer.events.AuctionClosedEvent;
import com.spq.group6.server.utils.observer.events.AuctionDeletedEvent;
import com.spq.group6.server.utils.observer.events.NewBidEvent;
import com.spq.group6.server.utils.observer.remote.IRemoteObserver;

import java.rmi.RemoteException;

public class ObserverDemo implements IRemoteObserver {
    public boolean newBid = false, auctionClosed = false, auctionDeleted = false, userDeleted = false;

    public void update(Object arg) throws RemoteException {
        if(arg instanceof NewBidEvent){
            newBid = true;
        }
        else if(arg instanceof AuctionClosedEvent){
            auctionClosed = true;
        }
        else if(arg instanceof AuctionDeletedEvent){
            auctionDeleted = true;
        }
        else{
            userDeleted = true;
        }
    }
}
