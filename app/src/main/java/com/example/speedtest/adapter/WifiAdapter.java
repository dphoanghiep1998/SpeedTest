package com.example.speedtest.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.speedtest.model.Wifi;

import java.util.List;

public class WifiAdapter {
    private List<Wifi> wifiList;
    public void setData(List<Wifi> mList){
        this.wifiList =mList;
    }
    public class WifiViewHolder extends RecyclerView.ViewHolder {


        public WifiViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
