package com.example.speedtest.fragments;
import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;

import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;

import com.example.speedtest.R;
import com.example.speedtest.core.Speedtest;
import com.example.speedtest.core.config.SpeedtestConfig;
import com.example.speedtest.core.serverSelector.TestPoint;
import com.example.speedtest.databinding.FragmentSpeedtestBinding;
import com.example.speedtest.services.CheckISPIP;
import com.example.speedtest.utils.NetworkUtils;
import com.github.anastr.speedviewlib.Gauge;
import com.github.anastr.speedviewlib.components.Style;

import org.json.JSONArray;


import java.io.BufferedReader;
import java.io.EOFException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import kotlin.Unit;

public class SpeedTestFragment extends Fragment implements View.OnClickListener {
    FragmentSpeedtestBinding binding;
    int duration = 0;
    Boolean isConnectivityChanged = false;

    private BroadcastReceiver internetBroad;
    private IntentFilter internetFilter;
    private static Speedtest st=null;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSpeedtestBinding.inflate(inflater, container, false);
        initBroadCast();
        getActivity().registerReceiver(internetBroad, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView();


    }

    @Override
    public void onResume() {
        Log.d("msg", "onResume: ");
        super.onResume();
        if(isConnectivityChanged){
            if(NetworkUtils.isWifiConnected(getContext())){
                SpannableString content = new SpannableString(NetworkUtils.getNameWifi(getContext().getApplicationContext()).substring(1,NetworkUtils.getNameWifi(getContext().getApplicationContext()).length()-1));
                content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                binding.tvWifiName.setText(content);
            }
        }
        isConnectivityChanged = false;
    }

