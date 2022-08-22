package com.example.speedtest.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.speedtest.R;
import com.example.speedtest.model.ConnectivityTestModel;

import java.util.List;

public class ConnectivityTestAdapter extends RecyclerView.Adapter<ConnectivityTestAdapter.ConnectivityTestViewHolder> {
    private List<ConnectivityTestModel> mList;
    public void setData(List<ConnectivityTestModel> mList){
        this.mList = mList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ConnectivityTestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_result,parent,false);
        return new ConnectivityTestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConnectivityTestViewHolder holder, int position) {
        ConnectivityTestModel model = mList.get(position);
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

    public class ConnectivityTestViewHolder extends RecyclerView.ViewHolder{
    TextView ssid;
    TextView date;
    TextView downloadRate;
    TextView uploadRate;
    TextView pingRate;
    ImageView connectionType;
        public ConnectivityTestViewHolder(@NonNull View itemView) {
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
