package com.spq.group6.admin.gui.actions;

import com.spq.group6.admin.gui.elements.UserJTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Action class used to delete a user in a event
 */
public class ActionDeleteUser extends AbstractAction {

    private static final long serialVersionUID = 1L;

    public void actionPerformed(ActionEvent e) {
        JTable table = (JTable) e.getSource();
        int modelRow = Integer.valueOf(e.getActionCommand());
        ((UserJTableModel) table.getModel()).deleteUserAt(modelRow);
    }
}
