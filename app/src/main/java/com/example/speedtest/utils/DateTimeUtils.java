package com.example.speedtest.utils;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtils {
    public static String getDateNowConverted(){
        Date date = new Date();
        SimpleDateFormat  formatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");
        return formatter.format(date);
    }
}
