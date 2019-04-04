package com.spq.group6.server.services;

import com.spq.group6.server.data.User;
import com.spq.group6.server.exceptions.UserException;

public interface IAccount {
    public User logIn(String username, String password) throws UserException;
    public User signIn(String username, String password, String country) throws UserException;
    public void updateUser(User user) throws UserException;
}
