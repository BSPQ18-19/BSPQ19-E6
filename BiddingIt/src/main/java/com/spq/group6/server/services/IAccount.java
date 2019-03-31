package com.spq.group6.server.services;

import com.spq.group6.server.data.User;

public interface IAccount {
    User logIn(String username, String password);

    User signIn(String username, String password, String country);
}
