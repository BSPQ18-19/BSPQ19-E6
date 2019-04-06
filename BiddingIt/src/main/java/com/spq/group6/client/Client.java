package com.spq.group6.client;

import java.rmi.RemoteException;

import com.spq.group6.client.controller.ClientController;
import com.spq.group6.client.gui.ClientWindow;
import com.spq.group6.client.remote.ServiceLocator;


public class Client 
{
    public static void main(String[] args) {
		if (args.length != 3) {
			System.err.println("Invalid number of arguments.");
			return;
		}
    	try {
			ClientController controller = new ClientController();
			ServiceLocator serviceLocator = ServiceLocator.getServiceLocator();
			serviceLocator.setService(args[0], args[1], args[2]);
			ClientWindow.getClientWindow(controller).setVisible(true);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
