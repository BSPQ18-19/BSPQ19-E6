package com.spq.group6.client.gui.utils.locale;

import java.util.Locale;

public class LanguageSelector {

    //English bundle
    public final static LanguageSelector ENGLISH = new LanguageSelector(
            new Locale("en", "US"),
            "en_US",
            "English/US",
            "English/US"
    );

    //Spanish bundle
    public final static LanguageSelector SPANISH = new LanguageSelector(
            new Locale("es", "ES"),
            "es_ES",
            "Spanish/Spain",
            "Espanyol/Espanya"
    );

    //Basque bundle
    public final static LanguageSelector BASQUE = new LanguageSelector(
            new Locale("eu"),
            "eu",
            "Basque",
            "Euskara"
    );

    public final static LanguageSelector[] ALLOWED_LOCALES = {ENGLISH, SPANISH, BASQUE};

    private Locale locale;

    private String code;

    private String englishName;

    private String localizedName;

    //constructor
    private LanguageSelector(Locale locale, String code, String englishName, String localizedName) {
        this.locale = locale;
        this.code = code;
        this.englishName = englishName;
        this.localizedName = localizedName;
    }

    //getters
    public Locale getLocale() {
        return locale;
    }

    public String getCode() {
        return code;
    }

    public String getEnglishName() {
        return englishName;
    }

    public String getLocalizedName() {
        return localizedName;
    }


    public String toString() {
        return localizedName;
    }

}
