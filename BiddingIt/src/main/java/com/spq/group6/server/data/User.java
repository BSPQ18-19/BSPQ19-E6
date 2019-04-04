package com.spq.group6.server.data;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@SuppressWarnings("unused")
@PersistenceCapable
public class User implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1067819197739525240L;
	@PrimaryKey
	private String username;
	private String password;
	private String country;
	private int money;
	private Product[] ownedProducts;

	public User(String username, String password, String country){
		super();
		this.username = username;
		this.password = password;
		this.country = country;
	}

	public User(String username, String password, String country, int money, Product[] ownedProducts) {
		super();
		this.username = username;
		this.password = password;
		this.country = country;
		this.money = money;
		this.ownedProducts = ownedProducts;
	}

	public Product[] getOwnedProducts() {
		return ownedProducts;
	}

	public void setOwnedProducts(Product[] ownedProducts) {
		this.ownedProducts = ownedProducts;
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

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}
	
}
