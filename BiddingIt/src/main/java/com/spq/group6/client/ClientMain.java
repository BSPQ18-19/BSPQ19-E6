package com.spq.group6.client;

import com.spq.group6.client.controller.ClientController;
import com.spq.group6.client.gui.ClientWindow;
import com.spq.group6.client.remote.ServiceLocator;
import com.spq.group6.client.utils.logger.ClientLogger;

import javax.swing.*;


public class ClientMain {
    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        if (args.length != 3) {
            ClientLogger.logger.error("Invalid number of arguments.");
            return;
        }
        ClientController controller = ClientController.getClientController();
        ServiceLocator serviceLocator = ServiceLocator.getServiceLocator();
        serviceLocator.setService(args[0], args[1], args[2]);
        UIManager.setLookAndFeel("com.jgoodies.looks.windows.WindowsLookAndFeel");
        ClientWindow.getClientWindow().setVisible(true);
        controller.setClientRemoteObserver();
        ClientLogger.logger.info("ClientMain running correctly.");
    }
}
