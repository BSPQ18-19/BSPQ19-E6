package com.spq.group6.admin;

import com.spq.group6.admin.controller.AdminController;
import com.spq.group6.admin.gui.AdminWindow;
import com.spq.group6.admin.remote.AdminServiceLocator;
import com.spq.group6.admin.utils.logger.AdminLogger;

import java.rmi.RemoteException;


public class Admin {
    public static void main(String[] args) {
        if (args.length != 3) {
            AdminLogger.logger.error("Invalid number of arguments.");
            return;
        }
        try {
            AdminController controller = new AdminController();
            AdminServiceLocator serviceLocator = AdminServiceLocator.getServiceLocator();
            serviceLocator.setService(args[0], args[1], args[2]);

            AdminWindow.getAdminWindow(controller).setVisible(true);
            AdminLogger.logger.info("Client running correctly");
        } catch (RemoteException e) {
            AdminLogger.logger.error("Error starting admin");

            e.printStackTrace();
        }

    }
}