package com.spq.group6.client.gui.panels;

import com.spq.group6.client.gui.ClientWindow;
import com.spq.group6.client.gui.utils.SDG2Util;
import com.spq.group6.client.gui.utils.ScreenType;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InitialPanel extends LocaleSelectorPanel {

    private static final long serialVersionUID = 1L;
    private JLabel infoLabel;
    private JLabel authorLabel;
    private JButton logInButton;
    private JButton signInButton;

    public InitialPanel(int screenWidth, int screenHeight) {

    	super(screenHeight, screenHeight);

        titleLabel = new JLabel(controller.getLanguageMessage("InitialPanel.titleLabel.text"), SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBackground(new Color(0, 204, 204));
        titleLabel.setOpaque(true);
        titleLabel.setSize(screenWidth, screenHeight / 4);
        titleLabel.setLocation(0, 0);
        SDG2Util.fixJLabelFontSize(titleLabel);

        infoLabel = new JLabel(controller.getLanguageMessage("InitialPanel.infoLabel.text"), SwingConstants.CENTER);
        infoLabel.setForeground(new Color(0, 102, 102));
        infoLabel.setSize((int) (screenWidth / 3.3), screenHeight / 6);
        infoLabel.setLocation(screenWidth / 2 - infoLabel.getWidth() / 2,
                (int) (titleLabel.getLocation().getY() + titleLabel.getFont().getSize() + screenHeight / 5));
        SDG2Util.fixJLabelFontSize(infoLabel);

        authorLabel = new JLabel(controller.getLanguageMessage("InitialPanel.authorLabel.text"), SwingConstants.CENTER);
        authorLabel.setForeground(new Color(0, 102, 102));
        authorLabel.setSize(screenWidth / 8, screenHeight / 15);
        authorLabel.setLocation((int) (screenWidth / 1.25 - authorLabel.getWidth() / 2),
                (int) (screenHeight / 1.25 - authorLabel.getHeight() / 2));
        SDG2Util.fixJLabelFontSize(authorLabel);

        logInButton = new JButton(controller.getLanguageMessage("InitialPanel.logInButton.text"));
        logInButton.setForeground(new Color(0, 102, 102));
        logInButton.setBackground(Color.white);
        logInButton.setBorder(new TitledBorder(""));
        logInButton.setContentAreaFilled(false);
        logInButton.setOpaque(true);
        logInButton.setSize(screenWidth / 5, screenHeight / 8);
        logInButton.setLocation(screenWidth / 2 - logInButton.getWidth() / 2,
                (int) (infoLabel.getLocation().getY() + infoLabel.getFont().getSize() + screenHeight / 7));
        SDG2Util.fixJButtonFontSize(logInButton);
        logInButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ClientWindow.getClientWindow().changeScreen(ScreenType.LOG_IN);
            }
        });
        logInButton.addFocusListener(ttsFocusListener);

        signInButton = new JButton(controller.getLanguageMessage("InitialPanel.signInButton.text"));
        signInButton.setForeground(new Color(0, 102, 102));
        signInButton.setBackground(Color.white);
        signInButton.setBorder(new TitledBorder(""));
        signInButton.setContentAreaFilled(false);
        signInButton.setOpaque(true);
        signInButton.setSize(screenWidth / 5, screenHeight / 8);
        signInButton.setLocation(screenWidth / 2 - signInButton.getWidth() / 2,
                (int) (logInButton.getLocation().getY() + signInButton.getSize().getHeight() + screenHeight / 15));
        SDG2Util.fixJButtonFontSize(signInButton);
        signInButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ClientWindow.getClientWindow().changeScreen(ScreenType.REGISTER);
            }
        });
        signInButton.addFocusListener(ttsFocusListener);
        
        this.add(titleLabel);
        this.add(infoLabel);
        this.add(authorLabel);
        this.add(logInButton);
        this.add(signInButton);
        
        bringSelectLanguageCBToFront();
        
        controller.sayText("Welcome to Bidding It! This is aun auction system. You can sell and bid other people's products."
        		+ " If you want to disable this beatiful voice, you can turn it off by pressing the sound ON button. You can also change"
        		+ "the language. Enjoy!");
        controller.sayText("You currently are in the initial menu.");
    }

    public static void main(String[] args) {
        JFrame testFrame = new JFrame();
        testFrame.setSize(800, 600);
        testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        testFrame.add(new InitialPanel(800, 600));
        testFrame.setVisible(true);
    }

}