package com.spq.group6.client.gui.elements;

import com.spq.group6.client.controller.ClientController;
import com.spq.group6.client.gui.panels.UserAuctionsPanel;
import com.spq.group6.client.gui.utils.locale.LanguageManager;
import com.spq.group6.server.data.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Edited JTableModel to satisfy BiddingIt standards
 */
public class AuctionJTableModel extends DefaultTableModel {

    private static final long serialVersionUID = 1L;
    private ClientController controller;
    private UserAuctionsPanel parentPanel;

    public AuctionJTableModel(Object[][] data, String[] columnNames, UserAuctionsPanel parentPanel) {
        super(data, columnNames);

        this.controller = ClientController.getClientController();
        this.parentPanel = parentPanel;
    }


    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return rowIndex == this.getRowCount() - 1 && columnIndex != 3 && columnIndex != 4;
    }

    public void createAuction(int rowIndex, Product product, String password, LocalDateTime dayLimit, String initialPrice) {
        if (!dayLimit.isAfter(LocalDateTime.now()))
            JOptionPane.showConfirmDialog(null, LanguageManager.getMessage("AuctionJTable.dialog.text1"), "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
        else if (Float.parseFloat(initialPrice) < 0)
            JOptionPane.showConfirmDialog(null, LanguageManager.getMessage("AuctionJTable.dialog.text2"), "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
        else if (password != null && !password.equals("")) {
            if (controller.createPrivateAuction(product, password, Timestamp.valueOf(dayLimit), Float.parseFloat(initialPrice))) {
                this.setValueAt("0", rowIndex, 3);
                this.setValueAt(LanguageManager.getMessage("AuctionJTable.button.open"), rowIndex, 4);
                this.setValueAt("", rowIndex, 6);
                this.addRow(new String[]{"", "", "", "-", "-", "", LanguageManager.getMessage("AuctionJTable.button.create")}); // add new row for a new auction
                JOptionPane.showConfirmDialog(null, LanguageManager.getMessage("AuctionJTable.dialog.text3"), "Info", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
                parentPanel.updateUserProductsComboBox();
            } else
                JOptionPane.showConfirmDialog(null, LanguageManager.getMessage("AuctionJTable.dialog.text4"), "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
        } else {
            if (controller.createPublicAuction(product, Timestamp.valueOf(dayLimit), Float.parseFloat(initialPrice))) {
                this.setValueAt("0", rowIndex, 3);
                this.setValueAt(LanguageManager.getMessage("AuctionJTable.button.open"), rowIndex, 4);
                this.setValueAt("", rowIndex, 6);
                this.addRow(new String[]{"", "", "", "-", "-", "", LanguageManager.getMessage("AuctionJTable.button.create")}); // add new row for a new auction
                JOptionPane.showConfirmDialog(null, LanguageManager.getMessage("AuctionJTable.dialog.text5"), "Info", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
                parentPanel.updateUserProductsComboBox();
            } else
                JOptionPane.showConfirmDialog(null, LanguageManager.getMessage("AuctionJTable.dialog.text6"), "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
        }
    }

}
