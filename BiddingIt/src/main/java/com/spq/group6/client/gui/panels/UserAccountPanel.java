package com.spq.group6.client.gui.panels;

import com.spq.group6.client.gui.ClientWindow;
import com.spq.group6.client.gui.utils.SDG2Util;
import com.spq.group6.client.gui.utils.ScreenType;
import com.spq.group6.client.gui.utils.VoiceHelper;
import com.spq.group6.client.gui.utils.locale.LanguageManager;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class UserAccountPanel extends LocaleSelectorPanel {

    private static final long serialVersionUID = 1L;
    private JLabel infoLabel;
    private JLabel countryLabel;
    private JLabel nameLabel;
    private JLabel moneyLabel;
    private JLabel confirmLabel;
    private JTextField countryTF;
    private JLabel passwordLabel;
    private JTextField passwordTF;
    private JButton confirmButton;
    private JButton backButton;

    public UserAccountPanel(int screenWidth, int screenHeight) {

        super(screenWidth, screenHeight);

        titleLabel = new JLabel(LanguageManager.getMessage("UserAccountPanel.titleLabel.text"), SwingConstants.LEFT);
        titleLabel.setForeground(Color.white);
        titleLabel.setBackground(new Color(0, 204, 204));
        titleLabel.setOpaque(true);
        titleLabel.setSize(screenWidth, screenHeight / 7);
        titleLabel.setLocation(0, 0);
        SDG2Util.fixJLabelFontSize(titleLabel);

        infoLabel = new JLabel(LanguageManager.getMessage("UserAccountPanel.infoLabel.text"),
                SwingConstants.LEFT);
        infoLabel.setForeground(new Color(0, 102, 102));
        infoLabel.setSize((int) (screenWidth / 1.3), screenHeight / 6);
        infoLabel.setLocation(screenWidth / 7,
                (int) (titleLabel.getLocation().getY() + titleLabel.getFont().getSize() * 1.5));
        infoLabel.setFont(new Font("Arial", Font.PLAIN, screenHeight / 30));

        countryLabel = new JLabel(LanguageManager.getMessage("UserAccountPanel.countryLabel.text"), SwingConstants.LEFT);
        countryLabel.setForeground(new Color(0, 102, 102));
        countryLabel.setSize(screenWidth / 5, screenHeight / 20);
        countryLabel.setLocation((int) infoLabel.getLocation().getX(),
                (int) (infoLabel.getLocation().getY() + infoLabel.getHeight() + screenHeight / 30));
        SDG2Util.fixJLabelFontSize(countryLabel);

        countryTF = new JTextField();
        countryTF.setName("country");
        countryTF.setText(controller.getCurrentUser().getCountry());
        countryTF.setBackground(Color.white);
        countryTF.setForeground(new Color(102, 69, 3));
        countryTF.setBorder(new TitledBorder(""));
        countryTF.setSize(screenWidth / 4, screenHeight / 20);
        countryTF.setLocation((int) countryLabel.getLocation().getX() + countryLabel.getWidth() - 80,
                (int) countryLabel.getLocation().getY());
        countryTF.setFont(new Font("Arial", Font.PLAIN, (int) (countryLabel.getFont().getSize() / 1.5)));
        countryTF.addFocusListener(ttsFocusListener);
        
        passwordLabel = new JLabel(LanguageManager.getMessage("UserAccountPanel.passwordLabel.text"), SwingConstants.LEFT);
        passwordLabel.setForeground(new Color(0, 102, 102));
        passwordLabel.setSize(countryLabel.getSize());
        passwordLabel.setLocation((int) countryLabel.getLocation().getX(),
                (int) (countryLabel.getLocation().getY() + countryLabel.getHeight() + screenHeight / 40));
        passwordLabel.setFont(countryLabel.getFont());

        passwordTF = new JPasswordField();
        passwordTF.setName("password");
        passwordTF.setText(controller.getCurrentUser().getPassword());
        passwordTF.setBackground(Color.white);
        passwordTF.setForeground(new Color(102, 69, 3));
        passwordTF.setBorder(new TitledBorder(""));
        passwordTF.setSize(countryTF.getSize());
        passwordTF.setLocation((int) countryTF.getLocation().getX(),
                (int) passwordLabel.getLocation().getY());
        passwordTF.setFont(countryTF.getFont());
        passwordTF.addFocusListener(ttsFocusListener);
        
        nameLabel = new JLabel("Username: " + controller.getCurrentUser().getUsername(), SwingConstants.LEFT);
        nameLabel.setForeground(new Color(0, 102, 102));
        nameLabel.setSize(screenWidth / 5, screenHeight / 20);
        nameLabel.setLocation((int) countryTF.getLocation().getX() + countryTF.getWidth() + 40,
                (int) countryTF.getLocation().getY());
        SDG2Util.fixJLabelFontSize(nameLabel);

        moneyLabel = new JLabel("Money: " + controller.getCurrentUser().getMoney(), SwingConstants.LEFT);
        moneyLabel.setForeground(new Color(0, 102, 102));
        moneyLabel.setSize(screenWidth / 5, screenHeight / 20);
        moneyLabel.setLocation((int) nameLabel.getLocation().getX(),
                (int) (nameLabel.getLocation().getY() + nameLabel.getHeight() + screenHeight / 30));
        SDG2Util.fixJLabelFontSize(moneyLabel);

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
        confirmButton.addFocusListener(ttsFocusListener);

        backButton = new JButton(LanguageManager.getMessage("General.backButton.text"));
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
        backButton.addFocusListener(ttsFocusListener);

        confirmLabel = new JLabel(" ",
                SwingConstants.LEFT);
        confirmLabel.setForeground(new Color(0, 102, 102));
        confirmLabel.setSize((int) (screenWidth / 1.3), screenHeight / 6);
        confirmLabel.setLocation(10, backButton.getY() + backButton.getHeight() + 20);
        confirmLabel.setFont(new Font("Arial", Font.PLAIN, screenHeight / 30));

        this.add(titleLabel);
        this.add(infoLabel);
        this.add(nameLabel);
        this.add(moneyLabel);
        this.add(countryLabel);
        this.add(countryTF);
        this.add(passwordLabel);
        this.add(passwordTF);
        this.add(confirmButton);
        this.add(backButton);
        this.add(confirmLabel);
        
        bringSelectLanguageCBToFront();

        VoiceHelper.textToSpeech("You currently are in the user account menu.");

    }
    
    @Override
    protected void updateComponentsText() {
    	titleLabel.setText(LanguageManager.getMessage("UserAccountPanel.titleLabel.text"));
    	SDG2Util.fixJLabelFontSize(titleLabel);
    	infoLabel.setText(LanguageManager.getMessage("UserAccountPanel.infoLabel.text"));
    	SDG2Util.fixJLabelFontSize(infoLabel);
    	countryLabel.setText(LanguageManager.getMessage("UserAccountPanel.countryLabel.text"));
    	SDG2Util.fixJLabelFontSize(countryLabel);
    	passwordLabel.setText(LanguageManager.getMessage("UserAccountPanel.passwordLabel.text"));
    	SDG2Util.fixJLabelFontSize(passwordLabel);
    	confirmButton.setText(LanguageManager.getMessage("General.confirmButton.text"));
    	SDG2Util.fixJButtonFontSize(confirmButton);
    	backButton.setText(LanguageManager.getMessage("General.backButton.text"));
    	SDG2Util.fixJButtonFontSize(backButton);
    	
    }

    public static void main(String[] args) {
        JFrame testFrame = new JFrame();
        testFrame.setSize(800, 600);
        testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        testFrame.add(new UserAccountPanel(800, 600));
        testFrame.setVisible(true);
    }

}