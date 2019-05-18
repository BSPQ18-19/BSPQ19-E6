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


public class UserAccountPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JLabel titleLabel;
    private JLabel infoLabel;
    private JLabel countryLabel;
    private JLabel confirmLabel;
    private JTextField countryTF;
    private JLabel passwordLabel;
    private JTextField passwordTF;
    private JButton confirmButton;
    private JButton backButton;

    private ClientController controller;

    public UserAccountPanel(int screenWidth, int screenHeight) {

        setBackground(Color.white);
        setLayout(null);
        this.controller = ClientController.getClientController();

        titleLabel = new JLabel("Account details", SwingConstants.LEFT);
        titleLabel.setForeground(Color.white);
        titleLabel.setBackground(new Color(0, 204, 204));
        titleLabel.setOpaque(true);
        titleLabel.setSize(screenWidth, screenHeight / 7);
        titleLabel.setLocation(0, 0);
        SDG2Util.fixJLabelFontSize(titleLabel);

        infoLabel = new JLabel("You can modify either your country or your password.",
                SwingConstants.LEFT);
        infoLabel.setForeground(new Color(0, 102, 102));
        infoLabel.setSize((int) (screenWidth / 1.3), screenHeight / 6);
        infoLabel.setLocation(screenWidth / 7,
                (int) (titleLabel.getLocation().getY() + titleLabel.getFont().getSize() * 1.5));
        infoLabel.setFont(new Font("Arial", Font.PLAIN, screenHeight / 30));

        countryLabel = new JLabel("Country:", SwingConstants.LEFT);
        countryLabel.setForeground(new Color(0, 102, 102));
        countryLabel.setSize(screenWidth / 5, screenHeight / 20);
        countryLabel.setLocation((int) infoLabel.getLocation().getX(),
                (int) (infoLabel.getLocation().getY() + infoLabel.getHeight() + screenHeight / 30));
        SDG2Util.fixJLabelFontSize(countryLabel);

        countryTF = new JTextField();
        countryTF.setText(controller.getCurrentUser().getCountry());
        countryTF.setBackground(Color.white);
        countryTF.setForeground(new Color(102, 69, 3));
        countryTF.setBorder(new TitledBorder(""));
        countryTF.setSize(screenWidth / 4, screenHeight / 20);
        countryTF.setLocation((int) countryLabel.getLocation().getX() + countryLabel.getWidth(),
                (int) countryLabel.getLocation().getY());
        countryTF.setFont(new Font("Arial", Font.PLAIN, (int) (countryLabel.getFont().getSize() / 1.5)));

        passwordLabel = new JLabel("Password:", SwingConstants.LEFT);
        passwordLabel.setForeground(new Color(0, 102, 102));
        passwordLabel.setSize(countryLabel.getSize());
        passwordLabel.setLocation((int) countryLabel.getLocation().getX(),
                (int) (countryLabel.getLocation().getY() + countryLabel.getHeight() + screenHeight / 40));
        passwordLabel.setFont(countryLabel.getFont());

        passwordTF = new JPasswordField();
        passwordTF.setText(controller.getCurrentUser().getPassword());
        passwordTF.setBackground(Color.white);
        passwordTF.setForeground(new Color(102, 69, 3));
        passwordTF.setBorder(new TitledBorder(""));
        passwordTF.setSize(countryTF.getSize());
        passwordTF.setLocation((int) countryTF.getLocation().getX(),
                (int) passwordLabel.getLocation().getY());
        passwordTF.setFont(countryTF.getFont());

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
                if (controller.updateUser(countryTF.getText(), passwordTF.getText())) {
                    confirmLabel.setForeground(new Color(0, 102, 29));
                    confirmLabel.setText("User updated.");
                } else {
                    confirmLabel.setForeground(new Color(102, 34, 22));
                    confirmLabel.setText("User not updated.");
                    countryTF.setText(controller.getCurrentUser().getCountry());
                    passwordTF.setText(controller.getCurrentUser().getPassword());
                }
                revalidate();
                repaint();
            }
        });

        backButton = new JButton("Back");
        backButton.setForeground(new Color(0, 102, 102));
        backButton.setBackground(Color.white);
        backButton.setBorder(new TitledBorder(""));
        backButton.setContentAreaFilled(false);
        backButton.setOpaque(true);
        backButton.setSize(screenWidth / 5, screenHeight / 10);
        backButton.setLocation(screenWidth / 6, (int) confirmButton.getLocation().getY());
        SDG2Util.fixJButtonFontSize(backButton);
        backButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ClientWindow.getClientWindow().changeScreen(ScreenType.MAIN_MENU, countryTF.getText());
            }
        });

        confirmLabel = new JLabel(" ",
                SwingConstants.LEFT);
        confirmLabel.setForeground(new Color(0, 102, 102));
        confirmLabel.setSize((int) (screenWidth / 1.3), screenHeight / 6);
        confirmLabel.setLocation(10, backButton.getY() + backButton.getHeight() + 20);
        confirmLabel.setFont(new Font("Arial", Font.PLAIN, screenHeight / 30));

        this.add(titleLabel);
        this.add(infoLabel);
        this.add(countryLabel);
        this.add(countryTF);
        this.add(passwordLabel);
        this.add(passwordTF);
        this.add(confirmButton);
        this.add(backButton);
        this.add(confirmLabel);

    }

    public static void main(String[] args) {
        JFrame testFrame = new JFrame();
        testFrame.setSize(800, 600);
        testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        testFrame.add(new UserAccountPanel(800, 600));
        testFrame.setVisible(true);
    }

}