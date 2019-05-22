package com.spq.group6.client.gui.panels;

import com.spq.group6.client.gui.ClientWindow;
import com.spq.group6.client.gui.actions.ActionDeleteProduct;
import com.spq.group6.client.gui.actions.ActionUpdateProduct;
import com.spq.group6.client.gui.elements.ButtonColumn;
import com.spq.group6.client.gui.elements.ProductJTableModel;
import com.spq.group6.client.gui.utils.SDG2Util;
import com.spq.group6.client.gui.utils.ScreenType;
import com.spq.group6.client.gui.utils.locale.LanguageManager;
import com.spq.group6.client.gui.utils.voice.VoiceHelper;
import com.spq.group6.server.data.Product;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * One of the panels used in the Client window
 */
public class UserProductsPanel extends LocaleSelectorPanel {

    private static final long serialVersionUID = 1L;
    private JLabel infoLabel;
    private JScrollPane productsTableScrollPane;
    private JTable productsTable;
    private JButton backButton;

    /**
     * Constructor to create a new User-Products Panel.
     * <p>
     * @param screenWidth width of the screen
     * @param screenHeight height of the screen
     */
    public UserProductsPanel(int screenWidth, int screenHeight) {

        super(screenWidth, screenHeight);

        titleLabel = new JLabel(LanguageManager.getMessage("UserProductsPanel.titleLabel.text"), SwingConstants.LEFT);
        titleLabel.setForeground(Color.white);
        titleLabel.setBackground(new Color(0, 204, 204));
        titleLabel.setOpaque(true);
        titleLabel.setSize(screenWidth, screenHeight / 7);
        titleLabel.setLocation(0, 0);
        SDG2Util.fixJLabelFontSize(titleLabel);

        infoLabel = new JLabel(LanguageManager.getMessage("UserProductsPanel.infoLabel.text"), SwingConstants.LEFT);
        infoLabel.setForeground(new Color(0, 102, 102));
        infoLabel.setSize((int) (screenWidth / 1.5), screenHeight / 8);
        infoLabel.setLocation(screenWidth / 20,
                (int) (titleLabel.getLocation().getY() + titleLabel.getFont().getSize() * 1.5));
        SDG2Util.fixJLabelFontSize(infoLabel);

        backButton = new JButton(LanguageManager.getMessage("General.backButton.text"));
        backButton.setForeground(new Color(0, 102, 102));
        backButton.setBackground(Color.white);
        backButton.setBorder(new TitledBorder(""));
        backButton.setContentAreaFilled(false);
        backButton.setOpaque(true);
        backButton.setSize(screenWidth / 8, screenHeight / 15);
        backButton.setLocation(infoLabel.getX(), (int) (screenHeight - (backButton.getHeight() * 2.5)));
        SDG2Util.fixJButtonFontSize(backButton);
        backButton.addActionListener(new ActionListener() {


            public void actionPerformed(ActionEvent e) {
                ClientWindow.getClientWindow().changeScreen(ScreenType.MAIN_MENU);
            }
        });
        backButton.addFocusListener(ttsFocusListener);

        String[] productsColumnNames = {LanguageManager.getMessage("UserProductsPanel.productsColumnNames.0"),
                LanguageManager.getMessage("UserProductsPanel.productsColumnNames.1"), "", ""};
        productsTable = new JTable(new ProductJTableModel(new Object[][]{}, productsColumnNames));
        updateProducts();
        productsTable.setRowHeight((int) (productsTable.getRowHeight() * 1.5));
        productsTable.getTableHeader().setOpaque(false);
        productsTable.getTableHeader().setBackground(new Color(234, 255, 255));
        productsTable.setBackground(Color.white);
        productsTable.setBorder(new MatteBorder(0, 0, 0, 0, Color.black));
        productsTable.setOpaque(true);
        productsTable.setForeground(new Color(0, 102, 102));

        productsTableScrollPane = new JScrollPane(productsTable);
        productsTableScrollPane.getViewport().setBackground(Color.WHITE);
        productsTableScrollPane.setBorder(new TitledBorder(""));
        productsTableScrollPane.setOpaque(true);
        productsTableScrollPane.setSize((int) (screenWidth - backButton.getWidth()), screenHeight / 2);
        productsTableScrollPane.setLocation(infoLabel.getX(),
                (int) (infoLabel.getLocation().getY() + infoLabel.getHeight()));


        this.add(titleLabel);
        this.add(infoLabel);
        this.add(productsTableScrollPane);
        this.add(backButton);

        bringSelectLanguageCBToFront();

        VoiceHelper.textToSpeech(LanguageManager.getMessage("Voice.UserProductsPanel.welcome"));

    }

    public static void main(String[] args) {
        JFrame testFrame = new JFrame();
        testFrame.setSize(800, 600);
        testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        testFrame.add(new UserProductsPanel(800, 600));
        testFrame.setVisible(true);
    }

    private void updateProducts() {
        String[] productsColumnNames = {LanguageManager.getMessage("UserProductsPanel.productsColumnNames.0"),
                LanguageManager.getMessage("UserProductsPanel.productsColumnNames.1"), "", ""};
        Object[][] productsData = new Object[][]{};
        if (controller.getCurrentUser() != null) {
            productsData = new Object[controller.getCurrentUserProducts().size() + 1][productsColumnNames.length];
            int i = 0;
            for (i = 0; i < controller.getCurrentUserProducts().size(); i++) {
                Product tempProduct = controller.getCurrentUserProducts().get(i);
                productsData[i][0] = tempProduct.getName();
                productsData[i][1] = tempProduct.getDescription();
                productsData[i][2] = LanguageManager.getMessage("UserProductsPanel.productsData.2");
                productsData[i][3] = LanguageManager.getMessage("UserProductsPanel.productsData.3");
            }
            productsData[i][0] = "";
            productsData[i][1] = "";
            productsData[i][2] = LanguageManager.getMessage("UserProductsPanel.productsData.2b");
            productsData[i][3] = "";
        }
        productsTable.setModel(new ProductJTableModel(productsData, productsColumnNames));
        @SuppressWarnings("unused")
        ButtonColumn updateButtonColumn = new ButtonColumn(productsTable, new ActionUpdateProduct(), 2);
        @SuppressWarnings("unused")
        ButtonColumn deleteButtonColumn = new ButtonColumn(productsTable, new ActionDeleteProduct(), 3);

        productsTable.getColumnModel().getColumn(1).setPreferredWidth(productsTable.getColumnModel().getColumn(1).getPreferredWidth() + 200);

        productsTable.revalidate();
        productsTable.repaint();
    }


    protected void updateComponentsText() {
        titleLabel.setText(LanguageManager.getMessage("UserProductsPanel.titleLabel.text"));
        SDG2Util.fixJLabelFontSize(titleLabel);
        infoLabel.setText(LanguageManager.getMessage("UserProductsPanel.infoLabel.text"));
        SDG2Util.fixJLabelFontSize(infoLabel);
        updateProducts();
        backButton.setText(LanguageManager.getMessage("General.backButton.text"));
        SDG2Util.fixJButtonFontSize(backButton);
    }

}
