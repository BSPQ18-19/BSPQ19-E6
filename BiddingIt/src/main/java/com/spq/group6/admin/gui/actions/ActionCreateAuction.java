package com.spq.group6.admin.gui.actions;

import java.awt.event.ActionEvent;
import java.time.LocalDateTime;

import javax.swing.AbstractAction;
import javax.swing.JTable;

import com.spq.group6.client.gui.elements.AuctionJTableModel;
import com.spq.group6.server.data.Product;

public class ActionCreateAuction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	@Override
	public void actionPerformed(ActionEvent e) {
		JTable table = (JTable)e.getSource();
        int modelRow = Integer.valueOf( e.getActionCommand() );
        ((AuctionJTableModel)table.getModel()).createAuction(modelRow,
        		(Product) table.getValueAt(modelRow, 0), (LocalDateTime) table.getValueAt(modelRow, 4),
        		(String) table.getValueAt(modelRow, 1));
	}
}
