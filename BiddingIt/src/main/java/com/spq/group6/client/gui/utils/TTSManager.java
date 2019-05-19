package com.spq.group6.client.gui.utils;

import java.util.Locale;

import javax.speech.Central;
import javax.speech.EngineException;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;
import javax.swing.JButton;

public class TTSManager {

	private boolean ttsIsON;
	private Synthesizer synthesizer;
	
	public TTSManager() {
		
		this.ttsIsON = true;

        try {
        	// set property as Kevin Dictionary 
            System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");  
        	// Register Engine 
			Central.registerEngineCentral("com.sun.speech.freetts.jsapi.FreeTTSEngineCentral");
	        // Create a Synthesizer 
	        synthesizer = Central.createSynthesizer(new SynthesizerModeDesc(Locale.US));      

		} catch (EngineException e1) {
			e1.printStackTrace();
		} 

		
		Thread ttsThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try 
		        {
		            // Allocate synthesizer 
		            synthesizer.allocate();
		            
		            // Resume Synthesizer 
		            synthesizer.resume();
		            		            
		        } catch (Exception e)  
		        { 
		            e.printStackTrace(); 
		        }
				
			}
		});
		
		ttsThread.run();
		
	}
	
	public boolean isTtsIsON() {
		return ttsIsON;
	}

	public void setTtsIsON(boolean ttsIsON) {
		this.ttsIsON = ttsIsON;
	}

	public void sayText(String text) {
		try {
			if (ttsIsON) {
	        	// speaks the given text until queue is empty. 
				synthesizer.cancel();
		        synthesizer.speakPlainText(text, null);
				//synthesizer.waitEngineState(Synthesizer.QUEUE_EMPTY);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
}
