package com.spq.group6.client.gui.panels;

import java.awt.FlowLayout;
import java.awt.LayoutManager;
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
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.github.lgooddatepicker.tableeditors.DateTimeTableEditor;
import com.spq.group6.client.controller.ClientController;
import com.spq.group6.client.gui.ClientWindow;
import com.spq.group6.client.gui.actions.ActionBid;
import com.spq.group6.client.gui.actions.ActionCreateAuction;
import com.spq.group6.client.gui.actions.ActionDeleteProduct;
import com.spq.group6.client.gui.actions.ActionUpdateProduct;
import com.spq.group6.client.gui.elements.AuctionJTableModel;
import com.spq.group6.client.gui.elements.ButtonColumn;
import com.spq.group6.client.gui.elements.MarketJTableModel;
import com.spq.group6.client.gui.utils.SDG2Util;
import com.spq.group6.client.gui.utils.SPQG6Util;
import com.spq.group6.client.gui.utils.ScreenType;
import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.Product;

public class MarketPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private JLabel titleLabel;
	private JLabel infoLabel;
	private JPanel searchPanel;
	private JLabel searchLabel, searchLabel2;
	private JComboBox<String> searchComboBox;
	private JTextField searchTF;
	private JButton searchButton;
	private JScrollPane auctionsTableScrollPane;
	private JTable auctionsTable;
	private JButton backButton;
	private JButton logOutButton;
	
	@SuppressWarnings("unused")
	private ClientController controller;
	
	public MarketPanel(int screenWidth, int screenHeight, ClientController controller) {
		
		this.setLayout(null);
		
		titleLabel = new JLabel("Market", SwingConstants.LEFT);
		titleLabel.setSize(screenWidth / 5, screenHeight / 15);
		titleLabel.setLocation((int) (screenWidth / 15), (int) (screenHeight / 4 - titleLabel.getHeight() / 2));
		SDG2Util.fixJLabelFontSize(titleLabel);	
				
		infoLabel = new JLabel("Here you can see others' auctions. Start bidding!", SwingConstants.LEFT);
		infoLabel.setSize((int) (screenWidth / 1.5), screenHeight / 8);
		infoLabel.setLocation((int) titleLabel.getLocation().getX(), 
				(int) (titleLabel.getLocation().getY() + titleLabel.getHeight()));
		SDG2Util.fixJLabelFontSize(infoLabel);	
				
		// searching filter
		searchLabel = new JLabel("Search for auctions where");
		searchComboBox = new JComboBox<>(new String[] {"Country", "Name"});
		searchLabel2 = new JLabel("is");
		searchTF = new JTextField(10);
		searchButton = new JButton("Search");
		String[] auctionsColumnNames = {"Prod. Name", "Description", "Highest Bid", "Time left", ""};
		searchButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Object[][] auctionsData = null;
				List<Auction> auctions = null;
				switch(searchComboBox.getSelectedIndex()) {
				case 0: 
					auctions = controller.searchAuctionByCountry(searchTF.getText());
					break;
				case 1:
					auctions = controller.searchAuctionByProductName(searchTF.getText());
					break;
				default:
					auctions = new ArrayList<>();
				}
				if (auctions.size() == 0) {
					auctionsData = new Object[][] {};
					JOptionPane.showConfirmDialog(MarketPanel.this, "No auctions found.", "Info", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
				
				} else {
					auctionsData = new Object[auctions.size()][auctionsColumnNames.length];
					int i = 0;
					for (i = 0; i < auctions.size(); i++) {
						Auction tempAuction = auctions.get(i);
						auctionsData[i][0] = tempAuction;
						auctionsData[i][1] = tempAuction.getProduct().getDescription();
						if (tempAuction.getHighestBid() == null)
							auctionsData[i][2] = 0 + " (initial:" + tempAuction.getInitialPrice() + ")";
						else
							auctionsData[i][2] = tempAuction.getHighestBid().getAmount();
						auctionsData[i][3] = SPQG6Util.getLocalDateTimeDifferenceFromNow(tempAuction.getDayLimit().toLocalDateTime());
						auctionsData[i][4] = "Bid";
					}
					JOptionPane.showConfirmDialog(MarketPanel.this, "Auctions found succesfully.", "Info", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);

				}
				auctionsTable.setModel(new MarketJTableModel(auctionsData, auctionsColumnNames, controller));
				@SuppressWarnings("unused")
				ButtonColumn bidButtonColumn = new ButtonColumn(auctionsTable, new ActionBid(), 4);
			}
		});
		searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		searchPanel.setSize((int) (screenWidth / 1.2), screenHeight / 14);
		searchPanel.setLocation((int) infoLabel.getLocation().getX(), 
				(int) (infoLabel.getLocation().getY() + infoLabel.getHeight()));
		
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
					JOptionPane.showConfirmDialog(MarketPanel.this, "Error logging out.", "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);

			}
		});

		auctionsTable = new JTable(new MarketJTableModel(new Object[][] {}, auctionsColumnNames, controller));
		@SuppressWarnings("unused")
		ButtonColumn bidButtonColumn = new ButtonColumn(auctionsTable, new ActionBid(), 4);
		
		// set column 4 to limit day
		auctionsTable.setDefaultEditor(LocalDateTime.class, new DateTimeTableEditor());
		auctionsTable.setDefaultRenderer(LocalDateTime.class, new DateTimeTableEditor());
		auctionsTable.getColumnModel().getColumn(4).setCellEditor(auctionsTable.getDefaultEditor(LocalDateTime.class));
		auctionsTable.getColumnModel().getColumn(4).setCellRenderer(auctionsTable.getDefaultRenderer(LocalDateTime.class));
		
		auctionsTableScrollPane = new JScrollPane(auctionsTable);
		auctionsTableScrollPane.setSize((int) (screenWidth - backButton.getLocation().getX() - (screenWidth - logOutButton.getLocation().getX()) + logOutButton.getWidth()), 
				(int) (screenHeight/2.25));
		auctionsTableScrollPane.setLocation((int) (titleLabel.getLocation().getX()),
				(int) (searchPanel.getLocation().getY() + searchPanel.getHeight()));

		
		this.add(titleLabel);
		this.add(infoLabel);
		searchPanel.add(searchLabel);
		searchPanel.add(searchComboBox);
		searchPanel.add(searchLabel2);
		searchPanel.add(searchTF);
		searchPanel.add(searchButton);
		this.add(searchPanel);
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
