package com.spq.group6.client.gui.panels;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.spq.group6.client.controller.ClientController;
import com.spq.group6.client.gui.utils.LanguageSelector;
import com.spq.group6.client.gui.utils.SDG2Util;

public class LocaleSelectorPanel extends JPanel {
    
	private JComboBox<LanguageSelector> selectLanguageCB;
	private JLabel label1;
	private ClientController controller;

	public LocaleSelectorPanel(int screenWidth, int screenHeight) {
		
        setBackground(Color.WHITE);
        this.setLayout(null);
		this.controller = ClientController.getClientController();
		
		label1 = new JLabel(controller.getLanguageMessage("InitialPanel.infoLabel.text"), SwingConstants.CENTER);
		label1.setForeground(Color.WHITE);
		label1.setBackground(new Color(0, 204, 204));
		label1.setOpaque(true);
		label1.setSize(screenWidth, screenHeight / 4);
		label1.setLocation(0, 0);
        SDG2Util.fixJLabelFontSize(label1);
        
        selectLanguageCB = new JComboBox<>(controller.getLanguagesAvailable());
        selectLanguageCB.setForeground(new Color(0, 102, 102));
        selectLanguageCB.setSize((int) (screenWidth / 3.3), screenHeight / 6);
        selectLanguageCB.setLocation(0, 300);
        selectLanguageCB.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                JComboBox<LanguageSelector> comboBox = (JComboBox<LanguageSelector>) e.getSource();
                Locale l = ((LanguageSelector) comboBox.getSelectedItem()).getLocale();
                controller.setLocale(l);
                label1.setText(controller.getLanguageMessage("InitialPanel.infoLabel.text"));
            }
        });  
        
        this.add(label1);
        this.add(selectLanguageCB);
	}
	
	public void bringSelectLanguageCBToFront() {
		selectLanguageCB.repaint();
	}
	
	public static void main(String[] args) {
		JFrame testFrame = new JFrame();
        testFrame.setSize(800, 600);
        testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        testFrame.add(new LocaleSelectorPanel(800, 600));
        testFrame.setVisible(true);
	}

}
