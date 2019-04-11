package com.spq.group6.server.dao;

import com.spq.group6.server.data.*;

import java.util.ArrayList;

public interface IAdminDAO {
    public Administrator getAdministratorByUsername(String username);

    public void deleteUser(User user);

    public void deleteAuction(Auction auction);
}
