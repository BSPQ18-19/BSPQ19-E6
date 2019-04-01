package com.spq.group6.client.gui;

import java.awt.*;
import javax.swing.*;

import com.spq.group6.client.controller.ClientController;

public class ClientWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private static ClientWindow clientWindow;
	private ClientController controller;
	private ScreenType currentScreenType;
	private int screenWidth, screenHeight;
	
	private JPanel mainPanel;

	// private constructor using lazy singleton
	private ClientWindow(ClientController controller) {
		this.controller = controller;
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setTitle("Easybooking client");
		Dimension windowSize = new Dimension((int) (screenSize.getWidth() / 1.3), (int) (screenSize.getHeight() / 1.3));
		this.setSize(windowSize);
		this.setLocationRelativeTo(null);
		mainPanel = (JPanel) this.getContentPane();

		this.screenWidth = (int) windowSize.getWidth();
		this.screenHeight = (int) windowSize.getHeight();
		
		changeScreen(ScreenType.INITIAL);
		
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	public void changeScreen(ScreenType nextScreenType, String... strings) {
		this.currentScreenType = nextScreenType;
		
		switch(nextScreenType) {
		case INITIAL:
			mainPanel = new InitialJPanel(screenWidth, screenHeight);
			break;
		case REGISTER:
			if (strings.length > 0)
				mainPanel = new RegisterJPanel(screenWidth, screenHeight, controller, AuthorizationSystem.GOOGLE, strings[0]);
			else
				mainPanel = new RegisterJPanel(screenWidth, screenHeight, controller, AuthorizationSystem.GOOGLE);

			break;
		case LOG_IN:
			mainPanel = new LogInJPanel(screenWidth, screenHeight, AuthorizationSystem.GOOGLE);
			break;
		case LOG_IN_SUCCESFUL:
			if (strings.length > 0)
				mainPanel = new LogInSuccesfulJPanel(screenWidth, screenHeight, strings[0]);
			else
				mainPanel = new LogInSuccesfulJPanel(screenWidth, screenHeight);

			break;
		default:
			break;
		}
		this.setContentPane(mainPanel);
		this.revalidate();
	}

	public ClientController getController() {
		return controller;
	}

	// lazy singleton
	public static ClientWindow getClientWindow(ClientController clientController) {
		if (clientWindow == null)
			clientWindow = new ClientWindow(clientController);
		return clientWindow;
	}

}