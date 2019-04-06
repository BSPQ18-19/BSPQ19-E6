package com.spq.group6.client.gui.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.*;

import com.spq.group6.client.controller.ClientController;
import com.spq.group6.client.gui.ClientWindow;
import com.spq.group6.client.gui.elements.ButtonColumn;
import com.spq.group6.client.gui.elements.ProductJTableModel;
import com.spq.group6.client.gui.utils.ScreenType;
import com.spq.group6.client.gui.actions.ActionDelete;
import com.spq.group6.server.data.Product;
import com.spq.group6.util.SDG2Util;

public class UserProductsPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	private JLabel titleLabel;
	private JLabel infoLabel;
	private JScrollPane productsTableScrollPane;
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
		
		
//		User user1 = new User("Alejandro", "1234", "ES");
//		Product[] products = new Product[2];
//		products[0] = new Product(user1, "Product1", "desc1");
//		products[1] = new Product(user1, "Product2", "desc2");
//		user1.setOwnedProducts(products);

		List<Product> userProducts = controller.getCurrentUserProducts();
		productsTable = new JTable();
		productsTable.setModel(new ProductJTableModel(controller, userProducts));
		@SuppressWarnings("unused")
		ButtonColumn modifyButtonColumn = new ButtonColumn(productsTable, new ActionDelete(), 2);
		@SuppressWarnings("unused")
		ButtonColumn deleteButtonColumn = new ButtonColumn(productsTable, new ActionDelete(), 3);
		
		productsTableScrollPane = new JScrollPane(productsTable);
		productsTableScrollPane.setSize((int) (screenWidth - backButton.getLocation().getX() - (screenWidth - logOutButton.getLocation().getX()) + logOutButton.getWidth()), screenHeight/2);
		productsTableScrollPane.setLocation((int) (titleLabel.getLocation().getX()),
				(int) (infoLabel.getLocation().getY() + infoLabel.getHeight()));

		
		this.add(titleLabel);
		this.add(infoLabel);
		this.add(productsTableScrollPane);
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
