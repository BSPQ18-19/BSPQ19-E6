package com.spq.group6.admin.gui.elements;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import com.spq.group6.admin.controller.AdminController;
import com.spq.group6.server.data.Auction;

public class AuctionJTableModel extends DefaultTableModel {

	// TODO
	
	private static final long serialVersionUID = 1L;
	private AdminController controller;
	
	public AuctionJTableModel(Object[][] data, String[] columnNames, AdminController controller) {
    	super(data, columnNames);
		
		this.controller = controller;
		
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnIndex == 4 ? true : false;
	}
	
	public void deleteAuctionAt(int rowIndex) {
    	if (rowIndex != this.getRowCount() - 1) { // not last row (new) product
    		Auction auction = controller.getAllAuctions().get(rowIndex);
    		if (controller.deleteAuction(auction)) {
    			JOptionPane.showConfirmDialog(null, "Auction deleted correctly.", "Info", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
    			this.removeRow(rowIndex);

    		} else
    			JOptionPane.showConfirmDialog(null, "Error deleting a auction.", "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
			
    	}
	}
	
}
