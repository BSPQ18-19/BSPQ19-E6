package com.spq.group6.admin.gui.elements;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import com.spq.group6.admin.controller.AdminController;
import com.spq.group6.server.data.User;

import java.util.ArrayList;

public class UserJTableModel extends DefaultTableModel {

	// TODO
	
	private static final long serialVersionUID = 1L;
	private AdminController controller;
	private ArrayList<User> users;

	public UserJTableModel(Object[][] data, String[] columnNames, AdminController controller, ArrayList<User> users) {
    	super(data, columnNames);
		this.controller = controller;
		this.users = users;
	}

	
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnIndex == 4;
	}
	
	public void deleteUserAt(int rowIndex) {
		User user = users.get(rowIndex);
		if (controller.deleteUser(user)) {
			JOptionPane.showConfirmDialog(null, "User deleted correctly.", "Info", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
			this.removeRow(rowIndex);

		} else {
			JOptionPane.showConfirmDialog(null, "Error deleting a auction.", "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
		}
	}
	
}
