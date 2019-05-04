package com.spq.group6.server.utils.observer.events;

import com.spq.group6.server.data.Auction;

import java.io.Serializable;

/**
 * Class representing an Auction Deleted event.
 * An instance of this Class is sent to RemoteObservers whenever
 * an Auction is deleted by an Administrator.
 * This class contains the deleted .
 */
public class AuctionDeletedEvent implements Serializable {
    public Auction auction;

    public AuctionDeletedEvent(Auction auction) {
        this.auction = auction;
    }
}
