package com.spq.group6.client.gui.utils.locale;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageManager {
    /**
     * The name of the ResourceBundle files
     */
    private static final String RESOURCE_BUNDLE_FILE_NAME = "locale/localization";

    /**
     * The default locale for the app.
     * Will always be en_EN fue to teacher's requirements.
     * (the course is in English)
     */
    public static final Locale DEFAULT_LOCALE = LanguageSelector.ENGLISH.getLocale();

    /**
     * The current locale of the system.
     * Initialized to the default locale constant
     */
    private Locale locale = DEFAULT_LOCALE;

    /**
     * Create a new LocaleManager instance
     */
    public LanguageManager() {
    }
    public Locale getLocale() {
        return locale;
    }

    /**
     * Set the locale of the system
     * @param locale the locale to be set as active
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    /**
     * Get the current ResourceBundle for the selected Locale
     * @return the ResourceBundle associated to the current locale
     */
    private ResourceBundle getResourceBundle() {
        return ResourceBundle.getBundle(RESOURCE_BUNDLE_FILE_NAME, locale);
    }


    /**
     * Get the ResourceBundle for the specified Locale
     * @return the ResourceBundle associated to the specified locale
     */
    private ResourceBundle getResourceBundle(Locale locale) {
        return ResourceBundle.getBundle(RESOURCE_BUNDLE_FILE_NAME, locale);
    }


    /**
     * Localize a message resource.
     * @param key the key of the resource
     * @param parameters vararg of the parameters for the resource
     * @return an already-formatted String
     */
    public String getMessage(String key, Object... parameters) {

        // The locale to use for the formatting below
        Locale localeToUse = getLocale();

        // Check if the key is present for the current locale
        if (!getResourceBundle().containsKey(key)) {

            // DEBUG mode: we don't fall back to default locale
            // useful for detecting untranslated UI elements
            if (!getResourceBundle(DEFAULT_LOCALE).containsKey(key)) {
                return key; // return just the translation key
            }

            // In this case we use the default locale (key not found in Locale)
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
