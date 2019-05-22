package com.spq.group6.client.gui.panels;

import com.spq.group6.client.controller.ClientController;
import com.spq.group6.client.gui.utils.SDG2Util;
import com.spq.group6.client.gui.utils.locale.LanguageManager;
import com.spq.group6.client.gui.utils.locale.LanguageSelector;
import com.spq.group6.client.gui.utils.voice.VoiceHelper;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.Locale;

/**
 * One of the panels used in the Client window
 */
public class LocaleSelectorPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    protected ClientController controller;
    protected JLabel titleLabel;
    protected FocusListener ttsFocusListener;
    protected JToggleButton ttsButton;
    protected int screenWidth, screenHeight;
    private JComboBox<LanguageSelector> selectLanguageCB;

    /**
     * Constructor to create a new Locale-Selector Panel.
     * <p>
     *
     * @param screenWidth  width of the screen
     * @param screenHeight height of the screen
     */
    public LocaleSelectorPanel(int screenWidth, int screenHeight) {

        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        setBackground(Color.WHITE);
        this.setLayout(null);
        this.controller = ClientController.getClientController();

        ttsFocusListener = new FocusListener() {


            public void focusLost(FocusEvent e) {

            }


            public void focusGained(FocusEvent e) {
                if (e.getSource() instanceof JButton) {
                    VoiceHelper.textToSpeech(LanguageManager.getMessage("Voice.LocaleSelectorPanel.button") + ((JButton) e.getSource()).getText());

                } else if (e.getSource() instanceof JToggleButton) {
                    VoiceHelper.textToSpeech(LanguageManager.getMessage("Voice.LocaleSelectorPanel.toggle") + ((JToggleButton) e.getSource()).getText());

                } else if (e.getSource() instanceof JComboBox<?>) {
                    VoiceHelper.textToSpeech(LanguageManager.getMessage("Voice.LocaleSelectorPanel.combobox") + ((JComboBox<?>) e.getSource()).getSelectedItem());

                } else if (e.getSource() instanceof JTextField) {
                    VoiceHelper.textToSpeech(LanguageManager.getMessage("Voice.LocaleSelectorPanel.textInput") + ((JTextField) e.getSource()).getName());

                } else if (e.getSource() instanceof JPasswordField) {
                    VoiceHelper.textToSpeech(LanguageManager.getMessage("Voice.LocaleSelectorPanel.passwordInput") + ((JPasswordField) e.getSource()).getName());
                }
            }
        };

        LanguageSelector tempLanguagesAvailable[] = LanguageManager.getLanguages();
        selectLanguageCB = new JComboBox<>(tempLanguagesAvailable);
        for (int i = 0; i < tempLanguagesAvailable.length; i++)
            if (tempLanguagesAvailable[i].getLocale().equals(LanguageManager.getLocale()))
                selectLanguageCB.setSelectedIndex(i);

        selectLanguageCB.setForeground(new Color(0, 102, 102));
        selectLanguageCB.setEditable(true);
        selectLanguageCB.getEditor().getEditorComponent().setBackground(Color.white);
        selectLanguageCB.getEditor().getEditorComponent().setForeground(new Color(0, 102, 102));
        selectLanguageCB.setBorder(new TitledBorder(""));
        selectLanguageCB.setOpaque(true);
        selectLanguageCB.setSize((int) (screenWidth / 8), screenHeight / 15);
        selectLanguageCB.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                @SuppressWarnings("unchecked")
                JComboBox<LanguageSelector> comboBox = (JComboBox<LanguageSelector>) e.getSource();
                Locale l = ((LanguageSelector) comboBox.getSelectedItem()).getLocale();
                LanguageManager.setLocale(l);
                updateComponentsText();
                LocaleSelectorPanel.this.bringSelectLanguageCBToFront();

            }
        });
        selectLanguageCB.addFocusListener(ttsFocusListener);

        if (VoiceHelper.isTtsON())
            ttsButton = new JToggleButton(LanguageManager.getMessage("LocaleSelectorPanel.ttsButton.soundOnText"), true);
        else
            ttsButton = new JToggleButton(LanguageManager.getMessage("LocaleSelectorPanel.ttsButton.soundOffText"), false);
        ttsButton.setForeground(new Color(0, 102, 102));
        ttsButton.setBackground(Color.white);
        TitledBorder title = BorderFactory.createTitledBorder("");
        title.setTitleColor(new Color(102, 82, 0));
        ttsButton.setBorder(title);
        ttsButton.setContentAreaFilled(false);
        ttsButton.setOpaque(true);
        ttsButton.setSize((int) (screenWidth / 8), screenHeight / 15);
        ttsButton.addItemListener(new ItemListener() {


            public void itemStateChanged(ItemEvent e) {
                if (ttsButton.isSelected()) {
                    ttsButton.setText(LanguageManager.getMessage("LocaleSelectorPanel.ttsButton.soundOnText"));
                } else {
                    ttsButton.setText(LanguageManager.getMessage("LocaleSelectorPanel.ttsButton.soundOffText"));
                }
                VoiceHelper.setTtsState(ttsButton.isSelected());

            }
        });
        ttsButton.addFocusListener(ttsFocusListener);

        this.add(selectLanguageCB);
        this.add(ttsButton);
    }

    public static void main(String[] args) {
        JFrame testFrame = new JFrame();
        testFrame.setSize(800, 600);
        testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        testFrame.add(new LocaleSelectorPanel(800, 600));
        testFrame.setVisible(true);
    }

    public void bringSelectLanguageCBToFront() {
        this.remove(titleLabel);
        this.remove(selectLanguageCB);
        this.remove(ttsButton);
        ttsButton.setLocation((int) (screenWidth / 1.55), titleLabel.getHeight() / 2 - selectLanguageCB.getHeight() / 2);
        selectLanguageCB.setLocation((int) (screenWidth / 1.25), titleLabel.getHeight() / 2 - selectLanguageCB.getHeight() / 2);
        this.repaint();
        this.add(ttsButton);
        this.add(selectLanguageCB);
        this.add(titleLabel);
    }

    protected void updateComponentsText() {
        titleLabel.setText(LanguageManager.getMessage("InitialPanel.infoLabel.text"));
        SDG2Util.fixJLabelFontSize(titleLabel);
    }

}
