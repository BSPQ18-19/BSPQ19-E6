package com.spq.group6.server.utils.observer.events;

import com.spq.group6.server.data.Auction;

import java.io.Serializable;

public class AuctionClosedEvent implements Serializable {
    public Auction auction;

    public AuctionClosedEvent(Auction auction) {
        this.auction = auction;
    }
}
