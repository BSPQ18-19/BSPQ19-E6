package com.spq.group6.client.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import sd.group2.SDG2Util;

public class InitialPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel titleLabel;
	private JLabel infoLabel;
	private JLabel authorLabel;
	private JButton authorizationGoogleButton;
	private JButton authorizationFacebookButton;
	
	public InitialJPanel(int screenWidth, int screenHeight) {
		
		this.setLayout(null);
		
		titleLabel = new JLabel("Easybooking", SwingConstants.CENTER);
		titleLabel.setSize(screenWidth / 2, screenHeight / 2);
		titleLabel.setLocation(screenWidth / 2 - titleLabel.getWidth() / 2, screenHeight / 4 - titleLabel.getHeight() / 2);
		SDG2Util.fixJLabelFontSize(titleLabel);	
				
		infoLabel = new JLabel("Welcome. Please sign in.", SwingConstants.CENTER);
		infoLabel.setSize((int) (screenWidth / 3.3), screenHeight / 6);
		infoLabel.setLocation(screenWidth / 2 - infoLabel.getWidth() / 2, 
				(int) (titleLabel.getLocation().getY() + titleLabel.getFont().getSize() + screenHeight / 7));
		SDG2Util.fixJLabelFontSize(infoLabel);	
		
		authorLabel = new JLabel("SD Group 2", SwingConstants.CENTER);
		authorLabel.setSize(screenWidth / 8, screenHeight / 15);
		authorLabel.setLocation((int) (screenWidth / 1.25 - authorLabel.getWidth() / 2),
				(int) (screenHeight / 1.25 - authorLabel.getHeight() / 2));
		SDG2Util.fixJLabelFontSize(authorLabel);	
		
		authorizationGoogleButton = new JButton("Google");		
		authorizationGoogleButton.setSize(screenWidth / 6, screenHeight / 8);
		authorizationGoogleButton.setLocation(screenWidth / 2 - authorizationGoogleButton.getWidth() / 2, 
				(int) (infoLabel.getLocation().getY() + infoLabel.getFont().getSize() + screenHeight / 8));
		SDG2Util.fixJButtonFontSize(authorizationGoogleButton);
		authorizationGoogleButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ClientWindow.getClientWindow(null).changeScreen(ScreenType.LOG_IN);
			}
		});
		
		authorizationFacebookButton = new JButton("Facebook");
		authorizationFacebookButton.setSize(screenWidth / 6, screenHeight / 8);
		authorizationFacebookButton.setLocation(screenWidth / 2 - authorizationFacebookButton.getWidth() / 2, 
				(int) (authorizationGoogleButton.getLocation().getY() + authorizationGoogleButton.getSize().getHeight() + screenHeight / 20));
		SDG2Util.fixJButtonFontSize(authorizationFacebookButton);
		authorizationFacebookButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("a");
				JOptionPane.showMessageDialog(InitialJPanel.this, "This feature is not available yet, sorry.", "Info", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		this.add(titleLabel);
		this.add(infoLabel);
		this.add(authorLabel);
		this.add(authorizationGoogleButton);
		this.add(authorizationFacebookButton);
	}
	
	public static void main(String[] args) {
		JFrame testFrame = new JFrame();
		testFrame.setSize(800, 600);
		testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		testFrame.add(new InitialJPanel(800, 600));
		testFrame.setVisible(true);
	}

}