package com.spq.group6.client;

import java.rmi.RemoteException;

import com.spq.group6.client.controller.ClientController;
import com.spq.group6.client.gui.ClientWindow;
import com.spq.group6.client.remote.ServiceLocator;
import com.spq.group6.client.utils.logger.ClientLogger;


public class Client 
{
    public static void main(String[] args) {
		if (args.length != 3) {
			ClientLogger.logger.error("Invalid number of arguments.");
			return;
		}
		ClientController controller = ClientController.getClientController();
		ServiceLocator serviceLocator = ServiceLocator.getServiceLocator();
		serviceLocator.setService(args[0], args[1], args[2]);

		ClientWindow.getClientWindow().setVisible(true);
		ClientLogger.logger.info("Client running correctly.");
	}
}
