package com.spq.group6.server.dao;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.Transaction;

import com.spq.group6.server.data.User;


public class AccountDAO implements IAccountDAO {
    @Override
    public User createUser(User user) {

		PersistenceManagerFactory persistentManagerFactory = JDOHelper
				.getPersistenceManagerFactory("datanucleus.properties");

		PersistenceManager persistentManager = persistentManagerFactory.getPersistenceManager();
		Transaction transaction = persistentManager.currentTransaction();

		try {
			transaction.begin();
			Query<User> userQuery = persistentManager.newQuery("SELECT FROM " + User.class.getName());//listado de usuarios

			int found = 0;
			for (User u : userQuery.executeList()) {
				if(u.getUsername().equals(user.getUsername())) {//comparacion del nombre de usuario del usuario con el nombre de usuario dado
					found++;
				}
			}

			if(found > 0){
				System.err.println("* User already exist.");
			}else {
				persistentManager.makePersistent(user);
				System.out.println("- Inserted into db: " + user.toString());
			}

			transaction.commit();
		} catch (Exception ex) {

			System.err.println("* Exception inserting data into db: " + ex.getMessage());

		} finally {
			if (transaction.isActive()) {
				transaction.rollback();
			}

			persistentManager.close();
		}
		return user;
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

		if(user == null){
			System.err.println("* No user found with this username.");
		}
        return user;
    }

    @Override
    public User updateUser(User user) {
    	
    	PersistenceManagerFactory persistentManagerFactory = JDOHelper
				.getPersistenceManagerFactory("datanucleus.properties");
		
		PersistenceManager persistentManager = persistentManagerFactory.getPersistenceManager();
		Transaction transaction = persistentManager.currentTransaction();
		
		try {
			transaction.begin();

			Query<User> userQuery = persistentManager.newQuery("SELECT FROM " + User.class.getName());

			int found = 0;
			for (User u : userQuery.executeList()) {
				if(u.getUsername().equals(user.getUsername())) {//comparacion del nombre de usuario del usuario con el nombre de usuario dado
					u.setCountry(user.getCountry());
					u.setMoney(user.getMoney());
					u.setPassword(user.getPassword());
					u.setOwnedProducts(user.getOwnedProducts());
					persistentManager.makePersistent(u);
					found++;
					System.out.println("- Updated into db: " + user.toString());
				}
			}

			if(found == 0){
				System.err.println("* No user found with this username.");
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
    	return user;
    }
}
