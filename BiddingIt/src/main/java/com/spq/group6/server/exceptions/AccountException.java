package com.spq.group6.server.exceptions;

/**
 * Exception when there is an Error while processing
 * a request on the Account service
 */
public class AccountException extends Exception {
    public AccountException() {
        super();
    }

    public AccountException(String message) {
        super(message);
    }
}
