package com.spq.group6.client.gui.panels;

import com.spq.group6.client.controller.ClientController;
import com.spq.group6.client.gui.ClientWindow;
import com.spq.group6.client.gui.actions.ActionBid;
import com.spq.group6.client.gui.elements.AuctionTimeLeftRunnable;
import com.spq.group6.client.gui.elements.ButtonColumn;
import com.spq.group6.client.gui.elements.MarketJTableModel;
import com.spq.group6.client.gui.utils.SDG2Util;
import com.spq.group6.client.gui.utils.SPQG6Util;
import com.spq.group6.client.gui.utils.ScreenType;
import com.spq.group6.server.data.Auction;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MarketPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JLabel titleLabel;
    private JLabel infoLabel;
    private JPanel searchPanel;
    private JLabel searchLabel, searchLabel2;
    private JComboBox<String> searchComboBox;
    private JTextField searchTF;
    private JButton searchButton;
    private JScrollPane auctionsTableScrollPane;
    private JTable auctionsTable;
    private JButton backButton;
    private JButton logOutButton;

    private String[] auctionsColumnNames = {"Prod. Name", "Description", "Private", "Highest Bid", "Time left", ""};


    private ClientController controller;
    private ArrayList<Thread> auctionsTimeLeftThread;
    private ArrayList<Auction> auctions;

    public MarketPanel(int screenWidth, int screenHeight) {

        setBackground(Color.WHITE);
        this.setLayout(null);

        controller = ClientController.getClientController();
        auctionsTimeLeftThread = new ArrayList<Thread>();

        titleLabel = new JLabel("Market", SwingConstants.LEFT);
        titleLabel.setForeground(Color.white);
        titleLabel.setBackground(new Color(0, 204, 204));
        titleLabel.setOpaque(true);
        titleLabel.setSize(screenWidth / 5, screenHeight / 15);
        titleLabel.setLocation(screenWidth / 15, screenHeight / 4 - titleLabel.getHeight() / 2);
        SDG2Util.fixJLabelFontSize(titleLabel);

        infoLabel = new JLabel("Here you can see others' auctions. Start bidding!", SwingConstants.LEFT);
        infoLabel.setForeground(new Color(0, 102, 102));
        infoLabel.setSize((int) (screenWidth / 1.5), screenHeight / 8);
        infoLabel.setLocation((int) titleLabel.getLocation().getX(),
                (int) (titleLabel.getLocation().getY() + titleLabel.getHeight()));
        SDG2Util.fixJLabelFontSize(infoLabel);

        // searching filter
        searchLabel = new JLabel("Search for auctions where");
        searchLabel.setForeground(new Color(0, 102, 102));
        searchComboBox = new JComboBox<>(new String[]{"Country", "Name"});
        searchComboBox.setForeground(new Color(0, 102, 102));
        searchComboBox.setEditable(true);
        searchComboBox.getEditor().getEditorComponent().setBackground(Color.white);
        searchComboBox.getEditor().getEditorComponent().setForeground(new Color(0, 102, 102));
        searchComboBox.setBorder(new TitledBorder(""));
        searchComboBox.setOpaque(true);
        searchLabel2 = new JLabel("is");
        searchLabel2.setForeground(new Color(0, 102, 102));
        searchTF = new JTextField(10);
        searchTF.setForeground(new Color(102, 69, 3));
        searchButton = new JButton("Search");
        searchButton.setForeground(new Color(0, 102, 102));
        searchButton.setBackground(Color.white);
        searchButton.setBorder(new TitledBorder(""));
        searchButton.setContentAreaFilled(false);
        searchButton.setOpaque(true);
        searchButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // stop previous threads
                if (auctionsTimeLeftThread != null)
                    for (int i = 0; i < auctionsTimeLeftThread.size(); i++)
                        auctionsTimeLeftThread.get(i).interrupt();

                switch (searchComboBox.getSelectedIndex()) {
                    case 0:
                        auctions = controller.searchAuctionByCountry(searchTF.getText());
                        break;
                    case 1:
                        auctions = controller.searchAuctionByProductName(searchTF.getText());
                        break;
                    default:
                        auctions = new ArrayList<>();
                }
                updateAuctions();
                if (auctions.size() == 0) {
                    JOptionPane.showConfirmDialog(MarketPanel.this, "No auctions found.", "Info", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showConfirmDialog(MarketPanel.this, "Auctions found succesfully.", "Info", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Color.white);
        searchPanel.setBorder(new MatteBorder(0, 0, 0, 0, Color.black));
        searchPanel.setOpaque(true);
        searchPanel.setForeground(new Color(0, 102, 102));
        searchPanel.setSize((int) (screenWidth / 1.2), screenHeight / 14);
        searchPanel.setLocation((int) infoLabel.getLocation().getX(),
                (int) (infoLabel.getLocation().getY() + infoLabel.getHeight()));

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
                    JOptionPane.showConfirmDialog(MarketPanel.this, "Error logging out.", "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);

            }
        });

        auctionsTable = new JTable(new MarketJTableModel(new Object[][]{}, auctionsColumnNames));
        auctionsTable.setRowHeight((int) (auctionsTable.getRowHeight() * 1.5));
        auctionsTable.getTableHeader().setOpaque(false);
        auctionsTable.getTableHeader().setBackground(new Color(234, 255, 255));
        auctionsTable.setBackground(Color.white);
        auctionsTable.setBorder(new MatteBorder(0, 0, 0, 0, Color.black));
        auctionsTable.setOpaque(true);
        auctionsTable.setForeground(new Color(0, 102, 102));
        auctionsTable.getColumnModel().getColumn(4).setPreferredWidth(auctionsTable.getColumnModel().getColumn(4).getPreferredWidth() + 100);

        auctionsTableScrollPane = new JScrollPane(auctionsTable);
        auctionsTableScrollPane.getViewport().setBackground(Color.WHITE);
        auctionsTableScrollPane.setBorder(new TitledBorder(""));
        auctionsTableScrollPane.setSize((int) (screenWidth - backButton.getLocation().getX() - (screenWidth - logOutButton.getLocation().getX()) + logOutButton.getWidth()),
                (int) (screenHeight / 2.25));
        auctionsTableScrollPane.setLocation((int) (titleLabel.getLocation().getX()),
                (int) (searchPanel.getLocation().getY() + searchPanel.getHeight()));


        this.add(titleLabel);
        this.add(infoLabel);
        searchPanel.add(searchLabel);
        searchPanel.add(searchComboBox);
        searchPanel.add(searchLabel2);
        searchPanel.add(searchTF);
        searchPanel.add(searchButton);
        this.add(searchPanel);
        this.add(auctionsTableScrollPane);
        this.add(backButton);
        this.add(logOutButton);
    }

    public void updateAuctions() {
        Object[][] auctionsData;
        if (auctions.size() == 0) {
            auctionsData = new Object[][]{};

        } else {
            auctionsData = new Object[auctions.size()][auctionsColumnNames.length];
            int i = 0;
            stopCountdowns();
            auctionsTimeLeftThread = new ArrayList<>();
            for (i = 0; i < auctions.size(); i++) {
                Auction tempAuction = auctions.get(i);
                auctionsData[i][0] = tempAuction;
                auctionsData[i][1] = tempAuction.getProduct().getDescription();
                if (tempAuction.getPassword() != null && !tempAuction.getPassword().equals(""))
                    auctionsData[i][2] = "Yes";
                else
                    auctionsData[i][2] = "No";
                if (tempAuction.getHighestBid() == null)
                    auctionsData[i][3] = 0 + " (initial:" + tempAuction.getInitialPrice() + ")";
                else
                    auctionsData[i][3] = tempAuction.getHighestBid().getAmount();
                auctionsData[i][4] = SPQG6Util.getLocalDateTimeDifferenceFromNow(tempAuction.getDayLimit().toLocalDateTime());
                auctionsData[i][5] = "Bid";
                Thread tempAuctionThread = new Thread(new AuctionTimeLeftRunnable(auctionsTable, i, tempAuction.getDayLimit().toLocalDateTime()));
                auctionsTimeLeftThread.add(tempAuctionThread);
            }

        }
        auctionsTable.setModel(new MarketJTableModel(auctionsData, auctionsColumnNames));
        auctionsTable.getColumnModel().getColumn(4).setPreferredWidth(auctionsTable.getColumnModel().getColumn(4).getPreferredWidth() + 100);

        ButtonColumn bidButtonColumn = new ButtonColumn(auctionsTable, new ActionBid(), 5);

        // start countdown threads
        for (int i = 0; i < auctionsData.length; i++) {
            auctionsTimeLeftThread.get(i).start();
        }

        auctionsTable.revalidate();
        auctionsTable.repaint();
    }

    public static void main(String[] args) {
        JFrame testFrame = new JFrame();
        testFrame.setSize(800, 600);
        testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        testFrame.add(new UserAuctionsPanel(800, 600));
        testFrame.setVisible(true);
    }

    public ArrayList<Auction> getAuctions() {
        return auctions;
    }

    public void setAuctions(ArrayList<Auction> auctions) {
        this.auctions = auctions;
    }

    private void stopCountdowns() {
        for (Thread thread : auctionsTimeLeftThread) {
            thread.interrupt();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
