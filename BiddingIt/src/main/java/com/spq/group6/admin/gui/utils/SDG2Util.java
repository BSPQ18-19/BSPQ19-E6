package com.spq.group6.admin.gui.utils;

import javax.swing.*;
import java.awt.*;

public class SDG2Util {

    // source: https://stackoverflow.com/a/2715279
    public static void fixJLabelFontSize(JLabel label) {
        String labelText = label.getText();
        Font labelFont = label.getFont();

        if (labelText.contains("<")) { // is <html>

            int lines = labelText.split("<br/>").length;
            int endLine1 = labelText.indexOf('<', 5);
            labelText = labelText.substring(6, endLine1);
            labelText = labelText.replaceAll("<*>", "");

            int newFontSize = (int) (label.getHeight() / (lines * 1.75));

            label.setFont(new Font(labelFont.getName(), Font.PLAIN, newFontSize));


        } else {

            int stringWidth = label.getFontMetrics(labelFont).stringWidth(labelText);
            int componentWidth = label.getWidth();
            double widthRatio = (double) componentWidth / (double) stringWidth;

            int newFontSize = (int) (labelFont.getSize() * widthRatio * 0.8);

            int fontSizeToUse = Math.min(newFontSize, componentWidth);
            label.setFont(new Font(labelFont.getName(), Font.PLAIN, fontSizeToUse));


        }

    }

    public static void fixJButtonFontSize(JButton button) {
        String buttonText = button.getText();
        if (buttonText.contains("<")) { // is <html>
            int endLine1 = buttonText.indexOf('<', 5);
            buttonText = buttonText.substring(6, endLine1);
            buttonText = buttonText.replaceAll("<*>", "");
        }
        Font buttonFont = button.getFont();

        int stringWidth = button.getFontMetrics(buttonFont).stringWidth(buttonText);
        int componentWidth = button.getWidth();

        // Find out how much the font can grow in width.
        double widthRatio = (double) componentWidth / (double) stringWidth;

        int newFontSize = (int) (buttonFont.getSize() * widthRatio * 0.7);
        int componentHeight = button.getHeight();

        // Pick a new font size so it will not be larger than the height of button.
        int fontSizeToUse = Math.min(newFontSize, componentHeight);

        // Set the button's font size to the newly determined size.
        button.setFont(new Font(buttonFont.getName(), Font.PLAIN, fontSizeToUse));
    }

    public static void main(String[] args) {

    }

}
