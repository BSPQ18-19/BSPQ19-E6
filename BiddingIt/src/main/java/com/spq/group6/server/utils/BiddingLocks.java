package com.spq.group6.server.utils;

import com.spq.group6.server.dao.BiddingDAO;
import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.User;

import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BiddingLocks {
    private static HashMap<Long, Lock> auctionLocks = new HashMap<Long, Lock>();
    private static HashMap<String, Lock> userLocks = new HashMap<String, Lock>();
    private static BiddingDAO biddingDAO = new BiddingDAO();

    public static synchronized Auction lockAndGetAuction(Auction auction) {
        long auctionID = auction.getAuctionID();
        if (!auctionLocks.containsKey(auctionID)) {
            auctionLocks.put(auctionID, new ReentrantLock());
        }
        auctionLocks.get(auctionID).lock();
        return biddingDAO.getAuctionByID(auctionID);
    }

    public static synchronized void setAuctionLock(Auction auction) {
        long auctionID = auction.getAuctionID();
        if (!auctionLocks.containsKey(auctionID)) {
            auctionLocks.put(auctionID, new ReentrantLock());
        }
    }

    public static void unlockAuction(Auction auction) {
        long auctionID = auction.getAuctionID();
        auctionLocks.get(auctionID).unlock();
    }

    public static synchronized User lockAndGetUser(User user) {
        String username = user.getUsername();
        if (!userLocks.containsKey(username)) {
            userLocks.put(username, new ReentrantLock());
        }
        userLocks.get(username).lock();
        return biddingDAO.getUserByUsername(username);
    }

    public static synchronized void setUserLock(User user) {
        String username = user.getUsername();
        if (!userLocks.containsKey(username)) {
            userLocks.put(username, new ReentrantLock());
        }
    }

    public static void unlockUser(User user) {
        String username = user.getUsername();
        userLocks.get(username).unlock();
    }
}
