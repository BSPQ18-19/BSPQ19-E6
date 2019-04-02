package com.spq.group6.util;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;

public class SDG2Util {

	// source: https://stackoverflow.com/a/2715279
	public static void fixJLabelFontSize(JLabel label) {
		String labelText = label.getText();
		Font labelFont = label.getFont();
		
		int stringWidth = label.getFontMetrics(labelFont).stringWidth(labelText);
		int componentWidth = label.getWidth();

		// Find out how much the font can grow in width.
		double widthRatio = (double)componentWidth / (double)stringWidth;

		int newFontSize = (int)(labelFont.getSize() * widthRatio * 0.8);
		int componentHeight = label.getHeight();

		// Pick a new font size so it will not be larger than the height of label.
		int fontSizeToUse = Math.min(newFontSize, componentHeight);
		// TEST System.out.println(labelText + " " + labelFont.getSize() + " "+ stringWidth + " " + componentWidth + " " + newFontSize + " " + fontSizeToUse);
		
		// Set the label's font size to the newly determined size.
		label.setFont(new Font(labelFont.getName(), Font.PLAIN, fontSizeToUse));
	}
	
	public static void fixJButtonFontSize(JButton button) {
		String buttonText = button.getText();
		Font buttonFont = button.getFont();
		
		int stringWidth = button.getFontMetrics(buttonFont).stringWidth(buttonText);
		int componentWidth = button.getWidth();

		// Find out how much the font can grow in width.
		double widthRatio = (double)componentWidth / (double)stringWidth;

		int newFontSize = (int)(buttonFont.getSize() * widthRatio * 0.8);
		int componentHeight = button.getHeight();

		// Pick a new font size so it will not be larger than the height of button.
		int fontSizeToUse = Math.min(newFontSize, componentHeight);
		// TEST System.out.println(buttonText + " " + buttonFont.getSize() + " "+ stringWidth + " " + componentWidth + " " + newFontSize + " " + fontSizeToUse);
		
		// Set the button's font size to the newly determined size.
		button.setFont(new Font(buttonFont.getName(), Font.PLAIN, fontSizeToUse));
	}
	
	public static void main(String[] args) {
		
	}

}
