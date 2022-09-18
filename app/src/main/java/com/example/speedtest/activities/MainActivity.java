package com.example.speedtest.activities;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
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
import com.example.speedtest.R;
import com.example.speedtest.adapter.ViewPagerAdapter;
import com.example.speedtest.config.SettingGlobal;
import com.example.speedtest.databinding.ActivityMainBinding;
import com.example.speedtest.receivers.WifiListener;
import com.example.speedtest.view_model.WifiTestViewModel;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    public ActivityMainBinding binding;
    ViewPagerAdapter viewPager;
    public WifiTestViewModel viewModel;
    public boolean first_time = true;
    private SpeedApplication application;
    private WifiListener wifiListener;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = SpeedApplication.create(this);
        application.getShareData().isScanning.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (!aBoolean && !first_time) {
                    showVipBtn();
                    binding.imvVip.setEnabled(true);
                } else {
                    binding.imvVip.setEnabled(false);
                }
            }
        });

        initWifiListener();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(WifiTestViewModel.class);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        initView();
        setActionMenu();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            application.getShareData().isPermissionRequested.postValue(true);
        } else {
            application.getShareData().isPermissionRequested.postValue(false);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopWifiListener();
    }

    public void initWifiListener(){
        wifiListener = new WifiListener(this);
       IntentFilter intentFilter = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
       registerReceiver(wifiListener,intentFilter);
   }
   public void stopWifiListener(){
        unregisterReceiver(wifiListener);
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
                showMenu();
                switch (position) {
                    case 0:
                        binding.tvTitle.setText("SPEEDTEST");
                        break;
                    case 1:
                        binding.tvTitle.setText("WIFI ANALYZER");
                        break;
                    case 2:
                        binding.tvTitle.setText("VPN");
                        break;
                    case 3:
                        binding.tvTitle.setText("RESULTS");
                        break;
                    default:
                        binding.tvTitle.setText("SPEEDTEST");
                        break;

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

        binding.navBottom.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                showMenu();

                switch (item.getItemId()) {
                    case R.id.speedtest:
                        binding.tvTitle.setText("SPEEDTEST");
                        binding.vpContainerFrament.setCurrentItem(0);

                        return true;
                    case R.id.analist:
                        binding.tvTitle.setText("WIFI ANALYZER");


                        binding.vpContainerFrament.setCurrentItem(1);
                        return true;

                    case R.id.vpn:
                        binding.tvTitle.setText("VPN");
                        binding.vpContainerFrament.setCurrentItem(2);

                        return true;
                    case R.id.history:
                        binding.tvTitle.setText("RESULTS");
                        binding.vpContainerFrament.setCurrentItem(3);

                        return true;

                }
                return true;

            }
        });
        binding.vpContainerFrament.setCurrentItem(0);
        binding.containerRate.setOnClickListener(view -> openLink("http://www.google.com"));
        binding.containerPolicy.setOnClickListener(view -> openLink("http://www.google.com"));
//        binding.containerFanpage.setOnClickListener(view -> openLink("http://www.facebook.com"));
//        binding.containerOtherApp.setOnClickListener(view -> openLink("http://www.google.com"));
        binding.containerShare.setOnClickListener(view -> shareApp());
        binding.imvStop.setOnClickListener(view1 -> {
            application.getShareData().isScanning.postValue(false);
        });
        binding.imvVip.setOnClickListener(view -> {
            Intent intent = new Intent();
        });
        binding.backBtn.setOnClickListener(view -> {
            onBackPressed();
            showMenu();
        });
    }

    public void setActionMenu() {
        binding.menu.setOnClickListener(view -> binding.drawerContainer.openDrawer(GravityCompat.START, true));

        binding.imvBack.setOnClickListener(view -> binding.drawerContainer.close());
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


    public void hideBottomTabWhenScan() {
        setViewGone(binding.navBottom);
        binding.vpContainerFrament.setUserInputEnabled(false);
    }

    public void showBottomTabAfterScan() {
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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

    public void showStopBtn() {
        YoYo.with(Techniques.FadeOut).duration(400).onEnd(animator -> {
            binding.imvVip.setVisibility(View.GONE);
        }).playOn(binding.imvVip);
        YoYo.with(Techniques.FadeIn).duration(400).onEnd(animator1 -> {
            binding.imvStop.setVisibility(View.VISIBLE);
        }).playOn(binding.imvStop);


    }

    public void showVipBtn() {
        YoYo.with(Techniques.FadeOut).duration(400).onEnd(animator -> {
            binding.imvStop.setVisibility(View.GONE);
        }).playOn(binding.imvStop);
        YoYo.with(Techniques.FadeIn).duration(400).onEnd(animator1 -> {
            binding.imvVip.setVisibility(View.VISIBLE);
        }).playOn(binding.imvVip);
    }

    public void showBackBtn() {
        binding.tvTitle.setText("CHANGE LOCATION");
        YoYo.with(Techniques.FadeOut).duration(400).onEnd(animator -> {
            binding.menu.setVisibility(View.GONE);
        }).playOn(binding.menu);

        YoYo.with(Techniques.FadeIn).duration(400).onEnd(animator1 -> {
            binding.backBtn.setVisibility(View.VISIBLE);
        }).playOn(binding.backBtn);
    }

    public void showMenu() {
        if (binding.menu.getVisibility() == View.VISIBLE) {
            return;
        }
        binding.tvTitle.setText("VPN");

        YoYo.with(Techniques.FadeOut).duration(400).onEnd(animator -> {
            binding.backBtn.setVisibility(View.GONE);
        }).playOn(binding.backBtn);
        YoYo.with(Techniques.FadeIn).duration(400).onEnd(animator1 -> {
            binding.menu.setVisibility(View.VISIBLE);
        }).playOn(binding.menu);
    }



}