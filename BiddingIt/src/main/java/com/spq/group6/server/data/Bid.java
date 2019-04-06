package com.spq.group6.server.data;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(detachable = "true")
public class Bid implements Serializable {
	private static final long serialVersionUID = 1L;
	@PrimaryKey
	@Persistent(valueStrategy=IdGeneratorStrategy.INCREMENT)
	private long bidID;
	@Persistent(defaultFetchGroup="true")
	private User user;
	private int amount;
	
	public Bid(User user, int amount) {
		super();
		this.user = user;
		this.amount = amount;
	}

	public long getBidID() {
		return bidID;
	}

	public void setBidID(long bidID) {
		this.bidID = bidID;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Bid bid = (Bid) o;
		return bidID == bid.bidID &&
				amount == bid.amount &&
				user.equals(bid.user);
	}
}
