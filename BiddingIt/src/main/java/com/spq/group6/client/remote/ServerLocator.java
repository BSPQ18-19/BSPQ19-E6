package com.spq.group6.client.remote;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import ecom.spq.group6.remote.IServer;

public class ServerLocator {
	private IServer service;

	public ServerLocator() {
		
	}
	
    public IEasyBookingServerRemoteFacade getService() {
        return service;
    }

    public void setService(String[] args) {
    	if (args.length != 3) {
			System.out.println("Use: java [policy] [codebase] Client.Client [host] [port] [server]");
			System.exit(0);
		}

		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		try {
			String name = "//" + args[0] + ":" + args[1] + "/" + args[2];
			IServer biddingItServerGateway = (IServer) java.rmi.Naming.lookup(name);
			// Register to be allowed to send messages
			User user = biddingItServerGateway.signIn("Alejandro", "1234", "Spain");
			System.out.println("response: " + user.getMoney());
			User user2 = biddingItServerGateway.logIn("Alejandro", "1234");
			
		} catch (Exception e) {
			System.err.println("RMI Example exception: " + e.getMessage());
			e.printStackTrace();
		}

    }
    
}