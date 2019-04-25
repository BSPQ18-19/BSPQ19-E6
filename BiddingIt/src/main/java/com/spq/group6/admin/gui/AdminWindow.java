package com.spq.group6.admin.gui;

import java.awt.*;
import javax.swing.*;

import com.spq.group6.admin.controller.AdminController;
import com.spq.group6.admin.gui.panels.AdminLogInSuccesfulPanel;
import com.spq.group6.admin.gui.panels.LogInPanel;
import com.spq.group6.admin.gui.utils.ScreenType;

public class AdminWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private static AdminWindow adminWindow;
	private AdminController controller;
	private ScreenType currentScreenType;
	private int screenWidth, screenHeight;
	
	private JPanel mainPanel;

	// private constructor using lazy singleton
	private AdminWindow(AdminController controller) {
		this.controller = controller;
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setTitle("Easybooking Admin");
		Dimension windowSize = new Dimension((int) (screenSize.getWidth() / 1.3), (int) (screenSize.getHeight() / 1.3));
		this.setSize(windowSize);
		this.setLocationRelativeTo(null);
		mainPanel = (JPanel) this.getContentPane();

		this.screenWidth = (int) windowSize.getWidth();
		this.screenHeight = (int) windowSize.getHeight();
		
		changeScreen(ScreenType.LOG_IN);
		
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	public void changeScreen(ScreenType nextScreenType, String... data) {
		this.currentScreenType = nextScreenType;
		
		switch(nextScreenType) {
		case LOG_IN:
			mainPanel = new LogInPanel(screenWidth, screenHeight, controller);
			break;
		case LOG_IN_SUCCESFUL:
			mainPanel = new AdminLogInSuccesfulPanel(screenWidth, screenHeight, data[0]);
			break;
//		case MAIN_MENU:
//			mainPanel = new MainMenuPanel(screenWidth, screenHeight, controller);
//			break;	
//		case MARKET:
//			mainPanel = new MarketPanel(screenWidth, screenHeight, controller);
//			break;
//		case USER_PRODUCTS:
//			mainPanel = new UserProductsPanel(screenWidth, screenHeight, controller);
//			break;
//		case USER_AUCTIONS:
//			mainPanel = new UserAuctionsPanel(screenWidth, screenHeight, controller);
//			break;
		default:
			break;
		}
		this.setContentPane(mainPanel);
		this.revalidate();
	}

	public AdminController getController() {
		return controller;
	}

	// lazy singleton
	public static AdminWindow getAdminWindow(AdminController adminController) {
		if (adminWindow == null)
			adminWindow = new AdminWindow(adminController);
		return adminWindow;
	}

}