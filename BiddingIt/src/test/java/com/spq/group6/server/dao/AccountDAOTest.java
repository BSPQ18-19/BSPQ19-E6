package com.spq.group6.server.dao;

import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.*;

public class AccountDAOTest {
    private BiddingDAO biddingDAO = new BiddingDAO();
    private User user;
    private Product product;

    @Before
    public void setUp(){
        user = new User("test_user", "test_pass", "spain");
        product = new Product("cd", "Mikel Urdangarin cd");
    }

    @Test
    public void getUserByUsername() {
        assertNull(biddingDAO.getUserByUsername(user.getUsername()));
        biddingDAO.persistUser(user);
        assertEquals(user, biddingDAO.getUserByUsername(user.getUsername()));
    }

    @Test
    public void updateUser(){
        // Set-up
        String oldCountry = user.getCountry(), newCountry = "uk";
        biddingDAO.persistUser(user);
        // Test
        user.setCountry(newCountry);
        biddingDAO.persistUser(user);
        User persistedUser = biddingDAO.getUserByUsername(user.getUsername());
        assertNotEquals(oldCountry, persistedUser.getCountry());
        assertEquals(newCountry, persistedUser.getCountry());
    }

    @Test
    public void createProductUpdatingUser(){
        // Setup
        biddingDAO.persistUser(user);
        // Test
        user.getOwnedProducts().add(product);
        biddingDAO.persistUser(user);
        User persistedUser = biddingDAO.getUserByUsername(user.getUsername());
        assertEquals(1, persistedUser.getOwnedProducts().size());
        assertEquals(product, persistedUser.getOwnedProducts().get(0));
    }

    @Test
    public void updateProductUpdatingUser() {
        // Setup
        String prodOldName = product.getName(), prodNewName = "dvd";
        user.getOwnedProducts().add(product);
        biddingDAO.persistUser(user);
        // Test
        product.setName(prodNewName);
        biddingDAO.persistUser(user);
        User persistedUser = biddingDAO.getUserByUsername(user.getUsername());
        assertEquals(prodNewName, persistedUser.getOwnedProducts().get(0).getName());
    }

    @Test
    public void updateExchange(){
        // Setup
        User user2 = new User("xavier", "test_pass", "uk");
        user.getOwnedProducts().add(product);
        biddingDAO.persistUser(user);
        biddingDAO.persistUser(user2);
        // Test
        User persistedUser;
        user.getOwnedProducts().clear();
        user2.getOwnedProducts().add(product);
        biddingDAO.persistUser(user2);
        biddingDAO.persistUser(user);
        persistedUser = biddingDAO.getUserByUsername(user.getUsername());
        assertEquals(0, persistedUser.getOwnedProducts().size());

        persistedUser = biddingDAO.getUserByUsername(user2.getUsername());
        assertEquals(1, persistedUser.getOwnedProducts().size());
        assertEquals(product, persistedUser.getOwnedProducts().get(0));
        // Clean-up
        biddingDAO.deleteUser(user2);
    }



    @After
    public void tearDown() {
        biddingDAO.deleteUser(user);
    }
}