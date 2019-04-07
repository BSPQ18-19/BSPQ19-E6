package com.spq.group6.client.gui.actions;

import com.spq.group6.client.gui.elements.ProductJTableModel;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JTable;

public class ActionModify extends AbstractAction {

	@Override
	public void actionPerformed(ActionEvent e) {
		JTable table = (JTable)e.getSource();
        int modelRow = Integer.valueOf( e.getActionCommand() );
        ((ProductJTableModel)table.getModel()).modifyProductAt(modelRow,
        		(String) table.getValueAt(modelRow, 1), (String) table.getValueAt(modelRow, 2));
	}

}
