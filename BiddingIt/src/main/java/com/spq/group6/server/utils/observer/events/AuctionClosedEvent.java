package com.spq.group6.server.utils.observer.events;

import com.spq.group6.server.data.Auction;

import java.io.Serializable;

/**
 * Class representing an Auction Closed event.
 * <p>
 * An instance of this Class is sent to RemoteObservers whenever an Auction is closed.
 * This class contains the closed Auction.
 */
public class AuctionClosedEvent implements Serializable {
    public Auction auction;

    public AuctionClosedEvent(Auction auction) {
        this.auction = auction;
    }
}
