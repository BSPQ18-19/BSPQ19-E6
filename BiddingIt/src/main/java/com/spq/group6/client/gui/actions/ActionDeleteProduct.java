package com.spq.group6.client.gui.actions;

import com.spq.group6.client.gui.elements.ProductJTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Action class used to delete a product in a event
 */
public class ActionDeleteProduct extends AbstractAction {

    private static final long serialVersionUID = 1L;


    public void actionPerformed(ActionEvent e) {
        JTable table = (JTable) e.getSource();
        int modelRow = Integer.valueOf(e.getActionCommand());
        ((ProductJTableModel) table.getModel()).removeProductAt(modelRow);
    }

}
