package com.spq.group6.client.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

import com.spq.group6.client.controller.ClientController;
import com.spq.group6.server.data.Product;

public class ProductJTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private ClientController controller;
	private List<Product> productsDisplayed;
	private String[] columnNames = { "Name", "Description", "", ""};
	
	public ProductJTableModel(ClientController controller, List<Product> products) {
    	this.controller = controller;
		
		this.productsDisplayed = new ArrayList<>();
    	this.productsDisplayed.addAll(products);
    	this.productsDisplayed.add(new Product("", ""));
    	 	
	}
	
	@Override
    public String getColumnName(int columnIndex){
         return columnNames[columnIndex];
    }
	
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return productsDisplayed.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Product product = productsDisplayed.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return product.getName();
		case 1:
			return product.getDescription();
		case 2:
			if (rowIndex == productsDisplayed.size() - 1) // last (new) product
				return "Create";
			else
				return "Save";
		case 3:
			if (rowIndex == productsDisplayed.size() - 1) // last (new) product
				return "";
			else
				return "Delete";
		default:
			break;
		}
		return null;
	}
	
	@Override
   public Class<?> getColumnClass(int columnIndex){
      switch (columnIndex){
      case 0:
    	  return String.class;
      case 1:
    	  return String.class;
      case 2:
    	  return String.class;
      case 3:
    	  return String.class;
      default:
    	  break;
      }
      return null;
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnIndex == 0 || columnIndex== 1 ? true : false;
	}
	
	public void modifyProductAt(int rowIndex, String name, String description) {
		Product product = productsDisplayed.get(rowIndex);
		if (rowIndex == productsDisplayed.size() - 1 && product.getName().equals("")) { // last row and new product (create product)
			if (controller.createProduct(product, name, description)) {
				productsDisplayed.add(new Product("", "")); // add new row for a new product
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
    	if (rowIndex != productsDisplayed.size() - 1) { // not last row (new) product
    		Product product = productsDisplayed.get(rowIndex);
    		if (controller.deleteProduct(product)) {
    			JOptionPane.showConfirmDialog(null, "Product deleted correctly.", "Info", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
    			productsDisplayed.remove(rowIndex);

    		} else
    			JOptionPane.showConfirmDialog(null, "Error deleting a product.", "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
			
    	}
	}

}
