package com.spq.group6.server.dao;

import com.spq.group6.server.data.User;

public interface IAccountDAO {
    public void createUser(User user);
    public void deleteUser(User user);
    public User getUserByUsername(String username);
    public void updateUser(User user);
}
