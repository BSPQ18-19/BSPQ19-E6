package com.spq.group6.admin.gui.panels;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import com.github.lgooddatepicker.tableeditors.DateTimeTableEditor;
import com.spq.group6.admin.controller.AdminController;
import com.spq.group6.admin.gui.AdminWindow;
import com.spq.group6.admin.gui.actions.ActionDeleteUser;
import com.spq.group6.admin.gui.elements.ButtonColumn;
import com.spq.group6.admin.gui.elements.UserJTableModel;
import com.spq.group6.admin.gui.utils.SDG2Util;
import com.spq.group6.admin.gui.utils.ScreenType;
import com.spq.group6.admin.utils.logger.AdminLogger;
import com.spq.group6.server.data.User;

public class AdminUsersPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private JLabel titleLabel;
	private JPanel searchPanel;
	private JButton searchButton;
	private JScrollPane usersTableScrollPane;
	private JTable usersTable;
	private JButton backButton;
	private JButton logOutButton;
	
	@SuppressWarnings("unused")
	private AdminController controller;
	private ArrayList<Thread> usersTimeLeftThread;
	
	public AdminUsersPanel(int screenWidth, int screenHeight, AdminController controller) {
		
		this.setLayout(null);
		
		titleLabel = new JLabel("Administrating users...", SwingConstants.LEFT);
		titleLabel.setSize((int)(screenWidth * 0.5), screenHeight / 15);
		titleLabel.setLocation((int) (screenWidth / 15), (int) (screenHeight / 4 - titleLabel.getHeight() / 2));
		SDG2Util.fixJLabelFontSize(titleLabel);	
		
		Thread animationThread = new Thread(new Runnable() {
			int dots = 0;
			
			@Override
			public void run() {
				
				while(AdminUsersPanel.this.isEnabled()) {
					
					if (dots++ == 3)
						dots = 0;
					
					String tempText = titleLabel.getText().substring(0, 20);
					for (int i = 0; i < dots; i++)
						tempText += ".";
					titleLabel.setText(tempText);
					
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		animationThread.start();	
				
		// searching filter
		searchButton = new JButton("Search");
		String[] usersColumnNames = {"Username", "Password", "Country", "Money", ""};
		searchButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// stop previous threads
				if (usersTimeLeftThread != null)
					for (int i = 0; i < usersTimeLeftThread.size(); i++)
						usersTimeLeftThread.get(i).interrupt();
				
				Object[][] usersData = null;
				List<User> users = controller.getAllUsers();
				if (users.size() == 0) {
					usersData = new Object[][] {};
					JOptionPane.showConfirmDialog(AdminUsersPanel.this, "No auctions found.", "Info", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
				
				} else {
					usersData = new Object[users.size()][usersColumnNames.length];
					int i = 0;
					usersTimeLeftThread = new ArrayList<>();
					for (i = 0; i < users.size(); i++) {
						User tempUser = users.get(i);
						usersData[i][0] = tempUser.getUsername();
						usersData[i][1] = tempUser.getPassword();
						usersData[i][2] = tempUser.getCountry();
						usersData[i][3] = tempUser.getMoney();
						usersData[i][4] = "Delete User";
					}
					JOptionPane.showConfirmDialog(AdminUsersPanel.this, "Users found succesfully.", "Info", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);

				}
				usersTable.setModel(new UserJTableModel(usersData, usersColumnNames, controller));
				usersTable.getColumnModel().getColumn(3).setPreferredWidth(usersTable.getColumnModel().getColumn(3).getPreferredWidth()+100);

				@SuppressWarnings("unused")
				ButtonColumn bidButtonColumn = new ButtonColumn(usersTable, new ActionDeleteUser(), 4);
				
				// start countdown threads
				for (int i = 0; i < usersData.length; i++)
					usersTimeLeftThread.get(i).start();
			}
		});
		searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		searchPanel.setSize((int) (screenWidth / 1.2), screenHeight / 14);
		searchPanel.setLocation((int) titleLabel.getLocation().getX(), 
				(int) (titleLabel.getLocation().getY() + titleLabel.getHeight()));
		
		backButton = new JButton("Back");		
		backButton.setSize(screenWidth / 8, screenHeight / 15);
		backButton.setLocation(backButton.getWidth()/2, 
				(int) (screenHeight / 10));
		SDG2Util.fixJButtonFontSize(backButton);
		backButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				AdminWindow.getAdminWindow(null).changeScreen(ScreenType.MAIN_MENU);
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
					AdminWindow.getAdminWindow(null).changeScreen(ScreenType.LOG_IN);
				else
					JOptionPane.showConfirmDialog(AdminUsersPanel.this, "Error logging out.", "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);

			}
		});

		usersTable = new JTable(new UserJTableModel(new Object[][] {}, usersColumnNames, controller));
		usersTable.getColumnModel().getColumn(3).setPreferredWidth(usersTable.getColumnModel().getColumn(3).getPreferredWidth()+100);

		// set column 4 to limit day
		usersTable.setDefaultEditor(LocalDateTime.class, new DateTimeTableEditor());
		usersTable.setDefaultRenderer(LocalDateTime.class, new DateTimeTableEditor());
		usersTable.getColumnModel().getColumn(4).setCellEditor(usersTable.getDefaultEditor(LocalDateTime.class));
		usersTable.getColumnModel().getColumn(4).setCellRenderer(usersTable.getDefaultRenderer(LocalDateTime.class));
		
		usersTableScrollPane = new JScrollPane(usersTable);
		usersTableScrollPane.setSize((int) (screenWidth - backButton.getLocation().getX() - (screenWidth - logOutButton.getLocation().getX()) + logOutButton.getWidth()), 
				(int) (screenHeight/2.25));
		usersTableScrollPane.setLocation((int) (titleLabel.getLocation().getX()),
				(int) (searchPanel.getLocation().getY() + searchPanel.getHeight()));

		
		this.add(titleLabel);
		searchPanel.add(searchButton);
		this.add(searchPanel);
		this.add(usersTableScrollPane);
		this.add(backButton);
		this.add(logOutButton);
	}
	
	public static void main(String[] args) {
		JFrame testFrame = new JFrame();
		testFrame.setSize(800, 600);
		testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		testFrame.add(new AdminUsersPanel(800, 600, null));
		testFrame.setVisible(true);
	}
	
}