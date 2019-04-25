package com.spq.group6.admin.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JTable;

import com.spq.group6.admin.gui.elements.UserJTableModel;

public class ActionDeleteUser extends AbstractAction {
	
	private static final long serialVersionUID = 1L;

	@Override
	public void actionPerformed(ActionEvent e) {
		JTable table = (JTable)e.getSource();
        int modelRow = Integer.valueOf( e.getActionCommand() );
        ((UserJTableModel)table.getModel()).deleteUserAt(modelRow);
	}
}
