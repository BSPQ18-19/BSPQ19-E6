package com.spq.group6.client.gui;

import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

import com.spq.group6.server.data.Product;

public class ProductJTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private ArrayList<Product> products;
	private String[] columnNames = { "ID", "Name", "Description", "", ""};
	
	public ProductJTableModel(ArrayList<Product> products) {
		this.products = products;
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
		return products.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Product product = products.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return product.getProductID();
		case 1:
			return product.getName();
		case 2:
			return product.getDescription();
		case 3:
			if (rowIndex == products.size() - 1) // last (new) product
				return "Create";
			else
				return "Save";
		case 4:
			if (rowIndex == products.size() - 1) // last (new) product
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
    	  return Long.class;
      case 1:
    	  return String.class;
      case 2:
    	  return String.class;
      case 3:
    	  return String.class;
      case 4:
    	  return String.class;
      default:
    	  break;
      }
      return null;
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnIndex ==1 || columnIndex==2 ? true : false;
	}
	
	public void modifyProductAt(int rowIndex, String name, String description) {
		Product product = products.get(rowIndex);
		if (rowIndex == products.size() - 1 && product.getName().equals("")) { // last row and new product
			product.setProductID(productID);
			products.add(new Product(product.getUserID(), "", "")); // to add new products
		}
		product.setName(name);
		product.setDescription(description);
	}

	public void removeRow(int rowIndex) {
		if (rowIndex != products.size() - 1) // not last row (new) product
			products.remove(rowIndex);
	}

}