package com.spq.group6.server.data;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

@PersistenceCapable(detachable = "true")
public class Auction implements Serializable {
    public static final long serialVersionUID = 2911721842372082865L;
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.INCREMENT)
    private long auctionID;
    @Persistent(defaultFetchGroup = "true")
    private User owner;
    @Persistent(defaultFetchGroup = "true")
    private Product product;
    private Timestamp dayLimit;
    private float initialPrice;
    @Persistent(defaultFetchGroup = "true")
    private Bid highestBid;
    private String password;
    private boolean isOpen;

    public Auction(User owner, Product product, Timestamp dayLimit, float initialPrice, String password) {
        this.owner = owner;
        this.product = product;
        this.dayLimit = dayLimit;
        this.initialPrice = initialPrice;
        this.password = password;

        this.highestBid = null;
        this.isOpen = true;
    }

    public long getAuctionID() {
        return auctionID;
    }

    public void setAuctionID(long auctionID) {
        this.auctionID = auctionID;
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

    public Timestamp getDayLimit() {
        return dayLimit;
    }

    public void setDayLimit(Timestamp dayLimit) {
        this.dayLimit = dayLimit;
    }

    public float getInitialPrice() {
        return initialPrice;
    }

    public void setInitialPrice(float initialPrice) {
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

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Auction auction = (Auction) o;
        return initialPrice == auction.initialPrice &&
                isOpen == auction.isOpen &&
                owner.equals(auction.owner) &&
                product.equals(auction.product) &&
                dayLimit.equals(auction.dayLimit) &&
                Objects.equals(highestBid, auction.highestBid) &&
                Objects.equals(password, auction.password);
    }
}
