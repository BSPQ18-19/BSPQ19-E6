package com.spq.group6.client.gui.panels;

import com.spq.group6.client.gui.ClientWindow;
import com.spq.group6.client.gui.utils.SDG2Util;
import com.spq.group6.client.gui.utils.ScreenType;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InitialPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JLabel titleLabel;
    private JLabel infoLabel;
    private JLabel authorLabel;
    private JButton logInButton;
    private JButton signInButton;

    public InitialPanel(int screenWidth, int screenHeight) {

        setBackground(Color.white);
        this.setLayout(null);

        titleLabel = new JLabel("BiddingIt", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBackground(new Color(0, 204, 204));
        titleLabel.setOpaque(true);
        titleLabel.setSize(screenWidth, screenHeight / 4);
        titleLabel.setLocation(0, 0);
        SDG2Util.fixJLabelFontSize(titleLabel);

        infoLabel = new JLabel("Welcome. Please sign in.", SwingConstants.CENTER);
        infoLabel.setForeground(new Color(0, 102, 102));
        infoLabel.setSize((int) (screenWidth / 3.3), screenHeight / 6);
        infoLabel.setLocation(screenWidth / 2 - infoLabel.getWidth() / 2,
                (int) (titleLabel.getLocation().getY() + titleLabel.getFont().getSize() + screenHeight / 5));
        SDG2Util.fixJLabelFontSize(infoLabel);

        authorLabel = new JLabel("SPQ Group 6", SwingConstants.CENTER);
        authorLabel.setForeground(new Color(0, 102, 102));
        authorLabel.setSize(screenWidth / 8, screenHeight / 15);
        authorLabel.setLocation((int) (screenWidth / 1.25 - authorLabel.getWidth() / 2),
                (int) (screenHeight / 1.25 - authorLabel.getHeight() / 2));
        SDG2Util.fixJLabelFontSize(authorLabel);

        logInButton = new JButton("Log in");
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

        signInButton = new JButton("Sign in");
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

        this.add(titleLabel);
        this.add(infoLabel);
        this.add(authorLabel);
        this.add(logInButton);
        this.add(signInButton);
    }

    public static void main(String[] args) {
        JFrame testFrame = new JFrame();
        testFrame.setSize(800, 600);
        testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        testFrame.add(new InitialPanel(800, 600));
        testFrame.setVisible(true);
    }

}