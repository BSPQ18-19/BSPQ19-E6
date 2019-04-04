package com.spq.group6.server.dao;

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
}
