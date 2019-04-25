package com.spq.group6.server.utils.observer.events;

import com.spq.group6.server.data.Auction;

import java.io.Serializable;

public class NewBidEvent implements Serializable {
    public NewBidEvent(Auction auction) {
        this.auction = auction;
    }

    public Auction auction;
}
