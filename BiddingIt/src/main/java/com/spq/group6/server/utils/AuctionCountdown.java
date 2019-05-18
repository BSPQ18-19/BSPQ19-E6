package com.spq.group6.server.utils;

import com.spq.group6.server.dao.BiddingDAO;
import com.spq.group6.server.dao.IBiddingDAO;
import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.Bid;
import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;
import com.spq.group6.server.services.AccountService;
import com.spq.group6.server.services.AuctionService;
import com.spq.group6.server.utils.logger.ServerLogger;
import com.spq.group6.server.utils.observer.events.AuctionClosedEvent;

import java.sql.Timestamp;

/**
 * Class implementing {@link Runnable} interface for running Auctions' CountDown thread.
 * <p>
 * This class is runned in a Thread. It represents a CountDown, and when the CountDown ends,
 * it executes the logic for the exchange between the Buyer and the Seller.
 * <p>
 * {@link AuctionService} is the one responsible for starting Countdown threads, and it has a HashMap
 * AuctionID --> AuctionCountdown
 */
public class AuctionCountdown implements Runnable {
    private static IBiddingDAO biddingDAO = new BiddingDAO();  /* DAO object for updating
     persistent data. It is used for updating Users and Auctions when while executing Auction end logic*/
    private Auction auction; // Auction that is controlled by the Countdown.
    private long auctionID; /* Auction's AuctionID, just in Auction is corrupted.
    Needed for retrieving latest version from the database */

    /**
     * AuctionCountdown constructor that fills auction and auctionID
     *
     * @param auction Auction that will be controlled by the countdown
     */
    public AuctionCountdown(Auction auction) {
        this.auction = auction;
        auctionID = auction.getAuctionID();
    }

    /**
     * Method that is executed by the Thread.
     * <p>
     * First it sleeps until the Auction closes (or an Administrator deletes it).
     * Then if there has been a valid buyer, Auction's product
     * is added to Buyer's User, and money transferred from the Buyer to the Seller. However,
     * if there is no valid Buyer, the product is given back to the Seller.
     * Finally, new changes are persisted on the database and a notification is sent
     * to all Users.
     */
    public void run() {
        ServerLogger.logger.debug("Countdown started for auction: " + auction.getAuctionID());
        Timestamp now = new Timestamp(System.currentTimeMillis());
        long remainingMilis = auction.getDayLimit().getTime() - now.getTime();
        try {
            Thread.sleep(Math.max(remainingMilis, 0));
            ServerLogger.logger.debug("Countdown ended for auction: " + auction.getAuctionID());
        } catch (InterruptedException e) { // An Administrator deletes the Auction
            ServerLogger.logger.debug("AdminMain has deleted auction: " + auctionID);
        }
        Auction oldAuction = auction;
        auction = BiddingLocks.lockAndGetAuction(auction);
        try {
            if (auction == null) { // This means an Administrator has already delete it.
                AuctionService.countdownObservables.remove(auctionID);
                BiddingLocks.unlockAuction(oldAuction);
                return;
            }

            auction.setOpen(false);
            biddingDAO.persistAuction(auction);

            Bid bid = auction.getHighestBid();
            User buyer = null;
            if (bid != null) { // Checks if there was a Bid
                buyer = bid.getUser();
            }
            if (bid != null && bid.getUser() != null && bid.getUser().getMoney() >= bid.getAmount()) { /* Check
            if the Bid was valid: the Buyer has enough money */
                User seller = BiddingLocks.lockAndGetUser(auction.getOwner());
                buyer = BiddingLocks.lockAndGetUser(buyer);

                try {
                    ServerLogger.logger.debug("Ended auction with exchange: " + auction.getAuctionID());
                    Product product = auction.getProduct();
                    // Update product
                    seller.getOwnedProducts().remove(product);
                    buyer.getOwnedProducts().add(product);
                    // Update money
                    float buyerMoney = buyer.getMoney() - bid.getAmount();
                    buyer.setMoney(buyerMoney);
                    float sellerMoney = seller.getMoney() + bid.getAmount();
                    buyer.setMoney(buyerMoney);
                    seller.setMoney(sellerMoney);
                    // Update and persiste auction
                    auction.setOwner(seller);
                    auction.getHighestBid().setUser(buyer);
                    biddingDAO.persistAuction(auction);
                } catch (Exception e) {
                    BiddingLocks.unlockUser(seller);
                    BiddingLocks.unlockUser(buyer);
                    throw e;
                }
                BiddingLocks.unlockUser(seller);
                BiddingLocks.unlockUser(buyer);

            } else { // There was no valid Bid
                endUnsoldAuction();
            }

            AccountService.observable.notifyRemoteObservers(new AuctionClosedEvent(auction)); /*Notify
            remote observers */
        } catch (Exception e) {
            BiddingLocks.unlockAuction(auction);
            throw e;
        }
        BiddingLocks.unlockAuction(auction);
    }

    /**
     * Method executed when there is no valid Bid.
     *
     * It gives back Auction's Product to the Seller, and persists changes on the database.
     *
     */
    private void endUnsoldAuction() {
        ServerLogger.logger.debug("Ended auction without exchange: " + auction.getAuctionID());
        User seller = BiddingLocks.lockAndGetUser(auction.getOwner());
        try {
            Bid highestBid = auction.getHighestBid();

            auction.setHighestBid(null);
            seller.getOwnedProducts().add(auction.getProduct());
            auction.setOwner(seller);
            biddingDAO.persistAuction(auction);
            biddingDAO.persistUser(seller);
            if (highestBid != null) {
                biddingDAO.deleteBid(highestBid);
            }
        } catch (Exception e) {
            BiddingLocks.unlockUser(seller);
            throw e;
        }
        BiddingLocks.unlockUser(seller);
    }

}
