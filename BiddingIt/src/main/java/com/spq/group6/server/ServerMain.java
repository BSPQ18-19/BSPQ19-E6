package com.spq.group6.server;

import com.spq.group6.server.remote.IServer;
import com.spq.group6.server.remote.Server;
import com.spq.group6.server.utils.logger.ServerLogger;

import java.rmi.Naming;

public class ServerMain {

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
//            objServer.createAdministrator(new Administrator("admin","pass"));
            Naming.rebind(name, objServer);
            java.io.InputStreamReader inputStreamReader = new java.io.InputStreamReader(System.in);
            java.io.BufferedReader stdin = new java.io.BufferedReader(inputStreamReader);
            ServerLogger.logger.info("BiddingIt active and listening...");
            stdin.readLine();

        } catch (Exception e) {
            ServerLogger.logger.error("Exception ocurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}