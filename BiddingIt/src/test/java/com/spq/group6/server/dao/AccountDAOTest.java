package com.spq.group6.server.dao;

import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import java.util.List;

import static org.junit.Assert.*;

public class AccountDAOTest {
    private BiddingDAO biddingDAO = new BiddingDAO();
    private User user;
    private User user2;
    private Product product;
    private List<User> users;
    private List<User> testUsers;

    @Before
    public void setUp(){
        user = new User("test_user", "test_pass", "spain");
        user2 = new User("xavier", "test_pass", "uk");
        product = new Product("cd", "Mikel Urdangarin cd");
        users.add(user);
        users.add(user2);

    }

    @Test
    public void getUserByUsername()
    {
        assertNull(biddingDAO.getUserByUsername(user.getUsername()));
        biddingDAO.createUser(user);
        assertEquals(user, biddingDAO.getUserByUsername(user.getUsername()));
    }

    @Test
    public void updateUser(){
        // Set-up
        String oldCountry = user.getCountry(), newCountry = "uk";
        biddingDAO.createUser(user);
        // Test
        user.setCountry(newCountry);
        biddingDAO.updateUser(user);
        User persistedUser = biddingDAO.getUserByUsername(user.getUsername());
        assertNotEquals(oldCountry, persistedUser.getCountry());
        assertEquals(newCountry, persistedUser.getCountry());
    }

    @Test
    public void createProductUpdatingUser(){
        // Setup
        biddingDAO.createUser(user);
        // Test
        user.getOwnedProducts().add(product);
        biddingDAO.updateUser(user);
        User persistedUser = biddingDAO.getUserByUsername(user.getUsername());
        assertEquals(1, persistedUser.getOwnedProducts().size());
        assertEquals(product, persistedUser.getOwnedProducts().get(0));
    }

    @Test
    public void updateProductUpdatingUser() {
        // Setup
        String prodOldName = product.getName(), prodNewName = "dvd";
        user.getOwnedProducts().add(product);
        biddingDAO.createUser(user);
        // Test
        product.setName(prodNewName);
        biddingDAO.updateUser(user);
        User persistedUser = biddingDAO.getUserByUsername(user.getUsername());
        assertEquals(prodNewName, persistedUser.getOwnedProducts().get(0).getName());
    }

    @Test
    public void updateExchange(){
        // Setup
        user.getOwnedProducts().add(product);
        biddingDAO.createUser(user);
        biddingDAO.createUser(user2);
        // Test
        User persistedUser;
        user.getOwnedProducts().clear();
        user2.getOwnedProducts().add(product);
        biddingDAO.updateUser(user2);
        biddingDAO.updateUser(user);
        persistedUser = biddingDAO.getUserByUsername(user.getUsername());
        assertEquals(0, persistedUser.getOwnedProducts().size());

        persistedUser = biddingDAO.getUserByUsername(user2.getUsername());
        assertEquals(1, persistedUser.getOwnedProducts().size());
        assertEquals(product, persistedUser.getOwnedProducts().get(0));
        // Clean-up
        biddingDAO.deleteUser(user2);
    }

    @Test
    public void getAllUsers(){

        assertFalse(users.size() == testUsers.size() &&
                users.containsAll(testUsers) && testUsers.containsAll(users));
        biddingDAO.createUser(user);
        biddingDAO.createUser(user2);
        testUsers = biddingDAO.getAllUsers();
        assertTrue(users.size() == testUsers.size() &&
                users.containsAll(testUsers) && testUsers.containsAll(users));
    }

    @After
    public void tearDown() {
        biddingDAO.deleteUser(user);
    }
}