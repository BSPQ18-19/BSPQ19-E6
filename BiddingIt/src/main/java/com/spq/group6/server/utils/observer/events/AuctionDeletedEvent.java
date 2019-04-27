package com.spq.group6.server.utils.observer.events;

import com.spq.group6.server.data.Auction;

import java.io.Serializable;

public class AuctionDeletedEvent implements Serializable {
    public Auction auction;

    public AuctionDeletedEvent(Auction auction) {
        this.auction = auction;
    }
}
