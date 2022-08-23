package com.example.speedtest.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "mobile_model")
public class Mobile implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String mobile_name;
    private String mobile_internal_ip;
    private String mobile_external_ip;
    private String mobile_isp;
    private boolean mobile_isConnected;

    public Mobile(String mobile_name, String mobile_internal_ip, String mobile_external_ip,String mobile_isp ,boolean mobile_isConnected) {
        this.mobile_name = mobile_name;
        this.mobile_internal_ip = mobile_internal_ip;
        this.mobile_external_ip = mobile_external_ip;
        this.mobile_isConnected = mobile_isConnected;
        this.mobile_isp = mobile_isp;
    }
    public Mobile(){

    }
    public String getMobile_isp() {
        return mobile_isp;
    }

    public void setMobile_isp(String mobile_isp) {
        this.mobile_isp = mobile_isp;
    }

    public String getMobile_name() {
        return mobile_name;
    }

    public void setMobile_name(String mobile_name) {
        this.mobile_name = mobile_name;
    }

    public String getMobile_internal_ip() {
        return mobile_internal_ip;
    }

    public void setMobile_internal_ip(String mobile_internal_ip) {
        this.mobile_internal_ip = mobile_internal_ip;
    }

    public String getMobile_external_ip() {
        return mobile_external_ip;
    }

    public void setMobile_external_ip(String mobile_external_ip) {
        this.mobile_external_ip = mobile_external_ip;
    }

    public boolean isMobile_isConnected() {
        return mobile_isConnected;
    }

    public void setMobile_isConnected(boolean mobile_isConnected) {
        this.mobile_isConnected = mobile_isConnected;
    }




}
