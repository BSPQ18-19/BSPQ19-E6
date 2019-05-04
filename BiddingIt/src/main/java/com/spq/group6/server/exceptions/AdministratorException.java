package com.spq.group6.server.exceptions;

/**
 * Exception when there is an Error while processing
 * a request on the Administrator service
 */
public class AdministratorException extends Exception {
    public AdministratorException() {
        super();
    }

    public AdministratorException(String message) {
        super(message);
    }
}
