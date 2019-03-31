package com.spq.group6.server;

import com.spq.group6.server.remote.IServer;
import com.spq.group6.server.remote.Server;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ServerMain{

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("How to invoke: java [policy] [codebase] Server.Server [host] [port] [server]");
            System.exit(0);
        }

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        String name = "//" + args[0] + ":" + args[1] + "/" + args[2];

        try {
            IServer objServer = new Server();
            Naming.rebind(name, objServer);
            System.out.println("Server '" + name + "' active and waiting...");

        } catch (Exception e) {
            System.err.println("Exception ocurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}