package com.spq.group6.client.gui.actions;

import com.spq.group6.client.gui.elements.MarketJTableModel;
import com.spq.group6.server.data.Auction;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ActionBid extends AbstractAction {

    private static final long serialVersionUID = 1L;

    @Override
    public void actionPerformed(ActionEvent e) {
        JTable table = (JTable) e.getSource();
        int modelRow = Integer.valueOf(e.getActionCommand());
        ((MarketJTableModel) table.getModel()).bidAuctionAt(modelRow, (Auction) table.getValueAt(modelRow, 0));
    }

}
