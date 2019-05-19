package com.spq.group6.client.gui.panels;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import com.spq.group6.client.controller.ClientController;
import com.spq.group6.client.gui.utils.locale.LanguageSelector;

public class LocaleSelectorPanel extends JPanel {
    
	private static final long serialVersionUID = 1L;
	protected ClientController controller;
	protected JLabel titleLabel;
	private JComboBox<LanguageSelector> selectLanguageCB;
	protected FocusListener ttsFocusListener;
	protected JToggleButton ttsButton;

	public LocaleSelectorPanel(int screenWidth, int screenHeight) {
		
        setBackground(Color.WHITE);
        this.setLayout(null);
		this.controller = ClientController.getClientController();
		
		ttsFocusListener = new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
					
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				if (e.getSource() instanceof JButton) {
			        controller.sayText("Button " + ((JButton) e.getSource()).getText());
			        
				} else if (e.getSource() instanceof JToggleButton) {
			        controller.sayText("Toggle button. Currently selected: " + ((JToggleButton) e.getSource()).getText());
				
				} else if (e.getSource() instanceof JComboBox<?>) {
			        controller.sayText("Combo box. Currently selected: " + ((JComboBox<?>) e.getSource()).getSelectedItem());

				} else if (e.getSource() instanceof JTextField) {
			        controller.sayText("Text input  " + ((JTextField) e.getSource()).getName().replaceAll("TF", ""));
					
				}
			}
		};
        
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
        selectLanguageCB.addFocusListener(ttsFocusListener);
        
        ttsButton = new JToggleButton("Sound ON", true);
        ttsButton.setForeground(new Color(0, 204, 204));
        ttsButton.setBackground(Color.WHITE);
        ttsButton.setOpaque(true);
        ttsButton.setSize((int) (screenWidth / 8), screenHeight / 15);
        ttsButton.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (ttsButton.isSelected()) {
					ttsButton.setText("Sound ON");
				} else {
					ttsButton.setText("Sound OFF");
				}
				controller.setTtsState(ttsButton.isSelected());

			}
		});
        ttsButton.addFocusListener(ttsFocusListener);
        
        this.add(selectLanguageCB);
        this.add(ttsButton);
	}
	
	public void bringSelectLanguageCBToFront() {
		this.remove(titleLabel);
        this.remove(selectLanguageCB);
        this.remove(ttsButton);
        ttsButton.setLocation((int) (titleLabel.getWidth() / 1.4), titleLabel.getHeight() / 2 - selectLanguageCB.getHeight() / 2);
        selectLanguageCB.setLocation((int) (titleLabel.getWidth() / 1.25), titleLabel.getHeight() / 2 - selectLanguageCB.getHeight() / 2);
        this.repaint();
        this.add(ttsButton);
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
