package com.spq.group6.server.services;

import com.spq.group6.server.data.Administrator;
import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.User;
import com.spq.group6.server.exceptions.AdministratorException;


public class AdminService implements IAdminService {
    private IAdminDAO adminDAO;

    public AdminService() {
        // Initializze Admin DAO
        adminDAO = null;
    }

    public Administrator logIn(String username, String password) throws AdministratorException {
        Administrator admin = adminDAO.getAdministratorByUsername(username);
        if (admin == null) throw new AdministratorException("Administrator does not exist");
        if (!password.equals(admin.getPassword())) throw new AdministratorException("Invalid username or password");
        return admin;
    }

    public void deleteAuction(Auction auction) {
        adminDAO.deleteAuction(auction);
    }

    public void deleteUser(User user) {
        adminDAO.deleteUser(user);
    }
}
