package com.spq.group6.server.utils.observer.events;

import com.spq.group6.server.data.User;

import java.io.Serializable;

/**
 * Class representing an User Deleted event.
 * <p>
 * An instance of this Class is sent to RemoteObservers whenever
 * a User is deleted by an Administrator.
 * This class contains the deleted User.
 */
public class UserDeletedEvent implements Serializable {
    public User user;

    public UserDeletedEvent(User user) {
        this.user = user;
    }
}
