package com.spq.group6.server.utils.observer.local;

import java.util.Observable;

public class LocalObservable extends Observable {
	
	public void notifyObservers(Object obj) {
		super.setChanged();
		super.notifyObservers(obj);
	}
}