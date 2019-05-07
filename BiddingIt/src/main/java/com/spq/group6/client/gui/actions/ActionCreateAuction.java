package com.spq.group6.client.gui.actions;

import com.spq.group6.client.gui.elements.AuctionJTableModel;
import com.spq.group6.server.data.Product;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;

public class ActionCreateAuction extends AbstractAction {

    private static final long serialVersionUID = 1L;

    @Override
    public void actionPerformed(ActionEvent e) {
        JTable table = (JTable) e.getSource();
        int modelRow = Integer.valueOf(e.getActionCommand());
        try {
            ((AuctionJTableModel) table.getModel()).createAuction(modelRow,
                    (Product) table.getValueAt(modelRow, 0), (String) table.getValueAt(modelRow, 1),
                    (LocalDateTime) table.getValueAt(modelRow, 5), (String) table.getValueAt(modelRow, 2));
        } catch (Exception ex) {
            JOptionPane.showConfirmDialog(table, "Error. Please fill all the information: product, initial price and day limit", "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
