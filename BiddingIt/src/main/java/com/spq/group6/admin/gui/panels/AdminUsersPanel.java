package com.spq.group6.admin.gui.panels;

import com.github.lgooddatepicker.tableeditors.DateTimeTableEditor;
import com.spq.group6.admin.controller.AdminController;
import com.spq.group6.admin.gui.AdminWindow;
import com.spq.group6.admin.gui.actions.ActionDeleteUser;
import com.spq.group6.admin.gui.elements.ButtonColumn;
import com.spq.group6.admin.gui.elements.UserJTableModel;
import com.spq.group6.admin.gui.utils.SDG2Util;
import com.spq.group6.admin.gui.utils.ScreenType;
import com.spq.group6.server.data.User;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * One of the panels used in the Admin window
 */
public class AdminUsersPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JLabel titleLabel;
    private JPanel searchPanel;
    private JButton searchButton;
    private JScrollPane usersTableScrollPane;
    private JTable usersTable;
    private JButton backButton;
    private JButton logOutButton;
    private List<User> users;
    private Object[][] usersData;

    @SuppressWarnings("unused")
    private AdminController controller;

    /**
     * Constructor to create a new User Panel.
     * <p>
     *
     * @param screenWidth  width of the screen
     * @param screenHeight height of the screen
     * @param controller   the controller that uses the Admin Window
     */
    public AdminUsersPanel(int screenWidth, int screenHeight, AdminController controller) {
        setBackground(Color.white);
        this.setLayout(null);

        titleLabel = new JLabel("Administrating users...", SwingConstants.LEFT);
        titleLabel.setForeground(new Color(0, 204, 204));
        titleLabel.setBackground(Color.WHITE);
        titleLabel.setOpaque(true);
        titleLabel.setSize((int) (screenWidth * 0.5), screenHeight / 15);
        titleLabel.setLocation(screenWidth / 15, screenHeight / 4 - titleLabel.getHeight() / 2);
        SDG2Util.fixJLabelFontSize(titleLabel);

        String[] usersColumnNames = {"Username", "Password", "Country", "Money", ""};

        Thread animationThread = new Thread(new Runnable() {
            int dots = 0;


            public void run() {

                while (AdminUsersPanel.this.isEnabled()) {

                    if (dots++ == 3)
                        dots = 0;

                    String tempText = titleLabel.getText().substring(0, 20);
                    for (int i = 0; i < dots; i++)
                        tempText += ".";
                    titleLabel.setText(tempText);

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        animationThread.start();

        // searching filter
        searchButton = new JButton("Update");
        searchButton.setForeground(new Color(0, 102, 102));
        searchButton.setBackground(Color.white);
        searchButton.setBorder(new TitledBorder(""));
        searchButton.setContentAreaFilled(false);
        searchButton.setOpaque(true);
        searchButton.addActionListener(new ActionListener() {


            public void actionPerformed(ActionEvent e) {

                usersData = null;
                users = controller.getAllUsers();
                Object[][] usersData = null;
                ArrayList<User> users = controller.getAllUsers();
                if (users.size() == 0) {
                    usersData = new Object[][]{};
                    JOptionPane.showConfirmDialog(AdminUsersPanel.this, "No users found.", "Info", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);

                } else {
                    usersData = new Object[users.size()][usersColumnNames.length];
                    int i = 0;
                    for (i = 0; i < users.size(); i++) {
                        User tempUser = users.get(i);
                        usersData[i][0] = tempUser.getUsername();
                        usersData[i][1] = tempUser.getPassword();
                        usersData[i][2] = tempUser.getCountry();
                        usersData[i][3] = tempUser.getMoney();
                        usersData[i][4] = "Delete User";
                    }
                    JOptionPane.showConfirmDialog(AdminUsersPanel.this, "Users found succesfully.", "Info", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);

                }
                usersTable.setModel(new UserJTableModel(usersData, usersColumnNames, controller, users));
                usersTable.getColumnModel().getColumn(3).setPreferredWidth(usersTable.getColumnModel().getColumn(3).getPreferredWidth() + 100);

                @SuppressWarnings("unused")
                ButtonColumn deleteUserColumn = new ButtonColumn(usersTable, new ActionDeleteUser(), 4);

                ButtonColumn bidButtonColumn = new ButtonColumn(usersTable, new ActionDeleteUser(), 4);
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
                    JOptionPane.showConfirmDialog(AdminUsersPanel.this, "Error logging out.", "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);

            }
        });

        usersTable = new JTable(new UserJTableModel(new Object[][]{}, usersColumnNames, controller, new ArrayList<User>()));
        usersTable.setRowHeight((int) (usersTable.getRowHeight() * 1.5));
        usersTable.getTableHeader().setOpaque(false);
        usersTable.getTableHeader().setBackground(new Color(234, 255, 255));
        usersTable.setBackground(Color.white);
        usersTable.setBorder(new MatteBorder(0, 0, 0, 0, Color.black));
        usersTable.setOpaque(true);
        usersTable.setForeground(new Color(0, 102, 102));
        usersTable.getColumnModel().getColumn(3).setPreferredWidth(usersTable.getColumnModel().getColumn(3).getPreferredWidth() + 100);

        // set column 4 to limit day
        usersTable.setDefaultEditor(LocalDateTime.class, new DateTimeTableEditor());
        usersTable.setDefaultRenderer(LocalDateTime.class, new DateTimeTableEditor());
        usersTable.getColumnModel().getColumn(4).setCellEditor(usersTable.getDefaultEditor(LocalDateTime.class));
        usersTable.getColumnModel().getColumn(4).setCellRenderer(usersTable.getDefaultRenderer(LocalDateTime.class));

        usersTableScrollPane = new JScrollPane(usersTable);
        usersTableScrollPane.getViewport().setBackground(Color.WHITE);
        usersTableScrollPane.setBorder(new TitledBorder(""));
        usersTableScrollPane.setSize((int) (screenWidth - backButton.getLocation().getX() - (screenWidth - logOutButton.getLocation().getX()) + logOutButton.getWidth()),
                (int) (screenHeight / 2.25));
        usersTableScrollPane.setLocation((int) (titleLabel.getLocation().getX()),
                (int) (searchPanel.getLocation().getY() + searchPanel.getHeight()));


        this.add(titleLabel);
        searchPanel.add(searchButton);
        this.add(searchPanel);
        this.add(usersTableScrollPane);
        this.add(backButton);
        this.add(logOutButton);
    }

    public static void main(String[] args) {
        JFrame testFrame = new JFrame();
        testFrame.setSize(800, 600);
        testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        testFrame.add(new AdminUsersPanel(800, 600, null));
        testFrame.setVisible(true);
    }

}
