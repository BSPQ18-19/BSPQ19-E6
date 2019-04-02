package com.spq.group6.client.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.spq.group6.client.controller.ClientController;
import com.spq.group6.util.SDG2Util;

public class RegisterPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel titleLabel;
	private JLabel title2Label;
	private JLabel infoLabel;
	private JLabel usernameLabel;
	private JTextField usernameTF;
	//private JLabelGraficoAjustado usernametickImage;
	private JLabel emailLabel;
	private JTextField emailTF;
	//private JLabelGraficoAjustado emailtickImage;
	private JLabel countryLabel;
	private JTextField countryTF;
	private JButton confirmButton;
	
	private ClientController controller;
	
	public RegisterPanel(int screenWidth, int screenHeight, ClientController controller) {
		
		this.setLayout(null);
		this.controller = controller;
		
		titleLabel = new JLabel("Easybooking", SwingConstants.LEFT);
		titleLabel.setSize((int) (screenWidth / 3), screenHeight / 7);
		titleLabel.setLocation(screenWidth / 8, (int) (screenHeight / 7 - titleLabel.getHeight() / 2));
		SDG2Util.fixJLabelFontSize(titleLabel);
		
		title2Label = new JLabel("You don't have an account. Please register first.", SwingConstants.LEFT);
		title2Label.setSize((int) (screenWidth / 1.2), screenHeight / 6);
		title2Label.setLocation(screenWidth / 7, (int) (titleLabel.getLocation().getY() + (titleLabel.getHeight()*0.7)));
		SDG2Util.fixJLabelFontSize(title2Label);
		
		infoLabel = new JLabel("<html>Please enter a username and your email:<br/>(the one associated to your Google/Facebook account)</html>",
				SwingConstants.LEFT);
		infoLabel.setSize((int) (screenWidth / 1.3), screenHeight / 6);
		infoLabel.setLocation(screenWidth / 7,
				(int) (titleLabel.getLocation().getY() + titleLabel.getHeight() + screenHeight / 20));
		infoLabel.setFont(new Font("Arial", Font.PLAIN, screenHeight/30));
		
		usernameLabel = new JLabel("Username:", SwingConstants.LEFT);
		usernameLabel.setSize((int) (screenWidth / 5), screenHeight / 20);
		usernameLabel.setLocation((int) infoLabel.getLocation().getX(),
				(int) (infoLabel.getLocation().getY() + infoLabel.getHeight() + screenHeight / 30));
		SDG2Util.fixJLabelFontSize(usernameLabel);

		usernameTF = new JTextField();
		usernameTF.setSize(screenWidth / 4, screenHeight / 20);
		usernameTF.setLocation((int) usernameLabel.getLocation().getX() + usernameLabel.getWidth(),
				(int) usernameLabel.getLocation().getY());
		usernameTF.setFont(new Font("Arial", Font.PLAIN, (int) (usernameLabel.getFont().getSize() / 1.25)));
		usernameTF.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {checkInput(true);}
			
			@Override
			public void insertUpdate(DocumentEvent e) {checkInput(true);}
			
			@Override
			public void changedUpdate(DocumentEvent e) {checkInput(true);}
		});
		
		/*
		usernametickImage = new JLabelGraficoAjustado("media/error.png", usernameTF.getHeight(), usernameTF.getHeight());
		usernametickImage.setLocation((int) (usernameTF.getLocation().getX() + usernameTF.getWidth() + screenWidth/75),
				usernameTF.getLocation().getY());
		*/
		emailLabel = new JLabel("Email:", SwingConstants.LEFT);
		emailLabel.setSize(usernameLabel.getSize());
		emailLabel.setLocation((int) usernameLabel.getLocation().getX(),
				(int) (usernameLabel.getLocation().getY() + usernameLabel.getHeight() + screenHeight / 40));
		emailLabel.setFont(usernameLabel.getFont());
		
		emailTF = new JTextField();
		emailTF.setSize(usernameTF.getSize());
		emailTF.setLocation((int) usernameTF.getLocation().getX(), 
				(int) emailLabel.getLocation().getY());
		emailTF.setFont(usernameTF.getFont());
		emailTF.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {checkInput(false);}
			
			@Override
			public void insertUpdate(DocumentEvent e) {checkInput(false);}
			
			@Override
			public void changedUpdate(DocumentEvent e) {checkInput(false);}
		});
		
		/*
		emailtickImage = new JLabelGraficoAjustado("/error.png", usernametickImage.getHeight(), usernametickImage.getHeight());
		emailtickImage.setLocation(usernametickImage.getLocation().getX(), emailTF.getLocation().getY());
		*/
		
		countryLabel = new JLabel("Select your default airport:", SwingConstants.LEFT);
		countryLabel.setSize(emailLabel.getWidth()*2, emailLabel.getHeight());
		countryLabel.setLocation((int) emailLabel.getLocation().getX(),
				(int) (emailLabel.getLocation().getY() + emailLabel.getHeight() + screenHeight / 40));
		countryLabel.setFont(emailLabel.getFont());
		
		countryTF = new JTextField();
		countryTF.setSize(usernameTF.getSize());
		countryTF.setLocation((int) countryLabel.getLocation().getX(),
				(int) (countryLabel.getLocation().getY() + countryLabel.getHeight() + screenHeight / 40));
		countryLabel.setFont(usernameTF.getFont());
		
		confirmButton = new JButton("Confirm");
		confirmButton.setSize(screenWidth / 6, screenHeight / 10);
		confirmButton.setLocation((int) (screenWidth / 1.5), 
				(int) (emailLabel.getLocation().getY() + emailLabel.getFont().getSize() + screenHeight / 8));
		SDG2Util.fixJButtonFontSize(confirmButton);
		confirmButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				try {
					controller.signIn(usernameTF.getText(), emailTF.getText(), countryTF.getText());
					ClientWindow.getClientWindow(null).changeScreen(ScreenType.LOG_IN_SUCCESFUL);
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				/*
				if (usernametickImage.getName().equals("media/checked.png") &&
						emailtickImage.getName().equals("media/checked.png")) {
					controller.registerUser(usernameTF.getText(), emailTF.getText(), (String) defaultAirportComboBox.getSelectedItem(), authSystem);
					ClientWindow.getClientWindow(null).changeScreen(ScreenType.LOG_IN_SUCCESFUL);
					// send server to register user, confirm and go to main menu
				} else if (!usernametickImage.getName().equals("media/checked.png") &&
						emailtickImage.getName().equals("media/checked.png")) {
					JOptionPane.showMessageDialog(RegisterJPanel.this, "Username is already taken.", "Error", JOptionPane.ERROR_MESSAGE);
				} else if (usernametickImage.getName().equals("media/checked.png") &&
						!emailtickImage.getName().equals("media/checked.png")) {
					JOptionPane.showMessageDialog(RegisterJPanel.this, "Email is already taken.", "Error", JOptionPane.ERROR_MESSAGE);
				} else if (!usernametickImage.getName().equals("media/checked.png") &&
						!emailtickImage.getName().equals("media/checked.png")) {
					JOptionPane.showMessageDialog(RegisterJPanel.this, "Username and email are already taken.", "Error", JOptionPane.ERROR_MESSAGE);
				}
				*/
			}
		});
		
		this.add(titleLabel);
		this.add(title2Label);
		this.add(infoLabel);
		this.add(usernameLabel);
		this.add(usernameTF);
		// this.add(usernametickImage);
		this.add(emailLabel);
		this.add(emailTF);
		// this.add(emailtickImage);
		this.add(countryLabel);
		this.add(countryTF);
		this.add(confirmButton);
	}
	
	// param isUsername is true when checking the usernametf and false when checking the emailtf
	private void checkInput(boolean isUsername) {
		/*
		if (isUsername) {
			String trimmedUsernameTFText = usernameTF.getText().trim();
			if (!trimmedUsernameTFText.equals("") && controller.existsUsername(trimmedUsernameTFText))
				usernametickImage = new JLabelGraficoAjustado("media/error.png", usernameTF.getHeight(), usernameTF.getHeight());
			else
				usernametickImage = new JLabelGraficoAjustado("media/checked.png", usernameTF.getHeight(), usernameTF.getHeight());
			usernametickImage.repaint();
		} else {
			String trimmedEmailTFText = emailTF.getText().trim();
			if (!trimmedEmailTFText.equals("") && controller.existsEmail(trimmedEmailTFText))
				emailtickImage = new JLabelGraficoAjustado("media/error.png", usernametickImage.getHeight(), usernametickImage.getHeight());
			else
				emailtickImage = new JLabelGraficoAjustado("media/checked.png", usernametickImage.getHeight(), usernametickImage.getHeight());
			emailtickImage.repaint();
		}
		System.out.println(usernametickImage.getName() + " " + emailtickImage.getName());
		*/
	}
	
	public static void main(String[] args) {
		JFrame testFrame = new JFrame();
		testFrame.setSize(800, 600);
		testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		testFrame.add(new RegisterPanel(800, 600, null));
		testFrame.setVisible(true);
	}
}