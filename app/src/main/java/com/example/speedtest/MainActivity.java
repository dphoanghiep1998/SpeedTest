package com.example.speedtest;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.speedtest.adapter.ViewPagerAdapter;
import com.example.speedtest.config.SettingGlobal;
import com.example.speedtest.databinding.ActivityMainBinding;
import com.example.speedtest.utils.NetworkUtils;
import com.example.speedtest.view_model.WifiTestViewModel;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    public ActivityMainBinding binding;
    ViewPagerAdapter viewPager;
    public WifiTestViewModel viewModel;
    public boolean first_time=true;
    private SpeedApplication application;




    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = SpeedApplication.create(this);
        application.getShareData().isScanning.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(!aBoolean && !first_time){
                    showMenuBtn();
                    binding.imvVip.setEnabled(true);
                }else{
                    binding.imvVip.setEnabled(false);
                }
            }
        });
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        requestLocationPermission();
        viewModel = new ViewModelProvider(this).get(WifiTestViewModel.class);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        initView();
        setActionMenu();
    }



    public void initView() {
        viewPager = new ViewPagerAdapter(this);
        binding.vpContainerFrament.setAdapter(viewPager);
        binding.vpContainerFrament.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                binding.navBottom.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
        binding.imvDelete.setVisibility(View.GONE);
        binding.navBottom.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.speedtest:
                        binding.imvDelete.setVisibility(View.GONE);
                        binding.vpContainerFrament.setCurrentItem(0);
                        return true;
                    case R.id.analist:

                            if (!NetworkUtils.isWifiEnabled(getApplicationContext())) {
//                                IntentFilter intent = new IntentFilter(ACTION)
                            }
                            binding.imvDelete.setVisibility(View.GONE);
                            binding.vpContainerFrament.setCurrentItem(1);
                            return true;

                    case R.id.vpn:
                        binding.vpContainerFrament.setCurrentItem(2);
                        return true;
                    case R.id.history:

                        binding.imvDelete.setVisibility(View.VISIBLE);
                        binding.vpContainerFrament.setCurrentItem(3);
                        return true;

                }
                return true;

            }
        });
        binding.vpContainerFrament.setCurrentItem(0);
        binding.containerRate.setOnClickListener(view -> openLink("http://www.google.com"));
        binding.containerPolicy.setOnClickListener(view -> openLink("http://www.google.com"));
        binding.containerFanpage.setOnClickListener(view -> openLink("http://www.facebook.com"));
        binding.containerOtherApp.setOnClickListener(view -> openLink("http://www.google.com"));
        binding.containerShare.setOnClickListener(view -> shareApp());

    }

    public void setActionMenu() {
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

    public void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, SettingGlobal.REQUEST_CODE_LOCATION_PERMISSION);
        } else {
           application.getShareData().isPermissionRequested.postValue(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SettingGlobal.REQUEST_CODE_LOCATION_PERMISSION) {
            if (grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                application.getShareData().isPermissionRequested.postValue(true);
            }
        }
    }

    public void openLink(String strUri) {
        try {
            Uri uri = Uri.parse(strUri);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void shareApp() {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Speedtest");
            String shareMessage = "Very good speed test app ~~";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "Choose one"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    public void hideBottomTabWhenScan(){

        setViewGone(binding.navBottom);
        binding.vpContainerFrament.setUserInputEnabled(false);
    }
    public void showBottomTabAfterScan(){
        setViewVisible(binding.navBottom);
        binding.vpContainerFrament.setUserInputEnabled(true);

    }
    private void setViewGone(final View view) {

        view.animate()
                .alpha(0f)
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(View.GONE);
                    }
                });

    }

    private void setViewVisible(final View view) {

        view.animate()
                .alpha(1f)
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(View.VISIBLE);
                    }
                });

    }
    public void showCloseBtn(){
        YoYo.with(Techniques.FadeOut).duration(400).onEnd(animator -> {
            binding.menu.setImageResource(R.drawable.ic_close);
            binding.menu.setOnClickListener(view1 -> {
                application.getShareData().isScanning.postValue(false);
            });
            YoYo.with(Techniques.FadeIn).playOn(binding.menu);
        }).playOn(binding.menu);


    }
    public void showMenuBtn(){
        YoYo.with(Techniques.FadeOut).duration(400).onEnd(animator -> {
            binding.menu.setImageResource(R.drawable.ic_menu);
            binding.menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    binding.drawerContainer.openDrawer(GravityCompat.START);
                }
            });
            YoYo.with(Techniques.FadeIn).playOn(binding.menu);
        }).playOn(binding.menu);
    }

}