package com.spq.group6.client.gui.panels;

import com.spq.group6.client.controller.ClientController;
import com.spq.group6.client.gui.ClientWindow;
import com.spq.group6.client.gui.utils.SDG2Util;
import com.spq.group6.client.gui.utils.ScreenType;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterPanel extends LocaleSelectorPanel {

    private static final long serialVersionUID = 1L;
    private JLabel infoLabel;
    private JLabel usernameLabel;
    private JTextField usernameTF;
    //private JLabelGraficoAjustado usernametickImage;
    private JLabel passwordLabel;
    private JLabel confirmLabel;
    private JTextField passwordTF;
    //private JLabelGraficoAjustado passwordtickImage;
    private JLabel countryLabel;
    private JTextField countryTF;
    private JButton confirmButton;
    private JButton cancelButton;

    private ClientController controller;

    public RegisterPanel(int screenWidth, int screenHeight) {

        super(screenWidth, screenHeight);

        titleLabel = new JLabel(controller.getLanguageMessage("RegisterPanel.titleLabel.text"), SwingConstants.LEFT);
        titleLabel.setForeground(Color.white);
        titleLabel.setBackground(new Color(0, 204, 204));
        titleLabel.setOpaque(true);
        titleLabel.setSize(screenWidth, screenHeight / 7);
        titleLabel.setLocation(0, 0);
        SDG2Util.fixJLabelFontSize(titleLabel);

        infoLabel = new JLabel(controller.getLanguageMessage("RegisterPanel.infoLabel.text"),
                SwingConstants.LEFT);
        infoLabel.setForeground(new Color(0, 102, 102));
        infoLabel.setSize((int) (screenWidth / 1.3), screenHeight / 6);
        infoLabel.setLocation(screenWidth / 7,
                (int) (titleLabel.getLocation().getY() + titleLabel.getFont().getSize() * 1.5));
        infoLabel.setFont(new Font("Arial", Font.PLAIN, screenHeight / 30));

        usernameLabel = new JLabel(controller.getLanguageMessage("RegisterPanel.usernameLabel.text"), SwingConstants.LEFT);
        usernameLabel.setForeground(new Color(0, 102, 102));
        usernameLabel.setSize(screenWidth / 5, screenHeight / 20);
        usernameLabel.setLocation((int) infoLabel.getLocation().getX(),
                (int) (infoLabel.getLocation().getY() + infoLabel.getHeight() + screenHeight / 30));
        SDG2Util.fixJLabelFontSize(usernameLabel);

        usernameTF = new JTextField();
        usernameTF.setBackground(Color.white);
        usernameTF.setForeground(new Color(102, 69, 3));
        usernameTF.setBorder(new TitledBorder(""));
        usernameTF.setSize(screenWidth / 4, screenHeight / 20);
        usernameTF.setLocation((int) usernameLabel.getLocation().getX() + usernameLabel.getWidth(),
                (int) usernameLabel.getLocation().getY());
        usernameTF.setFont(new Font("Arial", Font.PLAIN, (int) (usernameLabel.getFont().getSize() / 1.25)));
        usernameTF.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void removeUpdate(DocumentEvent e) {
                checkInput(true);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                checkInput(true);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                checkInput(true);
            }
        });
		
		/*
		usernametickImage = new JLabelGraficoAjustado("media/error.png", usernameTF.getHeight(), usernameTF.getHeight());
		usernametickImage.setLocation((int) (usernameTF.getLocation().getX() + usernameTF.getWidth() + screenWidth/75),
				usernameTF.getLocation().getY());
		*/
        passwordLabel = new JLabel(controller.getLanguageMessage("RegisterPanel.passwordLabel.text"), SwingConstants.LEFT);
        passwordLabel.setForeground(new Color(0, 102, 102));
        passwordLabel.setSize(usernameLabel.getSize());
        passwordLabel.setLocation((int) usernameLabel.getLocation().getX(),
                (int) (usernameLabel.getLocation().getY() + usernameLabel.getHeight() + screenHeight / 40));
        passwordLabel.setFont(usernameLabel.getFont());

        passwordTF = new JPasswordField();
        passwordTF.setBackground(Color.white);
        passwordTF.setForeground(new Color(102, 69, 3));
        passwordTF.setBorder(new TitledBorder(""));
        passwordTF.setSize(usernameTF.getSize());
        passwordTF.setLocation((int) usernameTF.getLocation().getX(),
                (int) passwordLabel.getLocation().getY());
        passwordTF.setFont(usernameTF.getFont());
        passwordTF.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void removeUpdate(DocumentEvent e) {
                checkInput(false);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                checkInput(false);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                checkInput(false);
            }
        });
		
		/*
		passwordtickImage = new JLabelGraficoAjustado("/error.png", usernametickImage.getHeight(), usernametickImage.getHeight());
		passwordtickImage.setLocation(usernametickImage.getLocation().getX(), passwordTF.getLocation().getY());
		*/

        countryLabel = new JLabel(controller.getLanguageMessage("RegisterPanel.countryLabel.text"), SwingConstants.LEFT);
        countryLabel.setForeground(new Color(0, 102, 102));
        countryLabel.setSize(passwordLabel.getWidth() * 2, passwordLabel.getHeight());
        countryLabel.setLocation((int) passwordLabel.getLocation().getX(),
                (int) (passwordLabel.getLocation().getY() + passwordLabel.getHeight() + screenHeight / 40));
        countryLabel.setFont(passwordLabel.getFont());

        countryTF = new JTextField();
        countryTF.setBackground(Color.white);
        countryTF.setForeground(new Color(102, 69, 3));
        countryTF.setBorder(new TitledBorder(""));
        countryTF.setSize(usernameTF.getSize());
        countryTF.setLocation((int) usernameTF.getLocation().getX(),
                (int) (countryLabel.getLocation().getY()));
        countryTF.setFont(usernameTF.getFont());

        confirmButton = new JButton(controller.getLanguageMessage("General.confirmButton.text"));
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

                if (controller.signIn(usernameTF.getText(), passwordTF.getText(), countryTF.getText())) {
                    ClientWindow.getClientWindow().changeScreen(ScreenType.LOG_IN_SUCCESFUL, usernameTF.getText());
                } else {
                    confirmLabel.setForeground(new Color(102, 34, 22));
                    confirmLabel.setText("Error while signin.");
                }
				
				/*
				if (usernametickImage.getName().equals("media/checked.png") &&
						passwordtickImage.getName().equals("media/checked.png")) {
					controller.registerUser(usernameTF.getText(), passwordTF.getText(), (String) defaultAirportComboBox.getSelectedItem(), authSystem);
					ClientWindow.getClientWindow(null).changeScreen(ScreenType.LOG_IN_SUCCESFUL);
					// send server to register user, confirm and go to main menu
				} else if (!usernametickImage.getName().equals("media/checked.png") &&
						passwordtickImage.getName().equals("media/checked.png")) {
					JOptionPane.showMessageDialog(RegisterJPanel.this, "Username is already taken.", "Error", JOptionPane.ERROR_MESSAGE);
				} else if (usernametickImage.getName().equals("media/checked.png") &&
						!passwordtickImage.getName().equals("media/checked.png")) {
					JOptionPane.showMessageDialog(RegisterJPanel.this, "password is already taken.", "Error", JOptionPane.ERROR_MESSAGE);
				} else if (!usernametickImage.getName().equals("media/checked.png") &&
						!passwordtickImage.getName().equals("media/checked.png")) {
					JOptionPane.showMessageDialog(RegisterJPanel.this, "Username and password are already taken.", "Error", JOptionPane.ERROR_MESSAGE);
				}
				*/
            }
        });

        cancelButton = new JButton(controller.getLanguageMessage("General.cancelButton.text"));
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
                ClientWindow.getClientWindow().changeScreen(ScreenType.INITIAL);
            }
        });
        confirmLabel = new JLabel(" ",
                SwingConstants.LEFT);
        confirmLabel.setForeground(new Color(0, 102, 102));
        confirmLabel.setSize((int) (screenWidth / 1.3), screenHeight / 6);
        confirmLabel.setLocation(10, cancelButton.getY() + cancelButton.getHeight() + 20);
        confirmLabel.setFont(new Font("Arial", Font.PLAIN, screenHeight / 30));

        this.add(titleLabel);
        this.add(infoLabel);
        this.add(usernameLabel);
        this.add(usernameTF);
        // this.add(usernametickImage);
        this.add(passwordLabel);
        this.add(passwordTF);
        // this.add(passwordtickImage);
        this.add(countryLabel);
        this.add(countryTF);
        this.add(confirmButton);
        this.add(cancelButton);
        this.add(confirmLabel);
        
        bringSelectLanguageCBToFront();

    }

    public static void main(String[] args) {
        JFrame testFrame = new JFrame();
        testFrame.setSize(800, 600);
        testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        testFrame.add(new RegisterPanel(800, 600));
        testFrame.setVisible(true);
    }

    // param isUsername is true when checking the usernametf and false when checking the passwordtf
    private void checkInput(boolean isUsername) {
		/*
		if (isUsername) {
			String trimmedUsernameTFText = usernameTF.getText().trim();
			if (!trimmedUsernameTFText.equals("") && controller.existsUsername(trimmedUsernameTFText))
				usernametickImage = new JLabelGraficoAjustado("media/error.png", usernameTF.getHeight(), usernameTF.getHeight());
			else
				usernametickImage = new JLabelGraficoAjustado("media/checked.png", usernameTF.getHeight(), usernameTF.getHeight());
			usernametickImage.repaint();
		} else {
			String trimmedpasswordTFText = passwordTF.getText().trim();
			if (!trimmedpasswordTFText.equals("") && controller.existspassword(trimmedpasswordTFText))
				passwordtickImage = new JLabelGraficoAjustado("media/error.png", usernametickImage.getHeight(), usernametickImage.getHeight());
			else
				passwordtickImage = new JLabelGraficoAjustado("media/checked.png", usernametickImage.getHeight(), usernametickImage.getHeight());
			passwordtickImage.repaint();
		}
		System.out.println(usernametickImage.getName() + " " + passwordtickImage.getName());
		*/
    }
}