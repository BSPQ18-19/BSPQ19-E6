package com.spq.group6.client.controller;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;

import easybooking.client.gui.ClientWindow;
import easybooking.client.remote.*;
import easybooking.server.data.AuthorizationSystem;
import easybooking.server.data.dto.AirportDTO;
import easybooking.server.data.dto.FlightDTO;
import easybooking.server.data.dto.UserDTO;

public class ClientController {

	private EasyBookingServerLocator serviceLocator;
	private UserDTO currentUser;
	private ArrayList<FlightDTO> currentFlights;
	
    public ClientController(String args[]) throws RemoteException {
		super();
		serviceLocator = new EasyBookingServerLocator();
		serviceLocator.setService(args);
		this.currentFlights = new ArrayList<>();
		ClientWindow.getClientWindow(this).setVisible(true);
	}
    
    public boolean signIn(String email, String password, AuthorizationSystem authSystem) throws RemoteException {
        try {
        	System.out.println("Trying to sign in with email " + email + " and password " + password + " using " + authSystem + " authorization.");
            UserDTO user = serviceLocator.getService().signIn(email, password, authSystem);
        	if (user != null) {
            	System.out.println("Sign in with email " + email + " and password " + password + " using " + authSystem + " authorization is correct.");
            	this.currentUser = user;
            	return true;
            } else
            	System.out.println("Sign in with email " + email + " and password " + password + " using " + authSystem + " authorization is incorrect.");
        } catch (RemoteException re) {
        	System.out.println(re);
        	throw re;
        }
        return false;
     }
    
    public boolean logOut() {
    	this.currentUser = null;
    	return true;
    }
    
    public boolean existsUsername(String username) {
    	try {
        	System.out.println("Checking if user with username " + username + " exists in easy booking server.");
            if (serviceLocator.getService().existsUsername(username)) {
            	System.out.println("User with username " + username + " already exists.");
            	return true;
            } else
            	System.out.println("User with username " + username + " does not exist.");
        } catch (Exception e) {
        	System.out.println("Error finding user: " + e.getMessage());
        }
        return false;
    }
    
    public boolean existsEmail(String email) {
    	try {
        	System.out.println("Checking if user with email " + email + " exists in easy booking server.");
            if (serviceLocator.getService().existsEmail(email)) {
            	System.out.println("User with email " + email + " already exists.");
            	return true;
            } else
            	System.out.println("User with email " + email + " does not exist.");
        } catch (Exception e) {
        	System.out.println("Error finding user: " + e.getMessage());
        }
        return false;
    }

    public boolean pay(float amount, String... details) {
		try {
			return serviceLocator.getService().pay(amount, details);
		} catch (RemoteException e) {
			e.printStackTrace();
			return false;
		}
	}
    
    public boolean registerUser(String username, String email, String airportCode, AuthorizationSystem authSystem) {
    	try {
        	System.out.println("Registering user with username " + username + ", email " + email + " using " + authSystem);
            UserDTO user = serviceLocator.getService().register(username, email, airportCode, authSystem);
        	if (user != null) {
            	System.out.println("User with username " + username + " registered succesfully.");
            	this.currentUser = user;
            	return true;
            } else
            	System.out.println("User with username " + username + " can not be registered.");
        } catch (Exception e) {
        	System.out.println("Error registering user: " + e.getMessage());
        }
        return false;
    }
    
    public String[] getAirports() {
    	try {
        	System.out.println("Getting list of airports from easy booking server.");
        	AirportDTO[] airportsRaw = serviceLocator.getService().getAirports();
        	if (airportsRaw != null && airportsRaw.length != 0) {
            	System.out.println("List of airports retrieved succesfully.");
	        	String[] airports = new String[airportsRaw.length];
	        	for (int i = 0; i < airports.length; i++)
	        		airports[i] = airportsRaw[i].getId() + " - " + airportsRaw[i].getCity() + " " + airportsRaw[i].getName();
	            return airports;
        	} else
            	System.out.println("Could not retreive list of airports.");

        } catch (Exception e) {
        	System.out.println("Error getting list of airports: " + e.getMessage());
        }
        return new String[0];
    }
    
    public FlightDTO[] searchFlights(LocalDate departureDate, String departureAirportCode, String arrivalAirportCode) {
    	try {
        	System.out.println("Getting list of flights from " + departureAirportCode + " to " + arrivalAirportCode + " on " + departureDate.toString() + ".");
            ArrayList<FlightDTO> flightsraw = serviceLocator.getService().searchFlights(departureDate, 
            		departureAirportCode.trim().toUpperCase(), arrivalAirportCode.trim().toUpperCase());
        	FlightDTO[] flights = flightsraw.toArray(new FlightDTO[flightsraw.size()]);
            if (flights != null && flights.length != 0) {
            	System.out.println("List of flights retrieved succesfully.");
            	return flights;
            } else
            	System.out.println("Could not retrieve list of flights.");
        	
        } catch (Exception e) {
        	System.out.println("Error getting list of flights: " + e.getMessage());
        }
    	return null;
    }

    public boolean bookSeats (FlightDTO flight, int seats, float price, String... names) {
    	try {
			return serviceLocator.getService().bookFlight(currentUser.getEmail(), flight.getCode(), seats, price, names);
		} catch (Exception e) {
			System.out.println("Error booking seats: " + e.getMessage());
		}
    	return false;
    }
    public UserDTO getCurrentUser() {
		return currentUser;
	} 

	public void exit(){
    	System.exit(0);
    }

	public ArrayList<FlightDTO> getCurrentFlights() {
		return currentFlights;
	}

	public void setCurrentFlights(FlightDTO	flightDTO) {
		this.currentFlights.add(flightDTO);
	}
	public void setCurrentFlights() {
		this.currentFlights.clear();
	}

	public static void main(String[] args) throws RemoteException {
		new ClientController(args);
	}
}