package com.spq.group6.server.dao;

import com.spq.group6.server.data.Administrator;
import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;
import org.junit.Test;

import static org.junit.Assert.*;

public class AccountDAOTest {
    private BiddingDAO biddingDao = new BiddingDAO();

    @Test
    public void getUserByUsername()
    {
        // Setup
        User testUser = new User("test_user", "test_pass", "uk");
        // Test
        assertNull(biddingDao.getUserByUsername(testUser.getUsername()));
        biddingDao.createUser(testUser);
        assertEquals(testUser, biddingDao.getUserByUsername(testUser.getUsername()));
        // Clean
        biddingDao.deleteUser(testUser);
    }

    @Test
    public void updateUser(){
        // Setup
        String country1 = "spain", country2 = "uk";
        String username1 = "carlos";
        User testUser = new User(username1, "test_pass", country1);
        biddingDao.createUser(testUser);
        // Test
        testUser.setCountry(country2);
        biddingDao.updateUser(testUser);
        User persistedUser = biddingDao.getUserByUsername(testUser.getUsername());
        assertNotEquals(country1, persistedUser.getCountry());
        // Clean
        biddingDao.deleteUser(testUser);
    }

    @Test
    public void createProductUpdatingUser(){
        // Setup
        User testUser = new User("charles", "test_pass", "uk");
        Product testProduct = new Product("cd", "Mikel Urdangarin cd");
        biddingDao.createUser(testUser);
        // Test
        testUser.getOwnedProducts().add(testProduct);
        //accountDao.updateProduct(testProduct);
        biddingDao.updateUser(testUser);
        User persistedUser = biddingDao.getUserByUsername(testUser.getUsername());
        assertEquals(1, persistedUser.getOwnedProducts().size());
        assertEquals(testProduct, persistedUser.getOwnedProducts().get(0));
        // Clean
        biddingDao.deleteUser(testUser);
    }
    @Test
    public void deleteProductUpdatingUser(){
        // Setup
        User testUser = new User("charles", "test_pass", "uk");
        Product testProduct = new Product("cd", "Mikel Urdangarin cd");
        testUser.getOwnedProducts().add(testProduct);
        biddingDao.createUser(testUser);
        // Test
        testUser.getOwnedProducts().clear();
        biddingDao.updateUser(testUser);
        User persistedUser = biddingDao.getUserByUsername(testUser.getUsername());
        assertEquals(0, persistedUser.getOwnedProducts().size());
        // Clean
        biddingDao.deleteProduct(testProduct);
        biddingDao.deleteUser(testUser);

    }

    @Test
    public void updateProductUpdatingUser(){
        // Setup
        String prodOldName = "cd", prodNewName = "dvd";
        User testUser = new User("charles", "test_pass", "uk");
        Product testProduct = new Product(prodOldName, "Mikel Urdangarin cd");
        testUser.getOwnedProducts().add(testProduct);
        biddingDao.createUser(testUser);
        // Test
        testProduct.setName(prodNewName);
        biddingDao.updateUser(testUser);
        User persistedUser = biddingDao.getUserByUsername(testUser.getUsername());
        assertEquals(prodNewName, persistedUser.getOwnedProducts().get(0).getName());
        // Clean
        biddingDao.deleteUser(testUser);
    }

    @Test
    public void updateExchange(){
        // Setup
        User testUser1 = new User("charles", "test_pass", "uk");
        User testUser2 = new User("xavier", "test_pass", "uk");
        Product testProduct = new Product("cd", "Mikel Urdangarin cd");
        testUser1.getOwnedProducts().add(testProduct);
        biddingDao.createUser(testUser1);
        biddingDao.createUser(testUser2);
        // Test
        User persistedUser;
        testUser1.getOwnedProducts().clear();
        testUser2.getOwnedProducts().add(testProduct);
        biddingDao.updateUser(testUser2);
        biddingDao.updateUser(testUser1);
        persistedUser = biddingDao.getUserByUsername(testUser1.getUsername());
        assertEquals(0, persistedUser.getOwnedProducts().size());

        persistedUser = biddingDao.getUserByUsername(testUser2.getUsername());
        assertEquals(1, persistedUser.getOwnedProducts().size());
        assertEquals(testProduct, persistedUser.getOwnedProducts().get(0));
        // Clean
        biddingDao.deleteUser(testUser1);
        biddingDao.deleteUser(testUser2);
    }

    @Test
    public void getAdministratorByUsername(){
        // Setup
        Administrator testAdmin = new Administrator("test_user", "test_pass");
        //Administrator with this same parameters shall be created before running the test
        // Test
        assertEquals(testAdmin, biddingDao.getAdministratorByUsername(testAdmin.getUsername()));
        // Clean
    }
}
