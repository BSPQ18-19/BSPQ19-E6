package com.spq.group6.client.gui.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import com.github.lgooddatepicker.tableeditors.DateTimeTableEditor;
import com.spq.group6.client.controller.ClientController;
import com.spq.group6.client.gui.ClientWindow;
import com.spq.group6.client.gui.actions.ActionCreateAuction;
import com.spq.group6.client.gui.elements.ButtonColumn;
import com.spq.group6.client.gui.elements.AuctionJTableModel;
import com.spq.group6.client.gui.utils.SDG2Util;
import com.spq.group6.client.gui.utils.ScreenType;
import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.Product;

public class UserAuctionsPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel titleLabel;
	private JLabel infoLabel;
	private JScrollPane auctionsTableScrollPane;
	private JTable auctionsTable;
	private JButton backButton;
	private JButton logOutButton;
	
	@SuppressWarnings("unused")
	private ClientController controller;
	
	public UserAuctionsPanel(int screenWidth, int screenHeight, ClientController controller) {
		
		this.setLayout(null);
		
		titleLabel = new JLabel("My auctions", SwingConstants.LEFT);
		titleLabel.setSize(screenWidth / 4, screenHeight / 15);
		titleLabel.setLocation((int) (screenWidth / 2 - titleLabel.getWidth()*1.75), (int) (screenHeight / 4 - titleLabel.getHeight() / 2));
		SDG2Util.fixJLabelFontSize(titleLabel);	
				
		infoLabel = new JLabel("Here you can see your auctions. Start selling and check your earnings!", SwingConstants.LEFT);
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
					JOptionPane.showConfirmDialog(UserAuctionsPanel.this, "Error logging out.", "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);

			}
		});

		String[] auctionsColumnNames = {"Prod. Name", "Initial Price", "Highest Bid", "Status", "Day Limit", ""};
		Object[][] auctionsData = null;
		List<Auction> userAuctions = controller.getCurrentUserAuctions();
		if (controller.getCurrentUser() != null) {
			auctionsData = new Object[userAuctions.size() + 1][auctionsColumnNames.length];
			int i = 0;
			for (i = 0; i < userAuctions.size(); i++) {
				Auction tempAuction = userAuctions.get(i);
				auctionsData[i][0] = tempAuction.getProduct().getName();
				auctionsData[i][1] = tempAuction.getInitialPrice();
				if (tempAuction.getHighestBid() == null)
					auctionsData[i][2] = 0;
				else
					auctionsData[i][2] = tempAuction.getHighestBid().getAmount();
				if (tempAuction.isOpen())
					auctionsData[i][3] = "Open";
				else
					auctionsData[i][3] = "Closed";
				auctionsData[i][4] = tempAuction.getDayLimit().toLocalDateTime();
				auctionsData[i][5] = "";
			}
			auctionsData[i][0] = "";
			auctionsData[i][1] = "";
			auctionsData[i][2] = "-";
			auctionsData[i][3] = "-";
			auctionsData[i][4] = "";
			auctionsData[i][5] = "Create";
		}
		auctionsTable = new JTable(new AuctionJTableModel(auctionsData, auctionsColumnNames, controller));
		@SuppressWarnings("unused")
		ButtonColumn createButtonColumn = new ButtonColumn(auctionsTable, new ActionCreateAuction(), 5);
		
		// set column 0 to combobox
		List<Product> userProductsNotAuction = controller.getCurrentUserProducts(); // get all user products		
		for (int i = userProductsNotAuction.size() - 1; i >= 0; i--) // remove products already in an auction
			for (int j = 0; j < userAuctions.size(); j++)
				if (userProductsNotAuction.get(i).equals(userAuctions.get(j).getProduct()))
					userProductsNotAuction.remove(i);
		Product[] userProductsNotAuctionArray = new Product[userProductsNotAuction.size()];
		for (int i = 0; i < userProductsNotAuction.size(); i++)
			userProductsNotAuctionArray[i] = userProductsNotAuction.get(i);
		JComboBox<Product> prodComboBox = new JComboBox<Product>(userProductsNotAuctionArray);
		prodComboBox.setSelectedIndex(0);
		auctionsTable.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(prodComboBox));
		
		// set column 4 to limit day
		auctionsTable.setDefaultEditor(LocalDateTime.class, new DateTimeTableEditor());
		auctionsTable.setDefaultRenderer(LocalDateTime.class, new DateTimeTableEditor());
		auctionsTable.getColumnModel().getColumn(4).setCellEditor(auctionsTable.getDefaultEditor(LocalDateTime.class));
		auctionsTable.getColumnModel().getColumn(4).setCellRenderer(auctionsTable.getDefaultRenderer(LocalDateTime.class));
		
		auctionsTableScrollPane = new JScrollPane(auctionsTable);
		auctionsTableScrollPane.setSize((int) (screenWidth - backButton.getLocation().getX() - (screenWidth - logOutButton.getLocation().getX()) + logOutButton.getWidth()), screenHeight/2);
		auctionsTableScrollPane.setLocation((int) (titleLabel.getLocation().getX()),
				(int) (infoLabel.getLocation().getY() + infoLabel.getHeight()));

		
		this.add(titleLabel);
		this.add(infoLabel);
		this.add(auctionsTableScrollPane);
		this.add(backButton);
		this.add(logOutButton);
	}
	
	public static void main(String[] args) {
		JFrame testFrame = new JFrame();
		testFrame.setSize(800, 600);
		testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		testFrame.add(new UserAuctionsPanel(800, 600, null));
		testFrame.setVisible(true);
	}
}
