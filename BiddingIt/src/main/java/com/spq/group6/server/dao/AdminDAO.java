package com.spq.group6.server.dao;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import com.spq.group6.server.data.*;
import com.spq.group6.server.utils.logger.ServerLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;


public class AdminDAO implements IAdminDAO{

    private PersistenceManager pm;
    private Lock pmLock;

    public AdminDAO() {

        pm = JdoManager.getPersistanceManager();
        pmLock = JdoManager.pmLock;
    }

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
            admin = (result.size() != 1) ? null : result.get(0); // Retrieves and detaches the User

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

    public void deleteUser(User user) {
        pmLock.lock();
        Transaction tx = pm.currentTransaction();
        AuctionDAO auctionDAO = new AuctionDAO();
        try {
            tx.begin();
            ArrayList<Auction> auctions = auctionDAO.getAuctionByUser(user);
            for (Auction auction : auctions) deleteAuction(auction); //deletes each auction owned by the user being deleted
            for (Product product : user.getOwnedProducts()) pm.deletePersistent(product);
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

}
