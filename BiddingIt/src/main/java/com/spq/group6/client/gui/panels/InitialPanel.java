/*
 * Created by JFormDesigner on Tue May 07 22:59:27 CEST 2019
 */

package com.spq.group6.client.gui.panels;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import com.spq.group6.client.gui.ClientWindow;
import com.spq.group6.client.gui.utils.ScreenType;

/**
 * @author aratz
 */
public class InitialPanel extends JPanel {
    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - aratz
    private JLabel label1;
    private JButton buttonSign;
    private JButton buttonLog;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    public InitialPanel() {
        initComponents();
    }

    private void buttonSignActionPerformed(ActionEvent e) {
        ClientWindow.getClientWindow().changeScreen(ScreenType.REGISTER);
    }

    private void buttonLogActionPerformed(ActionEvent e) {
        ClientWindow.getClientWindow().changeScreen(ScreenType.LOG_IN);
    }

    private void buttonSignFocusGained(FocusEvent e) {
        // TODO add your code here
    }

    private void buttonSignFocusLost(FocusEvent e) {
        // TODO add your code here
    }

    private void buttonLogFocusLost(FocusEvent e) {
        // TODO add your code here
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - aratz
        label1 = new JLabel();
        buttonSign = new JButton();
        buttonLog = new JButton();

        //======== this ========
        setBackground(Color.white);

        // JFormDesigner evaluation mark
        setBorder(new javax.swing.border.CompoundBorder(
            new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0),
                "JFormDesigner Evaluation", javax.swing.border.TitledBorder.CENTER,
                javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12),
                java.awt.Color.red), getBorder())); addPropertyChangeListener(new java.beans.PropertyChangeListener(){public void propertyChange(java.beans.PropertyChangeEvent e){if("border".equals(e.getPropertyName()))throw new RuntimeException();}});

        setLayout(new FormLayout(
            "14*(pref:grow, [5px,pref]:grow)",
            "5*(pref:grow, [5px,pref]:grow), pref:grow, 3*([5px,pref], pref)"));

        //---- label1 ----
        label1.setText("BiddingIt!");
        label1.setHorizontalAlignment(SwingConstants.CENTER);
        label1.setIcon(null);
        label1.setFont(label1.getFont().deriveFont(label1.getFont().getSize() + 6f));
        label1.setForeground(Color.white);
        label1.setBackground(new Color(0, 204, 204));
        label1.setOpaque(true);
        add(label1, CC.xywh(1, 1, 28, 4));

        //---- buttonSign ----
        buttonSign.setText("Signin");
        buttonSign.setBackground(Color.white);
        buttonSign.setFont(buttonSign.getFont().deriveFont(buttonSign.getFont().getSize() + 4f));
        buttonSign.setForeground(new Color(0, 102, 102));
        buttonSign.setBorder(new TitledBorder(""));
        buttonSign.setOpaque(false);
        buttonSign.addActionListener(e -> buttonSignActionPerformed(e));
        buttonSign.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                buttonSignFocusGained(e);
            }
            @Override
            public void focusLost(FocusEvent e) {
                buttonSignFocusLost(e);
            }
        });
        add(buttonSign, CC.xywh(3, 7, 10, 6));

        //---- buttonLog ----
        buttonLog.setText("Login");
        buttonLog.setBackground(Color.white);
        buttonLog.setForeground(new Color(0, 102, 102));
        buttonLog.setFont(buttonLog.getFont().deriveFont(buttonLog.getFont().getSize() + 4f));
        buttonLog.setBorder(new TitledBorder(""));
        buttonLog.setAutoscrolls(true);
        buttonLog.setOpaque(false);
        buttonLog.addActionListener(e -> buttonLogActionPerformed(e));
        add(buttonLog, CC.xywh(15, 7, 11, 6));
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }


}
