package com.example.speedtest.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.speedtest.databinding.ItemVpnBinding;

import java.util.List;

public class VpnAdapter extends RecyclerView.Adapter<VpnAdapter.VpnViewHolder> {
    List mList;
    @NonNull
    @Override
    public VpnViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }
    public void setData(List list){
        this.mList = list;
    }
    @Override
    public void onBindViewHolder(@NonNull VpnViewHolder holder, int position) {
        // Todo need Vpn model
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class VpnViewHolder extends RecyclerView.ViewHolder{
        private ItemVpnBinding binding;
        public VpnViewHolder(@NonNull ItemVpnBinding itemVpnBinding) {
            super(itemVpnBinding.getRoot());
            this.binding = itemVpnBinding;
        }
    }
}
