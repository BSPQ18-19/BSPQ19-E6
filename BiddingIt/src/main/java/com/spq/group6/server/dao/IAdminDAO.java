package com.spq.group6.server.dao;

import com.spq.group6.server.data.*;

import java.util.ArrayList;

public interface IAdminDAO {
    Administrator getAdministratorByUsername(String username);

    void deleteUser(User user);

    void deleteAuction(Auction auction);
}
