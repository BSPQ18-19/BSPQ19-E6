package com.spq.group6.client.gui.utils.locale;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageManager {

    //The name of the header of the files with the translations
    private static final String RESOURCE_BUNDLE_FILE_NAME = "locale/localization";

    //The default language for the app
    public static final Locale DEFAULT_LOCALE = LanguageSelector.ENGLISH.getLocale();

    //Default locale with the language for the app
    private Locale locale = DEFAULT_LOCALE;

    //constructor
    public LanguageManager() {
    }

    //get & set for the Locale
    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    //get the default bundle with the translations
    private ResourceBundle getResourceBundle() {
        return ResourceBundle.getBundle(RESOURCE_BUNDLE_FILE_NAME, locale);
    }


    //get of a specific bundle
    private ResourceBundle getResourceBundle(Locale locale) {
        return ResourceBundle.getBundle(RESOURCE_BUNDLE_FILE_NAME, locale);
    }


    // return the translation for the inserted key (if the key does not exist in neither the current bundle or the default one returns the key)
    public String getMessage(String key, Object... parameters) {

        Locale localeToUse = getLocale();

        // Check if the key is present for the current locale
        if (!getResourceBundle().containsKey(key)) {

            // Check if the key is in the default bundle
            if (!getResourceBundle(DEFAULT_LOCALE).containsKey(key)) {
                return key; // return just the translation key
            }

            // If it exist in the default bundle we use it
            localeToUse = DEFAULT_LOCALE;

        }

        // Format with parameters
        MessageFormat formatter = new MessageFormat("");
        formatter.setLocale(localeToUse);
        formatter.applyPattern(getResourceBundle(localeToUse).getString(key));
        return formatter.format(parameters);

    }
    
    /**
     * Get all languages available
     * @return the locales allowed
     */
    public LanguageSelector[] getLanguages() {
    	return LanguageSelector.ALLOWED_LOCALES;
    }
}