    private void initBroadCast() {
        internetFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        internetBroad = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                isConnectivityChanged = true;
            }
        };
    }



    private void unRegisterBroadCast() {
        requireContext().unregisterReceiver(internetBroad);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegisterBroadCast();
    }

    public void setupView() {
        // Underline wifi name
        String wifi_name = binding.tvWifiName.getText().toString();
        SpannableString underlineWifiName = new SpannableString(wifi_name);
        underlineWifiName.setSpan(new UnderlineSpan(), 0, wifi_name.length(), 0);
        binding.tvWifiName.setText(underlineWifiName);
        // custom tick
        customTick();

        //onClick start scan
        binding.btnStartScan.setOnClickListener(this);

        //select Wifi
        binding.tvWifiName.setOnClickListener(this);


    }

    public void customTick() {
        List<Float> arList = new ArrayList<>(Arrays.asList(0f, 0.05f, 0.1f, 0.15f, 0.20f, 0.30f, 0.5f, 0.75f, 1f));
        binding.speedView.clearSections();
        binding.speedView.makeSections(1,Color.GRAY, Style.BUTT);

        binding.speedView.setTicks(arList);
        binding.speedView.setSpeedTextPosition(Gauge.Position.BOTTOM_CENTER);
        binding.speedView.setUnit("");
        binding.speedView.setOnPrintTickLabel((tickPosition, tick) -> {
            int convertedTick = Math.round(tick);
            if (tick == 0) {
                SpannableString tickLabel = new SpannableString(convertedTick + "");
                tickLabel.setSpan(new ForegroundColorSpan(Color.WHITE), 0, tickLabel.toString().trim().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                return tickLabel;
            }
            if (tick == 5) {
                SpannableString tickLabel = new SpannableString(convertedTick + "");
                tickLabel.setSpan(new ForegroundColorSpan(Color.WHITE), 0, tickLabel.toString().trim().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                return tickLabel;
            }
            if (tick == 10) {
                SpannableString tickLabel = new SpannableString(convertedTick + "");
                tickLabel.setSpan(new ForegroundColorSpan(Color.WHITE), 0, tickLabel.toString().trim().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                return tickLabel;
            }
            if (tick == 15) {
                SpannableString tickLabel = new SpannableString(convertedTick + "");
                tickLabel.setSpan(new ForegroundColorSpan(Color.WHITE), 0, tickLabel.toString().trim().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                return tickLabel;
            }
            if (tick == 20) {
                SpannableString tickLabel = new SpannableString(convertedTick + "");
                tickLabel.setSpan(new ForegroundColorSpan(Color.WHITE), 0, tickLabel.toString().trim().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                return tickLabel;
            }
            if (tick == 30) {
                SpannableString tickLabel = new SpannableString(convertedTick + "");
                tickLabel.setSpan(new ForegroundColorSpan(Color.WHITE), 0, tickLabel.toString().trim().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                return tickLabel;
            }
            if (tick == 50) {
                SpannableString tickLabel = new SpannableString(convertedTick + "");
                tickLabel.setSpan(new ForegroundColorSpan(Color.WHITE), 0, tickLabel.toString().trim().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                return tickLabel;
            }
            if (tick == 75) {
                SpannableString tickLabel = new SpannableString(convertedTick + "");
                tickLabel.setSpan(new ForegroundColorSpan(Color.WHITE), 0, tickLabel.toString().trim().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                return tickLabel;
            }
            if (tick == 100) {
                SpannableString tickLabel = new SpannableString(convertedTick + "");
                tickLabel.setSpan(new ForegroundColorSpan(Color.WHITE), 0, tickLabel.toString().trim().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                return tickLabel;

            }
            return null;
        });
    }



    public void onClickStartButton() {
        if (!NetworkUtils.isWifiConnected(this.getContext())) {
            Toast.makeText(this.getContext(), "No wifi connected", Toast.LENGTH_SHORT).show();
            return;
        }
        loadServer();


    }

    public void onCickWifiName() {
        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
        startActivity(intent);
    }

    public void testPing(){
        try{
            ProcessBuilder ps = new ProcessBuilder("ping", "-c " + 4, "192.168.1.1");

            ps.redirectErrorStream(true);
            Process pr = ps.start();
            BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                Log.d("TAG", line.toString());
            }
            pr.waitFor();
            in.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start_scan:
                onClickStartButton();
                break;
            case R.id.tv_wifi_name:
                onCickWifiName();
                break;
        }
    }


    private String readFileFromAssets(String name) throws Exception{
        BufferedReader b=new BufferedReader(new InputStreamReader(requireContext().getApplicationContext().getAssets().open(name)));
        String ret="";
        try{
            for(;;){
                String s=b.readLine();
                if(s==null) break;
                ret+=s;
            }
        }catch(EOFException e){}
        return ret;
    }

    public void loadServer(){
      new Thread(){
          @Override
          public void run() {
              SpeedtestConfig config;
              TestPoint server;
              try{
            String c;
                  if(st!=null){
                      try{st.abort();}catch (Throwable e){}
                  }
                  st=new Speedtest();
                  c=readFileFromAssets("Server.json");
                  if(c.startsWith("\"")||c.startsWith("'")){ //fetch server list from URL
                      if(!st.loadServer(c.subSequence(1,c.length()-1).toString())){
                          throw new Exception("Failed to load server list");
                      }
                  }else{ //use provided server list
                      JSONArray a=new JSONArray(c);
                      if(a.length()==0) throw new Exception("No test points");
                      server = new TestPoint(a.getJSONObject(0));
                      st.addTestPoint(server);
                      runSpeedTest(server);
                  }
              } catch (Exception e) {
                  e.printStackTrace();
              }

          }
      }.start();
    }
    public void runSpeedTest(TestPoint tp){
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(100); //You can manage the blinking time with this parameter
        anim.setStartOffset(50);
        anim.setRepeatCount(Animation.INFINITE);
        st.start(new Speedtest.SpeedtestHandler() {
            @Override
            public void onDownloadUpdate(double dl, double progress) {
                if(progress == 0){
                    binding.tvDownloadValue.startAnimation(anim);

                }
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        binding.speedView.speedTo((float) dl);

                        if(progress >= 1){
                            binding.tvDownloadValue.clearAnimation();
                            binding.tvDownloadValue.setText(format(dl));
                            binding.speedView.speedTo(0);
                            binding.speedView.stop();
                        }
                    }
                });
            }

            @Override
            public void onUploadUpdate(double ul, double progress) {
                if(progress == 0){
                    binding.tvUploadValue.startAnimation(anim);
                }
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        binding.speedView.speedTo((float) ul);
                        if(progress >= 1){
                            binding.tvUploadValue.clearAnimation();
                            binding.tvUploadValue.setText(format(ul));
                            binding.speedView.setWithTremble(false);
                            binding.speedView.speedTo(0);
                            binding.speedView.stop();
                        }
                    }
                });
            }

            @Override
            public void onPingJitterUpdate(double ping, double jitter, double progress) {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        binding.tvPingCount.setText(format(ping) + " ms");
                        binding.tvJitterCount.setText(format(jitter)+ " ms");
                    }
                });
            }

            @Override
            public void onIPInfoUpdate(String ipInfo) {
                Log.d("TAG", "onIPInfoUpdate: "+ipInfo);
            }

            @Override
            public void onEnd() {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        binding.speedView.speedTo(0);
                        binding.speedView.stop();
                        Log.d("TAG", "onEnd: ");
                    }
                });

            }

            @Override
            public void onCriticalFailure(String err) {
                Log.d("TAG", "onCriticalFailure: "+err);
            }
        });
    }

    private String format(double d){
        Locale l=null;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N) {
            l = getResources().getConfiguration().getLocales().get(0);
        }else{
            l=getResources().getConfiguration().locale;
        }
        if(d<10) return String.format(l,"%.2f",d);
        if(d<100) return String.format(l,"%.1f",d);
        return ""+Math.round(d);
    }


}
