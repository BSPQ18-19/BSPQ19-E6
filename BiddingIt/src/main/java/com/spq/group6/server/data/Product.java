package com.spq.group6.server.data;

import java.io.Serializable;
import java.util.Objects;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(detachable = "true")
public class Product implements Serializable{
	private static final long serialVersionUID = -7363525693084022738L;
	@PrimaryKey
	@Persistent(valueStrategy=IdGeneratorStrategy.INCREMENT)
	private long productID;
	@Persistent(defaultFetchGroup="true")
	private User userID;
	private String name;
	private String description;
	
	public Product(User userID, String name, String description){
		super();
		this.userID = userID;
		this.name = name;
		this.description = description;
	}

	public long getProductID() {
		return productID;
	}

	public void setProductID(long productID) {
		this.productID = productID;
	}

	public User getUserID() {
		return userID;
	}

	public void setUserID(User userID) {
		this.userID = userID;
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
		return productID == product.productID &&
				userID.equals(product.userID) &&
				name.equals(product.name) &&
				description.equals(product.description);
	}

	public int hashCode() {
		return Objects.hash(productID, userID, name, description);
	}
}
