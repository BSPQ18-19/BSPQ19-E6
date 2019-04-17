package com.spq.group6.server.services;

import com.spq.group6.server.dao.AccountDAO;
import com.spq.group6.server.dao.IAccountDAO;
import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;
import com.spq.group6.server.exceptions.UserException;
import com.spq.group6.server.utils.logger.ServerLogger;

public class AccountService implements IAccountService {
    IAccountDAO accountDAO;

    public AccountService() {
        accountDAO = new AccountDAO();
    }

    public User logIn(String username, String password) throws UserException {
        User user = accountDAO.getUserByUsername(username);
        if (user == null) throw new UserException("User does not exist");
        if (!password.equals(user.getPassword())) throw new UserException("Invalid username or password");
        return user;
    }

    public User signIn(String username, String password, String country) throws UserException {
        User user = new User(username, password, country);
        checkDuplicatedUser(user);
        accountDAO.createUser(user);
        if (user != null) ServerLogger.logger.debug("User '" + username + "' has signed in.");
        return user;
    }

    public User updateUser(User user) throws UserException {
        checkDuplicatedUser(user);
        accountDAO.updateUser(user);
        return user;
    }

    public User createProduct(User user, String name, String description) {
        Product newProduct = new Product(name, description);
        user.getOwnedProducts().add(newProduct);
        accountDAO.updateUser(user);
        return user;
    }

    public Product updateProduct(Product product, String name, String description) {
        product.setName(name);
        product.setDescription(description);
        accountDAO.updateProduct(product);
        return product;
    }

    public User deleteProduct(User user, Product product) {
        user.getOwnedProducts().remove(product);
        accountDAO.updateUser(user);
        accountDAO.deleteProduct(product);
        return user;
    }

    private void checkDuplicatedUser(User user) throws UserException {
        String username = user.getUsername();
        if (accountDAO.getUserByUsername(username) != null) throw new UserException("Username already in use.");
    }
}
