package com.spq.group6.server;

import com.spq.group6.server.remote.BiddingIt;
import com.spq.group6.server.remote.IBiddingIt;

import java.rmi.Naming;

public class Server {
    private static String rmiIp, rmiPort;

    public static void main(String[] args) {
        if (args.length != 3) {
            System.exit(0);
        }

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        rmiIp = args[0];
        rmiPort = args[1];

        String name = "//" + args[0] + ":" + args[1] + "/" + args[2];

        try {
            IBiddingIt biddingSystem= new BiddingIt();
            Naming.rebind(name, biddingSystem);
            System.out.println("- BiddingIt system '" + name + "' active and waiting...");
        } catch (Exception e) {
            System.err.println("$ BiddingIt exception: " + e.getMessage());
            e.printStackTrace();
        }

    }
}
