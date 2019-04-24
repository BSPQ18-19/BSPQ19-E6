package com.spq.group6.server.services;

import com.spq.group6.server.dao.AccountDAO;
import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;
import com.spq.group6.server.exceptions.UserException;
import org.junit.Test;

import static org.junit.Assert.*;

public class AccountServiceTest {
    private AccountDAO accountDao = new AccountDAO();
    private AccountService service = new AccountService();

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
            fail();
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
        accountDao.deleteUser(testUser);
    }

    @Test
    public void testLogIn()
    {
        // Setup
        String username = "test_user", pass = "test_pass", country = "uk";
        User testUser = new User(username, pass, country);
        accountDao.createUser(testUser);
        // Test

        try {
            testUser = service.logIn(testUser.getUsername(), testUser.getPassword());
        } catch (UserException e) {
            fail();
        }
        try {
            testUser = service.logIn("wrong_user", "wrong_pass");
        } catch (UserException e) {
            assertTrue(true);
        }
        // Clean
        accountDao.deleteUser(testUser);
    }

    @Test
    public void testCreateProduct()
    {
        // Setup
        User persistedUser;
        User testUser = new User("test_user", "test_pass", "uk");
        accountDao.createUser(testUser);
        String prodName = "test_prod", prodDescription = "desc_prod";
        testUser = service.createProduct(testUser, prodName, prodDescription);
        Product testProduct = testUser.getOwnedProducts().get(testUser.getOwnedProducts().size() -1);
        // Test
        assertEquals(prodName, testProduct.getName());
        assertEquals(prodDescription, testProduct.getDescription());
        persistedUser = accountDao.getUserByUsername(testUser.getUsername());
        assertEquals(1, persistedUser.getOwnedProducts().size());
        assertEquals(testProduct, persistedUser.getOwnedProducts().get(0));
        // Clean
        accountDao.deleteUser(testUser);
    }

    @Test
    public void testUpdateProduct()
    {
        // Setup
        User testUser = new User("test_user", "test_pass", "uk");
        accountDao.createUser(testUser);
        String oldName = "test_prod", newName = "test_super_prod", prodDescription = "desc_prod";
        testUser = service.createProduct(testUser, newName, prodDescription);
        Product testProduct = testUser.getOwnedProducts().get(testUser.getOwnedProducts().size() -1);
        service.createProduct(testUser, oldName, prodDescription);
        // Test
        service.updateProduct(testProduct, newName, prodDescription);
        assertEquals(newName, testProduct.getName());
        User persistedUser = accountDao.getUserByUsername(testUser.getUsername());
        assertEquals(newName, persistedUser.getOwnedProducts().get(0).getName());
        // Clean
        accountDao.deleteUser(testUser);
    }

    @Test
    public void testDeleteProduct()
    {
        // Setup
        User testUser = new User("test_user", "test_pass", "uk");
        accountDao.createUser(testUser);
        String prodName = "test_prod", prodDescription = "desc_prod";
        testUser = service.createProduct(testUser, prodName, prodDescription);
        Product testProduct = testUser.getOwnedProducts().get(testUser.getOwnedProducts().size() -1);
        // Test
        service.deleteProduct(testUser, testProduct);
        User persistedUser = accountDao.getUserByUsername(testUser.getUsername());
        assertEquals(0, persistedUser.getOwnedProducts().size());
        assertEquals(0, testUser.getOwnedProducts().size());
        // Clean
        accountDao.deleteUser(testUser);
    }
}
