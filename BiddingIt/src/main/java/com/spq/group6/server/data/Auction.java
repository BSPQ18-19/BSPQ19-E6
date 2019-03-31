package com.spq.group6.server.data;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class Auction implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2911721842372082865L;
	@PrimaryKey
	@Persistent(valueStrategy=IdGeneratorStrategy.INCREMENT)
	private long auctionID;
	private User ownerID;
	private Product productID;
	private String dayLimit;
	private String startDay;
	private int initialPrice;
	private Bid highestBid;
	private String password;
	private String state;
	
	public Auction(User ownerID, Product productID, String dayLimit, String startDay, int initialPrice
			, Bid highestBid, String password, String state) {
		super();
		this.ownerID = ownerID;
		this.productID = productID;
		this.dayLimit = dayLimit;
		this.startDay = startDay;
		this.initialPrice = initialPrice;
		this.highestBid = highestBid;
		this.password = password;
		this.state = state;
	}

	public long getAuctionID() {
		return auctionID;
	}

	public void setAuctionID(long auctionID) {
		this.auctionID = auctionID;
	}

	public User getOwnerID() {
		return ownerID;
	}

	public void setOwnerID(User ownerID) {
		this.ownerID = ownerID;
	}

	public Product getProductID() {
		return productID;
	}

	public void setProductID(Product productID) {
		this.productID = productID;
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
	
	
	
}
