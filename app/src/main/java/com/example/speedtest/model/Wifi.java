package com.example.speedtest.model;

import java.io.Serializable;

public class Wifi implements Serializable {


    private String wifi_name;
    private String wifi_internal_ip;
    private String wifi_secure_type;
    private String wifi_level;
    private String wifi_frequency;
    private String wifi_bssid;
    private String wifi_channel;
    private String wifi_distance;
    private boolean wifi_isConnected;

    public Wifi(String wifi_name, String wifi_internal_ip, String wifi_secure_type, String wifi_level, String wifi_frequency, String wifi_bssid, String wifi_channel, String wifi_distance, boolean wifi_isConnected) {
        this.wifi_name = wifi_name;
        this.wifi_internal_ip = wifi_internal_ip;
        this.wifi_secure_type = wifi_secure_type;
        this.wifi_level = wifi_level;
        this.wifi_frequency = wifi_frequency;
        this.wifi_bssid = wifi_bssid;
        this.wifi_channel = wifi_channel;
        this.wifi_distance = wifi_distance;
        this.wifi_isConnected = wifi_isConnected;
    }

    public String getWifi_bssid() {
        return wifi_bssid;
    }

    public void setWifi_bssid(String wifi_bssid) {
        this.wifi_bssid = wifi_bssid;
    }

    public String getWifi_channel() {
        return wifi_channel;
    }

    public void setWifi_channel(String wifi_channel) {
        this.wifi_channel = wifi_channel;
    }

    public String getWifi_distance() {
        return wifi_distance;
    }

    public void setWifi_distance(String wifi_distance) {
        this.wifi_distance = wifi_distance;
    }

    public String getWifi_name() {
        return wifi_name;
    }

    public void setWifi_name(String wifi_name) {
        this.wifi_name = wifi_name;
    }

    public String getWifi_internal_ip() {
        return wifi_internal_ip;
    }

    public void setWifi_internal_ip(String wifi_internal_ip) {
        this.wifi_internal_ip = wifi_internal_ip;
    }

    public String getWifi_secure_type() {
        return wifi_secure_type;
    }

    public void setWifi_secure_type(String wifi_secure_type) {
        this.wifi_secure_type = wifi_secure_type;
    }

    public String getWifi_level() {
        return wifi_level;
    }

    public void setWifi_level(String wifi_level) {
        this.wifi_level = wifi_level;
    }

    public String getWifi_frequency() {
        return wifi_frequency;
    }

    public void setWifi_frequency(String wifi_frequency) {
        this.wifi_frequency = wifi_frequency;
    }

    public boolean isWifi_isConnected() {
        return wifi_isConnected;
    }

    public void setWifi_isConnected(boolean wifi_isConnected) {
        this.wifi_isConnected = wifi_isConnected;
    }

}
