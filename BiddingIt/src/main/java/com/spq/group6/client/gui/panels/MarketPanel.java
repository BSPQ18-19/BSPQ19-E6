package com.spq.group6.client.gui.panels;

import com.spq.group6.client.gui.ClientWindow;
import com.spq.group6.client.gui.actions.ActionBid;
import com.spq.group6.client.gui.elements.AuctionTimeLeftRunnable;
import com.spq.group6.client.gui.elements.ButtonColumn;
import com.spq.group6.client.gui.elements.MarketJTableModel;
import com.spq.group6.client.gui.utils.SDG2Util;
import com.spq.group6.client.gui.utils.SPQG6Util;
import com.spq.group6.client.gui.utils.ScreenType;
import com.spq.group6.client.gui.utils.locale.LanguageManager;
import com.spq.group6.client.gui.utils.voice.VoiceHelper;
import com.spq.group6.server.data.Auction;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * One of the panels used in the Client window
 */
public class MarketPanel extends LocaleSelectorPanel {

    private static final long serialVersionUID = 1L;
    private JLabel infoLabel;
    private JPanel searchPanel;
    private JLabel searchLabel, searchLabel2;
    private JComboBox<String> searchComboBox;
    private JTextField searchTF;
    private JButton searchButton;
    private JScrollPane auctionsTableScrollPane;
    private JTable auctionsTable;
    private JButton backButton;

    private String[] auctionsColumnNames;

    private ArrayList<Thread> auctionsTimeLeftThread;
    private ArrayList<Auction> auctions;

