package com.spq.group6.client.gui.panels;

import com.spq.group6.client.controller.ClientController;
import com.spq.group6.client.gui.ClientWindow;
import com.spq.group6.client.gui.actions.ActionDeleteProduct;
import com.spq.group6.client.gui.actions.ActionUpdateProduct;
import com.spq.group6.client.gui.elements.ButtonColumn;
import com.spq.group6.client.gui.elements.ProductJTableModel;
import com.spq.group6.client.gui.utils.SDG2Util;
import com.spq.group6.client.gui.utils.ScreenType;
import com.spq.group6.server.data.Product;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserProductsPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JLabel titleLabel;
    private JLabel infoLabel;
    private JScrollPane productsTableScrollPane;
    private JTable productsTable;
    private JButton backButton;
    private JButton logOutButton;

    @SuppressWarnings("unused")
    private ClientController controller;

    public UserProductsPanel(int screenWidth, int screenHeight) {

        setBackground(Color.WHITE);
        this.setLayout(null);

        controller = ClientController.getClientController();

        titleLabel = new JLabel("My products", SwingConstants.LEFT);
        titleLabel.setForeground(Color.white);
        titleLabel.setBackground(new Color(0, 204, 204));
        titleLabel.setOpaque(true);
        titleLabel.setSize(screenWidth / 4, screenHeight / 15);
        titleLabel.setLocation((int) (screenWidth / 2 - titleLabel.getWidth() * 1.75), screenHeight / 4 - titleLabel.getHeight() / 2);
        SDG2Util.fixJLabelFontSize(titleLabel);

        infoLabel = new JLabel("<html>Here you can see your products. Start selling and check your earnings!"
                + "<br/>You can edit the name and description and then click save.<br/>You can also delete your products.</html>", SwingConstants.LEFT);
        infoLabel.setForeground(new Color(0, 102, 102));
        infoLabel.setSize((int) (screenWidth / 1.5), screenHeight / 8);
        infoLabel.setLocation((int) titleLabel.getLocation().getX(),
                (int) (titleLabel.getLocation().getY() + titleLabel.getHeight()));
        SDG2Util.fixJLabelFontSize(infoLabel);

        backButton = new JButton("Back");
        backButton.setForeground(new Color(0, 102, 102));
        backButton.setBackground(Color.white);
        backButton.setBorder(new TitledBorder(""));
        backButton.setContentAreaFilled(false);
        backButton.setOpaque(true);
        backButton.setSize(screenWidth / 8, screenHeight / 15);
        backButton.setLocation(backButton.getWidth() / 2,
                screenHeight / 10);
        SDG2Util.fixJButtonFontSize(backButton);
        backButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ClientWindow.getClientWindow().changeScreen(ScreenType.MAIN_MENU);
            }
        });

        logOutButton = new JButton("Log out");
        logOutButton.setForeground(new Color(0, 102, 102));
        logOutButton.setBackground(Color.white);
        logOutButton.setBorder(new TitledBorder(""));
        logOutButton.setContentAreaFilled(false);
        logOutButton.setOpaque(true);
        logOutButton.setSize(backButton.getSize());
        logOutButton.setLocation((int) (screenWidth - logOutButton.getWidth() * 1.5),
                (int) backButton.getLocation().getY());
        SDG2Util.fixJButtonFontSize(logOutButton);
        logOutButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (controller.logOut())
                    ClientWindow.getClientWindow().changeScreen(ScreenType.INITIAL);
                else
                    JOptionPane.showConfirmDialog(UserProductsPanel.this, "Error logging out.", "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);

            }
        });


//		User user1 = new User("Alejandro", "1234", "ES");
//		Product[] products = new Product[2];
//		products[0] = new Product(user1, "Product1", "desc1");
//		products[1] = new Product(user1, "Product2", "desc2");
//		user1.setOwnedProducts(products);

        String[] productsColumnNames = {"Name", "Description", "", ""};
        Object[][] productsData = null;
        if (controller.getCurrentUser() != null) {
            productsData = new Object[controller.getCurrentUserProducts().size() + 1][productsColumnNames.length];
            int i = 0;
            for (i = 0; i < controller.getCurrentUserProducts().size(); i++) {
                Product tempProduct = controller.getCurrentUserProducts().get(i);
                productsData[i][0] = tempProduct.getName();
                productsData[i][1] = tempProduct.getDescription();
                productsData[i][2] = "Save";
                productsData[i][3] = "Delete";
            }
            productsData[i][0] = "";
            productsData[i][1] = "";
            productsData[i][2] = "Create";
            productsData[i][3] = "";
        }
        productsTable = new JTable(new ProductJTableModel(productsData, productsColumnNames));
        productsTable.setBackground(Color.white);
        productsTable.setBorder(new MatteBorder(0, 0, 0, 0, Color.black));
        productsTable.setOpaque(true);
        productsTable.setForeground(new Color(0, 102, 102));
        productsTable.getColumnModel().getColumn(1).setPreferredWidth(productsTable.getColumnModel().getColumn(1).getPreferredWidth() + 200);

        @SuppressWarnings("unused")
        ButtonColumn updateButtonColumn = new ButtonColumn(productsTable, new ActionUpdateProduct(), 2);
        @SuppressWarnings("unused")
        ButtonColumn deleteButtonColumn = new ButtonColumn(productsTable, new ActionDeleteProduct(), 3);

        productsTableScrollPane = new JScrollPane(productsTable);
        productsTableScrollPane.getViewport().setBackground(Color.WHITE);
        productsTableScrollPane.setBorder(new TitledBorder(""));
        productsTableScrollPane.setOpaque(true);
        productsTableScrollPane.setSize((int) (screenWidth - backButton.getLocation().getX() - (screenWidth - logOutButton.getLocation().getX()) + logOutButton.getWidth()), screenHeight / 2);
        productsTableScrollPane.setLocation((int) (titleLabel.getLocation().getX()),
                (int) (infoLabel.getLocation().getY() + infoLabel.getHeight()));


        this.add(titleLabel);
        this.add(infoLabel);
        this.add(productsTableScrollPane);
        this.add(backButton);
        this.add(logOutButton);
    }

    public static void main(String[] args) {
        JFrame testFrame = new JFrame();
        testFrame.setSize(800, 600);
        testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        testFrame.add(new UserProductsPanel(800, 600));
        testFrame.setVisible(true);
    }

}
