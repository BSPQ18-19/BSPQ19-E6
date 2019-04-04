package com.spq.group6.server.data;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(detachable = "true")
public class Product implements Serializable{
	private static final long serialVersionUID = -7363525693084022738L;
	@Persistent(defaultFetchGroup="true")
	private User user;
	private String name;
	private String description;
	
	public Product(User user, String name, String description){
		super();
		this.user = user;
		this.name = name;
		this.description = description;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Product product = (Product) o;
		return user.equals(product.user) &&
				name.equals(product.name) &&
				description.equals(product.description);
	}
}
