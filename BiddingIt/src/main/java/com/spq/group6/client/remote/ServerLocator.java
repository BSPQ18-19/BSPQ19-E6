package com.spq.group6.client.remote;

import com.spq.group6.server.data.User;
import com.spq.group6.server.remote.IServer;

public class ServerLocator {
	private IServer service;

	public ServerLocator() {
		
	}
	
    public IServer getService() {
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
			service = (IServer) java.rmi.Naming.lookup(name);
			
		} catch (Exception e) {
			System.err.println("RMI Example exception: " + e.getMessage());
			e.printStackTrace();
		}

    }
    
}