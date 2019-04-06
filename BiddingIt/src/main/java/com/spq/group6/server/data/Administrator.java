package com.spq.group6.server.data;

import java.io.Serializable;

import javax.jdo.annotations.PersistenceCapable;

@PersistenceCapable(detachable = "true")
public class Administrator implements Serializable {
	private static final long serialVersionUID = 7105382595085185972L;
	private String username;
	private String password;
	
	public Administrator(String username, String password) {
		super();
		this.username = username;
		this.password = password;
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

	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Administrator that = (Administrator) o;
		return username.equals(that.username) &&
				password.equals(that.password);
	}
}
