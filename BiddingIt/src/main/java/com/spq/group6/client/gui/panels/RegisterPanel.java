package com.spq.group6.client.gui.panels;

import com.spq.group6.client.gui.ClientWindow;
import com.spq.group6.client.gui.utils.SDG2Util;
import com.spq.group6.client.gui.utils.ScreenType;
import com.spq.group6.client.gui.utils.locale.LanguageManager;
import com.spq.group6.client.gui.utils.voice.VoiceHelper;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * One of the panels used in the Client window
 */
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

    /**
     * Constructor to create a new Register Panel.
     * <p>
     *
     * @param screenWidth  width of the screen
     * @param screenHeight height of the screen
     */
    public RegisterPanel(int screenWidth, int screenHeight) {

        super(screenWidth, screenHeight);

        titleLabel = new JLabel(LanguageManager.getMessage("RegisterPanel.titleLabel.text"), SwingConstants.LEFT);
        titleLabel.setForeground(Color.white);
        titleLabel.setBackground(new Color(0, 204, 204));
        titleLabel.setOpaque(true);
        titleLabel.setSize(screenWidth, screenHeight / 7);
        titleLabel.setLocation(0, 0);
        SDG2Util.fixJLabelFontSize(titleLabel);

        infoLabel = new JLabel(LanguageManager.getMessage("RegisterPanel.infoLabel.text"),
                SwingConstants.LEFT);
        infoLabel.setForeground(new Color(0, 102, 102));
        infoLabel.setSize((int) (screenWidth / 1.3), screenHeight / 6);
        infoLabel.setLocation(screenWidth / 7,
                (int) (titleLabel.getLocation().getY() + titleLabel.getFont().getSize() * 1.5));
        infoLabel.setFont(new Font("Arial", Font.PLAIN, screenHeight / 30));

        usernameLabel = new JLabel(LanguageManager.getMessage("RegisterPanel.usernameLabel.text"), SwingConstants.LEFT);
        usernameLabel.setForeground(new Color(0, 102, 102));
        usernameLabel.setSize(screenWidth / 5, screenHeight / 20);
        usernameLabel.setLocation((int) infoLabel.getLocation().getX(),
                (int) (infoLabel.getLocation().getY() + infoLabel.getHeight() + screenHeight / 30));
        SDG2Util.fixJLabelFontSize(usernameLabel);

        usernameTF = new JTextField();
        usernameTF.setName("username");
        usernameTF.setBackground(Color.white);
        usernameTF.setForeground(new Color(102, 69, 3));
        usernameTF.setBorder(new TitledBorder(""));
        usernameTF.setSize(screenWidth / 4, screenHeight / 20);
        usernameTF.setLocation((int) usernameLabel.getLocation().getX() + usernameLabel.getWidth(),
                (int) usernameLabel.getLocation().getY());
        usernameTF.setFont(new Font("Arial", Font.PLAIN, (int) (usernameLabel.getFont().getSize() / 1.25)));
        usernameTF.getDocument().addDocumentListener(new DocumentListener() {


            public void removeUpdate(DocumentEvent e) {
                checkInput(true);
            }


            public void insertUpdate(DocumentEvent e) {
                checkInput(true);
            }


            public void changedUpdate(DocumentEvent e) {
                checkInput(true);
            }
        });
        usernameTF.addFocusListener(ttsFocusListener);
		
		/*
		usernametickImage = new JLabelGraficoAjustado("media/error.png", usernameTF.getHeight(), usernameTF.getHeight());
		usernametickImage.setLocation((int) (usernameTF.getLocation().getX() + usernameTF.getWidth() + screenWidth/75),
				usernameTF.getLocation().getY());
		*/
        passwordLabel = new JLabel(LanguageManager.getMessage("RegisterPanel.passwordLabel.text"), SwingConstants.LEFT);
        passwordLabel.setForeground(new Color(0, 102, 102));
        passwordLabel.setSize(usernameLabel.getSize());
        passwordLabel.setLocation((int) usernameLabel.getLocation().getX(),
                (int) (usernameLabel.getLocation().getY() + usernameLabel.getHeight() + screenHeight / 40));
        passwordLabel.setFont(usernameLabel.getFont());

        passwordTF = new JPasswordField();
        passwordTF.setName("password");
        passwordTF.setBackground(Color.white);
        passwordTF.setForeground(new Color(102, 69, 3));
        passwordTF.setBorder(new TitledBorder(""));
        passwordTF.setSize(usernameTF.getSize());
        passwordTF.setLocation((int) usernameTF.getLocation().getX(),
                (int) passwordLabel.getLocation().getY());
        passwordTF.setFont(usernameTF.getFont());
        passwordTF.getDocument().addDocumentListener(new DocumentListener() {


            public void removeUpdate(DocumentEvent e) {
                checkInput(false);
            }


            public void insertUpdate(DocumentEvent e) {
                checkInput(false);
            }


            public void changedUpdate(DocumentEvent e) {
                checkInput(false);
            }
        });
        passwordTF.addFocusListener(ttsFocusListener);
		
		/*
		passwordtickImage = new JLabelGraficoAjustado("/error.png", usernametickImage.getHeight(), usernametickImage.getHeight());
		passwordtickImage.setLocation(usernametickImage.getLocation().getX(), passwordTF.getLocation().getY());
		*/

        countryLabel = new JLabel(LanguageManager.getMessage("RegisterPanel.countryLabel.text"), SwingConstants.LEFT);
        countryLabel.setForeground(new Color(0, 102, 102));
        countryLabel.setSize(passwordLabel.getWidth() * 2, passwordLabel.getHeight());
        countryLabel.setLocation((int) passwordLabel.getLocation().getX(),
                (int) (passwordLabel.getLocation().getY() + passwordLabel.getHeight() + screenHeight / 40));
        countryLabel.setFont(passwordLabel.getFont());

        countryTF = new JTextField();
        countryTF.setName("country");
        countryTF.setBackground(Color.white);
        countryTF.setForeground(new Color(102, 69, 3));
        countryTF.setBorder(new TitledBorder(""));
        countryTF.setSize(usernameTF.getSize());
        countryTF.setLocation((int) usernameTF.getLocation().getX(),
                (int) (countryLabel.getLocation().getY()));
        countryTF.setFont(usernameTF.getFont());
        countryTF.addFocusListener(ttsFocusListener);

        confirmButton = new JButton(LanguageManager.getMessage("General.confirmButton.text"));
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
        confirmButton.addFocusListener(ttsFocusListener);

        cancelButton = new JButton(LanguageManager.getMessage("General.cancelButton.text"));
        cancelButton.setForeground(new Color(0, 102, 102));
        cancelButton.setBackground(Color.white);
        cancelButton.setBorder(new TitledBorder(""));
        cancelButton.setContentAreaFilled(false);
        cancelButton.setOpaque(true);
        cancelButton.setSize(screenWidth / 5, screenHeight / 10);
        cancelButton.setLocation(screenWidth / 6, (int) confirmButton.getLocation().getY());
        SDG2Util.fixJButtonFontSize(cancelButton);
        cancelButton.addActionListener(new ActionListener() {


            public void actionPerformed(ActionEvent e) {
                ClientWindow.getClientWindow().changeScreen(ScreenType.INITIAL);
            }
        });
        cancelButton.addFocusListener(ttsFocusListener);

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
        this.add(passwordLabel);
        this.add(passwordTF);
        this.add(countryLabel);
        this.add(countryTF);
        this.add(confirmButton);
        this.add(cancelButton);
        this.add(confirmLabel);

        bringSelectLanguageCBToFront();

        VoiceHelper.textToSpeech(LanguageManager.getMessage("Voice.RegisterPanel.welcome"));

    }

    public static void main(String[] args) {
        JFrame testFrame = new JFrame();
        testFrame.setSize(800, 600);
        testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        testFrame.add(new RegisterPanel(800, 600));
        testFrame.setVisible(true);
    }


    protected void updateComponentsText() {
        titleLabel.setText(LanguageManager.getMessage("RegisterPanel.titleLabel.text"));
        SDG2Util.fixJLabelFontSize(titleLabel);
        infoLabel.setText(LanguageManager.getMessage("RegisterPanel.infoLabel.text"));
        SDG2Util.fixJLabelFontSize(infoLabel);
        usernameLabel.setText(LanguageManager.getMessage("RegisterPanel.usernameLabel.text"));
        SDG2Util.fixJLabelFontSize(usernameLabel);
        passwordLabel.setText(LanguageManager.getMessage("RegisterPanel.passwordLabel.text"));
        SDG2Util.fixJLabelFontSize(passwordLabel);
        countryLabel.setText(LanguageManager.getMessage("RegisterPanel.countryLabel.text"));
        SDG2Util.fixJLabelFontSize(countryLabel);
        confirmButton.setText(LanguageManager.getMessage("General.confirmButton.text"));
        SDG2Util.fixJButtonFontSize(confirmButton);
        cancelButton.setText(LanguageManager.getMessage("General.cancelButton.text"));
        SDG2Util.fixJButtonFontSize(cancelButton);
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