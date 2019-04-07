package com.spq.group6.client.gui.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import com.spq.group6.client.gui.ClientWindow;
import com.spq.group6.client.gui.utils.RoundedBorder;
import com.spq.group6.client.gui.utils.SDG2Util;
import com.spq.group6.client.gui.utils.ScreenType;
import java.awt.Font;
import java.awt.Color;
import java.awt.Cursor;

public class InitialPanelV1 extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel titleLabel;
	private JLabel authorLabel;
	private JButton logInButton;
	private JButton signInButton;
	
	public InitialPanelV1(int screenWidth, int screenHeight) {
		
		this.setLayout(null);
		this.setBackground(Color.CYAN);
		titleLabel = new JLabel("", SwingConstants.CENTER);
		titleLabel.setIcon(new ImageIcon(InitialPanelV1.class.getResource("/com/spq/group6/util/BiddingIt300x300.png")));
		titleLabel.setBounds(0, 0, 640, 480);
		titleLabel.setSize(screenWidth / 2, screenHeight / 2);
		titleLabel.setLocation(screenWidth / 2 - titleLabel.getWidth() / 2, screenHeight / 3 - titleLabel.getHeight() / 2);	
		
		logInButton = new JButton("Log in");
		logInButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ClientWindow.getClientWindow(null).changeScreen(ScreenType.LOG_IN);
			}
		});
		logInButton.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 36));
		logInButton.setSize(screenWidth / 5, screenHeight / 8);
		logInButton.setBorder(new RoundedBorder(20)); 
		logInButton.setLocation((int) (screenWidth * 0.5), (int) (screenHeight * 0.75));
		logInButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		signInButton = new JButton("Sign in");
		signInButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ClientWindow.getClientWindow(null).changeScreen(ScreenType.REGISTER);	
			}
		});
		signInButton.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 36));
		signInButton.setSize(screenWidth / 5, screenHeight / 8);
		signInButton.setBorder(new RoundedBorder(20));
		signInButton.setLocation((int) (screenWidth * 0.75), (int) (screenHeight * 0.75));
		signInButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		
		authorLabel = new JLabel("SPQ Group 6", SwingConstants.CENTER);
		authorLabel.setSize(screenWidth / 8, screenHeight / 15);
		authorLabel.setLocation((int) (screenWidth * 0.1 - authorLabel.getWidth() / 2),
				(int) (screenHeight *0.9 - authorLabel.getHeight() / 2));
		SDG2Util.fixJLabelFontSize(authorLabel);	
		
		this.add(titleLabel);
//		this.add(infoLabel);
		this.add(authorLabel);
		this.add(logInButton);
		this.add(signInButton);
	}
	
	public static void main(String[] args) {
		JFrame testFrame = new JFrame();
		testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		testFrame.setSize(800, 600);
		testFrame.getContentPane().add(new InitialPanelV1(800, 600));
		testFrame.setVisible(true);
		testFrame.setTitle("Bidding It");
		ImageIcon icono = new ImageIcon(InitialPanelV1.class.getResource("/com/spq/group6/util/bidicon.png"));
		testFrame.setIconImage(icono.getImage());
	}

}