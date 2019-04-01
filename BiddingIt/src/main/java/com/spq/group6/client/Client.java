package com.spq.group6.client;

import com.spq.group6.client.controller.ClientController;
import com.spq.group6.server.remote.IServer;
import com.spq.group6.server.data.User;


public class Client 
{
    public static void main(String[] args) {
		new ClientController(args);
		
	}
}
