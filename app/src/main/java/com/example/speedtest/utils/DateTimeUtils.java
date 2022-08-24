package com.example.speedtest.utils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtils {
    public static String getDateNowConverted(){
        Date date = new Date();
        SimpleDateFormat  formatter = new SimpleDateFormat("MMM d", Locale.ENGLISH);
        return formatter.format(date);
    }
}
