package com.spq.group6.client.gui.elements;

import com.spq.group6.client.controller.ClientController;
import com.spq.group6.client.gui.utils.locale.LanguageManager;
import com.spq.group6.server.data.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * Edited JTableModel to satisfy BiddingIt standards
 */
public class ProductJTableModel extends DefaultTableModel {

    private static final long serialVersionUID = 1L;
    private ClientController controller;

    public ProductJTableModel(Object[][] data, String[] columnNames) {
        super(data, columnNames);

        this.controller = ClientController.getClientController();

    }


    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 0 || columnIndex == 1 || columnIndex == 2 || columnIndex == 3;
    }

    public void updateProductAt(int rowIndex, String name, String description) {
        if (rowIndex == this.getRowCount() - 1) { // last row (new) product (create product)
            if (controller.createProduct(name, description)) {
                this.setValueAt(LanguageManager.getMessage("ProductJTable.button.save"), rowIndex, 2);
                this.setValueAt(LanguageManager.getMessage("ProductJTable.button.delete"), rowIndex, 3);
                this.addRow(new String[]{"", "", LanguageManager.getMessage("ProductJTable.button.create"), ""}); // add new row for a new product
                JOptionPane.showConfirmDialog(null, LanguageManager.getMessage("ProductJTable.dialog.text1"), "Info", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
            } else
                JOptionPane.showConfirmDialog(null, LanguageManager.getMessage("ProductJTable.dialog.text2"), "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
        } else { // (update product)
            if (controller.updateProduct(controller.getCurrentUserProducts().get(rowIndex), name, description)) {
                JOptionPane.showConfirmDialog(null, LanguageManager.getMessage("ProductJTable.dialog.text3"), "Info", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
            } else
                JOptionPane.showConfirmDialog(null, LanguageManager.getMessage("ProductJTable.dialog.text4"), "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
        }

    }

    public void removeProductAt(int rowIndex) {
        if (rowIndex != this.getRowCount() - 1) { // not last row (new) product
            Product product = controller.getCurrentUser().getOwnedProducts().get(rowIndex);
            if (controller.deleteProduct(product)) {
                JOptionPane.showConfirmDialog(null, "", LanguageManager.getMessage("ProductJTable.dialog.text5"), JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
                this.removeRow(rowIndex);

            } else
                JOptionPane.showConfirmDialog(null, LanguageManager.getMessage("ProductJTable.dialog.text6"), "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);

        }
    }

}
