package com.spq.group6.server.utils;

import com.spq.group6.server.dao.BiddingDAO;
import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.User;
import com.spq.group6.server.utils.logger.ServerLogger;

import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Class needed for limiting access to User and Auctions.
 * <p>
 * This class contains a Lock for each User and Auction.
 * Locks are needed when there are Client concurrent requests
 * updating the same Object on the database.
 * <p>
 * The following classes use locks contained in this class.
 * {@link com.spq.group6.server.services.AccountService}
 * {@link com.spq.group6.server.services.AuctionService}
 * {@link com.spq.group6.server.services.AdminService}
 */
public class BiddingLocks {
    private static HashMap<Long, Lock> auctionLocks = new HashMap<Long, Lock>();
    /**
     * static HashMap containing Locks for every Auction.
     * Auctions are identified by their ID (long)
     */
    private static HashMap<String, Lock> userLocks = new HashMap<String, Lock>();
    /**
     * static HashMap containing Locks for every Client.
     * Clients are identified by their username(String)
     */
    private static BiddingDAO biddingDAO = new BiddingDAO(); /* DAO object for updating
     persistent data. It is used for accesing Users and Auctions latest versions */

    /**
     * Method for locking the access for an Auction
     * and getting Auction's last version from the database
     *
     * @param auction Auction that will be locked and retrieved from the database
     * @return Auction's last version from the database
     */
    public static synchronized Auction lockAndGetAuction(Auction auction) {
        long auctionID = auction.getAuctionID();
        if (!auctionLocks.containsKey(auctionID)) { // If it does not exist, creates a Lock
            auctionLocks.put(auctionID, new ReentrantLock());
        }
        ServerLogger.logger.debug("Waiting Auction " + auctionID + " lock...");
        auctionLocks.get(auctionID).lock();
        ServerLogger.logger.debug("Locked Auction " + auctionID);
        return biddingDAO.getAuctionByID(auctionID); /* Retrieves Auction's latest version from the database,
        and returns it. */
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
     * Method for unlocking a previously Locked Auction
     *
     * @param auction Auction that will be unlocked
     */
    public static void unlockAuction(Auction auction) {
        long auctionID = auction.getAuctionID();
        auctionLocks.get(auctionID).unlock();
        ServerLogger.logger.debug("Unlocked Auction " + auctionID);
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
        ServerLogger.logger.debug("Waiting User "+ username + " lock...");
        userLocks.get(username).lock();
        ServerLogger.logger.debug("Locked User " + username);
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
        ServerLogger.logger.debug("Unlocked User " + username);
    }
}
