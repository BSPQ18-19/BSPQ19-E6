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
        return user;
    }

    public User signIn(String username, String password, String country) throws UserException {
        if (accountDAO.getUserByUsername(username) != null) throw new UserException("Username already in use.");
        User user = new User(username, password, country);
        accountDAO.createUser(user);
        return user;
    }

    public User updateUser(User user){
        return accountDAO.updateUser(user);
    }
}
