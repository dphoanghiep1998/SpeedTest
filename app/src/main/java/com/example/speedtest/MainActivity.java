package com.example.speedtest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.speedtest.config.SettingGlobal;
import com.example.speedtest.databinding.ActivityMainBinding;
import com.example.speedtest.fragments.AnalyzerFragment;
import com.example.speedtest.fragments.CheckResultFragment;
import com.example.speedtest.fragments.SpeedTestFragment;
import com.example.speedtest.utils.NetworkUtils;
import com.example.speedtest.view_model.WifiTestViewModel;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
   public ActivityMainBinding binding;
    SpeedTestFragment speedTestFragment = new SpeedTestFragment();
    AnalyzerFragment analiyzerFragment = new AnalyzerFragment();
    CheckResultFragment checkResultFragment = new CheckResultFragment();
    public WifiTestViewModel viewModel;
    boolean permission = false;
    boolean isScanning = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(WifiTestViewModel .class);
        checkMobileConnected();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        initView();
        setActionMenu();
    }
    public void checkMobileConnected(){
        Log.d("TAG", "checkMobileConnected: "+ NetworkUtils.isMobileConnected(getApplication()));
        Log.d("TAG", "checkMobileConnected: "+NetworkUtils.getInforMobileConnected(getApplication()).getSubtypeName());

    }

    public void initView() {
        binding.imvDelete.setVisibility(View.GONE);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, speedTestFragment).commit();
        binding.navBottom.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.speedtest:
                        binding.imvDelete.setVisibility(View.GONE);
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, speedTestFragment).commit();
                        return true;
                    case R.id.analist:
                        if(isScanning){
                            return false;
                        }
                        requestLocationPermission();
                        if(permission){
                            binding.imvDelete.setVisibility(View.GONE);
                            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, analiyzerFragment).commit();
                            return true;

                        }else{
                            return  false;
                        }

                    case R.id.history:
                        if(isScanning){
                            return false;
                        }
                        binding.imvDelete.setVisibility(View.VISIBLE);
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, checkResultFragment).commit();
                        return true;

                    default:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, speedTestFragment).commit();
                        return true;
                }
            }
        });
    }
    public void setActionMenu(){
        binding.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.drawerContainer.openDrawer(GravityCompat.START);
            }
        });
        binding.imvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.drawerContainer.close();
            }
        });
    }

    public void requestLocationPermission(){
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, SettingGlobal.REQUEST_CODE_LOCATION_PERMISSION);
        }else {
            permission = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == SettingGlobal.REQUEST_CODE_LOCATION_PERMISSION){
            if(grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                permission = true;
            }
        }
    }

    public void setIsScanning(boolean status){
        isScanning = status;
    }
}