package com.spq.group6.client.gui.utils;

import com.spq.group6.client.gui.utils.locale.LanguageManager;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class SPQG6Util {

    public static String getLocalDateTimeDifference(LocalDateTime fromDateTime, LocalDateTime toDateTime) {
        LocalDateTime tempDateTime = LocalDateTime.from(fromDateTime);

        long years = tempDateTime.until(toDateTime, ChronoUnit.YEARS);
        tempDateTime = tempDateTime.plusYears(years);

        long months = tempDateTime.until(toDateTime, ChronoUnit.MONTHS);
        tempDateTime = tempDateTime.plusMonths(months);

        long days = tempDateTime.until(toDateTime, ChronoUnit.DAYS);
        tempDateTime = tempDateTime.plusDays(days);


        long hours = tempDateTime.until(toDateTime, ChronoUnit.HOURS);
        tempDateTime = tempDateTime.plusHours(hours);

        long minutes = tempDateTime.until(toDateTime, ChronoUnit.MINUTES);
        tempDateTime = tempDateTime.plusMinutes(minutes);

        long seconds = tempDateTime.until(toDateTime, ChronoUnit.SECONDS);

        return (years + LanguageManager.getMessage("SPQG6Util.years") +
                months + " " + LanguageManager.getMessage("SPQG6Util.months") +
                days + " " + LanguageManager.getMessage("SPQG6Util.days") +
                hours + " " + LanguageManager.getMessage("SPQG6Util.hours") +
                minutes + " " + LanguageManager.getMessage("SPQG6Util.minutes") +
                seconds + " " + LanguageManager.getMessage("SPQG6Util.seconds")
        );
    }

    public static String getLocalDateTimeDifferenceFromNow(LocalDateTime toDateTime) {
        return getLocalDateTimeDifference(LocalDateTime.now(), toDateTime);
    }
}
