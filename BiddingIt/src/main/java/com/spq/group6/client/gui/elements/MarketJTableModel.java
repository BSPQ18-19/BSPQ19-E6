package com.spq.group6.client.gui.elements;

import com.spq.group6.client.controller.ClientController;
import com.spq.group6.server.data.Auction;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

public class MarketJTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;
    private ClientController controller;
    private Object[][] data;
    private String[] columnNames;

    public MarketJTableModel(Object[][] data, String[] columnNames) {
        this.data = data;
        this.columnNames = columnNames;
        this.controller = ClientController.getClientController();
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return data.length;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        return data[row][col];
    }

    /*
     * JTable uses this method to determine the default renderer/ editor for
     * each cell. If we didn't implement this method, then the last column
     * would contain text ("true"/"false"), rather than a check box.
     */
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 5;
    }

    public void bidAuctionAt(int rowIndex, Auction auction) {
        float actualHighestBid = 0;
        if (auction.getHighestBid() != null) {
            actualHighestBid = auction.getHighestBid().getAmount();
        } else {
            actualHighestBid = auction.getInitialPrice();
        }
        String bidValue = JOptionPane.showInputDialog(null,
                "Please enter the amount you want to bid (greater than " + actualHighestBid + ")", "Bid", JOptionPane.QUESTION_MESSAGE);
        if (bidValue != null && Float.parseFloat(bidValue) > actualHighestBid)
            if (controller.bid(auction, Float.parseFloat(bidValue))) {
                JOptionPane.showConfirmDialog(null, "Auction bidded correctly.", "Info", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
                this.setValueAt(bidValue, rowIndex, 3);

            } else
                JOptionPane.showConfirmDialog(null, "Error bidding the auction.", "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
        else
            JOptionPane.showConfirmDialog(null, "Error bidding the auction. Amount too small.", "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
    }

}
