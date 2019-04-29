package com.spq.group6.admin.gui.actions;

import com.spq.group6.admin.gui.elements.AuctionJTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ActionDeleteAuction extends AbstractAction {

    private static final long serialVersionUID = 1L;

    public void actionPerformed(ActionEvent e) {
        JTable table = (JTable) e.getSource();
        int modelRow = Integer.valueOf(e.getActionCommand());
        ((AuctionJTableModel) table.getModel()).deleteAuctionAt(modelRow);
    }
}
