package com.spq.group6.client.gui.elements;

import java.sql.Timestamp;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import com.spq.group6.client.controller.ClientController;
import com.spq.group6.server.data.Product;

public class AuctionJTableModel extends DefaultTableModel {

	// TODO
	
	private static final long serialVersionUID = 1L;
	private ClientController controller;
	
	public AuctionJTableModel(Object[][] data, String[] columnNames, ClientController controller) {
    	super(data, columnNames);
		
		this.controller = controller;
		
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return (rowIndex == this.getRowCount() - 1 && columnIndex != 2 && columnIndex != 3) ? true : false;
	}
	
	public void createAuction(int rowIndex, Product product, String dayLimit, String initialPrice) {
		if (controller.createPublicAuction(product, Timestamp.valueOf(dayLimit), Float.parseFloat(initialPrice))) {
			this.setValueAt("", rowIndex, 5);
			this.addRow(new String[] {"", "", "", "", "", "Create"}); // add new row for a new auction
			JOptionPane.showConfirmDialog(null, "Auction created correctly.", "Info", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
		} else
			JOptionPane.showConfirmDialog(null, "Error creating an auction.", "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);	
	}
	
}
