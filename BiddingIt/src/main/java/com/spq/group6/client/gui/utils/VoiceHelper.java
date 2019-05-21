package com.spq.group6.client.gui.utils;

import com.spq.group6.client.gui.utils.locale.LanguageManager;

import javax.swing.*;

public class VoiceHelper {

    private static TTSManager ttsManager = new TTSManager();

    public static void textToSpeech(String speechCode){
        String msg = LanguageManager.getMessage(speechCode);
        Thread t = new Thread(new Runnable() {
            public void run() {
                ttsManager.sayText(msg);
            }
        });
        t.start();
    }

    public TTSManager getTtsManager() {
        return ttsManager;
    }

    public static void setTtsState(boolean state) {
        ttsManager.setTtsIsON(state);
    }

    public static boolean isTtsON() {
        return ttsManager.isTtsIsON();
    }

    public void sayText(String text) {
        ttsManager.sayText(text);
    }
}
