package com.spq.group6.admin.gui.panels;

import com.spq.group6.admin.controller.AdminController;
import com.spq.group6.admin.gui.AdminWindow;
import com.spq.group6.admin.gui.utils.SDG2Util;
import com.spq.group6.admin.gui.utils.ScreenType;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminMainMenuPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JLabel titleLabel;
    private JButton AdminUsersButton;
    private JButton AdminAuctionsButton;
    private JButton logOutButton;

    private AdminController controller;

    public AdminMainMenuPanel(int screenWidth, int screenHeight, AdminController controller) {
        this.controller = controller;
        this.setLayout(null);

        titleLabel = new JLabel("BiddingIt Administration", SwingConstants.CENTER);
        titleLabel.setForeground(Color.white);
        titleLabel.setBackground(new Color(0, 204, 204));
        titleLabel.setOpaque(true);
        titleLabel.setSize(screenWidth, screenHeight / 7);
        titleLabel.setLocation(0, 0);
        SDG2Util.fixJLabelFontSize(titleLabel);

        AdminAuctionsButton = new JButton("Administrate Auctions");
        AdminAuctionsButton.setForeground(new Color(0, 102, 102));
        AdminAuctionsButton.setBackground(Color.white);
        AdminAuctionsButton.setBorder(new TitledBorder(""));
        AdminAuctionsButton.setContentAreaFilled(false);
        AdminAuctionsButton.setOpaque(true);
        AdminAuctionsButton.setSize(screenWidth / 5, screenHeight / 8);
        AdminAuctionsButton.setLocation((int) (titleLabel.getLocation().getX()) + screenWidth/10,
                (int) (titleLabel.getLocation().getY() + titleLabel.getFont().getSize() + screenHeight / 5));
        SDG2Util.fixJButtonFontSize(AdminAuctionsButton);
        AdminAuctionsButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                AdminWindow.getAdminWindow(null).changeScreen(ScreenType.ADMIN_AUCTIONS);
            }
        });

        AdminUsersButton = new JButton("Administrate Users");
        AdminUsersButton.setForeground(new Color(0, 102, 102));
        AdminUsersButton.setBackground(Color.white);
        AdminUsersButton.setBorder(new TitledBorder(""));
        AdminUsersButton.setContentAreaFilled(false);
        AdminUsersButton.setOpaque(true);
        AdminUsersButton.setSize(screenWidth / 5, screenHeight / 8);
        AdminUsersButton.setLocation((int) (titleLabel.getLocation().getX() + AdminUsersButton.getSize().getWidth() + screenWidth/5),
                (int) (AdminAuctionsButton.getLocation().getY()));
        SDG2Util.fixJButtonFontSize(AdminUsersButton);
        AdminUsersButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                AdminWindow.getAdminWindow(null).changeScreen(ScreenType.ADMIN_USERS);

            }
        });

        logOutButton = new JButton("Log out");
        logOutButton.setForeground(new Color(0, 102, 102));
        logOutButton.setBackground(Color.white);
        logOutButton.setBorder(new TitledBorder(""));
        logOutButton.setContentAreaFilled(false);
        logOutButton.setOpaque(true);
        logOutButton.setSize(screenWidth / 8, screenHeight / 15);
        logOutButton.setLocation((int) (screenWidth - logOutButton.getWidth() * 1.5),
                (int) screenHeight / 5);
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