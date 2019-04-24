package com.spq.group6.server.dao;

import com.spq.group6.server.data.Administrator;
import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;
import org.junit.Test;

import static org.junit.Assert.*;

public class AccountDAOTest {
    private AccountDAO accountDao = new AccountDAO();

    @Test
    public void getUserByUsername()
    {
        // Setup
        User testUser = new User("test_user", "test_pass", "uk");
        // Test
        assertNull(accountDao.getUserByUsername(testUser.getUsername()));
        accountDao.createUser(testUser);
        assertEquals(testUser, accountDao.getUserByUsername(testUser.getUsername()));
        // Clean
        accountDao.deleteUser(testUser);
    }

    @Test
    public void updateUser(){
        // Setup
        String country1 = "spain", country2 = "uk";
        String username1 = "carlos";
        User testUser = new User(username1, "test_pass", country1);
        accountDao.createUser(testUser);
        // Test
        testUser.setCountry(country2);
        accountDao.updateUser(testUser);
        User persistedUser = accountDao.getUserByUsername(testUser.getUsername());
        assertNotEquals(country1, persistedUser.getCountry());
        // Clean
        accountDao.deleteUser(testUser);
    }

    @Test
    public void createProductUpdatingUser(){
        // Setup
        User testUser = new User("charles", "test_pass", "uk");
        Product testProduct = new Product("cd", "Mikel Urdangarin cd");
        accountDao.createUser(testUser);
        // Test
        testUser.getOwnedProducts().add(testProduct);
        //accountDao.updateProduct(testProduct);
        accountDao.updateUser(testUser);
        User persistedUser = accountDao.getUserByUsername(testUser.getUsername());
        assertEquals(1, persistedUser.getOwnedProducts().size());
        assertEquals(testProduct, persistedUser.getOwnedProducts().get(0));
        // Clean
        accountDao.deleteUser(testUser);
    }
    @Test
    public void deleteProductUpdatingUser(){
        // Setup
        User testUser = new User("charles", "test_pass", "uk");
        Product testProduct = new Product("cd", "Mikel Urdangarin cd");
        testUser.getOwnedProducts().add(testProduct);
        accountDao.createUser(testUser);
        // Test
        testUser.getOwnedProducts().clear();
        accountDao.updateUser(testUser);
        User persistedUser = accountDao.getUserByUsername(testUser.getUsername());
        assertEquals(0, persistedUser.getOwnedProducts().size());
        // Clean
        accountDao.deleteProduct(testProduct);
        accountDao.deleteUser(testUser);

    }

    @Test
    public void updateProductUpdatingUser(){
        // Setup
        String prodOldName = "cd", prodNewName = "dvd";
        User testUser = new User("charles", "test_pass", "uk");
        Product testProduct = new Product(prodOldName, "Mikel Urdangarin cd");
        testUser.getOwnedProducts().add(testProduct);
        accountDao.createUser(testUser);
        // Test
        testProduct.setName(prodNewName);
        accountDao.updateUser(testUser);
        User persistedUser = accountDao.getUserByUsername(testUser.getUsername());
        assertEquals(prodNewName, persistedUser.getOwnedProducts().get(0).getName());
        // Clean
        accountDao.deleteUser(testUser);
    }

    @Test
    public void updateExchange(){
        // Setup
        User testUser1 = new User("charles", "test_pass", "uk");
        User testUser2 = new User("xavier", "test_pass", "uk");
        Product testProduct = new Product("cd", "Mikel Urdangarin cd");
        testUser1.getOwnedProducts().add(testProduct);
        accountDao.createUser(testUser1);
        accountDao.createUser(testUser2);
        // Test
        User persistedUser;
        testUser1.getOwnedProducts().clear();
        testUser2.getOwnedProducts().add(testProduct);
        accountDao.updateUser(testUser2);
        accountDao.updateUser(testUser1);
        persistedUser = accountDao.getUserByUsername(testUser1.getUsername());
        assertEquals(0, persistedUser.getOwnedProducts().size());

        persistedUser = accountDao.getUserByUsername(testUser2.getUsername());
        assertEquals(1, persistedUser.getOwnedProducts().size());
        assertEquals(testProduct, persistedUser.getOwnedProducts().get(0));
        // Clean
        accountDao.deleteUser(testUser1);
        accountDao.deleteUser(testUser2);
    }

    @Test
    public void getAdministratorByUsername(){
        // Setup
        Administrator testAdmin = new Administrator("test_user", "test_pass");
        //Administrator with this same parameters shall be created before running the test
        // Test
        assertEquals(testAdmin, accountDao.getAdministratorByUsername(testAdmin.getUsername()));
        // Clean
    }
}
