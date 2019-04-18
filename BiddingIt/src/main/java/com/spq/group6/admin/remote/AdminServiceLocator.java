package com.spq.group6.admin.remote;

import com.spq.group6.server.remote.IServer;

public class AdminServiceLocator {
	private static AdminServiceLocator sl;
	private IServer service;

	private AdminServiceLocator() {
		
	}

	public static AdminServiceLocator getServiceLocator(){
		if (sl == null) sl = new AdminServiceLocator();
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