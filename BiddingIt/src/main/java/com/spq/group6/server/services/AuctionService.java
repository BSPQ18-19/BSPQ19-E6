package com.spq.group6.server.services;

import com.spq.group6.server.dao.BiddingDAO;
import com.spq.group6.server.dao.IBiddingDAO;
import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.Bid;
import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;
import com.spq.group6.server.exceptions.AuctionException;
import com.spq.group6.server.utils.AuctionCountdown;
import com.spq.group6.server.utils.AuctionLocks;
import com.spq.group6.server.utils.observer.events.NewBidEvent;
import com.spq.group6.server.utils.observer.remote.IRemoteObserver;
import com.spq.group6.server.utils.observer.remote.RemoteObservable;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;

public class AuctionService implements IAuctionService {
    private IBiddingDAO biddingDAO;
    private HashMap<Long, RemoteObservable> observables;

    public AuctionService() {
        biddingDAO = new BiddingDAO();
        observables = new HashMap<Long, RemoteObservable>();
    }

    public Auction createPublicAuction(User owner, Product product, Timestamp dayLimit, float initialPrice) {
        Auction auction = new Auction(owner, product, dayLimit, initialPrice, null);
        biddingDAO.persistAuction(auction);

        RemoteObservable observable = new RemoteObservable();
        observables.put(auction.getAuctionID(), observable);

        AuctionLocks.setLock(auction.getAuctionID()); // create lock for auction
        Thread auctionCountdown = new Thread(new AuctionCountdown(auction, observable));
        auctionCountdown.start(); // Run thread for auction countdown

        return auction;
    }

    public Auction bid(Auction auction, User user, float amount) throws AuctionException {
        AuctionLocks.getLock(auction.getAuctionID()).lock();

        auction = biddingDAO.getAuctionByID(auction.getAuctionID());
        if (!auction.isOpen()){
            throw  new AuctionException("Auction is closed");
        }
        Bid oldBid = auction.getHighestBid();
        if (amount< auction.getInitialPrice() || (oldBid != null && oldBid.getAmount() >= amount)){
            throw  new AuctionException("Too low bid");
        }
        Bid newBid = new Bid(user, amount);
        auction.setHighestBid(newBid);
        biddingDAO.persistAuction(auction);
        biddingDAO.deleteBid(oldBid);
        // Notify about new Bid
        NewBidEvent newBidEvent = new NewBidEvent(auction);
        observables.get(auction.getAuctionID()).notifyRemoteObservers(newBidEvent);

        AuctionLocks.getLock(auction.getAuctionID()).unlock();
        return auction;
    }

    public ArrayList<Auction> searchAuctionByCountry(String country) {
        return biddingDAO.getAuctionByCountry(country);
    }

    public ArrayList<Auction> searchAuctionByProductName(String name) {
        return biddingDAO.getAuctionByProductName(name);
    }

    public ArrayList<Auction> getAllAuctions() {
        return biddingDAO.getAllAuctions();
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
