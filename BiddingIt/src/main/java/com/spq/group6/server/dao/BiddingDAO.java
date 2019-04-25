package com.spq.group6.server.dao;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import com.spq.group6.server.data.*;
import com.spq.group6.server.utils.logger.ServerLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;


public class BiddingDAO implements IBiddingDAO {
    private PersistenceManager pm;
    private Lock pmLock;

    public BiddingDAO() {

        pm = JdoManager.getPersistanceManager();
        pmLock = JdoManager.pmLock;
    }

    // Account DAO
    public void createUser(User user) {
        updateObject(user);
    }

    public User getUserByUsername(String username) {
        pmLock.lock();
        Transaction tx = pm.currentTransaction();

        User user = null;
        try {
            pm.setDetachAllOnCommit(true);
            tx.begin();

            Query<User> query = pm.newQuery(User.class);
            query.setFilter("username == '" + username + "'");
            List<User> result = (List<User>) query.execute();
            user = result.size() != 1 ? null : result.get(0); // Retrieves and detaches the User

            tx.commit();
        } catch (Exception ex) {

            ServerLogger.logger.error("* Exception taking data from db: " + ex.getMessage());

        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
        pmLock.unlock();
        return user;
    }

    public void updateUser(User user) {
        updateObject(user);
    }

    public void updateProduct(Product product) {
        updateObject(product);
    }

    public void deleteProduct(Product product) {
        deleteObject(product);
    }

    // Admin DAO
    public Administrator getAdministratorByUsername(String username) {
        pmLock.lock();
        Transaction tx = pm.currentTransaction();

        Administrator admin = null;
        try {
            pm.setDetachAllOnCommit(true);
            tx.begin();

            Query<Administrator> query = pm.newQuery(Administrator.class);
            query.setFilter("username == '" + username + "'");
            List<Administrator> result = (List<Administrator>) query.execute();
            admin = (result.size() != 1) ? null : result.get(0); // Retrieves and detaches the Admin

            tx.commit();
        } catch (Exception ex) {

            ServerLogger.logger.error("* Exception taking data from db: " + ex.getMessage());

        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
        pmLock.unlock();
        return admin;
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

    public void deleteUser(User user) {
        pmLock.lock();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            Query<Auction> query = pm.newQuery(Auction.class);
            query.setFilter("owner.username == '" + user.getUsername() + "'");
            List<Auction> auctionsTest = (List<Auction>) query.execute();
            tx.commit();
            for (Auction auction : auctionsTest)
                deleteAuction(auction); //deletes each auction owned by the user being deleted
            tx.begin();
            for (Product product : user.getOwnedProducts()) pm.deletePersistent(product);
            user.getOwnedProducts().clear();
            pm.deletePersistent(user); // Deletes user in the Database
            tx.commit();
        } catch (Exception ex) {
            ServerLogger.logger.error("* Exception deleting data into db: " + ex.getMessage());

        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
        pmLock.unlock();
    }

    // Auction DAO
    public void persistAuction(Auction auction) {
        updateObject(auction);
    }

    public void deleteBid(Bid bid) {
        deleteObject(bid);
    }

    public ArrayList<Auction> getAuctionByCountry(String country) {
        pmLock.lock();

        Transaction tx = pm.currentTransaction();
        ArrayList<Auction> auctions = new ArrayList<>();
        try {
            tx.begin();
            Query<User> query = pm.newQuery(User.class);
            query.setFilter("country == '" + country + "'");
            List<User> users = (List<User>) query.execute();// Retrieves and detaches the ArrayList of users

            for (int i = 0; i < users.size(); i++) {
                Query<Auction> query2 = pm.newQuery(Auction.class);
                query2.setFilter("owner.username == '" + users.get(i).getUsername() + "'");
                Auction isNull = (Auction) query2.execute();
                if (isNull != null && isNull.isOpen()) {
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

    public ArrayList<Auction> getAuctionByProductName(String name) {
        pmLock.lock();

        Transaction tx = pm.currentTransaction();
        ArrayList<Auction> auctions = new ArrayList<>();
        try {
            tx.begin();

            Query<Product> query = pm.newQuery(Product.class);
            query.setFilter("name == '" + name + "'");
            List<Product> products = (List<Product>) query.execute();

            for (int i = 0; i < products.size(); i++) {
                Query<Auction> query2 = pm.newQuery(Auction.class);
                query2.setFilter("product == '" + products.get(i) + "'");
                Auction isNull = (Auction) query2.execute();
                if (isNull != null && isNull.isOpen()) {
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

    public Auction getAuctionByID(long auctionID) {

        pmLock.lock();
        Transaction tx = pm.currentTransaction();

        Auction auction = null;
        try {
            pm.setDetachAllOnCommit(true);
            tx.begin();

            Query<Auction> query = pm.newQuery(Auction.class);
            query.setFilter("auctionID == '" + auctionID + "'");
            List<Auction> result = (List<Auction>) query.execute();
            auction = (result.size() != 1) ? null : result.get(0); // Retrieves and detaches the Auction

            tx.commit();
        } catch (Exception ex) {

            ServerLogger.logger.error("* Exception taking data from db: " + ex.getMessage());

        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
        pmLock.unlock();
        return auction;
    }

    public ArrayList<Auction> getAuctionByUser(User user) {
        pmLock.lock();

        Transaction tx = pm.currentTransaction();
        ArrayList<Auction> auctions = new ArrayList<>();
        try {
            tx.begin();
            Query<Auction> query = pm.newQuery(Auction.class);
            query.setFilter("owner.username == '" + user.getUsername() + "'");
            List<Auction> auctionsTest = (List<Auction>) query.execute();
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

    // Utils
    public void updateObject(Object obj) {
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

    public void deleteObject(Object obj) {
        pmLock.lock();

        Transaction tx = pm.currentTransaction();

        try {
            tx.begin();
            pm.deletePersistent(obj);
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
}
