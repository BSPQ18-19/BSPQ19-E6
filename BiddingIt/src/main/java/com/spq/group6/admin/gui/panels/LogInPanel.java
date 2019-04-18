package com.spq.group6.admin.gui.panels;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.*;

import com.spq.group6.admin.controller.AdminController;
import com.spq.group6.admin.gui.AdminWindow;
import com.spq.group6.admin.gui.utils.ScreenType;
import com.spq.group6.admin.gui.utils.SDG2Util;


public class LogInPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel titleLabel;
	private JLabel infoLabel;
	private JLabel usernameLabel;
	private JTextField usernameTF;
	private JLabel passwordLabel;
	private JTextField passwordTF;
	private JButton confirmButton;
	
	private AdminController controller;
	
	public LogInPanel(int screenWidth, int screenHeight, AdminController controller) {
		
		this.setLayout(null);
		this.controller = controller;
		
		titleLabel = new JLabel("Easybooking Administration", SwingConstants.LEFT);
		titleLabel.setSize((int) (screenWidth), screenHeight / 7);
		titleLabel.setLocation(screenWidth / 8, (int) (screenHeight / 7 - titleLabel.getHeight() / 2));
		SDG2Util.fixJLabelFontSize(titleLabel);
		
		infoLabel = new JLabel("Please enter your username and password.",
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
		
		confirmButton = new JButton("Confirm");
		confirmButton.setSize(screenWidth / 6, screenHeight / 10);
		confirmButton.setLocation((int) (screenWidth / 1.5), 
				(int) (passwordLabel.getLocation().getY() + passwordLabel.getFont().getSize() + screenHeight / 8));
		SDG2Util.fixJButtonFontSize(confirmButton);
		confirmButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				try {
					if (controller.logIn(usernameTF.getText(), passwordTF.getText()))
						AdminWindow.getAdminWindow(null).changeScreen(ScreenType.LOG_IN_SUCCESFUL, usernameTF.getText());
					else
						JOptionPane.showConfirmDialog(LogInPanel.this, "Error logging in.", "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);

				} catch (RemoteException e1) {

					e1.printStackTrace();
				}
			}
		});
		
		this.add(titleLabel);
		this.add(infoLabel);
		this.add(usernameLabel);
		this.add(usernameTF);
		this.add(passwordLabel);
		this.add(passwordTF);
		this.add(confirmButton);
		
	}
	
	public static void main(String[] args) {
		JFrame testFrame = new JFrame();
		testFrame.setSize(800, 600);
		testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		testFrame.add(new LogInPanel(800, 600, null));
		testFrame.setVisible(true);
	}

}