    /**
     * Constructor to create a new Market Panel.
     * <p>
     *
     * @param screenWidth  width of the screen
     * @param screenHeight height of the screen
     */
    public MarketPanel(int screenWidth, int screenHeight) {

        super(screenWidth, screenHeight);

        auctionsTimeLeftThread = new ArrayList<Thread>();
        auctions = controller.getAllAuctions();

        titleLabel = new JLabel(LanguageManager.getMessage("MarketPanel.titleLabel.text"), SwingConstants.LEFT);
        titleLabel.setForeground(Color.white);
        titleLabel.setBackground(new Color(0, 204, 204));
        titleLabel.setOpaque(true);
        titleLabel.setSize(screenWidth, screenHeight / 7);
        titleLabel.setLocation(0, 0);
        SDG2Util.fixJLabelFontSize(titleLabel);

        infoLabel = new JLabel(LanguageManager.getMessage("MarketPanel.infoLabel.text"), SwingConstants.LEFT);
        infoLabel.setForeground(new Color(0, 102, 102));
        infoLabel.setSize((int) (screenWidth / 1.5), screenHeight / 8);
        infoLabel.setLocation(screenWidth / 20,
                (int) (titleLabel.getLocation().getY() + titleLabel.getHeight()));
        SDG2Util.fixJLabelFontSize(infoLabel);

        // searching filter
        searchLabel = new JLabel(LanguageManager.getMessage("MarketPanel.searchLabel.text"));
        searchLabel.setForeground(new Color(0, 102, 102));

        searchComboBox = new JComboBox<>(new String[]{LanguageManager.getMessage("MarketPanel.comboBox.option1"),
                LanguageManager.getMessage("MarketPanel.comboBox.option2")});
        searchComboBox.setForeground(new Color(0, 102, 102));
        searchComboBox.setEditable(true);
        searchComboBox.getEditor().getEditorComponent().setBackground(Color.white);
        searchComboBox.getEditor().getEditorComponent().setForeground(new Color(0, 102, 102));
        searchComboBox.setBorder(new TitledBorder(""));
        searchComboBox.setOpaque(true);
        searchComboBox.addFocusListener(ttsFocusListener);

        searchLabel2 = new JLabel(LanguageManager.getMessage("MarketPanel.searchLabel2.text"));
        searchLabel2.setForeground(new Color(0, 102, 102));

        searchTF = new JTextField(10);
        searchTF.setName("search");
        searchTF.setForeground(new Color(102, 69, 3));
        searchTF.addFocusListener(ttsFocusListener);

        searchButton = new JButton(LanguageManager.getMessage("MarketPanel.searchButton.text"));
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
                    JOptionPane.showConfirmDialog(MarketPanel.this, LanguageManager.getMessage("MarketPanel.confirmDialog1"),
                            "Info", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showConfirmDialog(MarketPanel.this, LanguageManager.getMessage("MarketPanel.confirmDialog2"),
                            "Info", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        searchButton.addFocusListener(ttsFocusListener);

        searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Color.white);
        searchPanel.setBorder(new MatteBorder(0, 0, 0, 0, Color.black));
        searchPanel.setOpaque(true);
        searchPanel.setForeground(new Color(0, 102, 102));
        searchPanel.setSize((int) (screenWidth / 1.2), screenHeight / 14);
        searchPanel.setLocation((int) infoLabel.getLocation().getX(),
                (int) (infoLabel.getLocation().getY() + infoLabel.getHeight()));

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

        auctionsColumnNames = new String[]{LanguageManager.getMessage("MarketPanel.auctionsColumnNames.0"),
                LanguageManager.getMessage("MarketPanel.auctionsColumnNames.1"), LanguageManager.getMessage("MarketPanel.auctionsColumnNames.2"),
                LanguageManager.getMessage("MarketPanel.auctionsColumnNames.3"), LanguageManager.getMessage("MarketPanel.auctionsColumnNames.4"), ""};
        auctionsTable = new JTable(new MarketJTableModel(new Object[][]{}, auctionsColumnNames, this));
        auctionsTable.setRowHeight((int) (auctionsTable.getRowHeight() * 1.5));
        auctionsTable.getTableHeader().setOpaque(false);
        auctionsTable.getTableHeader().setBackground(new Color(234, 255, 255));
        auctionsTable.setBackground(Color.white);
        auctionsTable.setBorder(new MatteBorder(0, 0, 0, 0, Color.black));
        auctionsTable.setOpaque(true);
        auctionsTable.setForeground(new Color(0, 102, 102));
        auctionsTable.getColumnModel().getColumn(3).setPreferredWidth(auctionsTable.getColumnModel().getColumn(3).getPreferredWidth() - 500);
        auctionsTable.getColumnModel().getColumn(4).setPreferredWidth(auctionsTable.getColumnModel().getColumn(4).getPreferredWidth() + 300);

        auctionsTableScrollPane = new JScrollPane(auctionsTable);
        auctionsTableScrollPane.getViewport().setBackground(Color.WHITE);
        auctionsTableScrollPane.setBorder(new TitledBorder(""));
        auctionsTableScrollPane.setSize((int) (screenWidth - backButton.getWidth()), (int) (screenHeight / 2.25));
        auctionsTableScrollPane.setLocation(infoLabel.getX(),
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

        bringSelectLanguageCBToFront();

        VoiceHelper.textToSpeech(LanguageManager.getMessage("Voice.MarketPanel.welcome"));

    }

    public static void main(String[] args) {
        JFrame testFrame = new JFrame();
        testFrame.setSize(800, 600);
        testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        testFrame.add(new UserAuctionsPanel(800, 600));
        testFrame.setVisible(true);
    }

    public void updateAuctions() {
        auctionsColumnNames = new String[]{LanguageManager.getMessage("MarketPanel.auctionsColumnNames.0"),
                LanguageManager.getMessage("MarketPanel.auctionsColumnNames.1"), LanguageManager.getMessage("MarketPanel.auctionsColumnNames.2"),
                LanguageManager.getMessage("MarketPanel.auctionsColumnNames.3"), LanguageManager.getMessage("MarketPanel.auctionsColumnNames.4"), ""};
        Object[][] auctionsData = new Object[][]{};
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
                    auctionsData[i][2] = LanguageManager.getMessage("MarketPanel.auctionsData.2");
                else
                    auctionsData[i][2] = LanguageManager.getMessage("MarketPanel.auctionsData.2b");
                if (tempAuction.getHighestBid() == null)
                    auctionsData[i][3] = 0 + " (" + LanguageManager.getMessage("MarketPanel.auctionsData.3") + tempAuction.getInitialPrice() + ")";
                else
                    auctionsData[i][3] = Float.toString(tempAuction.getHighestBid().getAmount());
                auctionsData[i][4] = SPQG6Util.getLocalDateTimeDifferenceFromNow(tempAuction.getDayLimit().toLocalDateTime());
                auctionsData[i][5] = LanguageManager.getMessage("MarketPanel.auctionsData.5");
                Thread tempAuctionThread = new Thread(new AuctionTimeLeftRunnable(auctionsTable, i, tempAuction.getDayLimit().toLocalDateTime()));
                auctionsTimeLeftThread.add(tempAuctionThread);
            }

        }
        auctionsTable.setModel(new MarketJTableModel(auctionsData, auctionsColumnNames, this));
        auctionsTable.getColumnModel().getColumn(3).setPreferredWidth(auctionsTable.getColumnModel().getColumn(3).getPreferredWidth() - 500);
        auctionsTable.getColumnModel().getColumn(4).setPreferredWidth(auctionsTable.getColumnModel().getColumn(4).getPreferredWidth() + 200);

        ButtonColumn bidButtonColumn = new ButtonColumn(auctionsTable, new ActionBid(), 5);

        // start countdown threads
        for (int i = 0; i < auctionsData.length; i++) {
            auctionsTimeLeftThread.get(i).start();
        }

        auctionsTable.revalidate();
        auctionsTable.repaint();
    }


    protected void updateComponentsText() {
        titleLabel.setText(LanguageManager.getMessage("MarketPanel.titleLabel.text"));
        SDG2Util.fixJLabelFontSize(titleLabel);
        infoLabel.setText(LanguageManager.getMessage("MarketPanel.infoLabel.text"));
        SDG2Util.fixJLabelFontSize(infoLabel);
        searchLabel.setText(LanguageManager.getMessage("MarketPanel.searchLabel.text"));
        searchComboBox.setModel(new DefaultComboBoxModel<>(new String[]{LanguageManager.getMessage("MarketPanel.comboBox.option1"),
                LanguageManager.getMessage("MarketPanel.comboBox.option2")}));
        searchLabel2.setText(LanguageManager.getMessage("MarketPanel.searchLabel2.text"));
        searchButton.setText(LanguageManager.getMessage("MarketPanel.searchButton.text"));
        backButton.setText(LanguageManager.getMessage("General.backButton.text"));
        SDG2Util.fixJButtonFontSize(backButton);

        updateAuctions();
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
