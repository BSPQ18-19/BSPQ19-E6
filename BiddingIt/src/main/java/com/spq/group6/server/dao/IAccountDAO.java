package com.spq.group6.server.dao;

import com.spq.group6.server.data.Administrator;
import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;

public interface IAccountDAO {
    void createUser(User user);
    User getUserByUsername(String username);
    void updateUser(User user);
    void deleteUser(User user);
    void updateProduct(Product product);
    void deleteProduct(Product product);
    Administrator getAdministratorByUsername(String username);

}
