package com.spq.group6.admin.gui.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import com.spq.group6.admin.controller.AdminController;
import com.spq.group6.admin.gui.AdminWindow;
import com.spq.group6.admin.gui.utils.SDG2Util;
import com.spq.group6.admin.gui.utils.ScreenType;

public class AdminMainMenuPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel titleLabel;
	private JButton AdminUsersButton;
	private JButton AdminAuctionsButton;
	private JButton logOutButton;
	
	private AdminController controller;
	
	public AdminMainMenuPanel(int screenWidth, int screenHeight, AdminController controller) {
		this.controller=controller;
		this.setLayout(null);
		
		titleLabel = new JLabel("BiddingIt Administration", SwingConstants.LEFT);
		titleLabel.setSize((int)(screenWidth * 0.5), screenHeight / 3);
		titleLabel.setLocation((int) (screenWidth * 0.1), (int) (screenHeight / 4 - titleLabel.getHeight() / 1.25));
		SDG2Util.fixJLabelFontSize(titleLabel);
		
		AdminAuctionsButton = new JButton("Administrate Auctions");
		AdminAuctionsButton.setSize(screenWidth / 5, screenHeight / 8);
		AdminAuctionsButton.setLocation((int) (titleLabel.getLocation().getX()), 
				(int) (titleLabel.getLocation().getY() + titleLabel.getFont().getSize() + screenHeight / 5));
		SDG2Util.fixJButtonFontSize(AdminAuctionsButton);
		AdminAuctionsButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				AdminWindow.getAdminWindow(null).changeScreen(ScreenType.ADMIN_AUCTIONS);
			}
		});
		
		AdminUsersButton = new JButton("Administrate Users");		
		AdminUsersButton.setSize(screenWidth / 5, screenHeight / 8);
		AdminUsersButton.setLocation((int) (titleLabel.getLocation().getX() + AdminUsersButton.getSize().getWidth() + screenWidth/ 20), 
				(int) (AdminAuctionsButton.getLocation().getY()));
		SDG2Util.fixJButtonFontSize(AdminUsersButton);
		AdminUsersButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				AdminWindow.getAdminWindow(null).changeScreen(ScreenType.ADMIN_USERS);

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
					AdminWindow.getAdminWindow(null).changeScreen(ScreenType.LOG_IN);
				else
					JOptionPane.showConfirmDialog(AdminMainMenuPanel.this, "Error logging out.", "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);

			}
		});
		
		this.add(titleLabel);
		this.add(AdminUsersButton);
		this.add(AdminAuctionsButton);
		this.add(logOutButton);
	}
	
	public static void main(String[] args) {
		JFrame testFrame = new JFrame();
		testFrame.setSize(800, 600);
		testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		testFrame.add(new AdminMainMenuPanel(800, 600, null));
		testFrame.setVisible(true);
	}

}