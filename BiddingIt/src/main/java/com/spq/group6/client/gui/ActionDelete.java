package com.spq.group6.client.gui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JTable;

public class ActionDelete extends AbstractAction {

	@Override
	public void actionPerformed(ActionEvent e) {
		JTable table = (JTable)e.getSource();
        int modelRow = Integer.valueOf( e.getActionCommand() );
        ((ProductJTableModel)table.getModel()).removeRow(modelRow);
	}

}
