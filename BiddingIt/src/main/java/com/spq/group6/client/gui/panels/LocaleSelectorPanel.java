package com.spq.group6.client.gui.panels;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.spq.group6.client.controller.ClientController;
import com.spq.group6.client.gui.utils.LanguageSelector;

public class LocaleSelectorPanel extends JPanel {
    
	private static final long serialVersionUID = 1L;
	private JComboBox<LanguageSelector> selectLanguageCB;
	protected JLabel titleLabel;
	protected ClientController controller;

	public LocaleSelectorPanel(int screenWidth, int screenHeight) {
		
        setBackground(Color.WHITE);
        this.setLayout(null);
		this.controller = ClientController.getClientController();
		
//		titleLabel = new JLabel(controller.getLanguageMessage("asdads"), SwingConstants.CENTER);
//		titleLabel.setForeground(Color.WHITE);
//		titleLabel.setBackground(new Color(0, 204, 204));
//		titleLabel.setOpaque(true);
//		titleLabel.setSize(screenWidth, screenHeight / 4);
//		titleLabel.setLocation(0, 0);
//      SDG2Util.fixJLabelFontSize(titleLabel);
        
        selectLanguageCB = new JComboBox<>(controller.getLanguagesAvailable());
        selectLanguageCB.setForeground(new Color(0, 204, 204));
        selectLanguageCB.setBackground(Color.WHITE);
        selectLanguageCB.setOpaque(true);
        selectLanguageCB.setSize((int) (screenWidth / 8), screenHeight / 15);
        selectLanguageCB.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                @SuppressWarnings("unchecked")
				JComboBox<LanguageSelector> comboBox = (JComboBox<LanguageSelector>) e.getSource();
                Locale l = ((LanguageSelector) comboBox.getSelectedItem()).getLocale();
                controller.setLocale(l);
                titleLabel.setText(controller.getLanguageMessage("InitialPanel.infoLabel.text"));
                LocaleSelectorPanel.this.bringSelectLanguageCBToFront();

            }
        });  
        
        this.add(selectLanguageCB);
	}
	
	public void bringSelectLanguageCBToFront() {
		this.remove(titleLabel);
        this.remove(selectLanguageCB);
        selectLanguageCB.setLocation((int) (titleLabel.getWidth() / 1.25), titleLabel.getHeight() / 2 - selectLanguageCB.getHeight() / 2);
        this.repaint();
        this.add(selectLanguageCB);
        this.add(titleLabel);
	}
	
	public static void main(String[] args) {
		JFrame testFrame = new JFrame();
        testFrame.setSize(800, 600);
        testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        testFrame.add(new LocaleSelectorPanel(800, 600));
        testFrame.setVisible(true);
	}

}
