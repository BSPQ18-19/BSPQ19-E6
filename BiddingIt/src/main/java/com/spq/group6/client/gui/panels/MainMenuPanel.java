package com.spq.group6.client.gui.panels;

import com.spq.group6.client.gui.ClientWindow;
import com.spq.group6.client.gui.utils.SDG2Util;
import com.spq.group6.client.gui.utils.ScreenType;
import com.spq.group6.client.gui.utils.locale.LanguageManager;
import com.spq.group6.client.gui.utils.voice.VoiceHelper;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenuPanel extends LocaleSelectorPanel {

    private static final long serialVersionUID = 1L;
    private JLabel infoLabel;
    private JButton marketButton;
    private JButton userAccountButton;
    private JButton userProductsButton;
    private JButton userAuctionsButton;
    private JButton logOutButton;

    public MainMenuPanel(int screenWidth, int screenHeight) {

        super(screenWidth, screenHeight);

        titleLabel = new JLabel(LanguageManager.getMessage("MainMenuPanel.titleLabel.text"), SwingConstants.LEFT);
        titleLabel.setForeground(Color.white);
        titleLabel.setBackground(new Color(0, 204, 204));
        titleLabel.setOpaque(true);
        titleLabel.setSize(screenWidth, screenHeight / 3);
        titleLabel.setLocation(0, 0);
        SDG2Util.fixJLabelFontSize(titleLabel);

        infoLabel = new JLabel(LanguageManager.getMessage("MainMenuPanel.infoLabel.text"), SwingConstants.LEFT);
        infoLabel.setForeground(new Color(0, 102, 102));
        infoLabel.setSize((int) (screenWidth / 1.5), screenHeight / 6);
        infoLabel.setLocation(screenWidth / 20,
                (int) (titleLabel.getLocation().getY() + titleLabel.getFont().getSize() + screenHeight / 10));
        SDG2Util.fixJLabelFontSize(infoLabel);

        userAuctionsButton = new JButton(LanguageManager.getMessage("MainMenuPanel.userAuctionsButton.text"));
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


            public void actionPerformed(ActionEvent e) {
                ClientWindow.getClientWindow().changeScreen(ScreenType.USER_AUCTIONS);
            }
        });
        userAuctionsButton.addFocusListener(ttsFocusListener);

        userProductsButton = new JButton(LanguageManager.getMessage("MainMenuPanel.userProductsButton.text"));
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


            public void actionPerformed(ActionEvent e) {
                ClientWindow.getClientWindow().changeScreen(ScreenType.USER_PRODUCTS);
            }
        });
        userProductsButton.addFocusListener(ttsFocusListener);

        marketButton = new JButton(LanguageManager.getMessage("MainMenuPanel.marketButton.text"));
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


            public void actionPerformed(ActionEvent e) {
                ClientWindow.getClientWindow().changeScreen(ScreenType.MARKET);

            }
        });
        marketButton.addFocusListener(ttsFocusListener);

        userAccountButton = new JButton(LanguageManager.getMessage("MainMenuPanel.userAccountButton.text"));
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


            public void actionPerformed(ActionEvent e) {
                ClientWindow.getClientWindow().changeScreen(ScreenType.USER_ACCOUNT);

            }
        });
        userAccountButton.addFocusListener(ttsFocusListener);

        logOutButton = new JButton(LanguageManager.getMessage("MainMenuPanel.logOutButton.text"));
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


            public void actionPerformed(ActionEvent e) {
                if (controller.logOut())
                    ClientWindow.getClientWindow().changeScreen(ScreenType.INITIAL);
                else
                    JOptionPane.showConfirmDialog(MainMenuPanel.this, LanguageManager.getMessage("MainMenuPanel.confirmDialog1"), "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);

            }
        });
        logOutButton.addFocusListener(ttsFocusListener);

        this.add(titleLabel);
        this.add(infoLabel);
        this.add(marketButton);
        this.add(userProductsButton);
        this.add(userAuctionsButton);
        this.add(userAccountButton);
        this.add(logOutButton);

        bringSelectLanguageCBToFront();

        VoiceHelper.textToSpeech(LanguageManager.getMessage("Voice.MainMenuPanel.welcome"));

    }

    public static void main(String[] args) {
        JFrame testFrame = new JFrame();
        testFrame.setSize(800, 600);
        testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        testFrame.add(new MainMenuPanel(800, 600));
        testFrame.setVisible(true);
    }


    protected void updateComponentsText() {
        titleLabel.setText(LanguageManager.getMessage("MainMenuPanel.titleLabel.text"));
        SDG2Util.fixJLabelFontSize(titleLabel);
        infoLabel.setText(LanguageManager.getMessage("MainMenuPanel.infoLabel.text"));
        SDG2Util.fixJLabelFontSize(infoLabel);
        marketButton.setText(LanguageManager.getMessage("MainMenuPanel.marketButton.text"));
        SDG2Util.fixJButtonFontSize(marketButton);
        userProductsButton.setText(LanguageManager.getMessage("MainMenuPanel.userProductsButton.text"));
        SDG2Util.fixJButtonFontSize(userProductsButton);
        userAuctionsButton.setText(LanguageManager.getMessage("MainMenuPanel.userAuctionsButton.text"));
        SDG2Util.fixJButtonFontSize(userAuctionsButton);
        userAccountButton.setText(LanguageManager.getMessage("MainMenuPanel.userAccountButton.text"));
        SDG2Util.fixJButtonFontSize(userAccountButton);
        logOutButton.setText(LanguageManager.getMessage("MainMenuPanel.logOutButton.text"));
        SDG2Util.fixJButtonFontSize(logOutButton);
    }

}
