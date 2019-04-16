package com.spq.group6.server.services;

import com.spq.group6.server.data.Administrator;
import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.User;
import com.spq.group6.server.exceptions.AdministratorException;

public interface IAdminService {
    public Administrator logIn(String username, String password) throws AdministratorException;

    public void deleteAuction(Auction auction);

    public void deleteUser(User user);
}
