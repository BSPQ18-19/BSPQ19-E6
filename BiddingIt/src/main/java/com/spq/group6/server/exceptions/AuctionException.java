package com.spq.group6.server.exceptions;

/**
 * Exception when there is an Error while processing
 * a request on the Auctions service
 */
public class AuctionException extends Exception {
    public AuctionException() {
        super();
    }

    public AuctionException(String message) {
        super(message);
    }
}
