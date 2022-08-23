package com.example.speedtest.database;

import androidx.room.TypeConverter;

import com.example.speedtest.model.Mobile;
import com.example.speedtest.model.Wifi;
import com.google.gson.Gson;

public class Convertes {
    Gson gson = new Gson();
    @TypeConverter
    public Wifi toWifi(String wifi){
        return gson.fromJson(wifi,Wifi.class);
    }
    @TypeConverter
    public String fromWifi(Wifi wifi){
        return gson.toJson(wifi);
    }
    @TypeConverter
    public Mobile toMobile(String mobile){
        return gson.fromJson(mobile,Mobile.class);
    }
    @TypeConverter
    public String fromMobile(Mobile mobile){
        return gson.toJson(mobile);
    }

}
