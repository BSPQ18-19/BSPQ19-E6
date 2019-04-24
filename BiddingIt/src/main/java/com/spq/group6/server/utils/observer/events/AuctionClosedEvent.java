package com.spq.group6.server.utils.observer.events;

import com.spq.group6.server.data.Auction;

public class AuctionClosedEvent {
    public Auction auction;

    public AuctionClosedEvent(Auction auction) {
        this.auction = auction;
    }
}
