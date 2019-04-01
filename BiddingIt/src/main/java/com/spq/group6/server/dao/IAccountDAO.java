package com.spq.group6.server.dao;

import com.spq.group6.server.data.User;

public interface IAccountDAO {
    public User createUser(User user);
    public User getUserByUsername(String username);
    public User updateUser(User user);
}
