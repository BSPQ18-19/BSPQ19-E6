package com.spq.group6.server.services;

import com.spq.group6.server.dao.BiddingDAO;
import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;
import com.spq.group6.server.exceptions.UserException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AccountServiceTest {
    private BiddingDAO accountDao = new BiddingDAO();
    private AccountService service = new AccountService();
    private User testUser;

    @Before
    public void setUp(){
        String username = "test_user", pass = "test_pass", country = "uk";
        testUser = new User(username, pass, country);
    }

    @Test
    public void testSignIn()
    {
        String username = testUser.getUsername(), pass = testUser.getPassword(), country = testUser.getCountry();
        try {
            testUser = service.signIn(username, pass, country, null);
        } catch (UserException e) {
            fail();
        }
        assertEquals(username, testUser.getUsername());
        assertEquals(pass, testUser.getPassword());
        assertEquals(country, testUser.getCountry());

        try {
            testUser = service.signIn(testUser.getUsername(), testUser.getPassword(), testUser.getCountry(), null);
        } catch (UserException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testLogIn() throws UserException {
        // Setup
        accountDao.createUser(testUser);
        service.init_user(testUser);
        // Test
        try {
            testUser = service.logIn(testUser.getUsername(), testUser.getPassword(), null);
        } catch (UserException e) {
            fail();
        }
        try {
            testUser = service.logIn("wrong_user", "wrong_pass", null);
        } catch (UserException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testCreateProduct()
    {
        // Setup
        User persistedUser;
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
    }

    @Test
    public void testUpdateProduct()
    {
        // Setup
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
    }

    @Test
    public void testDeleteProduct(){
        // Setup
        accountDao.createUser(testUser);
        service.init_user(testUser);
        String prodName = "test_prod", prodDescription = "desc_prod";
        testUser = service.createProduct(testUser, prodName, prodDescription);
        Product testProduct = testUser.getOwnedProducts().get(testUser.getOwnedProducts().size() -1);
        // Test
        service.deleteProduct(testUser, testProduct);
        User persistedUser = accountDao.getUserByUsername(testUser.getUsername());
        assertEquals(0, persistedUser.getOwnedProducts().size());
    }

    @After
    public void tearDown(){
        accountDao.deleteUser(testUser);
    }
}
