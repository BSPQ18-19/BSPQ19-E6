package com.spq.group6.client.gui.panels;

import com.spq.group6.client.gui.ClientWindow;
import com.spq.group6.client.gui.utils.SDG2Util;
import com.spq.group6.client.gui.utils.ScreenType;
import com.spq.group6.client.gui.utils.locale.LanguageManager;
import com.spq.group6.client.gui.utils.voice.VoiceHelper;

import javax.swing.*;
import java.awt.*;

public class LogInSuccesfulPanel extends LocaleSelectorPanel {

    private static final long serialVersionUID = 1L;
    private JLabel infoLabel1;
    private JLabel infoLabel2;
    private String username;

    public LogInSuccesfulPanel(int screenWidth, int screenHeight, String username) {

        super(screenWidth, screenHeight);
        this.username = username;

        if (!username.isEmpty())
            titleLabel = new JLabel(LanguageManager.getMessage("LogInSuccesfulPanel.titleLabel.text") + " " + username + "! :)", SwingConstants.LEFT);
        else
            titleLabel = new JLabel(LanguageManager.getMessage("LogInSuccesfulPanel.titleLabel.text") + "! :)", SwingConstants.LEFT);
        titleLabel.setForeground(Color.white);
        titleLabel.setBackground(new Color(0, 204, 204));
        titleLabel.setSize(screenWidth, screenHeight / 6);
        titleLabel.setLocation(0, 0);
        SDG2Util.fixJLabelFontSize(titleLabel);

        infoLabel1 = new JLabel(LanguageManager.getMessage("LogInSuccesfulPanel.infoLabel1.text"), SwingConstants.CENTER);
        infoLabel1.setForeground(new Color(0, 102, 102));
        infoLabel1.setSize((int) (screenWidth / 1.5), screenHeight / 7);
        infoLabel1.setLocation(screenWidth / 2 - infoLabel1.getWidth() / 2, screenHeight / 2 - infoLabel1.getHeight() / 2);
        SDG2Util.fixJLabelFontSize(infoLabel1);

        String infoLabel2Clean = LanguageManager.getMessage("LogInSuccesfulPanel.infoLabel2.text");
        infoLabel2 = new JLabel(infoLabel2Clean, SwingConstants.CENTER);
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

                while (LogInSuccesfulPanel.this.isEnabled()) {

                    if (dots++ == 2)
                        dots = 0;

                    String tempText = infoLabel2Clean;
                    for (int i = 0; i < dots; i++)
                        tempText += ".";
                    infoLabel2.setText(tempText);

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (repetitions == 2)
                        ClientWindow.getClientWindow().changeScreen(ScreenType.MAIN_MENU);
                    repetitions++;
                }
            }
        });
        animationThread.start();

        bringSelectLanguageCBToFront();

        VoiceHelper.textToSpeech(LanguageManager.getMessage("Voice.LogInSuccesfulPanel.welcome"));

    }

    public static void main(String[] args) {
        JFrame testFrame = new JFrame();
        testFrame.setSize(800, 600);
        testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        testFrame.add(new LogInSuccesfulPanel(800, 600, "Alejandro"));
        testFrame.setVisible(true);
    }


    protected void updateComponentsText() {
        if (!username.isEmpty())
            titleLabel.setText(LanguageManager.getMessage("LogInSuccesfulPanel.titleLabel.text") + " " + username + "! :)");
        else
            titleLabel.setText(LanguageManager.getMessage("LogInSuccesfulPanel.titleLabel.text") + "! :)");
        SDG2Util.fixJLabelFontSize(titleLabel);
        infoLabel1.setText(LanguageManager.getMessage("LogInSuccesfulPanel.infoLabel1.text"));
        SDG2Util.fixJLabelFontSize(infoLabel1);
        infoLabel2.setText(LanguageManager.getMessage("LogInSuccesfulPanel.infoLabel2.text"));
        SDG2Util.fixJLabelFontSize(infoLabel2);

    }

}
