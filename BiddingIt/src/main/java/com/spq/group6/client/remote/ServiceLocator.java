package com.spq.group6.client.remote;

import com.spq.group6.server.remote.IServer;

public class ServiceLocator {
	private static ServiceLocator sl;
	private IServer service;

	private ServiceLocator() {
		
	}

	public static ServiceLocator getServiceLocator(){
		if (sl == null) sl = new ServiceLocator();
		return sl;
	}
    public IServer getService() {
        return service;
    }

    public void setService(String ip, String port, String serviceName) {
		String name = "//" + ip + ":" + port + "/" + serviceName;
		try {
			service = (IServer) java.rmi.Naming.lookup(name);
			System.out.println("Connected to BiddingIt server");
		}catch (Exception e) {
			System.err.println("- Exception running the client: " + e.getMessage());
			e.printStackTrace();
		}
    }
    
}