package com.spq.group6.client.gui.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import com.spq.group6.client.Client;
import com.spq.group6.client.controller.ClientController;
import com.spq.group6.client.gui.ClientWindow;
import com.spq.group6.client.gui.utils.SDG2Util;
import com.spq.group6.client.gui.utils.ScreenType;

public class MainMenuPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel titleLabel;
	private JLabel infoLabel;
	private JButton marketButton;
	private JButton userProductsButton;
	private JButton userAuctionsButton;
	private JButton logOutButton;
	
	private ClientController controller;
	
	public MainMenuPanel(int screenWidth, int screenHeight) {
		
		this.setLayout(null);

		controller = ClientController.getClientController();

		titleLabel = new JLabel("BiddingIt", SwingConstants.LEFT);
		titleLabel.setSize(screenWidth / 3, screenHeight / 3);
		titleLabel.setLocation((int) (screenWidth / 2 - titleLabel.getWidth()*1.25), (int) (screenHeight / 4 - titleLabel.getHeight() / 1.25));
		SDG2Util.fixJLabelFontSize(titleLabel);
				
		infoLabel = new JLabel("Welcome to the best bidding system in the world!", SwingConstants.LEFT);
		infoLabel.setSize((int) (screenWidth / 1.5), screenHeight / 6);
		infoLabel.setLocation((int) titleLabel.getLocation().getX(), 
				(int) (titleLabel.getLocation().getY() + titleLabel.getFont().getSize() + screenHeight / 10));
		SDG2Util.fixJLabelFontSize(infoLabel);	
		
		userAuctionsButton = new JButton("My auctions");
		userAuctionsButton.setSize(screenWidth / 5, screenHeight / 8);
		userAuctionsButton.setLocation((int) (titleLabel.getLocation().getX()), 
				(int) (infoLabel.getLocation().getY() + infoLabel.getFont().getSize() + screenHeight / 5));
		SDG2Util.fixJButtonFontSize(userAuctionsButton);
		userAuctionsButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ClientWindow.getClientWindow().changeScreen(ScreenType.USER_AUCTIONS);
			}
		});
		
		userProductsButton = new JButton("My products");
		userProductsButton.setSize(screenWidth / 5, screenHeight / 8);
		userProductsButton.setLocation((int) (userAuctionsButton.getLocation().getX()), 
				(int) (userAuctionsButton.getLocation().getY() + userProductsButton.getSize().getHeight() + screenHeight / 20));
		SDG2Util.fixJButtonFontSize(userProductsButton);
		userProductsButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ClientWindow.getClientWindow().changeScreen(ScreenType.USER_PRODUCTS);
			}
		});
		
		marketButton = new JButton("Market");		
		marketButton.setSize(screenWidth / 5, screenHeight / 8);
		marketButton.setLocation((int) (titleLabel.getLocation().getX() + marketButton.getSize().getWidth() + screenWidth/ 20), 
				(int) (userAuctionsButton.getLocation().getY()));
		SDG2Util.fixJButtonFontSize(marketButton);
		marketButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ClientWindow.getClientWindow().changeScreen(ScreenType.MARKET);

			}
		});
		
		logOutButton = new JButton("Log out");
		logOutButton.setSize(screenWidth / 8, screenHeight / 15);
		logOutButton.setLocation((int) (screenWidth / 2 + 2*logOutButton.getWidth()), 
				(int) (screenHeight / 10));
		SDG2Util.fixJButtonFontSize(logOutButton);
		logOutButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (controller.logOut())
					ClientWindow.getClientWindow().changeScreen(ScreenType.INITIAL);
				else
					JOptionPane.showConfirmDialog(MainMenuPanel.this, "Error logging out.", "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);

			}
		});
		
		this.add(titleLabel);
		this.add(infoLabel);
		this.add(marketButton);
		this.add(userProductsButton);
		this.add(userAuctionsButton);
		this.add(logOutButton);
	}
	
	public static void main(String[] args) {
		JFrame testFrame = new JFrame();
		testFrame.setSize(800, 600);
		testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		testFrame.add(new MainMenuPanel(800, 600));
		testFrame.setVisible(true);
	}

}
