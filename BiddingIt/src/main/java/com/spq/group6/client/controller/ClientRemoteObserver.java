package com.spq.group6.client.controller;

import com.spq.group6.client.gui.ClientWindow;
import com.spq.group6.client.gui.panels.MarketPanel;
import com.spq.group6.client.gui.panels.UserAuctionsPanel;
import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.Bid;
import com.spq.group6.server.utils.observer.events.AuctionClosedEvent;
import com.spq.group6.server.utils.observer.events.AuctionDeletedEvent;
import com.spq.group6.server.utils.observer.events.NewBidEvent;
import com.spq.group6.server.utils.observer.events.UserDeletedEvent;
import com.spq.group6.server.utils.observer.remote.IRemoteObserver;

import javax.swing.*;
import java.rmi.RemoteException;

public class ClientRemoteObserver implements IRemoteObserver {
    ClientController controller;
    ClientWindow window;

    public ClientRemoteObserver() throws RemoteException {
        controller = ClientController.getClientController();
        window = ClientWindow.getClientWindow();
    }

    public void update(Object arg) throws RemoteException {
        if(arg instanceof NewBidEvent){
            // TODO:
        }
        else if(arg instanceof AuctionClosedEvent){
            checkAuctionClosed((AuctionClosedEvent) arg);
        }
        else if(arg instanceof AuctionDeletedEvent){
            // TODO:
        }
        else if(arg instanceof UserDeletedEvent){
            // TODO:
        }
    }

    private void checkNewBid(NewBidEvent event){
        // TODO:
    }

    private void checkAuctionClosed(AuctionClosedEvent event){
        Auction auction = event.auction;
        Bid bid = auction.getHighestBid();
        if (auction.getOwner() == controller.getCurrentUser()){
            controller.setCurrentUser(auction.getOwner());
            JOptionPane.showMessageDialog(null, "One of your auction has finished!", "Info", JOptionPane.INFORMATION_MESSAGE);

        } else if (bid != null && bid.getUser() == controller.getCurrentUser()){
            controller.setCurrentUser(bid.getUser());
            JOptionPane.showMessageDialog(null, "One auction has finished with your Bid being the highest", "Info", JOptionPane.INFORMATION_MESSAGE);
            // TODO: Alert message
        }
        updateAuctionWindows(auction);
    }

    private void updateAuctionWindows(Auction auction) {
        JPanel panel = window.getMainPanel();
        if (panel instanceof MarketPanel){
            MarketPanel marketPanel = (MarketPanel) panel;
            marketPanel.getAuctions().remove(auction);
            marketPanel.updateAuctions();
        }else if(panel instanceof UserAuctionsPanel){
            UserAuctionsPanel ownAuctionsPanel = (UserAuctionsPanel) panel;
            ownAuctionsPanel.getUserAuctions().remove(auction);
            ownAuctionsPanel.updateAuctions();
        }
    }


}
