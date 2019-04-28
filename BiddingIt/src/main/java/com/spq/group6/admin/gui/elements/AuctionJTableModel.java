package com.spq.group6.admin.gui.elements;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import com.spq.group6.admin.controller.AdminController;
import com.spq.group6.server.data.Auction;

import java.util.ArrayList;

public class AuctionJTableModel extends DefaultTableModel {

	// TODO
	
	private static final long serialVersionUID = 1L;
	private AdminController controller;
	private ArrayList<Auction> auctions;

	public AuctionJTableModel(Object[][] data, String[] columnNames, AdminController controller, ArrayList<Auction> auctions) {
    	super(data, columnNames);
		this.controller = controller;
		this.auctions = auctions;
	}
	
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnIndex == 4;
	}
	
	public void deleteAuctionAt(int rowIndex) {
		Auction auction = auctions.get(rowIndex);
		if (controller.deleteAuction(auction)) {
			JOptionPane.showConfirmDialog(null, "Auction deleted correctly.", "Info", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
			this.removeRow(rowIndex);

		} else
			JOptionPane.showConfirmDialog(null, "Error deleting a auction.", "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
	}
	
}
