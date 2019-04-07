package com.spq.group6.client.gui.elements;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import com.spq.group6.client.controller.ClientController;
import com.spq.group6.server.data.Product;

public class ProductJTableModel extends DefaultTableModel {

	private static final long serialVersionUID = 1L;
	private ClientController controller;
	
	public ProductJTableModel(Object[][] data, String[] columnNames, ClientController controller) {
    	super(data, columnNames);
		
		this.controller = controller;
		
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnIndex == 0 || columnIndex == 1 || columnIndex == 2 || columnIndex == 3 ? true : false;
	}
	
	public void updateProductAt(int rowIndex, String name, String description) {
		if (rowIndex == this.getRowCount() - 1) { // last row (new) product (create product)
			if (controller.createProduct(name, description)) {
				this.setValueAt("Save", rowIndex, 2);
				this.setValueAt("Delete", rowIndex, 3);
				this.addRow(new String[] {"", "", "Create", ""}); // add new row for a new product
				JOptionPane.showConfirmDialog(null, "Product created correctly.", "Info", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
			} else
				JOptionPane.showConfirmDialog(null, "Error creating a product.", "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
		} else { // (update product)
			if (controller.updateProduct(controller.getCurrentUserProducts().get(rowIndex), name, description)) {
				JOptionPane.showConfirmDialog(null, "Product modified correctly", "Info", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
			} else
				JOptionPane.showConfirmDialog(null, "Error creating a product.", "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
		}
		
	}

	public void removeProductAt(int rowIndex) {
    	if (rowIndex != this.getRowCount() - 1) { // not last row (new) product
    		Product product = controller.getCurrentUser().getOwnedProducts().get(rowIndex);
    		if (controller.deleteProduct(product)) {
    			JOptionPane.showConfirmDialog(null, "Product deleted correctly.", "Info", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
    			this.removeRow(rowIndex);

    		} else
    			JOptionPane.showConfirmDialog(null, "Error deleting a product.", "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
			
    	}
	}

}
