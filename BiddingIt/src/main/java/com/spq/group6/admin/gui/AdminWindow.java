package com.spq.group6.admin.gui;

import com.spq.group6.admin.controller.AdminController;
import com.spq.group6.admin.gui.panels.*;
import com.spq.group6.admin.gui.utils.ScreenType;

import javax.swing.*;
import java.awt.*;

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
        this.setTitle("Easybooking AdminMain");
        Dimension windowSize = new Dimension((int) (screenSize.getWidth() / 1.3), (int) (screenSize.getHeight() / 1.3));
        this.setSize(windowSize);
        this.setLocationRelativeTo(null);
        mainPanel = (JPanel) this.getContentPane();

        this.screenWidth = (int) windowSize.getWidth();
        this.screenHeight = (int) windowSize.getHeight();

        changeScreen(ScreenType.LOG_IN);

        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    // lazy singleton
    public static AdminWindow getAdminWindow(AdminController adminController) {
        if (adminWindow == null)
            adminWindow = new AdminWindow(adminController);
        return adminWindow;
    }

    public void changeScreen(ScreenType nextScreenType, String... data) {
        this.currentScreenType = nextScreenType;

        switch (nextScreenType) {
            case LOG_IN:
                mainPanel = new AdminLogInPanel(screenWidth, screenHeight, controller);
                break;
            case LOG_IN_SUCCESFUL:
                mainPanel = new AdminLogInSuccesfulPanel(screenWidth, screenHeight, data[0]);
                break;
            case MAIN_MENU:
                mainPanel = new AdminMainMenuPanel(screenWidth, screenHeight, controller);
                break;
            case ADMIN_USERS:
                mainPanel = new AdminUsersPanel(screenWidth, screenHeight, controller);
                break;
            case ADMIN_AUCTIONS:
                mainPanel = new AdminAuctionsPanel(screenWidth, screenHeight, controller);
                break;
            default:
                break;
        }
        this.setContentPane(mainPanel);
        this.revalidate();
    }

    public AdminController getController() {
        return controller;
    }

}