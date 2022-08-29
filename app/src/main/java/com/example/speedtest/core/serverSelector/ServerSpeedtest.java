package com.example.speedtest.core.serverSelector;

public class ServerSpeedtest {
    private String uploadAddress;
    private String name;
    private String country;
    private String cc;
    private String sponsor;
    private String lat;
    private String lon;
    private String host;


    public ServerSpeedtest(String uploadAddress, String name, String country, String cc, String sponsor, String lat, String lon, String host) {
        this.uploadAddress = uploadAddress;
        this.name = name;
        this.country = country;
        this.cc = cc;
        this.sponsor = sponsor;
        this.lat = lat;
        this.lon = lon;
        this.host = host;

    }

    public ServerSpeedtest(){

    }


    public String getUploadAddress() {
        return uploadAddress;
    }

    public void setUploadAddress(String uploadAddress) {
        this.uploadAddress = uploadAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getSponsor() {
        return sponsor;
    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
