package com.example.speedtest.adapter;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.speedtest.R;
import com.example.speedtest.interfaces.ItemTouchHelper;
import com.example.speedtest.model.Wifi;

import java.util.List;

public class WifiChannelAdapter extends RecyclerView.Adapter<WifiChannelAdapter.WifiChannelViewHolder> {
    private List<Wifi> mList;
    private ItemTouchHelper helper;

    public void setData(List<Wifi> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    public WifiChannelAdapter(ItemTouchHelper helper) {
        this.helper = helper;
    }

    @NonNull
    @Override
    public WifiChannelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new WifiChannelViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wifi_channel, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WifiChannelViewHolder holder, int position) {
        Wifi wifiModel = mList.get(position);
        if (wifiModel != null) {
            int level = Integer.parseInt(wifiModel.getWifi_level());
            int source = level >= -60 ? R.drawable.ic_signal_good : level < -60 && level >= -90 ? R.drawable.ic_signal_normal : R.drawable.ic_signal_low;
            holder.imv_wifi.setImageResource(source);
            holder.tv_frequency.setText(wifiModel.getWifi_frequency().toString() + " MHz");
            holder.tv_strength.setText(wifiModel.getWifi_level().toString() + " dBm");
            holder.tv_internalIp.setText(wifiModel.getWifi_internal_ip());
            holder.tv_secureType.setText(wifiModel.getWifi_secure_type());
            holder.tv_wifiName.setText(wifiModel.getWifi_name());
            if (wifiModel.isWifi_isConnected()) {
                holder.tv_internalIp.setVisibility(View.GONE);
                holder.tv_frequency.setVisibility(View.GONE);
                holder.tv_connected.setVisibility(View.VISIBLE);
                //setdata
                holder.tv_connected.setText("Đã kết nối");

            } else {
                holder.itemView.setBackground(holder.itemView.getResources().getDrawable(R.drawable.infor_container));
                holder.tv_internalIp.setVisibility(View.GONE);
                holder.tv_frequency.setVisibility(View.GONE);
                holder.tv_connected.setVisibility(View.GONE);

            }
            holder.itemView.setOnClickListener(view -> {
                helper.onClickItemWifi(wifiModel);
            });
        }

    }


    @Override
    public int getItemCount() {
        if (mList != null) {
            return mList.size();
        }
        return 0;
    }

    public class WifiChannelViewHolder extends RecyclerView.ViewHolder {
        ImageView imv_wifi;
        TextView tv_wifiName;
        TextView tv_internalIp;
        TextView tv_secureType;
        TextView tv_strength;
        TextView tv_frequency;
        TextView tv_connected;

        public WifiChannelViewHolder(@NonNull View itemView) {
            super(itemView);
            imv_wifi = itemView.findViewById(R.id.imv_wifi);
            tv_wifiName = itemView.findViewById(R.id.tv_wifi_name);
            tv_internalIp = itemView.findViewById(R.id.tv_internal_ip);
            tv_secureType = itemView.findViewById(R.id.tv_security_type);
            tv_strength = itemView.findViewById(R.id.tv_signal_strength);
            tv_frequency = itemView.findViewById(R.id.tv_hz);
            tv_connected = itemView.findViewById(R.id.tv_connected);
        }
    }
}
