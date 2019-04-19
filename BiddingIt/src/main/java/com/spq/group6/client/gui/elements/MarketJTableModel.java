package com.spq.group6.client.gui.elements;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import com.spq.group6.client.controller.ClientController;
import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.Product;

public class MarketJTableModel extends DefaultTableModel {

	// TODO
	
	private static final long serialVersionUID = 1L;
	private ClientController controller;
	
	public MarketJTableModel(Object[][] data, String[] columnNames, ClientController controller) {
    	super(data, columnNames);
		
		this.controller = controller;
		
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnIndex == 0 || columnIndex == 1 || columnIndex == 2 || columnIndex == 3 ? true : false;
	}

	public void bidAuctionAt(int rowIndex, Auction auction) {
		String bidValue = JOptionPane.showInputDialog(null, 
				"Please enter the amount you want to bid (greater than " + auction.getHighestBid(), "Bid", JOptionPane.QUESTION_MESSAGE);
		if (Float.parseFloat(bidValue) > auction.getHighestBid().getAmount())
			if (controller.bid(auction, Float.parseFloat(bidValue))) {
				JOptionPane.showConfirmDialog(null, "Auction bidded correctly.", "Info", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
				this.removeRow(rowIndex);
	
			} else
				JOptionPane.showConfirmDialog(null, "Error bidding the auction.", "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
		else
			JOptionPane.showConfirmDialog(null, "Error bidding the auction. Amount too small.", "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
	}
	
}
