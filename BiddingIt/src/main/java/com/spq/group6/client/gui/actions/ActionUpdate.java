package com.spq.group6.client.gui.actions;

import com.spq.group6.client.gui.elements.ProductJTableModel;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JTable;

public class ActionUpdate extends AbstractAction {

	@Override
	public void actionPerformed(ActionEvent e) {
		JTable table = (JTable)e.getSource();
        int modelRow = Integer.valueOf( e.getActionCommand() );
        System.out.println("asd");
        ((ProductJTableModel)table.getModel()).updateProductAt(modelRow,
        		(String) table.getValueAt(modelRow, 0), (String) table.getValueAt(modelRow, 1));
	}

}
