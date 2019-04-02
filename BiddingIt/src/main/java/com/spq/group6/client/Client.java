package com.spq.group6.client;

import java.rmi.RemoteException;

import com.spq.group6.client.controller.ClientController;
import com.spq.group6.server.remote.IServer;
import com.spq.group6.server.data.User;


public class Client 
{
    public static void main(String[] args) {
		try {
			new ClientController(args);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
