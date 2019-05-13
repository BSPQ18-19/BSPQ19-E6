package com.spq.group6.server.utils;

import com.spq.group6.server.dao.BiddingDAO;
import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.User;

import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Class needed for limiting access to User and Auctions.
 * This class contains a Lock for each User and Auction.
 * Locks are needed when there are ClientMain concurrent requests
 * updating the same Object on the database
 */
public class BiddingLocks {
    private static HashMap<Long, Lock> auctionLocks = new HashMap<Long, Lock>(); /** HashMap containing Locks for every Auction.
     Auctions are identified by their ID (long)*/
    private static HashMap<String, Lock> userLocks = new HashMap<String, Lock>();/** HashMap containing Locks for every Client.
     Clients are identified by their username(String)*/
    private static BiddingDAO biddingDAO = new BiddingDAO();

    /**
     * Method for locking the access for an Auction
     * and getting Auction's last version from the database
     *
     * @param auction Auction that will be locked and retrieved from the database
     * @return Auction's last version from the database
     */
    public static synchronized Auction lockAndGetAuction(Auction auction) {
        long auctionID = auction.getAuctionID();
        if (!auctionLocks.containsKey(auctionID)) {
            auctionLocks.put(auctionID, new ReentrantLock());
        }
        auctionLocks.get(auctionID).lock();
        return biddingDAO.getAuctionByID(auctionID);
    }

    /**
     * Method for creating a Lock for an Auction
     *
     * @param auction Auction that a Lock will be created for
     */
    public static synchronized void setAuctionLock(Auction auction) {
        long auctionID = auction.getAuctionID();
        if (!auctionLocks.containsKey(auctionID)) {
            auctionLocks.put(auctionID, new ReentrantLock());
        }
    }

    /**
     * Method for unlocking and Auction previously Locked
     *
     * @param auction Auction that will be unlocked
     */
    public static void unlockAuction(Auction auction) {
        long auctionID = auction.getAuctionID();
        auctionLocks.get(auctionID).unlock();
    }

    /**
     * Method for locking the access for a User
     * and getting User's last version from the database
     *
     * @param user User that will be locked and retrieved from the database
     * @return User's last version from the database
     */
    public static synchronized User lockAndGetUser(User user) {
        String username = user.getUsername();
        if (!userLocks.containsKey(username)) {
            userLocks.put(username, new ReentrantLock());
        }
        userLocks.get(username).lock();
        return biddingDAO.getUserByUsername(username);
    }

    /**
     * Method for creating a Lock for a User
     *
     * @param user User that a Lock will be created for
     */
    public static synchronized void setUserLock(User user) {
        String username = user.getUsername();
        if (!userLocks.containsKey(username)) {
            userLocks.put(username, new ReentrantLock());
        }
    }

    /**
     * Method for unlocking a User previously locked
     *
     * @param user User that will be unlocked
     */
    public static void unlockUser(User user) {
        String username = user.getUsername();
        userLocks.get(username).unlock();
    }
}
