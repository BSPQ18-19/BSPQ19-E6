package com.spq.group6.server.data;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(detachable = "true")
public class Bid implements Serializable {
	private static final long serialVersionUID = 1L;
	@Persistent(defaultFetchGroup="true")
	private User user;
	private float amount;
	
	public Bid(User user, int amount) {
		super();
		this.user = user;
		this.amount = amount;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Bid bid = (Bid) o;
		return amount == bid.amount &&
				user.equals(bid.user);
	}
}
