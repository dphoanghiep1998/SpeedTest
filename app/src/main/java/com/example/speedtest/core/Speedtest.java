package com.example.speedtest.core;


import android.util.Log;

import com.example.speedtest.core.config.SpeedtestConfig;
import com.example.speedtest.core.serverSelector.TestPoint;
import com.example.speedtest.core.worker.SpeedtestWorker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class Speedtest {
    private TestPoint server=null;
    private SpeedtestConfig config=new SpeedtestConfig();
    private int state=0; //0=configs, 1=test points, 2=server selection, 3=ready, 4=testing, 5=finished

    private Object mutex=new Object();

    private String originalExtra="";

    public Speedtest(){

    }



    public void addTestPoint(TestPoint t){
        synchronized (mutex) {
            if (state == 0) state = 1;
            if (state > 1) throw new IllegalStateException("Cannot add test points at this moment");
         server = t;
         state = 3;
        }
    }


    public void addTestPoint(JSONObject json){
        synchronized (mutex) {
            addTestPoint(new TestPoint(json));
        }
    }

    public boolean loadServer(String url){
        synchronized (mutex) {
            if (state == 0) state = 1;
            if (state > 1) throw new IllegalStateException("Cannot add test points at this moment");
            TestPoint pts= ServerLoader.loadServer(url);
            if(pts!=null){
                addTestPoint(pts);
                return true;
            }else return false;
        }
    }

    private static class ServerLoader {
        private static String read(String url){
            try{
                URL u=new URL(url);
                InputStream in=u.openStream();
                BufferedReader br=new BufferedReader(new InputStreamReader(u.openStream()));
                String s="";
                try{
                    for(;;){
                        String r=br.readLine();
                        if(r==null) break; else s+=r;
                    }
                }catch(Throwable t){}
                br.close();
                in.close();
                return s;
            }catch(Throwable t){
                return null;
            }
        }

        public static TestPoint loadServer(String url){
            try{
                String s=null;
                if(url.startsWith("//")){
                    s=read("https:"+url);
                    if(s==null) s=read("http:"+url);
                }else s=read(url);
                if(s==null) throw new Exception("Failed");
                JSONArray a=new JSONArray(s);
                return new TestPoint(a.getJSONObject(0));
            }catch(Throwable t){
                return null;
            }
        }
    }


    private SpeedtestWorker st=null;
    public void start(final SpeedtestHandler callback){
        synchronized (mutex) {
            if (state < 3) throw new IllegalStateException("Server hasn't been selected yet");
            if (state == 4) throw new IllegalStateException("Test already running");
            state = 4;
            try {
                JSONObject extra = new JSONObject();
                if (originalExtra != null && !originalExtra.isEmpty())
                    extra.put("extra", originalExtra);
                extra.put("server", server.getName());
                config.setTelemetry_extra(extra.toString());
            } catch (Throwable t) {
            }
            st = new SpeedtestWorker(server, config) {
                @Override
                public void onDownloadUpdate(double dl, double progress) {
                    callback.onDownloadUpdate(dl, progress);
                }

                @Override
                public void onUploadUpdate(double ul, double progress) {
                    callback.onUploadUpdate(ul, progress);
                }

                @Override
                public void onPingJitterUpdate(double ping, double jitter, double progress) {
                    callback.onPingJitterUpdate(ping, jitter, progress);
                }

                @Override
                public void onIPInfoUpdate(String ipInfo) {
                    callback.onIPInfoUpdate(ipInfo);
                }



                @Override
                public void onEnd() {
                    synchronized (mutex) {
                        state = 5;
                    }
                    callback.onEnd();
                }

                @Override
                public void onCriticalFailure(String err) {
                    synchronized (mutex) {
                        state = 5;
                    }
                    callback.onCriticalFailure(err);
                }

                @Override
                public void onAbort() {

                    callback.onAbort();
                }

            };
        }
    }


    public void abort(){
        synchronized (mutex) {
            if (state == 4) st.abort();
            state = 5;

        }
    }

    public static abstract class SpeedtestHandler{
        public abstract void onDownloadUpdate(double dl, double progress);
        public abstract void onUploadUpdate(double ul, double progress);
        public abstract void onPingJitterUpdate(double ping, double jitter, double progress);
        public abstract void onIPInfoUpdate(String ipInfo);
        public abstract void onEnd();
        public abstract void onAbort();
        public abstract void onCriticalFailure(String err);
    }
}
