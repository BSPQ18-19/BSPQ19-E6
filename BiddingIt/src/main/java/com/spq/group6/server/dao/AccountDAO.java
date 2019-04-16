package com.spq.group6.server.dao;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;

import java.util.List;
import java.util.concurrent.locks.Lock;


public class AccountDAO implements IAccountDAO {
    private static AccountDAO accountDAO = null;
    private PersistenceManager pm;
    private Lock pmLock;

    public AccountDAO(){

        pm = JdoManager.getPersistanceManager();
        pmLock = JdoManager.pmLock;
    }

    public void createUser(User user) {
        pmLock.lock();

        Transaction tx = pm.currentTransaction();

        try {
            tx.begin();
            pm.makePersistent(user); // Saves user in the Database
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

    public void deleteUser(User user) {
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

            System.err.println("* Exception taking data from db: " + ex.getMessage());

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
        pmLock.lock();

        Transaction tx = pm.currentTransaction();

        try {
            tx.begin();
            pm.deletePersistent(product);
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


    private void updateObject(Object obj) {
        pmLock.lock();

        Transaction tx = pm.currentTransaction();

        try {
            tx.begin();

            pm.makePersistent(obj);
            tx.commit();
        } catch (Exception ex) {

            System.err.println("* Exception updating data into db: " + ex.getMessage());

        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
        pmLock.unlock();
    }
}
