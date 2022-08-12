package com.example.speedtest.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "wifi_model")
public class WifiTestModel {
    @PrimaryKey(autoGenerate = true)
    private String id;
    private String name;
    private String date;
    private String downloadSpeed;
    private String upLoadSpeed;
    private String ping;

    public WifiTestModel(String name, String date, String downloadSpeed, String upLoadSpeed, String ping) {
        this.name = name;
        this.date = date;
        this.downloadSpeed = downloadSpeed;
        this.upLoadSpeed = upLoadSpeed;
        this.ping = ping;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDownloadSpeed() {
        return downloadSpeed;
    }

    public void setDownloadSpeed(String downloadSpeed) {
        this.downloadSpeed = downloadSpeed;
    }

    public String getUpLoadSpeed() {
        return upLoadSpeed;
    }

    public void setUpLoadSpeed(String upLoadSpeed) {
        this.upLoadSpeed = upLoadSpeed;
    }

    public String getPing() {
        return ping;
    }

    public void setPing(String ping) {
        this.ping = ping;
    }
}
