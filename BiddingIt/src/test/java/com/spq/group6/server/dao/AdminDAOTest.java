package com.spq.group6.server.dao;

import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class AdminDAOTest {
    private BiddingDAO biddingDAO = new BiddingDAO();
    private User user;
    private User user2;
    private Product product;
    private List<User> users;
    private List<User> databaseUsers;
    private Auction auction;
    private ArrayList<Auction> auctionList;

    @Before
    public void setUp() {
        user = new User("test_user", "test_pass", "spain");
        user2 = new User("xavier", "test_pass", "uk");
        product = new Product("cd", "Mikel Urdangarin cd");
        users = new ArrayList<User>();
        databaseUsers = new ArrayList<User>();
        users.add(user);
        users.add(user2);

        int seconds = 10;
        Timestamp limit = new Timestamp(System.currentTimeMillis() + seconds * 1000);
        auction = new Auction(user, product, limit, 600, "123");
        auctionList = new ArrayList<>();
        auctionList.add(auction);

    }

    @Test
    public void getAllUsers() {

        assertFalse(users.size() == databaseUsers.size() &&
                users.containsAll(databaseUsers) && databaseUsers.containsAll(users));
        biddingDAO.persistUser(user);
        biddingDAO.persistUser(user2);
        databaseUsers = biddingDAO.getAllUsers();
        assertTrue(users.size() == databaseUsers.size() &&
                users.containsAll(databaseUsers) && databaseUsers.containsAll(users));
        // Clean-up
        biddingDAO.deleteUser(user2);
    }


    @Test
    public void getAllAuctions() {
        assertEquals(0, biddingDAO.getAllAuctions().size());
        biddingDAO.persistAuction(auction);
        ArrayList<Auction> auctions = biddingDAO.getAllAuctions();
        assertTrue(auctionList.size() == auctions.size() &&
                auctionList.containsAll(auctions) && auctions.containsAll(auctionList));
        // Clean-up
        biddingDAO.deleteAuction(auction);
    }

    @After
    public void tearDown() {
        biddingDAO.deleteUser(user);
    }
}
