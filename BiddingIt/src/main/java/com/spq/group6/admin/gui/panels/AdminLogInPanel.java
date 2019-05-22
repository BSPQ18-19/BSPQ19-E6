package com.spq.group6.admin.gui.panels;

import com.spq.group6.admin.controller.AdminController;
import com.spq.group6.admin.gui.AdminWindow;
import com.spq.group6.admin.gui.utils.SDG2Util;
import com.spq.group6.admin.gui.utils.ScreenType;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

/**
 * One of the panels used in the Admin window
 */
public class AdminLogInPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JLabel titleLabel;
    private JLabel infoLabel;
    private JLabel usernameLabel;
    private JTextField usernameTF;
    private JLabel passwordLabel;
    private JTextField passwordTF;
    private JButton confirmButton;

    private AdminController controller;

    /**
     * Constructor to create a new Log-In Panel.
     * <p>
     *
     * @param screenWidth  width of the screen
     * @param screenHeight height of the screen
     * @param controller   the controller that uses the Admin Window
     */
    public AdminLogInPanel(int screenWidth, int screenHeight, AdminController controller) {
        setBackground(Color.WHITE);
        this.setLayout(null);
        this.controller = controller;

        titleLabel = new JLabel("BiddingIt - Administration", SwingConstants.LEFT);
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
        usernameTF.setForeground(new Color(102, 69, 3));
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

        passwordTF = new JPasswordField();
        passwordTF.setBackground(Color.white);
        passwordTF.setForeground(new Color(102, 69, 3));
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


            public void actionPerformed(ActionEvent e) {

                try {
                    if (controller.logIn(usernameTF.getText(), passwordTF.getText()))
                        AdminWindow.getAdminWindow(null).changeScreen(ScreenType.LOG_IN_SUCCESFUL, usernameTF.getText());
                    else
                        JOptionPane.showConfirmDialog(AdminLogInPanel.this, "Error logging in.", "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);

                } catch (RemoteException e1) {

                    e1.printStackTrace();
                }
            }
        });

        this.add(titleLabel);
        this.add(infoLabel);
        this.add(usernameLabel);
        this.add(usernameTF);
        this.add(passwordLabel);
        this.add(passwordTF);
        this.add(confirmButton);

    }

    public static void main(String[] args) {
        JFrame testFrame = new JFrame();
        testFrame.setSize(800, 600);
        testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        testFrame.add(new AdminLogInPanel(800, 600, null));
        testFrame.setVisible(true);
    }

}