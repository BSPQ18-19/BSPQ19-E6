package com.spq.group6.client.gui.actions;

import java.awt.event.ActionEvent;
import java.time.LocalDateTime;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import com.spq.group6.client.gui.elements.AuctionJTableModel;
import com.spq.group6.client.gui.panels.UserAuctionsPanel;
import com.spq.group6.server.data.Product;

public class ActionCreateAuction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	@Override
	public void actionPerformed(ActionEvent e) {
		JTable table = (JTable)e.getSource();
        int modelRow = Integer.valueOf( e.getActionCommand() );
        try {
        ((AuctionJTableModel)table.getModel()).createAuction(modelRow,
        		(Product) table.getValueAt(modelRow, 0), (LocalDateTime) table.getValueAt(modelRow, 4),
        		(String) table.getValueAt(modelRow, 1));
        } catch (Exception ex) {
			JOptionPane.showConfirmDialog(table, "Error. Please fill all the information: product, initial price and day limit", "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
        }
	}
}
