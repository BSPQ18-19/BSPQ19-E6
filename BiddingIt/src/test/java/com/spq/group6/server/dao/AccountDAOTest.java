package com.spq.group6.server.dao;

import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;
import org.junit.Test;

import static org.junit.Assert.*;

public class AccountDAOTest {
    private AccountDAO dao = new AccountDAO();

    @Test
    public void getUserByUsername()
    {
        // Setup
        User testUser = new User("test_user", "test_pass", "uk");
        // Test
        assertNull(dao.getUserByUsername(testUser.getUsername()));
        dao.createUser(testUser);
        assertEquals(testUser, dao.getUserByUsername(testUser.getUsername()));
        // Clean
        dao.deleteUser(testUser);
    }

    @Test
    public void updateUser(){
        // Setup
        String country1 = "spain", country2 = "uk";
        String username1 = "carlos", username2 = "charles";
        User testUser = new User(username1, "test_pass", country1);
        dao.createUser(testUser);
        // Test
        testUser.setUsername(username2);
        testUser.setCountry(country2);
        dao.updateUser(testUser);
        User persistedUser = dao.getUserByUsername(testUser.getUsername());
        assertNotEquals(username1, persistedUser.getUsername());
        assertNotEquals(country1, persistedUser.getCountry());
        // Clean
        dao.deleteUser(testUser);
    }

    @Test
    public void createProductUpdatingUser(){
        // Setup
        User testUser = new User("charles", "test_pass", "uk");
        Product testProduct = new Product(testUser, "cd", "Mikel Urdangarin cd");
        dao.createUser(testUser);
        // Test
        testUser.getOwnedProducts().add(testProduct);
        dao.updateUser(testUser);
        User persistedUser = dao.getUserByUsername(testUser.getUsername());
        assertEquals(1, persistedUser.getOwnedProducts().size());
        assertEquals(testProduct, persistedUser.getOwnedProducts().get(0));
        // Clean
        dao.deleteUser(testUser);
    }
    @Test
    public void deleteProductUpdatingUser(){
        // Setup
        User testUser = new User("charles", "test_pass", "uk");
        Product testProduct = new Product(testUser, "cd", "Mikel Urdangarin cd");
        testUser.getOwnedProducts().add(testProduct);
        dao.createUser(testUser);
        // Test
        testUser.getOwnedProducts().clear();
        dao.updateUser(testUser);
        User persistedUser = dao.getUserByUsername(testUser.getUsername());
        assertEquals(0, persistedUser.getOwnedProducts().size());
        // Clean
        dao.deleteUser(testUser);
    }

    
}
