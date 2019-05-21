package com.spq.group6.client.gui.panels;

import com.spq.group6.client.gui.ClientWindow;
import com.spq.group6.client.gui.utils.SDG2Util;
import com.spq.group6.client.gui.utils.ScreenType;
import com.spq.group6.client.gui.utils.voice.VoiceHelper;
import com.spq.group6.client.gui.utils.locale.LanguageManager;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;


public class LogInPanel extends LocaleSelectorPanel {

    private static final long serialVersionUID = 1L;
    private JLabel infoLabel;
    private JLabel usernameLabel;
    private JLabel confirmLabel;
    private JTextField usernameTF;
    private JLabel passwordLabel;
    private JTextField passwordTF;
    private JButton confirmButton;
    private JButton cancelButton;

    public LogInPanel(int screenWidth, int screenHeight) {

    	super(screenWidth, screenHeight);

    	System.out.println(controller);
    	
        titleLabel = new JLabel(LanguageManager.getMessage("LogInPanel.titleLabel.text"), SwingConstants.LEFT);
        titleLabel.setForeground(Color.white);
        titleLabel.setBackground(new Color(0, 204, 204));
        titleLabel.setOpaque(true);
        titleLabel.setSize(screenWidth, screenHeight / 7);
        titleLabel.setLocation(0, 0);
        SDG2Util.fixJLabelFontSize(titleLabel);

        infoLabel = new JLabel(LanguageManager.getMessage("LogInPanel.infoLabel.text"),
                SwingConstants.LEFT);
        infoLabel.setForeground(new Color(0, 102, 102));
        infoLabel.setSize((int) (screenWidth / 1.3), screenHeight / 6);
        infoLabel.setLocation(screenWidth / 7,
                (int) (titleLabel.getLocation().getY() + titleLabel.getFont().getSize() * 1.5));
        infoLabel.setFont(new Font("Arial", Font.PLAIN, screenHeight / 30));

        usernameLabel = new JLabel(LanguageManager.getMessage("LogInPanel.usernameLabel.text"), SwingConstants.LEFT);
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
        usernameTF.setFont(new Font("Arial", Font.PLAIN, (int) (usernameLabel.getFont().getSize() / 1.5)));
        usernameTF.addFocusListener(ttsFocusListener);

        passwordLabel = new JLabel(LanguageManager.getMessage("LogInPanel.passwordLabel.text"), SwingConstants.LEFT);
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
        passwordTF.addFocusListener(ttsFocusListener);

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

            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    if (controller.logIn(usernameTF.getText(), passwordTF.getText()))
                        ClientWindow.getClientWindow().changeScreen(ScreenType.LOG_IN_SUCCESFUL, usernameTF.getText());
                    else {
                        confirmLabel.setForeground(new Color(102, 34, 22));
                        confirmLabel.setText("Error while login.");
                    }

                } catch (RemoteException e1) {

                    e1.printStackTrace();
                }
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

            @Override
            public void actionPerformed(ActionEvent e) {
                ClientWindow.getClientWindow().changeScreen(ScreenType.INITIAL, usernameTF.getText());
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
        this.add(confirmButton);
        this.add(cancelButton);
        this.add(confirmLabel);
        
        bringSelectLanguageCBToFront();

        VoiceHelper.textToSpeech(LanguageManager.getMessage("Voice.LogInPanel.welcome"));
        
    }
    
    @Override
    protected void updateComponentsText() {
    	titleLabel.setText(LanguageManager.getMessage("LogInPanel.titleLabel.text"));
    	SDG2Util.fixJLabelFontSize(titleLabel);
    	infoLabel.setText(LanguageManager.getMessage("LogInPanel.infoLabel.text"));
    	SDG2Util.fixJLabelFontSize(infoLabel);
    	usernameLabel.setText(LanguageManager.getMessage("LogInPanel.usernameLabel.text"));
    	SDG2Util.fixJLabelFontSize(usernameLabel);
    	passwordLabel.setText(LanguageManager.getMessage("LogInPanel.passwordLabel.text"));
    	SDG2Util.fixJLabelFontSize(passwordLabel);
    	confirmButton.setText(LanguageManager.getMessage("General.confirmButton.text"));
    	SDG2Util.fixJButtonFontSize(confirmButton);
    	cancelButton.setText(LanguageManager.getMessage("General.cancelButton.text"));
    	SDG2Util.fixJButtonFontSize(cancelButton);
    }

    public static void main(String[] args) {
        JFrame testFrame = new JFrame();
        testFrame.setSize(800, 600);
        testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        testFrame.add(new LogInPanel(800, 600));
        testFrame.setVisible(true);
    }

}