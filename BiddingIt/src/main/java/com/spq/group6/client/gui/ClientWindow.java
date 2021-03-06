package com.spq.group6.client.gui;

import com.spq.group6.client.controller.ClientController;
import com.spq.group6.client.gui.panels.*;
import com.spq.group6.client.gui.utils.ScreenType;
import com.spq.group6.client.gui.utils.locale.LanguageManager;

import javax.swing.*;
import java.awt.*;

public class ClientWindow extends JFrame {

    private static final long serialVersionUID = 1L;
    private static ClientWindow clientWindow;
    private ClientController controller;
    private ScreenType currentScreenType;
    private int screenWidth, screenHeight;

    private JPanel mainPanel;

    /**
     * private constructor using lazy singleton
     */
    private ClientWindow() {

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setTitle(LanguageManager.getMessage("General.clientWindow.title"));
        Dimension windowSize = new Dimension((int) (screenSize.getWidth() / 1.3), (int) (screenSize.getHeight() / 1.3));
        this.setSize(windowSize);
        this.setLocationRelativeTo(null);
        mainPanel = (JPanel) this.getContentPane();

        this.screenWidth = (int) windowSize.getWidth();
        this.screenHeight = (int) windowSize.getHeight();

        changeScreen(ScreenType.INITIAL);

        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    /**
     * lazy singleton
     */
    public static ClientWindow getClientWindow() {
        if (clientWindow == null)
            clientWindow = new ClientWindow();
        return clientWindow;
    }

    /**
     * Method to change the panels of the Client window to access the different options
     *
     * @param nextScreenType name of the panel upcoming
     */
    public void changeScreen(ScreenType nextScreenType, String... data) {
        this.currentScreenType = nextScreenType;

        switch (nextScreenType) {
            case INITIAL:
                mainPanel = new InitialPanel(screenWidth, screenHeight);
                break;
            case REGISTER:
                mainPanel = new RegisterPanel(screenWidth, screenHeight);
                break;
            case LOG_IN:
                mainPanel = new LogInPanel(screenWidth, screenHeight);
                break;
            case LOG_IN_SUCCESFUL:
                mainPanel = new LogInSuccesfulPanel(screenWidth, screenHeight, data[0]);
                break;
            case MAIN_MENU:
                mainPanel = new MainMenuPanel(screenWidth, screenHeight);
                break;
            case MARKET:
                mainPanel = new MarketPanel(screenWidth, screenHeight);
                break;
            case USER_PRODUCTS:
                mainPanel = new UserProductsPanel(screenWidth, screenHeight);
                break;
            case USER_AUCTIONS:
                mainPanel = new UserAuctionsPanel(screenWidth, screenHeight);
                break;
            case USER_ACCOUNT:
                mainPanel = new UserAccountPanel(screenWidth, screenHeight);
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

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void setMainPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
    }
}