package com.spq.group6.server.dao;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import javax.rmi.CORBA.Util;

import com.spq.group6.server.data.Administrator;
import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;
import com.spq.group6.server.utils.logger.ServerLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;


public class AccountDAO implements IAccountDAO {
    private PersistenceManager pm;
    private Lock pmLock;
    private UtilsDAO utilsDAO;
    public AccountDAO(){

        pm = JdoManager.getPersistanceManager();
        pmLock = JdoManager.pmLock;
        utilsDAO = new UtilsDAO();
    }

    public void createUser(User user) {
        utilsDAO.updateObject(user);
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
        utilsDAO.updateObject(user);
    }

    public void deleteUser(User user) {
        pmLock.lock();
        Transaction tx = pm.currentTransaction();
        AuctionDAO auctionDAO = new AuctionDAO();
        try {
            tx.begin();
            ArrayList<Auction> auctions = auctionDAO.getAuctionByUser(user);
            for (Auction auction : auctions) auctionDAO.deleteAuction(auction); //deletes each auction owned by the user being deleted
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

    public void updateProduct(Product product) {
        utilsDAO.updateObject(product);
    }

    public void deleteProduct(Product product) {
        pmLock.lock();

        Transaction tx = pm.currentTransaction();

        try {
            tx.begin();
            pm.deletePersistent(product);
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

}
