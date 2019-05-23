package com.spq.group6.server.utils.observer.events;

import com.spq.group6.server.data.Auction;

import java.io.Serializable;

/**
 * Class representing an New Bid event.
 * <p>
 * An instance of this Class is sent to RemoteObservers whenever a new Bid is created.
 * This class contains the Auction that the Bid was made to.
 */
public class NewBidEvent implements Serializable {
    public Auction auction;

    public NewBidEvent(Auction auction) {
        this.auction = auction;
    }
}
