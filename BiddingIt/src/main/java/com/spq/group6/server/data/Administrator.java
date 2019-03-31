package com.spq.group6.server.data;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class Administrator implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7105382595085185972L;
	@PrimaryKey
	@Persistent(valueStrategy=IdGeneratorStrategy.INCREMENT)
	private long admindID;
	private String username;
	private String password;
	
	public Administrator(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public long getAdmindID() {
		return admindID;
	}

	public void setAdmindID(long admindID) {
		this.admindID = admindID;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
