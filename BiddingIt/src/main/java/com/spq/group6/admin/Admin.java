package com.spq.group6.admin;

import java.rmi.RemoteException;

import com.spq.group6.admin.controller.AdminController;
import com.spq.group6.admin.gui.AdminWindow;
import com.spq.group6.client.remote.ServiceLocator;


public class Admin 
{
    public static void main(String[] args) {
		if (args.length != 3) {
			System.err.println("Invalid number of arguments.");
			return;
		}
    	try {
			AdminController controller = new AdminController();
			ServiceLocator serviceLocator = ServiceLocator.getServiceLocator();
			serviceLocator.setService(args[0], args[1], args[2]);

			AdminWindow.getAdminWindow(controller).setVisible(true);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}