package com.spq.group6.client.gui.utils;

import java.util.Locale;

import javax.speech.Central;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;
import javax.swing.JButton;

public class TTSManager {

	private boolean ttsIsON;
	private Synthesizer synthesizer;
	
	public TTSManager() {
		
		try 
        { 
            // set property as Kevin Dictionary 
            System.setProperty("freetts.voices", 
                "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");  
                  
            // Register Engine 
            Central.registerEngineCentral 
                ("com.sun.speech.freetts.jsapi.FreeTTSEngineCentral"); 
  
            // Create a Synthesizer 
            synthesizer = Central.createSynthesizer(new SynthesizerModeDesc(Locale.US));      
      
            // Allocate synthesizer 
            synthesizer.allocate();
            
            // Resume Synthesizer 
            synthesizer.resume();
            
    		this.ttsIsON = true;
            
        } catch (Exception e)  
        { 
            e.printStackTrace(); 
        }
		
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
		        synthesizer.speakPlainText(text, null);
				synthesizer.waitEngineState(Synthesizer.QUEUE_EMPTY);
			}
		} catch (Exception ee) {
			ee.printStackTrace();
		}
	}
	
	
	
}
