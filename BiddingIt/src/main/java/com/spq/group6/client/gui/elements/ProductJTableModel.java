package com.spq.group6.client.gui.elements;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import com.spq.group6.client.controller.ClientController;
import com.spq.group6.server.data.Product;

public class ProductJTableModel extends DefaultTableModel {

	private static final long serialVersionUID = 1L;
	private ClientController controller;
	private String[][] productsDisplayed;
	
	public ProductJTableModel(Object[][] data, String[] columnNames, ClientController controller) {
    	super(data, columnNames);
		
		this.controller = controller;
		
		this.productsDisplayed = (String[][]) data;   	 	
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnIndex == 0 || columnIndex== 1 ? true : false;
	}
	
	public void modifyProductAt(int rowIndex, String name, String description) {
		Product product = controller.getCurrentUserProducts().get(rowIndex);
		if (rowIndex == productsDisplayed.length - 1) { // last row (new) product (create product)
			if (controller.createProduct(product, name, description)) {
				this.addRow(new String[] {"", "", "Create", ""}); // add new row for a new product
				JOptionPane.showConfirmDialog(null, "Product created correctly.", "Info", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
			} else
				JOptionPane.showConfirmDialog(null, "Error creating a product.", "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
		} else { // (modify product)
			if (controller.modifyProduct(product, name, description))
				JOptionPane.showConfirmDialog(null, "Product modified correctly", "Info", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
			else
				JOptionPane.showConfirmDialog(null, "Error creating a product.", "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
		}
		
	}

	public void removeRow(int rowIndex) {
    	if (rowIndex != productsDisplayed.length - 1) { // not last row (new) product
    		Product product = new Product(productsDisplayed[rowIndex][0], productsDisplayed[rowIndex][1]);
    		if (controller.deleteProduct(product)) {
    			JOptionPane.showConfirmDialog(null, "Product deleted correctly.", "Info", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
    			this.removeRow(rowIndex);

    		} else
    			JOptionPane.showConfirmDialog(null, "Error deleting a product.", "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
			
    	}
	}

}
