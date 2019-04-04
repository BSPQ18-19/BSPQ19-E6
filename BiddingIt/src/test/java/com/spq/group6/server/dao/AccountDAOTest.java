package com.spq.group6.server.dao;

import com.spq.group6.server.data.User;
import org.junit.Test;
import org.junit.After;

import static org.junit.Assert.*;

public class AccountDAOTest {



    @Test
    public void getUserByUsername()
    {
        AccountDAO dao = new AccountDAO();
        User user1 = new User("test_user", "test_pass", "uk");
        assertNull(dao.getUserByUsername(user1.getUsername()));

        user1 = dao.createUser(user1);
        assertNotNull(user1);

        assertEquals(user1, dao.getUserByUsername(user1.getUsername()));
    }
}
