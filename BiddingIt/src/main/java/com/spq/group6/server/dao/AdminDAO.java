package com.spq.group6.server.dao;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import com.spq.group6.server.data.Administrator;
import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;

import java.util.List;
import java.util.concurrent.locks.Lock;

public class AdminDAO {

    private static AccountDAO accountDAO = null;
    private PersistenceManager pm;
    private Lock pmLock;

    public AdminDAO(){

        pm = JdoManager.getPersistanceManager();
        pmLock = JdoManager.pmLock;
    }

    public Administrator getAdministratorByUsername(String username){
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

            System.err.println("* Exception taking data from db: " + ex.getMessage());

        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
        pmLock.unlock();
        return admin;
    }

    public void deleteUser(User user){
        pmLock.lock();

        Transaction tx = pm.currentTransaction();

        try {
            tx.begin();
            for (Product product : user.getOwnedProducts()) pm.deletePersistent(product);
            pm.deletePersistent(user); // Saves user in the Database
            tx.commit();
        } catch (Exception ex) {

            System.err.println("* Exception inserting data into db: " + ex.getMessage());

        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
        pmLock.unlock();
    }

    public void deleteAuction(Auction auction){ //debería poderse hacer un método genérico para todos los delete
        pmLock.lock();

        Transaction tx = pm.currentTransaction();

        try {
            tx.begin();
            pm.deletePersistent(auction);
            tx.commit();
        } catch (Exception ex) {

            System.err.println("* Exception deleting data: " + ex.getMessage());
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
        pmLock.unlock();
    }

}
