package com.spq.group6.client.gui.elements;

import com.spq.group6.client.gui.utils.SPQG6Util;

import javax.swing.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class AuctionTimeLeftRunnable implements Runnable {

    private int tableRow;
    private JTable auctionsTable;
    private LocalDateTime tempAuctionTime;

    public AuctionTimeLeftRunnable(JTable auctionsTable, int tableRow, LocalDateTime tempAuctionTime) {
        this.auctionsTable = auctionsTable;
        this.tableRow = tableRow;
        this.tempAuctionTime = tempAuctionTime;
    }

    public void run() {
        boolean failed = false;
        while (!Thread.interrupted() && !failed && LocalDateTime.from(LocalDateTime.now()).until(tempAuctionTime, ChronoUnit.SECONDS) > 0) {
            try {
                auctionsTable.setValueAt(SPQG6Util.getLocalDateTimeDifferenceFromNow(tempAuctionTime), tableRow, 4);
                Thread.sleep(1000);
            } catch (Exception e) {
                failed = true;
            }
        }
    }

}
