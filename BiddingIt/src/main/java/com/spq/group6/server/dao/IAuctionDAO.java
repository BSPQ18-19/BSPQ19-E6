package com.spq.group6.server.dao;

import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.Bid;

public interface IAuctionDAO {
    public void persistAuction(Auction auction);
    public void deleteAuction(Auction auction);
    public void deleteBid(Bid bid);
}
