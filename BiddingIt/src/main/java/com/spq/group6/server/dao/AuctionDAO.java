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
    private UtilsDAO utilsDAO;
    public AuctionDAO(){

        pm = JdoManager.getPersistanceManager();
        pmLock = JdoManager.pmLock;
        utilsDAO = new UtilsDAO();
    }

    public void persistAuction(Auction auction){
        utilsDAO.updateObject(auction);
    }

    public void deleteAuction(Auction auction) {
        pmLock.lock();

        Transaction tx = pm.currentTransaction();

        try {
            tx.begin();
            Bid bid = auction.getHighestBid();
            pm.deletePersistent(auction);
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

    public void persistBid(Auction auction, Bid bid){
        auction.setHighestBid(bid);
        utilsDAO.updateObject(auction);
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
            Query<User> query = pm.newQuery(User.class);
            query.setFilter("country == '" + country + "'");
            ArrayList<User> users = (ArrayList<User>) query.execute();// Retrieves and detaches the ArrayList of users

            for(int i = 0; i < users.size(); i++) {
                Query<Auction> query2 = pm.newQuery(Auction.class);
                query2.setFilter("owner == '" + users.get(i) + "'");
                Auction isNull = (Auction) query2.execute();
                if(isNull != null && isNull.isOpen()) {
                    auctions.add(i, isNull);// Retrieves and detaches the ArrayList of auctions
                }
            }
            tx.commit();
        } catch (Exception ex) {

            ServerLogger.logger.error("* Exception taking data: " + ex.getMessage());
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

            Query<Product> query = pm.newQuery(Product.class);
            query.setFilter("name == '" + name + "'");
            ArrayList<Product> products = (ArrayList<Product>) query.execute();

            for(int i = 0; i < products.size(); i++) {
                Query<Auction> query2 = pm.newQuery(Auction.class);
                query2.setFilter("product == '" + products.get(i) + "'");
                Auction isNull = (Auction) query2.execute();
                if(isNull != null && isNull.isOpen()) {
                    auctions.add(i, isNull);// Retrieves and detaches the ArrayList of auctions
                }
            }

            tx.commit();
        } catch (Exception ex) {

            ServerLogger.logger.error("* Exception taking data: " + ex.getMessage());
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
            query.setFilter("auctionID == " + auctionID );
            List<Auction> result = (List<Auction>) query.execute();
            Auction auction = result.size() != 1 ? null : result.get(0); // Retrieves and detaches the Auction
            if(auction == null){
                isOpen = false;
            }else {
                isOpen = auction.isOpen();
            }
            tx.commit();
        } catch (Exception ex) {

            ServerLogger.logger.error("* Exception taking data: " + ex.getMessage());
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

            ServerLogger.logger.error("* Exception taking data: " + ex.getMessage());
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

            ServerLogger.logger.error("* Exception updating data: " + ex.getMessage());
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
        pmLock.unlock();
    }

    public ArrayList<Auction> getAuctionByUser(User user) {
        pmLock.lock();

        Transaction tx = pm.currentTransaction();
        ArrayList<Auction> auctions = null;
        try {
            tx.begin();
            Query<Auction> query = pm.newQuery(Auction.class);
            query.setFilter("owner == '" + user.getUsername() + "'");
            ArrayList<Auction> auctionsTest = (ArrayList<Auction>) query.execute();
            for (Auction auction : auctionsTest) {
                if (auction.isOpen()) {
                    auctions.add(auction);
                }
            }
            tx.commit();
        } catch (Exception ex) {

            ServerLogger.logger.error("* Exception taking data: " + ex.getMessage());
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
        pmLock.unlock();
        return auctions;
    }

}
