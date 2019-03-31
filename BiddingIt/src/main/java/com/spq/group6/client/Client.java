package com.spq.group6.client;

/**
 * Hello world!
 *
 */
public class Client 
{
    public static void main(String[] args) {
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
			biddingItServerGateway.registerUser("Alejandro", "1234");
			System.out.println("* Message coming from the server: '" + biddingItServerGateway.sayMessage("Alejandro", "Lorite", "This is a test!") + "'");
			
		} catch (Exception e) {
			System.err.println("RMI Example exception: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
