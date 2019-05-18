package com.spq.group6.client.gui.utils;

import java.util.Locale;

public class LanguageSelector {
    /**
     * Constant corresponding to the English AllowedLocale
     */
    public final static LanguageSelector ENGLISH = new LanguageSelector(
            new Locale("en", "US"),
            "en_US",
            "English/US",
            "English/US"
    );

    /**
     * Constant corresponding to the Spanish AllowedLocale
     */
    public final static LanguageSelector SPANISH = new LanguageSelector(
            new Locale("es", "ES"),
            "es_ES",
            "Spanish/Spain",
            "Espanyol/Espanya"
    );

    /**
     * Constant corresponding to the Basque AllowedLocale
     */
    public final static LanguageSelector BASQUE = new LanguageSelector(
            new Locale("eu"),
            "eu",
            "Basque",
            "Euskara"
    );
    /**
     * An array with the pre-set allowed locales for the app
     */
    public final static LanguageSelector[] ALLOWED_LOCALES = {ENGLISH, SPANISH, BASQUE};

    private Locale locale;

    /**
     * The language code (e.g.: es_ES for Spanish, Spain)
     */
    private String code;

    /**
     * The name of the locale in English
     */
    private String englishName;

    /**
     * The localized name of the locale
     */
    private String localizedName;

    private LanguageSelector(Locale locale, String code, String englishName, String localizedName) {
        this.locale = locale;
        this.code = code;
        this.englishName = englishName;
        this.localizedName = localizedName;
    }

    /**
     * @return the Locale
     */
    public Locale getLocale() {
        return locale;
    }

    public String getCode() {
        return code;
    }

    /**
     * @return the name in English of the locale
     */
    public String getEnglishName() {
        return englishName;
    }

    public String getLocalizedName() {
        return localizedName;
    }

	@Override
	public String toString() {
		return localizedName;
	}

}
