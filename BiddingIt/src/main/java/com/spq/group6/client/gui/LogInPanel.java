package com.spq.group6.client.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import com.spq.group6.util.SDG2Util;


public class LogInPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel infoLabel1;
	private JLabel infoLabel2;
	private JButton cancelButton;
	
	public LogInPanel(int screenWidth, int screenHeight) {
		
		this.setLayout(null);
		
		infoLabel1 = new JLabel("Use the authentication pop-up", SwingConstants.CENTER);
		infoLabel1.setSize((int) (screenWidth / 1.5), screenHeight / 2);
		infoLabel1.setLocation(screenWidth / 2 - infoLabel1.getWidth() / 2, (int) (screenHeight / 2 - infoLabel1.getHeight()));
		SDG2Util.fixJLabelFontSize(infoLabel1);	
		
		infoLabel2 = new JLabel("to sign-in/register", SwingConstants.CENTER);
		infoLabel2.setSize((int) (screenWidth / 1.5), screenHeight / 2);
		infoLabel2.setLocation(screenWidth / 2 - infoLabel2.getWidth() / 2, 
				(int) (infoLabel1.getLocation().getY() + infoLabel1.getFont().getSize() + screenHeight / 25));
		SDG2Util.fixJLabelFontSize(infoLabel2);	
		
		cancelButton = new JButton("cancel");
		cancelButton.setSize(screenWidth / 5, screenHeight / 10);
		cancelButton.setLocation((int) (screenWidth / 6), 
				(int) (infoLabel2.getLocation().getY() + infoLabel2.getFont().getSize() + screenHeight / 2));
		SDG2Util.fixJButtonFontSize(cancelButton);
		cancelButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ClientWindow.getClientWindow(null).changeScreen(ScreenType.INITIAL);
			}
		});
		
		this.add(infoLabel1);		
		this.add(infoLabel2);
		this.add(cancelButton);
		
	}
	
	public static void main(String[] args) {
		JFrame testFrame = new JFrame();
		testFrame.setSize(800, 600);
		testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		testFrame.add(new LogInPanel(800, 600));
		testFrame.setVisible(true);
	}

}