package com.spq.group6.server.data;

import java.io.Serializable;
import java.util.Objects;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable(detachable = "true")
public class Auction implements Serializable{
	private static final long serialVersionUID = 2911721842372082865L;
	@Persistent(defaultFetchGroup="true")
	private User owner;
	@Persistent(defaultFetchGroup="true")
	private Product product;
	private String dayLimit;
	private String startDay;
	private int initialPrice;
	@Persistent(defaultFetchGroup="true")
	private Bid highestBid;
	private String password;
	private String state;
	
	public Auction(User owner, Product product, String dayLimit, String startDay, int initialPrice
			, Bid highestBid, String password, String state) {
		super();
		this.owner = owner;
		this.product = product;
		this.dayLimit = dayLimit;
		this.startDay = startDay;
		this.initialPrice = initialPrice;
		this.highestBid = highestBid;
		this.password = password;
		this.state = state;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getDayLimit() {
		return dayLimit;
	}

	public void setDayLimit(String dayLimit) {
		this.dayLimit = dayLimit;
	}

	public String getStartDay() {
		return startDay;
	}

	public void setStartDay(String startDay) {
		this.startDay = startDay;
	}

	public int getInitialPrice() {
		return initialPrice;
	}

	public void setInitialPrice(int initialPrice) {
		this.initialPrice = initialPrice;
	}

	public Bid getHighestBid() {
		return highestBid;
	}

	public void setHighestBid(Bid highestBid) {
		this.highestBid = highestBid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Auction auction = (Auction) o;
		return initialPrice == auction.initialPrice &&
				owner.equals(auction.owner) &&
				product.equals(auction.product) &&
				dayLimit.equals(auction.dayLimit) &&
				startDay.equals(auction.startDay) &&
				highestBid.equals(auction.highestBid) &&
				Objects.equals(password, auction.password) &&
				state.equals(auction.state);
	}
}
