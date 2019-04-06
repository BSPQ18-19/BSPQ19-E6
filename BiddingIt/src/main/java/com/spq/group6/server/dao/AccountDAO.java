package com.spq.group6.server.dao;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.Transaction;

import com.spq.group6.server.data.User;

import java.util.List;


public class AccountDAO implements IAccountDAO {
    private static PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory("datanucleus.properties");
    private PersistenceManager pm;

    public AccountDAO() {
        pm = pmf.getPersistenceManager();
        pm.setDetachAllOnCommit(true);
        pm.getFetchPlan().setMaxFetchDepth(3);
    }

    public void createUser(User user) {

        Transaction tx = pm.currentTransaction();

        try {
            pm.setDetachAllOnCommit(true);
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
    }

    public void deleteUser(User user) {

        Transaction tx = pm.currentTransaction();

        try {
            pm.setDetachAllOnCommit(true);
            tx.begin();
            pm.deletePersistent(user); // Saves user in the Database
            tx.commit();
        } catch (Exception ex) {

            System.err.println("* Exception inserting data into db: " + ex.getMessage());

        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    public User getUserByUsername(String username) {
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
        if (user == null) {
            System.err.println("* No user found with this username.");
        }
        return user;
    }

    public void updateUser(User user) {

        Transaction tx = pm.currentTransaction();

        try {
            pm.setDetachAllOnCommit(true);
            tx.begin();

            pm.makePersistent(user);
            tx.commit();
        } catch (Exception ex) {

            System.err.println("* Exception updating data into db: " + ex.getMessage());

        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }

        }
    }


}
