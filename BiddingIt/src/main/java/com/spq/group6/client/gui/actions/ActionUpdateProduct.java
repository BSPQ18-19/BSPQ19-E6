package com.spq.group6.client.gui.actions;

import com.spq.group6.client.gui.elements.ProductJTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ActionUpdateProduct extends AbstractAction {

    private static final long serialVersionUID = 1L;

    @Override
    public void actionPerformed(ActionEvent e) {
        JTable table = (JTable) e.getSource();
        int modelRow = Integer.valueOf(e.getActionCommand());
        ((ProductJTableModel) table.getModel()).updateProductAt(modelRow,
                (String) table.getValueAt(modelRow, 0), (String) table.getValueAt(modelRow, 1));
    }

}
