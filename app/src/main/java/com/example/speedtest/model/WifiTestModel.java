package com.example.speedtest.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "connect_model")
public class WifiTestModel {
    @PrimaryKey(autoGenerate = true)
    private int id;


    private String name;
    private String date;
    private String downloadSpeed;
    private String upLoadSpeed;
    private String ping;
    private String type;
    private String jitter;
    private String loss;

    public String getJitter() {
        return jitter;
    }

    public void setJitter(String jitter) {
        this.jitter = jitter;
    }

    public String getLoss() {
        return loss;
    }

    public void setLoss(String loss) {
        this.loss = loss;
    }




    public WifiTestModel(String name, String date, String downloadSpeed, String upLoadSpeed, String ping,String type) {
        this.name = name;
        this.date = date;
        this.downloadSpeed = downloadSpeed;
        this.upLoadSpeed = upLoadSpeed;
        this.ping = ping;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
