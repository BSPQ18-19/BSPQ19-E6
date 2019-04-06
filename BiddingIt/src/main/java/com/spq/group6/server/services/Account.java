package com.spq.group6.server.services;

import com.spq.group6.server.dao.AccountDAO;
import com.spq.group6.server.dao.IAccountDAO;
import com.spq.group6.server.data.User;
import com.spq.group6.server.exceptions.UserException;

public class Account implements IAccount {
    IAccountDAO accountDAO;

    public Account() {
        accountDAO = new AccountDAO();
    }

    public User logIn(String username, String password) throws UserException{
        User user = accountDAO.getUserByUsername(username);
        if(user == null) throw new UserException("User does not exist");
        if (! password.equals(user.getPassword())) throw new UserException("Invalid username or password");
        System.out.println("User " + username + " has logged in.");
        return user;
    }

    public User signIn(String username, String password, String country) throws UserException {
        User user = new User(username, password, country);
        checkDuplicatedUser(user);
        accountDAO.createUser(user);
        if (user != null) System.out.println("User '" + username + "' has signed in.");
        return user;
    }

    public void updateUser(User user) throws UserException {
        checkDuplicatedUser(user);
        accountDAO.updateUser(user);
        System.out.println("User '" + user.getUsername() + "' updated.");

    }

    private void checkDuplicatedUser(User user) throws UserException{
        String username = user.getUsername();
        if (accountDAO.getUserByUsername(username) != null) throw new UserException("Username already in use.");
    }
}
