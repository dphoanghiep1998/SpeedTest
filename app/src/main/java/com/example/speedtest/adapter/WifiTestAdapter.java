package com.example.speedtest.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.speedtest.R;
import com.example.speedtest.model.WifiTestModel;

import java.util.List;

public class WifiTestAdapter extends RecyclerView.Adapter<WifiTestAdapter.WifiTestViewHolder> {
    private List<WifiTestModel> mList;
    public void setData(List<WifiTestModel> mList){
        this.mList = mList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public WifiTestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_result,parent,false);
        return new WifiTestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WifiTestViewHolder holder, int position) {
        WifiTestModel model = mList.get(position);
        if(model != null){
            holder.ssid.setText(model.getName());
            holder.date.setText(model.getDate());
            holder.uploadRate.setText(model.getUpLoadSpeed());
            holder.downloadRate.setText(model.getDownloadSpeed());
            holder.pingRate.setText(model.getPing());
        }
    }

    @Override
    public int getItemCount() {
        if(mList != null){
            return mList.size();
        }
        return 0;
    }

    public class WifiTestViewHolder extends RecyclerView.ViewHolder{
    TextView ssid;
    TextView date;
    TextView downloadRate;
    TextView uploadRate;
    TextView pingRate;
    ImageView connectionType;
        public WifiTestViewHolder(@NonNull View itemView) {
            super(itemView);
            ssid = itemView.findViewById(R.id.tv_ssid);
            date = itemView.findViewById(R.id.tv_dateScan);
            downloadRate = itemView.findViewById(R.id.tv_downloadRate);
            uploadRate = itemView.findViewById(R.id.tv_uploadRate);
            pingRate = itemView.findViewById(R.id.tv_pingRate);
            connectionType = itemView.findViewById(R.id.imv_connectionType);
        }
    }
}
