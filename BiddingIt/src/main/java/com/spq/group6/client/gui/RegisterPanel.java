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
	private JLabel infoLabel;
	private JLabel usernameLabel;
	private JTextField usernameTF;
	//private JLabelGraficoAjustado usernametickImage;
	private JLabel passwordLabel;
	private JTextField passwordTF;
	//private JLabelGraficoAjustado passwordtickImage;
	private JLabel countryLabel;
	private JTextField countryTF;
	private JButton confirmButton;
	private JButton cancelButton;
	
	private ClientController controller;
	
	public RegisterPanel(int screenWidth, int screenHeight, ClientController controller) {
		
		this.setLayout(null);
		this.controller = controller;
		
		titleLabel = new JLabel("Easybooking", SwingConstants.LEFT);
		titleLabel.setSize((int) (screenWidth / 3), screenHeight / 7);
		titleLabel.setLocation(screenWidth / 8, (int) (screenHeight / 7 - titleLabel.getHeight() / 2));
		SDG2Util.fixJLabelFontSize(titleLabel);
		
		infoLabel = new JLabel("<html>Please enter a username, a password<br/>and the country you live in.</html>",
				SwingConstants.LEFT);
		infoLabel.setSize((int) (screenWidth / 1.3), screenHeight / 6);
		infoLabel.setLocation(screenWidth / 7,
				(int) (titleLabel.getLocation().getY() + titleLabel.getFont().getSize()*1.5));
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
		passwordLabel = new JLabel("Password:", SwingConstants.LEFT);
		passwordLabel.setSize(usernameLabel.getSize());
		passwordLabel.setLocation((int) usernameLabel.getLocation().getX(),
				(int) (usernameLabel.getLocation().getY() + usernameLabel.getHeight() + screenHeight / 40));
		passwordLabel.setFont(usernameLabel.getFont());
		
		passwordTF = new JTextField();
		passwordTF.setSize(usernameTF.getSize());
		passwordTF.setLocation((int) usernameTF.getLocation().getX(), 
				(int) passwordLabel.getLocation().getY());
		passwordTF.setFont(usernameTF.getFont());
		passwordTF.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {checkInput(false);}
			
			@Override
			public void insertUpdate(DocumentEvent e) {checkInput(false);}
			
			@Override
			public void changedUpdate(DocumentEvent e) {checkInput(false);}
		});
		
		/*
		passwordtickImage = new JLabelGraficoAjustado("/error.png", usernametickImage.getHeight(), usernametickImage.getHeight());
		passwordtickImage.setLocation(usernametickImage.getLocation().getX(), passwordTF.getLocation().getY());
		*/
		
		countryLabel = new JLabel("Country:", SwingConstants.LEFT);
		countryLabel.setSize(passwordLabel.getWidth()*2, passwordLabel.getHeight());
		countryLabel.setLocation((int) passwordLabel.getLocation().getX(),
				(int) (passwordLabel.getLocation().getY() + passwordLabel.getHeight() + screenHeight / 40));
		countryLabel.setFont(passwordLabel.getFont());
		
		countryTF = new JTextField();
		countryTF.setSize(usernameTF.getSize());
		countryTF.setLocation((int) usernameTF.getLocation().getX(),
				(int) (countryLabel.getLocation().getY()));
		countryTF.setFont(usernameTF.getFont());
		
		confirmButton = new JButton("Confirm");
		confirmButton.setSize(screenWidth / 6, screenHeight / 10);
		confirmButton.setLocation((int) (screenWidth / 1.5), 
				(int) (passwordLabel.getLocation().getY() + passwordLabel.getFont().getSize() + screenHeight / 8));
		SDG2Util.fixJButtonFontSize(confirmButton);
		confirmButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				try {
					controller.signIn(usernameTF.getText(), passwordTF.getText(), countryTF.getText());
					ClientWindow.getClientWindow(null).changeScreen(ScreenType.LOG_IN_SUCCESFUL);
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				/*
				if (usernametickImage.getName().equals("media/checked.png") &&
						passwordtickImage.getName().equals("media/checked.png")) {
					controller.registerUser(usernameTF.getText(), passwordTF.getText(), (String) defaultAirportComboBox.getSelectedItem(), authSystem);
					ClientWindow.getClientWindow(null).changeScreen(ScreenType.LOG_IN_SUCCESFUL);
					// send server to register user, confirm and go to main menu
				} else if (!usernametickImage.getName().equals("media/checked.png") &&
						passwordtickImage.getName().equals("media/checked.png")) {
					JOptionPane.showMessageDialog(RegisterJPanel.this, "Username is already taken.", "Error", JOptionPane.ERROR_MESSAGE);
				} else if (usernametickImage.getName().equals("media/checked.png") &&
						!passwordtickImage.getName().equals("media/checked.png")) {
					JOptionPane.showMessageDialog(RegisterJPanel.this, "password is already taken.", "Error", JOptionPane.ERROR_MESSAGE);
				} else if (!usernametickImage.getName().equals("media/checked.png") &&
						!passwordtickImage.getName().equals("media/checked.png")) {
					JOptionPane.showMessageDialog(RegisterJPanel.this, "Username and password are already taken.", "Error", JOptionPane.ERROR_MESSAGE);
				}
				*/
			}
		});
		
		cancelButton = new JButton("cancel");
		cancelButton.setSize(screenWidth / 5, screenHeight / 10);
		cancelButton.setLocation((int) (screenWidth / 6), (int) confirmButton.getLocation().getY());
		SDG2Util.fixJButtonFontSize(cancelButton);
		cancelButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ClientWindow.getClientWindow(null).changeScreen(ScreenType.INITIAL);
			}
		});
		
		this.add(titleLabel);
		this.add(infoLabel);
		this.add(usernameLabel);
		this.add(usernameTF);
		// this.add(usernametickImage);
		this.add(passwordLabel);
		this.add(passwordTF);
		// this.add(passwordtickImage);
		this.add(countryLabel);
		this.add(countryTF);
		this.add(confirmButton);
		this.add(cancelButton);

	}
	
	// param isUsername is true when checking the usernametf and false when checking the passwordtf
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
			String trimmedpasswordTFText = passwordTF.getText().trim();
			if (!trimmedpasswordTFText.equals("") && controller.existspassword(trimmedpasswordTFText))
				passwordtickImage = new JLabelGraficoAjustado("media/error.png", usernametickImage.getHeight(), usernametickImage.getHeight());
			else
				passwordtickImage = new JLabelGraficoAjustado("media/checked.png", usernametickImage.getHeight(), usernametickImage.getHeight());
			passwordtickImage.repaint();
		}
		System.out.println(usernametickImage.getName() + " " + passwordtickImage.getName());
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