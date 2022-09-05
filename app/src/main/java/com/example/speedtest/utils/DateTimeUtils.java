package com.example.speedtest.utils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtils {
    public static String getDateConverted(Date date){
        SimpleDateFormat  formatter = new SimpleDateFormat("MMM d", Locale.ENGLISH);
        return formatter.format(date).toString();
    }
    public static String getDateConvertedToResult(Date date){
        SimpleDateFormat  formatter = new SimpleDateFormat("dd/MM/yyyy, hh:mm", Locale.ENGLISH);
        return formatter.format(date).toString();
    }
}
