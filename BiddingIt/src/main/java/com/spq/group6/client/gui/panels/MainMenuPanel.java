package com.spq.group6.client.gui.panels;

import com.spq.group6.client.controller.ClientController;
import com.spq.group6.client.gui.ClientWindow;
import com.spq.group6.client.gui.utils.SDG2Util;
import com.spq.group6.client.gui.utils.ScreenType;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenuPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JLabel titleLabel;
    private JLabel infoLabel;
    private JButton marketButton;
    private JButton userAccountButton;
    private JButton userProductsButton;
    private JButton userAuctionsButton;
    private JButton logOutButton;

    private ClientController controller;

    public MainMenuPanel(int screenWidth, int screenHeight) {

        setBackground(Color.WHITE);
        this.setLayout(null);

        controller = ClientController.getClientController();

        titleLabel = new JLabel("BiddingIt", SwingConstants.LEFT);
        titleLabel.setForeground(Color.white);
        titleLabel.setBackground(new Color(0, 204, 204));
        titleLabel.setOpaque(true);
        titleLabel.setSize(screenWidth, screenHeight / 3);
        titleLabel.setLocation(0, 0);
        SDG2Util.fixJLabelFontSize(titleLabel);

        infoLabel = new JLabel("Welcome to the best bidding system in the world!", SwingConstants.LEFT);
        infoLabel.setForeground(new Color(0, 102, 102));
        infoLabel.setSize((int) (screenWidth / 1.5), screenHeight / 6);
        infoLabel.setLocation(screenWidth / 20,
                (int) (titleLabel.getLocation().getY() + titleLabel.getFont().getSize() + screenHeight / 10));
        SDG2Util.fixJLabelFontSize(infoLabel);

        userAuctionsButton = new JButton("My auctions");
        userAuctionsButton.setForeground(new Color(0, 102, 102));
        userAuctionsButton.setBackground(Color.white);
        userAuctionsButton.setBorder(new TitledBorder(""));
        userAuctionsButton.setContentAreaFilled(false);
        userAuctionsButton.setOpaque(true);
        userAuctionsButton.setSize(screenWidth / 5, screenHeight / 8);
        userAuctionsButton.setLocation(infoLabel.getX(),
                (int) (infoLabel.getLocation().getY() + infoLabel.getFont().getSize() + screenHeight / 5));
        SDG2Util.fixJButtonFontSize(userAuctionsButton);
        userAuctionsButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ClientWindow.getClientWindow().changeScreen(ScreenType.USER_AUCTIONS);
            }
        });

        userProductsButton = new JButton("My products");
        userProductsButton.setForeground(new Color(0, 102, 102));
        userProductsButton.setBackground(Color.white);
        userProductsButton.setBorder(new TitledBorder(""));
        userProductsButton.setContentAreaFilled(false);
        userProductsButton.setOpaque(true);
        userProductsButton.setSize(screenWidth / 5, screenHeight / 8);
        userProductsButton.setLocation(userAuctionsButton.getX(),
                (int) (userAuctionsButton.getLocation().getY() + userProductsButton.getSize().getHeight() + screenHeight / 20));
        SDG2Util.fixJButtonFontSize(userProductsButton);
        userProductsButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ClientWindow.getClientWindow().changeScreen(ScreenType.USER_PRODUCTS);
            }
        });

        marketButton = new JButton("Market");
        marketButton.setForeground(new Color(0, 102, 102));
        marketButton.setBackground(Color.white);
        marketButton.setBorder(new TitledBorder(""));
        marketButton.setContentAreaFilled(false);
        marketButton.setOpaque(true);
        marketButton.setSize(screenWidth / 5, screenHeight / 8);
        marketButton.setLocation((int) (userAuctionsButton.getX() + marketButton.getSize().getWidth() + screenWidth / 20),
                (int) (userAuctionsButton.getLocation().getY()));
        SDG2Util.fixJButtonFontSize(marketButton);
        marketButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ClientWindow.getClientWindow().changeScreen(ScreenType.MARKET);

            }
        });

        userAccountButton = new JButton("Account");
        userAccountButton.setForeground(new Color(0, 102, 102));
        userAccountButton.setBackground(Color.white);
        userAccountButton.setBorder(new TitledBorder(""));
        userAccountButton.setContentAreaFilled(false);
        userAccountButton.setOpaque(true);
        userAccountButton.setSize(screenWidth / 5, screenHeight / 8);
        SDG2Util.fixJButtonFontSize(userAccountButton);
        userAccountButton.setLocation(marketButton.getX(),
                (int) (userProductsButton.getLocation().getY()));
        userAccountButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ClientWindow.getClientWindow().changeScreen(ScreenType.USER_ACCOUNT);

            }
        });

        logOutButton = new JButton("Log out");
        logOutButton.setForeground(new Color(0, 102, 102));
        logOutButton.setBackground(Color.white);
        logOutButton.setBorder(new TitledBorder(""));
        logOutButton.setContentAreaFilled(false);
        logOutButton.setOpaque(true);
        logOutButton.setSize(screenWidth / 8, screenHeight / 15);
        logOutButton.setLocation(screenWidth / 2 + 2 * logOutButton.getWidth(),
                (int) (screenHeight / 1.2));
        SDG2Util.fixJButtonFontSize(logOutButton);
        logOutButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (controller.logOut())
                    ClientWindow.getClientWindow().changeScreen(ScreenType.INITIAL);
                else
                    JOptionPane.showConfirmDialog(MainMenuPanel.this, "Error logging out.", "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);

            }
        });

        this.add(titleLabel);
        this.add(infoLabel);
        this.add(marketButton);
        this.add(userProductsButton);
        this.add(userAuctionsButton);
        this.add(userAccountButton);
        this.add(logOutButton);
    }

    public static void main(String[] args) {
        JFrame testFrame = new JFrame();
        testFrame.setSize(800, 600);
        testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        testFrame.add(new MainMenuPanel(800, 600));
        testFrame.setVisible(true);
    }

}
