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
import java.rmi.RemoteException;


public class LogInPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JLabel titleLabel;
    private JLabel infoLabel;
    private JLabel usernameLabel;
    private JTextField usernameTF;
    private JLabel passwordLabel;
    private JTextField passwordTF;
    private JButton confirmButton;
    private JButton cancelButton;

    private ClientController controller;

    public LogInPanel(int screenWidth, int screenHeight) {

        setBackground(Color.white);
        setLayout(null);
        this.controller = ClientController.getClientController();

        titleLabel = new JLabel("Easybooking", SwingConstants.LEFT);
        titleLabel.setForeground(Color.white);
        titleLabel.setBackground(new Color(0, 204, 204));
        titleLabel.setOpaque(true);
        titleLabel.setSize(screenWidth, screenHeight / 7);
        titleLabel.setLocation(0, 0);
        SDG2Util.fixJLabelFontSize(titleLabel);

        infoLabel = new JLabel("Please enter your username and password.",
                SwingConstants.LEFT);
        infoLabel.setForeground(new Color(0, 102, 102));
        infoLabel.setSize((int) (screenWidth / 1.3), screenHeight / 6);
        infoLabel.setLocation(screenWidth / 7,
                (int) (titleLabel.getLocation().getY() + titleLabel.getFont().getSize() * 1.5));
        infoLabel.setFont(new Font("Arial", Font.PLAIN, screenHeight / 30));

        usernameLabel = new JLabel("Username:", SwingConstants.LEFT);
        usernameLabel.setForeground(new Color(0, 102, 102));
        usernameLabel.setSize(screenWidth / 5, screenHeight / 20);
        usernameLabel.setLocation((int) infoLabel.getLocation().getX(),
                (int) (infoLabel.getLocation().getY() + infoLabel.getHeight() + screenHeight / 30));
        SDG2Util.fixJLabelFontSize(usernameLabel);

        usernameTF = new JTextField();
        usernameTF.setBackground(Color.white);
        usernameTF.setBorder(new TitledBorder(""));
        usernameTF.setSize(screenWidth / 4, screenHeight / 20);
        usernameTF.setLocation((int) usernameLabel.getLocation().getX() + usernameLabel.getWidth(),
                (int) usernameLabel.getLocation().getY());
        usernameTF.setFont(new Font("Arial", Font.PLAIN, (int) (usernameLabel.getFont().getSize() / 1.5)));

        passwordLabel = new JLabel("Password:", SwingConstants.LEFT);
        passwordLabel.setForeground(new Color(0, 102, 102));
        passwordLabel.setSize(usernameLabel.getSize());
        passwordLabel.setLocation((int) usernameLabel.getLocation().getX(),
                (int) (usernameLabel.getLocation().getY() + usernameLabel.getHeight() + screenHeight / 40));
        passwordLabel.setFont(usernameLabel.getFont());

        passwordTF = new JTextField();
        passwordTF.setBackground(Color.white);
        passwordTF.setBorder(new TitledBorder(""));
        passwordTF.setSize(usernameTF.getSize());
        passwordTF.setLocation((int) usernameTF.getLocation().getX(),
                (int) passwordLabel.getLocation().getY());
        passwordTF.setFont(usernameTF.getFont());

        confirmButton = new JButton("Confirm");
        confirmButton.setForeground(new Color(0, 102, 102));
        confirmButton.setBackground(Color.white);
        confirmButton.setBorder(new TitledBorder(""));
        confirmButton.setContentAreaFilled(false);
        confirmButton.setOpaque(true);
        confirmButton.setSize(screenWidth / 6, screenHeight / 10);
        confirmButton.setLocation((int) (screenWidth / 1.5),
                (int) (passwordLabel.getLocation().getY() + passwordLabel.getFont().getSize() + screenHeight / 8));
        SDG2Util.fixJButtonFontSize(confirmButton);
        confirmButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    if (controller.logIn(usernameTF.getText(), passwordTF.getText()))
                        ClientWindow.getClientWindow().changeScreen(ScreenType.LOG_IN_SUCCESFUL, usernameTF.getText());
                    else
                        JOptionPane.showConfirmDialog(LogInPanel.this, "Error logging in.", "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);

                } catch (RemoteException e1) {

                    e1.printStackTrace();
                }
            }
        });

        cancelButton = new JButton("cancel");
        cancelButton.setForeground(new Color(0, 102, 102));
        cancelButton.setBackground(Color.white);
        cancelButton.setBorder(new TitledBorder(""));
        cancelButton.setContentAreaFilled(false);
        cancelButton.setOpaque(true);
        cancelButton.setSize(screenWidth / 5, screenHeight / 10);
        cancelButton.setLocation(screenWidth / 6, (int) confirmButton.getLocation().getY());
        SDG2Util.fixJButtonFontSize(cancelButton);
        cancelButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ClientWindow.getClientWindow().changeScreen(ScreenType.INITIAL, usernameTF.getText());
            }
        });

        this.add(titleLabel);
        this.add(infoLabel);
        this.add(usernameLabel);
        this.add(usernameTF);
        this.add(passwordLabel);
        this.add(passwordTF);
        this.add(confirmButton);
        this.add(cancelButton);

    }

    public static void main(String[] args) {
        JFrame testFrame = new JFrame();
        testFrame.setSize(800, 600);
        testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        testFrame.add(new LogInPanel(800, 600));
        testFrame.setVisible(true);
    }

}