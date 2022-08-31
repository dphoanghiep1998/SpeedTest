package com.example.speedtest.core.serverSelector;

import org.json.JSONException;
import org.json.JSONObject;

public class TestPoint {
    private final String name, server, dlURL, ulURL, pingURL;
    protected float ping=-1;

    public TestPoint(String name, String server, String dlURL, String ulURL, String pingURL){
        this.name=name;
        this.server=server;
        this.dlURL=dlURL;
        this.ulURL=ulURL;
        this.pingURL=pingURL;

    }


    public String getName() {
        return name;
    }

    public String getServer() {
        return server;
    }

    public String getDlURL() {
        return dlURL;
    }

    public String getUlURL() {
        return ulURL;
    }

    public String getPingURL() {
        return pingURL;
    }


    public float getPing() {
        return ping;
    }
}
