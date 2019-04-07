package com.spq.group6.server.services;

import com.spq.group6.server.dao.AccountDAO;
import com.spq.group6.server.data.User;
import com.spq.group6.server.exceptions.UserException;
import org.junit.Test;

import static org.junit.Assert.*;

public class AccountTest {
    private AccountDAO dao = new AccountDAO();
    private Account service = new Account();

    @Test
    public void testSignIn()
    {
        // Setup
        String username = "test_user", pass = "test_pass", country = "uk";
        User testUser = null;
        // Test
        try {
            testUser = service.signIn(username, pass, country);
        } catch (UserException e) {
            assertTrue(false);
        }
        assertEquals(username, testUser.getUsername());
        assertEquals(pass, testUser.getPassword());
        assertEquals(country, testUser.getCountry());

        try {
            testUser = service.signIn(testUser.getUsername(), testUser.getPassword(), testUser.getCountry());
        } catch (UserException e) {
            assertTrue(true);
        }
        // Clean
        dao.deleteUser(testUser);
    }
}
