package com.example.speedtest.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.speedtest.R;
import com.example.speedtest.interfaces.ResultTouchHelper;
import com.example.speedtest.model.ConnectivityTestModel;
import com.example.speedtest.utils.DateTimeUtils;

import java.util.Date;
import java.util.List;

public class ConnectivityTestAdapter extends RecyclerView.Adapter<ConnectivityTestAdapter.ConnectivityTestViewHolder> {
    private List<ConnectivityTestModel> mList;
    private ResultTouchHelper resultTouchHelper;
    public void setData(List<ConnectivityTestModel> mList){
        this.mList = mList;
        notifyDataSetChanged();
    }
    public ConnectivityTestAdapter(ResultTouchHelper resultTouchHelper){
        this.resultTouchHelper = resultTouchHelper;
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
            int level = model.getType().equals("wifi") ? Integer.parseInt(model.getWifi().getWifi_level()) : 0;
            if(model.getType().equals("wifi")){
                int source = level >= -60 ? R.drawable.ic_signal_good : level < -60 && level >= -90 ? R.drawable.ic_signal_normal :R.drawable.ic_signal_low;
                holder.connectionType.setImageResource(source);


            }else {
                holder.connectionType.setImageResource(R.drawable.ic_mobiledata);
            }
            holder.date.setText(DateTimeUtils.getDateConverted(model.getDate()));
            holder.uploadRate.setText(model.getUpLoadSpeed());
            holder.downloadRate.setText(model.getDownloadSpeed());
            holder.itemView.setOnClickListener(view -> resultTouchHelper.onClickResultTest(model));
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
            date = itemView.findViewById(R.id.tv_dateScan);
            downloadRate = itemView.findViewById(R.id.tv_downloadRate);
            uploadRate = itemView.findViewById(R.id.tv_uploadRate);
            connectionType = itemView.findViewById(R.id.imv_connectionType);
        }
    }
}
