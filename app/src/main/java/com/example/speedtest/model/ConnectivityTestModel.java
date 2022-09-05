package com.example.speedtest.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "connectivity_model")
public class ConnectivityTestModel implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private Date date;
    private String downloadSpeed;
    private String upLoadSpeed;
    private String ping;
    private String jitter;
    private String loss;
    private Mobile mobile;
    private Wifi wifi;



    public ConnectivityTestModel(String name, Date date, String downloadSpeed, String upLoadSpeed, String ping,String jitter,String loss,Mobile mobile,Wifi wifi,String type) {
        this.name = name;
        this.date = date;
        this.downloadSpeed = downloadSpeed;
        this.upLoadSpeed = upLoadSpeed;
        this.ping = ping;
        this.mobile = mobile;
        this.wifi = wifi;
        this.jitter = jitter;
        this.loss = loss;
        this.type = type;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type;

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

    public ConnectivityTestModel(){}

    public Mobile getMobile() {
        return mobile;
    }

    public void setMobile(Mobile mobile) {
        this.mobile = mobile;
    }

    public Wifi getWifi() {
        return wifi;
    }

    public void setWifi(Wifi wifi) {
        this.wifi = wifi;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
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
