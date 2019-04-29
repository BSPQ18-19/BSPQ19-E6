package com.spq.group6.server.utils.observer.events;

import com.spq.group6.server.data.User;

import java.io.Serializable;

public class UserDeletedEvent implements Serializable {
    public UserDeletedEvent(User user) {
        this.user = user;
    }

    public User user;
}
