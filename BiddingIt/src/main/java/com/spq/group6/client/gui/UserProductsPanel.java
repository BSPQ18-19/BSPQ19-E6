package com.spq.group6.client.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.table.TableColumnModel;

import com.spq.group6.client.controller.ClientController;
import com.spq.group6.util.SDG2Util;

public class UserProductsPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	private JLabel titleLabel;
	private JLabel infoLabel;
	private JTable productsTable;
	private JButton backButton;
	private JButton logOutButton;
	
	private ClientController controller;
	
	public UserProductsPanel(int screenWidth, int screenHeight, ClientController controller) {
		
		this.setLayout(null);
		
		titleLabel = new JLabel("My products", SwingConstants.LEFT);
		titleLabel.setSize(screenWidth / 4, screenHeight / 15);
		titleLabel.setLocation((int) (screenWidth / 2 - titleLabel.getWidth()*1.75), (int) (screenHeight / 4 - titleLabel.getHeight() / 2));
		SDG2Util.fixJLabelFontSize(titleLabel);	
				
		infoLabel = new JLabel("<html>Here you can see your products. Start selling and check your earnings!"
				+ "<br/>You can edit the name and description and then click save.<br/>You can also delete your products.</html>", SwingConstants.LEFT);
		infoLabel.setSize((int) (screenWidth / 1.5), screenHeight / 8);
		infoLabel.setLocation((int) titleLabel.getLocation().getX(), 
				(int) (titleLabel.getLocation().getY() + titleLabel.getHeight()));
		SDG2Util.fixJLabelFontSize(infoLabel);	
				
		backButton = new JButton("Back");		
		backButton.setSize(screenWidth / 8, screenHeight / 15);
		backButton.setLocation(backButton.getWidth()/2, 
				(int) (screenHeight / 10));
		SDG2Util.fixJButtonFontSize(backButton);
		backButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ClientWindow.getClientWindow(null).changeScreen(ScreenType.MAIN_MENU);
			}
		});
		
		logOutButton = new JButton("Log out");
		logOutButton.setSize(backButton.getSize());
		logOutButton.setLocation((int) (screenWidth - logOutButton.getWidth()*1.5), 
				(int) backButton.getLocation().getY());
		SDG2Util.fixJButtonFontSize(logOutButton);
		logOutButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (controller.logOut())
					ClientWindow.getClientWindow(null).changeScreen(ScreenType.INITIAL);
				else
					JOptionPane.showConfirmDialog(UserProductsPanel.this, "Error logging out.", "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);

			}
		});
		
		productsTable = new JTable(new String[][] {}, new String[] {"ID", "Name", "Description", "", ""});
		productsTable.setSize((int) (screenWidth - backButton.getLocation().getX() - (screenWidth - logOutButton.getLocation().getX()) + logOutButton.getWidth()), screenHeight/2);
		productsTable.setLocation((int) (titleLabel.getLocation().getX()),
				(int) (infoLabel.getLocation().getY() + infoLabel.getHeight()));
		
		this.add(titleLabel);
		this.add(infoLabel);
		this.add(productsTable);
		this.add(backButton);
		this.add(logOutButton);
	}
	
	public static void main(String[] args) {
		JFrame testFrame = new JFrame();
		testFrame.setSize(800, 600);
		testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		testFrame.add(new UserProductsPanel(800, 600, null));
		testFrame.setVisible(true);
	}
	
}
