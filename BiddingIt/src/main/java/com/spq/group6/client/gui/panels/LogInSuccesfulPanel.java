package com.spq.group6.client.gui.panels;

import com.spq.group6.client.gui.ClientWindow;
import com.spq.group6.client.gui.utils.SDG2Util;
import com.spq.group6.client.gui.utils.ScreenType;

import javax.swing.*;

public class LogInSuccesfulPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JLabel titleLabel;
    private JLabel infoLabel1;
    private JLabel infoLabel2;

    public LogInSuccesfulPanel(int screenWidth, int screenHeight, String username) {

        this.setLayout(null);

        if (!username.isEmpty())
            titleLabel = new JLabel("Welcome " + username + "! :)", SwingConstants.LEFT);
        else
            titleLabel = new JLabel("Welcome! :)", SwingConstants.LEFT);
        titleLabel.setSize(screenWidth / 2, screenHeight / 6);
        titleLabel.setLocation(screenWidth / 8, screenHeight / 7 - titleLabel.getHeight() / 2);
        SDG2Util.fixJLabelFontSize(titleLabel);

        infoLabel1 = new JLabel("You have logged in succesfully.", SwingConstants.CENTER);
        infoLabel1.setSize((int) (screenWidth / 1.5), screenHeight / 7);
        infoLabel1.setLocation(screenWidth / 2 - infoLabel1.getWidth() / 2, screenHeight / 2 - infoLabel1.getHeight() / 2);
        SDG2Util.fixJLabelFontSize(infoLabel1);

        infoLabel2 = new JLabel("Please wait while we set everything up", SwingConstants.CENTER);
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

                while (LogInSuccesfulPanel.this.isEnabled()) {

                    if (dots++ == 3)
                        dots = 0;

                    String tempText = infoLabel2.getText().substring(0, 38);
                    for (int i = 0; i < dots; i++)
                        tempText += ".";
                    infoLabel2.setText(tempText);

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    if (repetitions == 2)
                        ClientWindow.getClientWindow().changeScreen(ScreenType.MAIN_MENU);
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
        testFrame.add(new LogInSuccesfulPanel(800, 600, "Alejandro"));
        testFrame.setVisible(true);
    }

}
