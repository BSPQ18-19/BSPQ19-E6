package com.spq.group6.client.gui.panels;

import com.github.lgooddatepicker.tableeditors.DateTimeTableEditor;
import com.spq.group6.client.gui.ClientWindow;
import com.spq.group6.client.gui.actions.ActionCreateAuction;
import com.spq.group6.client.gui.elements.AuctionJTableModel;
import com.spq.group6.client.gui.elements.ButtonColumn;
import com.spq.group6.client.gui.utils.SDG2Util;
import com.spq.group6.client.gui.utils.ScreenType;
import com.spq.group6.client.gui.utils.locale.LanguageManager;
import com.spq.group6.client.gui.utils.voice.VoiceHelper;
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

/**
 * One of the panels used in the Client window
 */
public class UserAuctionsPanel extends LocaleSelectorPanel {

    private static final long serialVersionUID = 1L;
    private JLabel infoLabel;
    private JScrollPane auctionsTableScrollPane;
    private JTable auctionsTable;
    private JButton backButton;
    private String[] auctionsColumnNames;

    private List<Auction> userAuctions;

    /**
     * Constructor to create a new User-Auctions Panel.
     * <p>
     * @param screenWidth width of the screen
     * @param screenHeight height of the screen
     */
    public UserAuctionsPanel(int screenWidth, int screenHeight) {

        super(screenWidth, screenHeight);

        titleLabel = new JLabel(LanguageManager.getMessage("UserAuctionsPanel.titleLabel.text"), SwingConstants.LEFT);
        titleLabel.setForeground(Color.white);
        titleLabel.setBackground(new Color(0, 204, 204));
        titleLabel.setOpaque(true);
        titleLabel.setSize(screenWidth, screenHeight / 7);
        titleLabel.setLocation(0, 0);
        SDG2Util.fixJLabelFontSize(titleLabel);

        infoLabel = new JLabel(LanguageManager.getMessage("UserAuctionsPanel.infoLabel.text"), SwingConstants.LEFT);
        infoLabel.setForeground(new Color(0, 102, 102));
        infoLabel.setSize((int) (screenWidth / 1.5), screenHeight / 8);
        infoLabel.setLocation(screenWidth / 20,
                (int) (titleLabel.getLocation().getY() + titleLabel.getHeight()));
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

        userAuctions = controller.getCurrentUserAuctions();
        auctionsColumnNames = new String[]{LanguageManager.getMessage("UserAuctionsPanel.auctionsColumnNames.0"),
                LanguageManager.getMessage("UserAuctionsPanel.auctionsColumnNames.1"), LanguageManager.getMessage("UserAuctionsPanel.auctionsColumnNames.2"),
                LanguageManager.getMessage("UserAuctionsPanel.auctionsColumnNames.3"), LanguageManager.getMessage("UserAuctionsPanel.auctionsColumnNames.4"),
                LanguageManager.getMessage("UserAuctionsPanel.auctionsColumnNames.5"), ""};
        auctionsTable = new JTable(new AuctionJTableModel(new Object[][]{}, auctionsColumnNames, this));
        updateAuctions();
        auctionsTable.setRowHeight((int) (auctionsTable.getRowHeight() * 1.5));
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
        auctionsTableScrollPane.setSize((int) (screenWidth - backButton.getWidth()), screenHeight / 2);
        auctionsTableScrollPane.setLocation(infoLabel.getX(),
                (int) (infoLabel.getLocation().getY() + infoLabel.getHeight()));

        this.add(titleLabel);
        this.add(infoLabel);
        this.add(auctionsTableScrollPane);
        this.add(backButton);

        bringSelectLanguageCBToFront();

        VoiceHelper.textToSpeech(LanguageManager.getMessage("Voice.UserAuctionsPanel.welcome"));

    }

    public static void main(String[] args) {
        JFrame testFrame = new JFrame();
        testFrame.setSize(800, 600);
        testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        testFrame.add(new UserAuctionsPanel(800, 600));
        testFrame.setVisible(true);
    }

    public void updateAuctions() {
        auctionsColumnNames = new String[]{LanguageManager.getMessage("UserAuctionsPanel.auctionsColumnNames.0"),
                LanguageManager.getMessage("UserAuctionsPanel.auctionsColumnNames.1"), LanguageManager.getMessage("UserAuctionsPanel.auctionsColumnNames.2"),
                LanguageManager.getMessage("UserAuctionsPanel.auctionsColumnNames.3"), LanguageManager.getMessage("UserAuctionsPanel.auctionsColumnNames.4"),
                LanguageManager.getMessage("UserAuctionsPanel.auctionsColumnNames.5"), ""};
        Object[][] auctionsData = new Object[][]{};
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
                    auctionsData[i][4] = LanguageManager.getMessage("UserAuctionsPanel.auctionsData.4");
                else
                    auctionsData[i][4] = LanguageManager.getMessage("UserAuctionsPanel.auctionsData.4b");
                auctionsData[i][5] = tempAuction.getDayLimit().toLocalDateTime();
                auctionsData[i][6] = "";
            }
            auctionsData[i][0] = "";
            auctionsData[i][1] = "";
            auctionsData[i][2] = "";
            auctionsData[i][3] = "-";
            auctionsData[i][4] = "-";
            auctionsData[i][5] = "";
            auctionsData[i][6] = LanguageManager.getMessage("UserAuctionsPanel.auctionsData.6");
        }
        auctionsTable.setModel(new AuctionJTableModel(auctionsData, auctionsColumnNames, this));
        auctionsTable.getColumnModel().getColumn(5).setPreferredWidth(auctionsTable.getColumnModel().getColumn(5).getPreferredWidth() + 150);
        @SuppressWarnings("unused")
        ButtonColumn createButtonColumn = new ButtonColumn(auctionsTable, new ActionCreateAuction(), 6);

        // set column 0 to combobox
        updateUserProductsComboBox();

        // set column 4 to limit day
        DateTimeTableEditor dateTimeEditor = new DateTimeTableEditor();
        dateTimeEditor.getDatePickerSettings().setLocale(LanguageManager.getLocale());
        auctionsTable.setDefaultEditor(LocalDateTime.class, dateTimeEditor);
        auctionsTable.setDefaultRenderer(LocalDateTime.class, dateTimeEditor);
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

    protected void updateComponentsText() {
        titleLabel.setText(LanguageManager.getMessage("UserAuctionsPanel.titleLabel.text"));
        SDG2Util.fixJLabelFontSize(titleLabel);
        infoLabel.setText(LanguageManager.getMessage("UserAuctionsPanel.infoLabel.text"));
        SDG2Util.fixJLabelFontSize(infoLabel);
        backButton.setText(LanguageManager.getMessage("General.backButton.text"));
        SDG2Util.fixJButtonFontSize(backButton);
        updateAuctions();
    }

    public List<Auction> getUserAuctions() {
        return userAuctions;
    }

    public void setUserAuctions(List<Auction> userAuctions) {
        this.userAuctions = userAuctions;
    }
}
