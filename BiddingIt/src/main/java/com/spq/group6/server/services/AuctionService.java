package com.spq.group6.server.services;

import com.spq.group6.server.dao.AuctionDAO;
import com.spq.group6.server.dao.IAuctionDAO;
import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.Bid;
import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;
import com.spq.group6.server.exceptions.AuctionException;
import com.spq.group6.server.utils.AuctionCountdown;
import com.spq.group6.server.utils.AuctionLocks;
import com.spq.group6.server.utils.observer.remote.IRemoteObserver;
import com.spq.group6.server.utils.observer.remote.RemoteObservable;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;

public class AuctionService implements IAuctionService {
    private IAuctionDAO auctionDAO;
    private HashMap<Long, RemoteObservable> observables;

    public AuctionService() {
        auctionDAO = new AuctionDAO();
        observables = new HashMap<Long, RemoteObservable>();
    }

    public Auction createPublicAuction(User owner, Product product, Timestamp dayLimit, float initialPrice) {
        Auction auction = new Auction(owner, product, dayLimit, initialPrice, null);
        auctionDAO.persistAuction(auction);

        // create an observable for the auction
        RemoteObservable observable = new RemoteObservable();
        observables.put(auction.getAuctionID(), observable);

        AuctionLocks.setLock(auction.getAuctionID()); // create lock for auction
        Thread auctionCountdown = new Thread(new AuctionCountdown(auction, observable));
        //auctionCountdown.start(); // Run thread for auction countdown

        return auction;
    }

    public Auction bid(Auction auction, User user, float amount) throws AuctionException {
        Lock auctionLock = AuctionLocks.getLock(auction.getAuctionID());
        auctionLock.lock();

        if (!auctionDAO.isOpen(auction.getAuctionID())){
            throw  new AuctionException("Auction is closed");
        }
        Bid oldBid = auctionDAO.getHighestBid(auction.getAuctionID());
        if (amount< auction.getInitialPrice() || (oldBid != null && oldBid.getAmount() >= amount)){
            throw  new AuctionException("Too low bid");
        }
        Bid newBid = new Bid(user, amount);
        auction.setHighestBid(newBid);
        auctionDAO.persistAuction(auction);
        auctionDAO.deleteBid(oldBid);

        auctionLock.unlock();
        return auction;
    }

    public ArrayList<Auction> searchAuctionByCountry(String country) {
        return auctionDAO.getAuctionByCountry(country);
    }

    public ArrayList<Auction> searchAuctionByProductName(String name) {
        return auctionDAO.getAuctionByProductName(name);
    }

    public ArrayList<Auction> searchAuctionByUser(User user) {
        return auctionDAO.getAuctionByUser(user);
    }

    public void addRemoteObserver(Auction auction, IRemoteObserver observer) throws RemoteException {
        RemoteObservable observable = observables.get(auction.getAuctionID());
        observable.addRemoteObserver(observer);
    }

    public void deleteRemoteObserver(Auction auction, IRemoteObserver observer) throws RemoteException {
        RemoteObservable observable = observables.get(auction.getAuctionID());
        observable.deleteRemoteObserver(observer);
    }
}
