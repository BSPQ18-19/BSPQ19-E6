package com.spq.group6.server.dao;

import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AdminDAOTest {
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
        users = new ArrayList<User>();
        testUsers = new ArrayList<User>();
        users.add(user);
        users.add(user2);

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
        biddingDAO.deleteUser(user2);
    }
}
