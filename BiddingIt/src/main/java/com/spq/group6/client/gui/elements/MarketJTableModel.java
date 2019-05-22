package com.spq.group6.client.gui.elements;

import com.spq.group6.client.controller.ClientController;
import com.spq.group6.client.gui.panels.MarketPanel;
import com.spq.group6.client.gui.utils.locale.LanguageManager;
import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.Bid;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

/**
 * Edited JTableModel to satisfy BiddingIt standards
 */
public class MarketJTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;
    private ClientController controller;
    private Object[][] data;
    private String[] columnNames;
    private MarketPanel parentPanel;

    public MarketJTableModel(Object[][] data, String[] columnNames, MarketPanel parentPanel) {
        this.data = data;
        this.columnNames = columnNames;
        this.controller = ClientController.getClientController();
        this.parentPanel = parentPanel;
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


    public void setValueAt(Object value, int rowIndex, int colIndex) {
        data[rowIndex][colIndex] = value;
        fireTableCellUpdated(rowIndex, colIndex);
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
        boolean passOK = false;
        boolean isPrivate = auction.getPassword() != null && !auction.getPassword().equals("");
        if (isPrivate) {
            String bidPassword = JOptionPane.showInputDialog(null,
                    LanguageManager.getMessage("MarketJTable.bid.password.text1"), LanguageManager.getMessage("MarketJTable.bid.password.text2"), JOptionPane.QUESTION_MESSAGE);
            if (bidPassword != null && bidPassword.equals(auction.getPassword()))
                passOK = true;

        } else
            passOK = true;

        if (passOK) {
            float actualHighestBid = 0;

            if (auction.getHighestBid() != null) {
                actualHighestBid = auction.getHighestBid().getAmount();
            } else {
                actualHighestBid = auction.getInitialPrice();
            }
            String bidValue;
            if (isPrivate)
                bidValue = JOptionPane.showInputDialog(null,
                        LanguageManager.getMessage("MarketJTable.bid.password.text3") + actualHighestBid + ")", "Bid", JOptionPane.QUESTION_MESSAGE);
            else
                bidValue = JOptionPane.showInputDialog(null,
                        LanguageManager.getMessage("MarketJTable.bid.text1") + actualHighestBid + ")", LanguageManager.getMessage("MarketJTable.bid.text2"), JOptionPane.QUESTION_MESSAGE);
            if (bidValue != null && Float.parseFloat(bidValue) > actualHighestBid)
                if (controller.bid(auction, Float.parseFloat(bidValue))) {
                    JOptionPane.showConfirmDialog(null, LanguageManager.getMessage("MarketJTable.bid.text3"), "Info", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
                    auction.setHighestBid(new Bid(controller.getCurrentUser(), Float.parseFloat(bidValue)));
                    this.setValueAt(bidValue, rowIndex, 3);

                } else
                    JOptionPane.showConfirmDialog(null, LanguageManager.getMessage("MarketJTable.error.text1"), "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            else
                JOptionPane.showConfirmDialog(null, LanguageManager.getMessage("MarketJTable.error.text2"), "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
        } else
            JOptionPane.showConfirmDialog(null, LanguageManager.getMessage("MarketJTable.error.text3"), "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);

    }

}
