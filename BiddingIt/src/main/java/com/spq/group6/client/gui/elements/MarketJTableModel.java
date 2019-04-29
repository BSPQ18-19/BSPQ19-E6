package com.spq.group6.client.gui.elements;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import com.spq.group6.client.controller.ClientController;
import com.spq.group6.server.data.Auction;

public class MarketJTableModel extends DefaultTableModel {

	// TODO
	
	private static final long serialVersionUID = 1L;
	private ClientController controller;
	
	public MarketJTableModel(Object[][] data, String[] columnNames) {
    	super(data, columnNames);

		this.controller = ClientController.getClientController();
		
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnIndex == 4 ? true : false;
	}

	public void bidAuctionAt(int rowIndex, Auction auction) {
		float actualHighestBid = 0;
		if (auction.getHighestBid() != null) {
			actualHighestBid = auction.getHighestBid().getAmount();
		} else {
			actualHighestBid = auction.getInitialPrice();
		}
		String bidValue = JOptionPane.showInputDialog(null,
				"Please enter the amount you want to bid (greater than " + actualHighestBid + ")", "Bid", JOptionPane.QUESTION_MESSAGE);
		if (bidValue != null && Float.parseFloat(bidValue) > actualHighestBid)
			if (controller.bid(auction, Float.parseFloat(bidValue))) {
				JOptionPane.showConfirmDialog(null, "Auction bidded correctly.", "Info", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
				this.setValueAt(bidValue, rowIndex, 2);
				
			} else
				JOptionPane.showConfirmDialog(null, "Error bidding the auction.", "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
		else
			JOptionPane.showConfirmDialog(null, "Error bidding the auction. Amount too small.", "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
	}
	
}
