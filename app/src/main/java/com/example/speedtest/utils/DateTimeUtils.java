package com.example.speedtest.utils;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtils {
    public static String getDateNowConverted(){
        Date date = new Date();
        SimpleDateFormat  formatter = new SimpleDateFormat("dd MMM ");
        return formatter.format(date);
    }
}
