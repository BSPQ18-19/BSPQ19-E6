package com.spq.group6.client.controller;

import com.spq.group6.client.gui.ClientWindow;
import com.spq.group6.client.gui.panels.MarketPanel;
import com.spq.group6.client.gui.panels.UserAuctionsPanel;
import com.spq.group6.client.gui.utils.ScreenType;
import com.spq.group6.client.utils.logger.ClientLogger;
import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.Bid;
import com.spq.group6.server.data.User;
import com.spq.group6.server.utils.observer.events.AuctionClosedEvent;
import com.spq.group6.server.utils.observer.events.AuctionDeletedEvent;
import com.spq.group6.server.utils.observer.events.NewBidEvent;
import com.spq.group6.server.utils.observer.events.UserDeletedEvent;
import com.spq.group6.server.utils.observer.remote.IRemoteObserver;

import javax.swing.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientRemoteObserver extends UnicastRemoteObject implements IRemoteObserver {
    private ClientWindow window;

    public ClientRemoteObserver() throws RemoteException {
        window = ClientWindow.getClientWindow();
    }

    public void update(Object arg) throws RemoteException {
        try {
            ClientController controller = ClientController.getClientController();

            if (arg instanceof NewBidEvent) {
                ClientLogger.logger.debug("Received New Bid Event");
                checkNewBid((NewBidEvent) arg, controller);
            } else if (arg instanceof AuctionClosedEvent) {
                ClientLogger.logger.debug("Received Auction Closed Event");
                checkAuctionClosed((AuctionClosedEvent) arg, controller);
            } else if (arg instanceof AuctionDeletedEvent) {
                ClientLogger.logger.debug("Received Auction Deleted Event");
                checkAuctionDeleted((AuctionDeletedEvent) arg, controller);
            } else if (arg instanceof UserDeletedEvent) {
                ClientLogger.logger.debug("Received User Deleted Event");
                checkUserDeleted((UserDeletedEvent) arg, controller);
            }
        } catch (Exception e) {
            throw new RemoteException();
        }
    }

    private void checkAuctionDeleted(AuctionDeletedEvent event, ClientController controller) {
        Auction auction = event.auction;
        if (auction.getOwner() == controller.getCurrentUser()) {
            controller.setCurrentUser(auction.getOwner());
            updateProductFromWindows();
        }
        removeAuctionFromWindows(auction);
    }

    private void updateProductFromWindows() {
    }

    private void checkUserDeleted(UserDeletedEvent event, ClientController controller) {
        User user = event.user;
        if (user.getUsername().equals(controller.getCurrentUser().getUsername())) {
            controller.logOut();
            window.changeScreen(ScreenType.INITIAL);
            String msg = "Sorry, your User has been deleted";
            showNonBlockingMessage(msg);
        }
    }

    private void checkNewBid(NewBidEvent event, ClientController controller) {
        Auction auction = event.auction;
        if (auction.getHighestBid().getUser() != controller.getCurrentUser()) {
            updateAuctionFromWindows(auction);
        }
    }

    private void checkAuctionClosed(AuctionClosedEvent event, ClientController controller) {
        Auction auction = event.auction;
        Bid bid = auction.getHighestBid();

        if (auction.getOwner() == controller.getCurrentUser()) {
            controller.setCurrentUser(auction.getOwner());
        } else if (bid != null && bid.getUser() == controller.getCurrentUser()) {
            controller.setCurrentUser(bid.getUser());
        }
        removeAuctionFromWindows(auction);
    }

    private void removeAuctionFromWindows(Auction auction) {
        JPanel panel = window.getMainPanel();
        String msg = "Auction closed - " + auction.getAuctionID();

        if (panel instanceof MarketPanel) {
            MarketPanel marketPanel = (MarketPanel) panel;
            marketPanel.getAuctions().remove(auction);
            marketPanel.updateAuctions();
            showNonBlockingMessage(msg);
        } else if (panel instanceof UserAuctionsPanel) {
            UserAuctionsPanel ownAuctionsPanel = (UserAuctionsPanel) panel;
            ownAuctionsPanel.getUserAuctions().remove(auction);
            ownAuctionsPanel.updateAuctions();
            showNonBlockingMessage(msg);
        }
    }

    private void updateAuctionFromWindows(Auction auction) {
        JPanel panel = window.getMainPanel();
        String msg = "New bid - " + auction.getAuctionID();
        if (panel instanceof MarketPanel) {
            MarketPanel marketPanel = (MarketPanel) panel;
            int index = marketPanel.getAuctions().indexOf(auction);
            if (index != -1) {
                marketPanel.getAuctions().set(index, auction);
                marketPanel.updateAuctions();
            }
            showNonBlockingMessage(msg);
        } else if (panel instanceof UserAuctionsPanel) {
            UserAuctionsPanel ownAuctionsPanel = (UserAuctionsPanel) panel;
            int index = ownAuctionsPanel.getUserAuctions().indexOf(auction);
            if (index != -1) {
                ownAuctionsPanel.getUserAuctions().set(index, auction);
                ownAuctionsPanel.updateAuctions();
                showNonBlockingMessage(msg);
            }
        }
    }

    private void showNonBlockingMessage(String msg) {
        Thread t = new Thread(new Runnable() {
            public void run() {
                JOptionPane.showMessageDialog(null, msg, "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        t.start();

    }

    public User getUser() {
        return ClientController.getClientController().getCurrentUser();
    }

}
