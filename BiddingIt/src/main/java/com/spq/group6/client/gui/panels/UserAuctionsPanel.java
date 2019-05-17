package com.spq.group6.client.gui.panels;

import com.github.lgooddatepicker.tableeditors.DateTimeTableEditor;
import com.spq.group6.client.controller.ClientController;
import com.spq.group6.client.gui.ClientWindow;
import com.spq.group6.client.gui.actions.ActionCreateAuction;
import com.spq.group6.client.gui.elements.AuctionJTableModel;
import com.spq.group6.client.gui.elements.ButtonColumn;
import com.spq.group6.client.gui.utils.SDG2Util;
import com.spq.group6.client.gui.utils.ScreenType;
import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.Product;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.List;

public class UserAuctionsPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JLabel titleLabel;
    private JLabel infoLabel;
    private JScrollPane auctionsTableScrollPane;
    private JTable auctionsTable;
    private JButton backButton;
    private JButton logOutButton;
    private int screenWidth, screenHeight;
    private String[] auctionsColumnNames = {"Prod. Name", "Password", "Initial Price", "Highest Bid", "Status", "Day Limit", ""};

    private ClientController controller;
    private List<Auction> userAuctions;

    public UserAuctionsPanel(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        setBackground(Color.WHITE);
        this.setLayout(null);
        this.controller = ClientController.getClientController();

        titleLabel = new JLabel("My auctions", SwingConstants.LEFT);
        titleLabel.setForeground(Color.white);
        titleLabel.setBackground(new Color(0, 204, 204));
        titleLabel.setOpaque(true);
        titleLabel.setSize(screenWidth / 4, screenHeight / 15);
        titleLabel.setLocation((int) (screenWidth / 2 - titleLabel.getWidth() * 1.75), screenHeight / 4 - titleLabel.getHeight() / 2);
        SDG2Util.fixJLabelFontSize(titleLabel);

        infoLabel = new JLabel("Here you can see your auctions. Start selling and check your earnings!", SwingConstants.LEFT);
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
                    JOptionPane.showConfirmDialog(UserAuctionsPanel.this, "Error logging out.", "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);

            }
        });

        userAuctions = controller.getCurrentUserAuctions();
        auctionsTable = new JTable(new AuctionJTableModel(new Object[][]{}, auctionsColumnNames, this));
        updateAuctions();
        auctionsTable.setRowHeight((int)(auctionsTable.getRowHeight()*1.5));
        auctionsTable.getTableHeader().setOpaque(false);
        auctionsTable.getTableHeader().setBackground(new Color(234, 255, 255));
        auctionsTable.setBackground(Color.white);
        auctionsTable.setBorder(new MatteBorder(0, 0, 0, 0, Color.black));
        auctionsTable.setOpaque(true);
        auctionsTable.setForeground(new Color(0, 102, 102));

        auctionsTableScrollPane = new JScrollPane(auctionsTable);
        auctionsTableScrollPane.getViewport().setBackground(Color.WHITE);
        auctionsTableScrollPane.setBorder(new TitledBorder(""));
        auctionsTableScrollPane.setOpaque(true);
        auctionsTableScrollPane.setSize((int) (screenWidth - backButton.getLocation().getX() - (screenWidth - logOutButton.getLocation().getX()) + logOutButton.getWidth()), screenHeight / 2);
        auctionsTableScrollPane.setLocation((int) (titleLabel.getLocation().getX()),
                (int) (infoLabel.getLocation().getY() + infoLabel.getHeight()));

        this.add(auctionsTableScrollPane);
        this.add(titleLabel);
        this.add(infoLabel);
        this.add(backButton);
        this.add(logOutButton);

    }

    public void updateAuctions() {
        Object[][] auctionsData = null;
        if (controller.getCurrentUser() != null) {
            auctionsData = new Object[userAuctions.size() + 1][auctionsColumnNames.length];
            int i = 0;
            for (i = 0; i < userAuctions.size(); i++) {
                Auction tempAuction = userAuctions.get(i);
                auctionsData[i][0] = tempAuction.getProduct().getName();
                auctionsData[i][1] = tempAuction.getPassword();
                auctionsData[i][2] = tempAuction.getInitialPrice();
                if (tempAuction.getHighestBid() == null)
                    auctionsData[i][3] = 0;
                else {
                    auctionsData[i][3] = tempAuction.getHighestBid().getAmount();
                }
                if (tempAuction.isOpen())
                    auctionsData[i][4] = "Open";
                else
                    auctionsData[i][4] = "Closed";
                auctionsData[i][5] = tempAuction.getDayLimit().toLocalDateTime();
                auctionsData[i][6] = "";
            }
            auctionsData[i][0] = "";
            auctionsData[i][1] = "";
            auctionsData[i][2] = "";
            auctionsData[i][3] = "-";
            auctionsData[i][4] = "-";
            auctionsData[i][5] = "";
            auctionsData[i][6] = "Create";
        }
        auctionsTable.setModel(new AuctionJTableModel(auctionsData, auctionsColumnNames, this));
        auctionsTable.getColumnModel().getColumn(5).setPreferredWidth(auctionsTable.getColumnModel().getColumn(5).getPreferredWidth() + 150);
        ButtonColumn createButtonColumn = new ButtonColumn(auctionsTable, new ActionCreateAuction(), 6);

        // set column 0 to combobox
        updateUserProductsComboBox();

        // set column 4 to limit day
        auctionsTable.setDefaultEditor(LocalDateTime.class, new DateTimeTableEditor());
        auctionsTable.setDefaultRenderer(LocalDateTime.class, new DateTimeTableEditor());
        auctionsTable.getColumnModel().getColumn(5).setCellEditor(auctionsTable.getDefaultEditor(LocalDateTime.class));
        auctionsTable.getColumnModel().getColumn(5).setCellRenderer(auctionsTable.getDefaultRenderer(LocalDateTime.class));

        auctionsTable.revalidate();
        auctionsTable.repaint();
    }

    public void updateUserProductsComboBox() {
    	userAuctions = controller.getCurrentUserAuctions();
        List<Product> userProductsNotAuction = controller.getCurrentUserProducts(); // get all user products
        for (int i = userProductsNotAuction.size() - 1; i >= 0; i--) // remove products already in an auction
            for (int j = 0; j < userAuctions.size(); j++)
                if (userProductsNotAuction.get(i).equals(userAuctions.get(j).getProduct()))
                    userProductsNotAuction.remove(i);
        Product[] userProductsNotAuctionArray = new Product[userProductsNotAuction.size()];
        for (int i = 0; i < userProductsNotAuction.size(); i++)
            userProductsNotAuctionArray[i] = userProductsNotAuction.get(i);
        JComboBox<Product> prodComboBox = new JComboBox<Product>(userProductsNotAuctionArray);
        auctionsTable.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(prodComboBox));
    }

    public static void main(String[] args) {
        JFrame testFrame = new JFrame();
        testFrame.setSize(800, 600);
        testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        testFrame.add(new UserAuctionsPanel(800, 600));
        testFrame.setVisible(true);
    }

    public List<Auction> getUserAuctions() {
        return userAuctions;
    }

    public void setUserAuctions(List<Auction> userAuctions) {
        this.userAuctions = userAuctions;
    }
}
