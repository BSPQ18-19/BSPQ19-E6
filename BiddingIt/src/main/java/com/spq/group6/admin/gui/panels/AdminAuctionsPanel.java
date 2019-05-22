package com.spq.group6.admin.gui.panels;

import com.github.lgooddatepicker.tableeditors.DateTimeTableEditor;
import com.spq.group6.admin.controller.AdminController;
import com.spq.group6.admin.gui.AdminWindow;
import com.spq.group6.admin.gui.actions.ActionDeleteAuction;
import com.spq.group6.admin.gui.elements.AuctionJTableModel;
import com.spq.group6.admin.gui.elements.AuctionTimeLeftRunnable;
import com.spq.group6.admin.gui.elements.ButtonColumn;
import com.spq.group6.admin.gui.utils.SDG2Util;
import com.spq.group6.admin.gui.utils.SPQG6Util;
import com.spq.group6.admin.gui.utils.ScreenType;
import com.spq.group6.server.data.Auction;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * One of the panels used in the Admin window
 */
public class AdminAuctionsPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JLabel titleLabel;
    private JPanel searchPanel;
    private JButton searchButton;
    private JScrollPane auctionsTableScrollPane;
    private JTable auctionsTable;
    private JButton backButton;
    private JButton logOutButton;
    private List<Auction> auctions;
    private Object[][] auctionsData;

    private ArrayList<Thread> auctionsTimeLeftThread;

    /**
     * Constructor to create a new Auction Panel.
     * <p>
     *
     * @param screenWidth  width of the screen
     * @param screenHeight height of the screen
     * @param controller   the controller that uses the Admin Window
     */
    public AdminAuctionsPanel(int screenWidth, int screenHeight, AdminController controller) {
        setBackground(Color.white);
        this.setLayout(null);

        titleLabel = new JLabel("Administrating auctions...", SwingConstants.LEFT);
        titleLabel.setForeground(new Color(0, 204, 204));
        titleLabel.setBackground(Color.WHITE);
        titleLabel.setOpaque(true);
        titleLabel.setSize((int) (screenWidth * 0.5), screenHeight / 15);
        titleLabel.setLocation(screenWidth / 15, screenHeight / 4 - titleLabel.getHeight() / 2);
        SDG2Util.fixJLabelFontSize(titleLabel);

        String[] auctionsColumnNames = {"Prod. Name", "Description", "Highest Bid", "Time left", ""};


        Thread animationThread = new Thread(new Runnable() {
            int dots = 0;


            public void run() {

                while (AdminAuctionsPanel.this.isEnabled()) {

                    if (dots++ == 3)
                        dots = 0;

                    String tempText = titleLabel.getText().substring(0, 23);
                    for (int i = 0; i < dots; i++)
                        tempText += ".";
                    titleLabel.setText(tempText);

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });
        animationThread.start();


        searchButton = new JButton("Update");
        searchButton.setForeground(new Color(0, 102, 102));
        searchButton.setBackground(Color.white);
        searchButton.setBorder(new TitledBorder(""));
        searchButton.setContentAreaFilled(false);
        searchButton.setOpaque(true);
        searchButton.addActionListener(new ActionListener() {


            public void actionPerformed(ActionEvent e) {
                // stop previous threads
                if (auctionsTimeLeftThread != null)
                    for (int i = 0; i < auctionsTimeLeftThread.size(); i++)
                        auctionsTimeLeftThread.get(i).interrupt();

                auctionsData = null;
                auctions = controller.getAllAuctions();

                Object[][] auctionsData = null;
                ArrayList<Auction> auctions = controller.getAllAuctions();
                if (auctions.size() == 0) {
                    auctionsData = new Object[][]{};
                    JOptionPane.showConfirmDialog(AdminAuctionsPanel.this, "No auctions found.", "Info", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);

                } else {
                    auctionsData = new Object[auctions.size()][auctionsColumnNames.length];
                    int i = 0;
                    auctionsTimeLeftThread = new ArrayList<>();
                    for (i = 0; i < auctions.size(); i++) {
                        Auction tempAuction = auctions.get(i);
                        auctionsData[i][0] = tempAuction;
                        auctionsData[i][1] = tempAuction.getProduct().getDescription();
                        if (tempAuction.getHighestBid() == null)
                            auctionsData[i][2] = 0 + " (initial:" + tempAuction.getInitialPrice() + ")";
                        else
                            auctionsData[i][2] = tempAuction.getHighestBid().getAmount();
                        auctionsData[i][3] = SPQG6Util.getLocalDateTimeDifferenceFromNow(tempAuction.getDayLimit().toLocalDateTime());
                        auctionsData[i][4] = "Delete Auction";

                        Thread tempAuctionThread = new Thread(new AuctionTimeLeftRunnable(auctionsTable, i, tempAuction.getDayLimit().toLocalDateTime()));
                        auctionsTimeLeftThread.add(tempAuctionThread);
                    }
                    JOptionPane.showConfirmDialog(AdminAuctionsPanel.this, "Auctions found succesfully.", "Info", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);

                }
                auctionsTable.setModel(new AuctionJTableModel(auctionsData, auctionsColumnNames, controller, auctions));
                auctionsTable.getColumnModel().getColumn(3).setPreferredWidth(auctionsTable.getColumnModel().getColumn(3).getPreferredWidth() + 100);

                @SuppressWarnings("unused")
                ButtonColumn deleteAuctionButtonColumn = new ButtonColumn(auctionsTable, new ActionDeleteAuction(), 4);

                // start countdown threads
                for (int i = 0; i < auctionsData.length; i++)
                    auctionsTimeLeftThread.get(i).start();
            }
        });
        searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Color.white);
        searchPanel.setSize((int) (screenWidth / 1.2), screenHeight / 14);
        searchPanel.setLocation((int) titleLabel.getLocation().getX(),
                (int) (titleLabel.getLocation().getY() + titleLabel.getHeight()));

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


            public void actionPerformed(ActionEvent e) {
                AdminWindow.getAdminWindow(null).changeScreen(ScreenType.MAIN_MENU);
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


            public void actionPerformed(ActionEvent e) {
                if (controller.logOut())
                    AdminWindow.getAdminWindow(null).changeScreen(ScreenType.LOG_IN);
                else
                    JOptionPane.showConfirmDialog(AdminAuctionsPanel.this, "Error logging out.", "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);

            }
        });

        auctionsTable = new JTable(new AuctionJTableModel(new Object[][]{}, auctionsColumnNames, controller, new ArrayList<Auction>()));
        auctionsTable.setRowHeight((int) (auctionsTable.getRowHeight() * 1.5));
        auctionsTable.getTableHeader().setOpaque(false);
        auctionsTable.getTableHeader().setBackground(new Color(234, 255, 255));
        auctionsTable.setBackground(Color.white);
        auctionsTable.setBorder(new MatteBorder(0, 0, 0, 0, Color.black));
        auctionsTable.setOpaque(true);
        auctionsTable.setForeground(new Color(0, 102, 102));
        auctionsTable.getColumnModel().getColumn(3).setPreferredWidth(auctionsTable.getColumnModel().getColumn(3).getPreferredWidth() + 100);

        // set column 4 to limit day
        auctionsTable.setDefaultEditor(LocalDateTime.class, new DateTimeTableEditor());
        auctionsTable.setDefaultRenderer(LocalDateTime.class, new DateTimeTableEditor());
        auctionsTable.getColumnModel().getColumn(4).setCellEditor(auctionsTable.getDefaultEditor(LocalDateTime.class));
        auctionsTable.getColumnModel().getColumn(4).setCellRenderer(auctionsTable.getDefaultRenderer(LocalDateTime.class));

        auctionsTableScrollPane = new JScrollPane(auctionsTable);
        auctionsTableScrollPane.getViewport().setBackground(Color.WHITE);
        auctionsTableScrollPane.setBorder(new TitledBorder(""));
        auctionsTableScrollPane.setSize((int) (screenWidth - backButton.getLocation().getX() - (screenWidth - logOutButton.getLocation().getX()) + logOutButton.getWidth()),
                (int) (screenHeight / 2.25));
        auctionsTableScrollPane.setLocation((int) (titleLabel.getLocation().getX()),
                (int) (searchPanel.getLocation().getY() + searchPanel.getHeight()));


        this.add(titleLabel);
        searchPanel.add(searchButton);
        this.add(searchPanel);
        this.add(auctionsTableScrollPane);
        this.add(backButton);
        this.add(logOutButton);
    }

    public static void main(String[] args) {
        JFrame testFrame = new JFrame();
        testFrame.setSize(800, 600);
        testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        try {
            testFrame.add(new AdminAuctionsPanel(800, 600, new AdminController()));
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        testFrame.setVisible(true);
    }

}
