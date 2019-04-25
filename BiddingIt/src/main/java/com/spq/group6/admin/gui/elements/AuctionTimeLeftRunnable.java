package com.spq.group6.admin.gui.elements;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import javax.swing.JTable;

import com.spq.group6.client.gui.utils.SPQG6Util;

public class AuctionTimeLeftRunnable implements Runnable {

	private int tableRow;
	private JTable auctionsTable;
	private LocalDateTime tempAuctionTime;
	
	public AuctionTimeLeftRunnable(JTable auctionsTable, int tableRow, LocalDateTime tempAuctionTime) {
		this.auctionsTable = auctionsTable;
		this.tableRow = tableRow;
		this.tempAuctionTime = tempAuctionTime;
	}
	
	@Override
	public void run() {
		while (!Thread.interrupted() && LocalDateTime.from(LocalDateTime.now()).until(tempAuctionTime, ChronoUnit.SECONDS) > 0) {
			auctionsTable.setValueAt(SPQG6Util.getLocalDateTimeDifferenceFromNow(tempAuctionTime), tableRow, 3);
			try{
	            Thread.sleep(1000);
	        } catch(Exception e) {}
		}
	}
	
}
