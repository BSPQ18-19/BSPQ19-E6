package com.spq.group6.server.utils;

import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AuctionLocks {
    private static HashMap<Long, Lock> locks = new HashMap<Long, Lock>();

    public static Lock getLock(Long auctionID) {
        return locks.get(auctionID);
    }
    public static synchronized void setLock(Long auctionID) {
        if (!locks.containsKey(auctionID)) {
            locks.put(auctionID, new ReentrantLock());
        }
    }
}
