package com.spq.group6.server.dao;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.Bid;
import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;
import com.spq.group6.server.utils.logger.ServerLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;

public class AuctionDAO implements IAuctionDAO{

    private PersistenceManager pm;
    private Lock pmLock;

    public AuctionDAO(){

        pm = JdoManager.getPersistanceManager();
        pmLock = JdoManager.pmLock;
    }

    public void persistAuction(Auction auction){
        updateObject(auction);
    }

    public void persistBid(Auction auction, Bid bid){
        auction.setHighestBid(bid);
        updateObject(auction);
    }

    public void deleteBid(Bid bid){
        pmLock.lock();

        Transaction tx = pm.currentTransaction();

        try {
            tx.begin();
            pm.deletePersistent(bid);
            tx.commit();
        } catch (Exception ex) {

            ServerLogger.logger.error("* Exception deleting data: " + ex.getMessage());
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
        pmLock.unlock();
    }

    public ArrayList<Auction> getAuctionByCountry(String country){
        pmLock.lock();

        Transaction tx = pm.currentTransaction();
        ArrayList<Auction> auctions = null;
        try {
            tx.begin();
            Query<Auction> query = pm.newQuery(Auction.class);
            query.setFilter("country == '" + country + "'");
            auctions = (ArrayList<Auction>) query.execute();// Retrieves and detaches the ArrayList of auctions

            tx.commit();
        } catch (Exception ex) {

            ServerLogger.logger.error("* Exception deleting data: " + ex.getMessage());
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
        pmLock.unlock();
        return auctions;
    }

    public ArrayList<Auction> getAuctionByProductName(String name){
        pmLock.lock();

        Transaction tx = pm.currentTransaction();
        ArrayList<Auction> auctions = null;
        try {
            tx.begin();
            Query<Auction> query = pm.newQuery(Auction.class);
            query.setFilter("name == '" + name + "'");
            auctions = (ArrayList<Auction>) query.execute();// Retrieves and detaches the ArrayList of auctions

            tx.commit();
        } catch (Exception ex) {

            ServerLogger.logger.error("* Exception deleting data: " + ex.getMessage());
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
        pmLock.unlock();
        return auctions;
    }

    public boolean isOpen(long auctionID){
        pmLock.lock();

        Transaction tx = pm.currentTransaction();
        boolean isOpen = true;
        try {
            tx.begin();
            Query<Auction> query = pm.newQuery(Auction.class);
            query.setFilter("auctionID == '" + auctionID + "'");
            List<Auction> result = (List<Auction>) query.execute();
            Auction auction = result.size() != 1 ? null : result.get(0); // Retrieves and detaches the Auction
            if(auction == null){
                return false;
            }
            isOpen = auction.isOpen();

            tx.commit();
        } catch (Exception ex) {

            ServerLogger.logger.error("* Exception deleting data: " + ex.getMessage());
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
        pmLock.unlock();
        return isOpen;
    }

    public Bid getHighestBid(long auctionID){
        pmLock.lock();

        Transaction tx = pm.currentTransaction();
        Bid highestBid = null;
        try {
            tx.begin();
            Query<Auction> query = pm.newQuery(Auction.class);
            query.setFilter("auctionID == '" + auctionID + "'");
            List<Auction> result = (List<Auction>) query.execute();
            highestBid = result.size() != 1 ? null : result.get(0).getHighestBid(); // Retrieves and detaches the Bid

            tx.commit();
        } catch (Exception ex) {

            ServerLogger.logger.error("* Exception deleting data: " + ex.getMessage());
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
        pmLock.unlock();
        return highestBid;
    }

    public void closeAuction(long auctionID){
        pmLock.lock();

        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            Query<Auction> query = pm.newQuery(Auction.class);
            query.setFilter("auctionID == '" + auctionID + "'");
            List<Auction> result = (ArrayList<Auction>) query.execute();
            Auction auction = (result.size() != 1) ? null : result.get(0);
            if(auction != null) {
                auction.setOpen(false);
                pm.makePersistent(auction);
            }
            tx.commit();
        } catch (Exception ex) {

            ServerLogger.logger.error("* Exception deleting data: " + ex.getMessage());
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
        pmLock.unlock();
    }

    @Override
    public ArrayList<Auction> getAuctionByUser(User user) {
        return null;
    }

    private void updateObject(Object obj) {
        pmLock.lock();

        Transaction tx = pm.currentTransaction();

        try {
            tx.begin();

            pm.makePersistent(obj);
            tx.commit();
        } catch (Exception ex) {

            ServerLogger.logger.error("* Exception inserting/updating data into db: " + ex.getMessage());

        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
        pmLock.unlock();
    }
}
