package com.spq.group6.server.dao;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.Transaction;

import com.spq.group6.server.data.User;


public class AccountDAO implements IAccountDAO {
    @Override
    public void createUser(User user) {

		PersistenceManagerFactory persistentManagerFactory = JDOHelper
				.getPersistenceManagerFactory("datanucleus.properties");

		PersistenceManager persistentManager = persistentManagerFactory.getPersistenceManager();
		Transaction transaction = persistentManager.currentTransaction();

		try {
			transaction.begin();
			//
			//Query<User> userQuery = persistentManager.newQuery("SELECT FROM " + User.class.getName());

			//User newU = new User(user.getUsername(), user.getPassword(), user.getCountry(), user.getMoney(), user.getOwnedProducts);
			//persistentManager.makePersistent(newU);
			persistentManager.makePersistent(user);
			System.out.println("- Inserted into db: " + user.toString());

			transaction.commit();
		} catch (Exception ex) {

			System.err.println("* Exception inserting data into db: " + ex.getMessage());

		} finally {
			if (transaction.isActive()) {
				transaction.rollback();
			}

			persistentManager.close();
		}
    }

    public User getUserByUsername(String username) {
    	
		PersistenceManagerFactory persistentManagerFactory = JDOHelper
				.getPersistenceManagerFactory("datanucleus.properties");
		
		PersistenceManager persistentManager = persistentManagerFactory.getPersistenceManager();
		Transaction transaction = persistentManager.currentTransaction();
		
		User user = null;
		try {
			transaction.begin();
			Query<User> userQuery = persistentManager.newQuery("SELECT FROM " + User.class.getName());//listado de usuarios

			for (User u : userQuery.executeList()) {
				if(u.getUsername().equals(username)) {//comparacion del nombre de usuario del usuario con el nombre de usuario dado
					user = u;
					System.out.println("- Selected from db: " + u.toString());
				}
			}

			transaction.commit();
		} catch (Exception ex) {
			
			System.err.println("* Exception taking data from db: " + ex.getMessage());
			
		} finally {
			if (transaction.isActive()) {
				transaction.rollback();
			}

			persistentManager.close();
		}
    	
        return user;
    }

    @Override
    public void updateUser(User user) {
    	
    	PersistenceManagerFactory persistentManagerFactory = JDOHelper
				.getPersistenceManagerFactory("datanucleus.properties");
		
		PersistenceManager persistentManager = persistentManagerFactory.getPersistenceManager();
		Transaction transaction = persistentManager.currentTransaction();
		
		try {
			transaction.begin();

			Query<User> userQuery = persistentManager.newQuery("SELECT FROM " + User.class.getName());

			for (User u : userQuery.executeList()) {
				if(user.getUserID() == u.getUserID()) {//comparacion del nombre de usuario del usuario con el nombre de usuario dado
					u.setCountry(user.getCountry());
					u.setMoney(user.getMoney());
					u.setPassword(user.getPassword());
					//u.setUsername(user.getUsername());
					u.setOwnedProducts(user.getOwnedProducts());
					persistentManager.makePersistent(u);
					System.out.println("- Updated into db: " + user.toString());
				}
			}
			
			transaction.commit();
		} catch (Exception ex) {
			
			System.err.println("* Exception updating data into db: " + ex.getMessage());
			
		} finally {
			if (transaction.isActive()) {
				transaction.rollback();
			}

			persistentManager.close();
		}
    	
    }
}
