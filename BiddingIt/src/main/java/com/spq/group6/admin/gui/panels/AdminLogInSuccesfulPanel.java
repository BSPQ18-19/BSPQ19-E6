package com.spq.group6.admin.gui.panels;

import com.spq.group6.admin.gui.AdminWindow;
import com.spq.group6.admin.gui.utils.SDG2Util;
import com.spq.group6.admin.gui.utils.ScreenType;

import javax.swing.*;
import java.awt.*;

/**
 * One of the panels used in the Admin window
 */
public class AdminLogInSuccesfulPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JLabel titleLabel;
    private JLabel infoLabel1;
    private JLabel infoLabel2;

    /**
     * Constructor to create a new Log-In-Succesful Panel.
     * <p>
     *
     * @param screenWidth  width of the screen
     * @param screenHeight height of the screen
     * @param username     username of the user that logs in
     */
    public AdminLogInSuccesfulPanel(int screenWidth, int screenHeight, String username) {
        setBackground(Color.white);
        this.setLayout(null);

        if (!username.isEmpty())
            titleLabel = new JLabel("Welcome administrator " + username + "! :)", SwingConstants.LEFT);
        else
            titleLabel = new JLabel("Welcome! :)", SwingConstants.LEFT);
        titleLabel.setForeground(Color.white);
        titleLabel.setBackground(new Color(0, 204, 204));
        titleLabel.setOpaque(true);
        titleLabel.setSize(screenWidth, screenHeight / 6);
        titleLabel.setLocation(0, 0);
        SDG2Util.fixJLabelFontSize(titleLabel);

        infoLabel1 = new JLabel("You have logged in succesfully.", SwingConstants.CENTER);
        infoLabel1.setForeground(new Color(0, 102, 102));
        infoLabel1.setSize((int) (screenWidth / 1.5), screenHeight / 7);
        infoLabel1.setLocation(screenWidth / 2 - infoLabel1.getWidth() / 2, screenHeight / 2 - infoLabel1.getHeight() / 2);
        SDG2Util.fixJLabelFontSize(infoLabel1);

        infoLabel2 = new JLabel("Please wait while we set everything up", SwingConstants.CENTER);
        infoLabel2.setForeground(new Color(102, 69, 3));
        infoLabel2.setSize((int) (screenWidth / 1.5), screenHeight / 7);
        infoLabel2.setLocation(screenWidth / 2 - infoLabel2.getWidth() / 2,
                (int) (infoLabel1.getLocation().getY() + infoLabel1.getFont().getSize() + screenHeight / 25));
        SDG2Util.fixJLabelFontSize(infoLabel2);

        this.add(titleLabel);
        this.add(infoLabel1);
        this.add(infoLabel2);

        Thread animationThread = new Thread(new Runnable() {
            int dots = 0, repetitions = 0;


            public void run() {

                while (AdminLogInSuccesfulPanel.this.isEnabled()) {

                    if (dots++ == 3)
                        dots = 0;

                    String tempText = infoLabel2.getText().substring(0, 38);
                    for (int i = 0; i < dots; i++)
                        tempText += ".";
                    infoLabel2.setText(tempText);

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (repetitions == 2)
                        AdminWindow.getAdminWindow(null).changeScreen(ScreenType.MAIN_MENU);
                    repetitions++;
                }
            }
        });
        animationThread.start();
    }

    public static void main(String[] args) {
        JFrame testFrame = new JFrame();
        testFrame.setSize(800, 600);
        testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        testFrame.add(new AdminLogInSuccesfulPanel(800, 600, "Inaki"));
        testFrame.setVisible(true);
    }

}
