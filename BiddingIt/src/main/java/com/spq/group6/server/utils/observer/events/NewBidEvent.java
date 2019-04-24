package com.spq.group6.server.utils.observer.events;

import com.spq.group6.server.data.Auction;

public class NewBidEvent {
    public NewBidEvent(Auction auction) {
        this.auction = auction;
    }

    public Auction auction;
}
