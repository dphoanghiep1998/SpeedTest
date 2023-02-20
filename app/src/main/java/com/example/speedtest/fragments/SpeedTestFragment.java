package com.example.speedtest.fragments;


import static android.view.View.VISIBLE;

import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.speedtest.R;
import com.example.speedtest.activities.MainActivity;
import com.example.speedtest.activities.ResultActivity;
import com.example.speedtest.activities.SpeedApplication;
import com.example.speedtest.core.Speedtest;
import com.example.speedtest.core.getIP.GetIP;
import com.example.speedtest.core.serverSelector.TestPoint;
import com.example.speedtest.databinding.FragmentSpeedtestBinding;
import com.example.speedtest.model.ConnectivityTestModel;
import com.example.speedtest.model.Mobile;
import com.example.speedtest.model.Wifi;
import com.example.speedtest.utils.NetworkUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class SpeedTestFragment extends Fragment implements View.OnClickListener {
    FragmentSpeedtestBinding binding;
    private boolean isConnectivityChanged = true;
    private boolean isFragmentFreezing = false;
    private BroadcastReceiver internetBroad;
    private static Speedtest speedTest;
    private final Wifi wifi = new Wifi();
    private final Mobile mobile = new Mobile();
    private String type = "no connection";
    private SpeedApplication application;
    private GetIP getIP;
    private HashSet<String> tempBlackList;
    private TestPoint testPoint;
    private CountDownTimer countDownTimer;
    private Boolean isLoadedServer = false;


    @Override
    public void onPause() {
        super.onPause();
        isFragmentFreezing = true;
    }

    @Nullable
    @Override
    public View onCreateView(@androidx.annotation.NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSpeedtestBinding.inflate(inflater, container, false);
        application = SpeedApplication.create(requireContext());
        tempBlackList = new HashSet<>();
        initBroadCast();
        checkFirstWhenStart();
        registBroadcast();
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView();
        handleInternetChange();
        application.getShareData().isScanning.observe(getViewLifecycleOwner(), aBoolean -> {
            ((MainActivity) requireActivity()).first_time = false;
            Log.e("isScanning", aBoolean.toString());

            if (aBoolean) {

                prepareViewSpeedTest();
            } else {
                resetView();
            }
        });

        application.getShareData().isPermissionRequested.observe(getViewLifecycleOwner(), isPermissionRequested -> {
            if (!isPermissionRequested) setViewWithNoPermission();
            else setViewWithPermission();

        });
        loadServer();
    }

    private void handleInternetChange() {
        ((MainActivity) requireActivity()).viewModel.hasInternetConnection.observe(getViewLifecycleOwner(), status -> {
            if (!status) {
                if (application.getShareData().isScanning.getValue()) {
                    application.getShareData().isScanning.postValue(false);
                    Toast.makeText(requireContext(), "Problem when connect to internet, check your connection again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setViewWithNoPermission() {
        if (NetworkUtils.isWifiConnected(requireContext())) {
            binding.tvWifiName.setText("Wifi");
        } else if (NetworkUtils.isMobileConnected(requireContext())) {
            NetworkInfo info = NetworkUtils.getInforMobileConnected(getContext());
            String name = info.getTypeName() + " - " + info.getSubtypeName();
            binding.tvWifiName.setText(name);
        } else {
            binding.tvIspName.setText("No data");
            binding.tvWifiName.setText("No connection");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        isFragmentFreezing = false;
        if (isConnectivityChanged) {
            Log.d("TAG", "        if (isConnectivityChanged) {\n: " + isConnectivityChanged);
            if (application.getShareData().isPermissionRequested.getValue() != null && application.getShareData().isPermissionRequested.getValue()) {
                setViewWithPermission();
            } else {
                setViewWithNoPermission();
            }
        }
        isConnectivityChanged = false;
    }

    @Override
    public void onStop() {
        super.onStop();
        isFragmentFreezing = true;
    }

    public void showLoadingPanel() {
        binding.containerInforWifi.setVisibility(View.GONE);
        binding.containerLoading.setVisibility(VISIBLE);
    }

    public void hideLoadingPanel() {
        binding.containerInforWifi.setVisibility(VISIBLE);
        binding.containerLoading.setVisibility(View.GONE);
    }

    private boolean isLocationPermissionGranted() {
        return Objects.equals(Boolean.TRUE, application.getShareData().isPermissionRequested.getValue());
    }


    private void initBroadCast() {
        IntentFilter internetFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        internetBroad = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                isConnectivityChanged = true;
                if (isConnectivityChanged) {
                    isLoadedServer = false;
                    if (isLocationPermissionGranted()) {
                        setViewWithPermission();
                    } else {
                        setViewWithNoPermission();
                    }
                    isConnectivityChanged = false;
                }

            }
        };
    }

    private void registBroadcast() {
        requireActivity().registerReceiver(internetBroad, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    private void unRegisterBroadCast() {
        requireContext().unregisterReceiver(internetBroad);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegisterBroadCast();
    }

    boolean isExpanded = false;

    public void setupView() {
        customSpeedview();

        // speedview
        binding.speedView.setCenterCircleColor(getResources().getColor(R.color.gray_400));

        Shader shader = new LinearGradient(0, 0, 0, binding.tvGo.getLineHeight(),
                getResources().getColor(R.color.gradient_green_start), getResources().getColor(R.color.gradient_green_end), Shader.TileMode.REPEAT);
        binding.tvGo.getPaint().setShader(shader);
        // Underline wifi name
        String wifi_name = binding.tvWifiName.getText().toString();
        SpannableString underlineWifiName = new SpannableString(wifi_name);
        underlineWifiName.setSpan(new UnderlineSpan(), 0, wifi_name.length(), 0);
        binding.tvWifiName.setText(underlineWifiName);
        // custom tick

        //onClick start scan
        binding.btnStart.setOnClickListener(this);


        //animation expandview
        ValueAnimator anim = ValueAnimator.ofInt(binding.containerExpandView.getMeasuredWidth(), 600);
        anim.addUpdateListener(valueAnimator -> binding.containerExpandView.getLayoutParams());

        //expandview
        binding.containerExpandView.setOnClickListener(view -> {
            TransitionManager.beginDelayedTransition((ViewGroup) view, new AutoTransition());
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            if (!isExpanded) {
                layoutParams.width = ConstraintLayout.LayoutParams.MATCH_PARENT;
                view.setLayoutParams(layoutParams);
                isExpanded = true;
            } else {
                layoutParams.width = ConstraintLayout.LayoutParams.WRAP_CONTENT;
                view.setLayoutParams(layoutParams);
                isExpanded = false;
            }
        });
    }

    private void customSpeedview() {
        binding.speedView.setWithPointer(false);
        binding.speedView.setOnPrintTickLabel((tickPosition, tick) -> {

            if (tick == 20) {
                SpannableString s = new SpannableString(String.format(Locale.getDefault(), "%d", Math.round(tick)));
                s.setSpan(new ForegroundColorSpan(0xffff1117), 0, 1, 0); // change first char color to Red.
                return s;
            }
            // null means draw default tick.
            return null;

        });
    }

    //click button
    public void onClickStartButton() {
        if (!NetworkUtils.isNetworkConnected(requireContext())) {
            Toast.makeText(requireContext(), "No connectivity!", Toast.LENGTH_SHORT).show();
            return;
        }
        application.getShareData().isScanning.postValue(true);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start:
                onClickStartButton();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }
    }


    public void loadServer() {
        Log.d("TAG", "loadServer: vao day");
        new Thread() {
            @Override
            public void run() {
                try {
                    requireActivity().runOnUiThread(() -> showLoadingPanel());
                    speedTest = new Speedtest();
                    getIP = new GetIP();
                    getIP.run();
                    getIP.join();


                    HashMap<Integer, String> mapKey = getIP.getMapKey();
                    HashMap<Integer, List<String>> mapValue = getIP.getMapValue();
                    double selfLat = getIP.getSelfLat();
                    double selfLon = getIP.getSelfLon();
                    double tmp = 19349458;
                    int findServerIndex = 0;
                    for (int index : mapKey.keySet()) {
                        if (tempBlackList.contains(mapValue.get(index).get(5))) {
                            continue;
                        }

                        Location source = new Location("Source");
                        source.setLatitude(selfLat);
                        source.setLongitude(selfLon);

                        List<String> ls = mapValue.get(index);
                        Location dest = new Location("Dest");
                        dest.setLatitude(Double.parseDouble(ls.get(0)));
                        dest.setLongitude(Double.parseDouble(ls.get(1)));

                        double distance = source.distanceTo(dest);
                        if (tmp > distance) {
                            tmp = distance;
                            findServerIndex = index;
                        }
                    }
                    final List<String> info = mapValue.get(findServerIndex);

                    if (info == null) {
                        requireActivity().runOnUiThread(() -> {
                            binding.tvIspName.setText("No connection");
                            hideLoadingPanel();
                            isLoadedServer = false;
                        });
                        return;
                    } else {
                        testPoint = new TestPoint(info.get(3), "http://" + info.get(6), "speedtest/", "speedtest/upload", "");
                        requireActivity().runOnUiThread(() -> {
                            if (type.equals("wifi")) {
                                wifi.setWifi_external_ip(getIP.getSelfIspIp());
                                wifi.setWifi_ISP(getIP.getSelfIsp());
                            } else if (type.equals("mobile")) {
                                mobile.setMobile_external_ip(getIP.getSelfIspIp());
                                mobile.setMobile_isp(getIP.getSelfIsp());
                            }
                            binding.tvIspName.setText(getIP.getSelfIsp());
                            hideLoadingPanel();
                        });
                        isLoadedServer = true;
                    }
                } catch (Exception e) {
                    Log.d("TAG", "run: to Exception ");
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void prepareViewSpeedTest() {
        countDownTimer = new CountDownTimer(4000, 1000) {
            @Override
            public void onTick(long l) {
                if (l <= 1000) {
                    YoYo.with(Techniques.FadeIn).onStart(animator -> {
                        binding.speedView.setVisibility(VISIBLE);
                    }).playOn(binding.speedView);
                    YoYo.with(Techniques.FadeIn).onStart(animator -> {
                        binding.containerSpeed.setVisibility(VISIBLE);
                    }).playOn(binding.containerSpeed);
                    YoYo.with(Techniques.FadeIn).onStart(animator -> {
                        binding.tvSpeedValue.setVisibility(VISIBLE);
                    }).playOn(binding.tvSpeedValue);
                } else if (l <= 2000) {
                    if (type.equals("no connection") || !isLoadedServer) {
                        application.getShareData().isScanning.postValue(false);
                        Toast.makeText(requireContext(), "No connection", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        YoYo.with(Techniques.FadeOut).onEnd(animator -> {
                            binding.loading.setVisibility(View.GONE);
                        }).playOn(binding.loading);
                        YoYo.with(Techniques.FadeOut).onEnd(animator -> {
                            binding.tvConnecting.setVisibility(View.GONE);
                        }).playOn(binding.tvConnecting);
                        YoYo.with(Techniques.SlideInDown).onStart(animator -> {
                            binding.topView.setVisibility(VISIBLE);
                        }).playOn(binding.topView);
                    }

                } else if (l <= 3000) {
                    YoYo.with(Techniques.FadeIn).onStart(animator -> {
                        binding.loading.setVisibility(VISIBLE);
                    }).playOn(binding.loading);
                } else if (l <= 4000) {
                    binding.btnStart.setEnabled(false);
                    YoYo.with(Techniques.FadeOut).playOn(binding.btnStart);
                    YoYo.with(Techniques.SlideOutLeft).playOn(binding.tvGo);
                    YoYo.with(Techniques.SlideInRight).onStart(animator -> {
                        binding.tvConnecting.setVisibility(VISIBLE);
                    }).playOn(binding.tvConnecting);
                }
            }

            @Override
            public void onFinish() {
                runSpeedTest();
            }
        };
        countDownTimer.start();
    }

    public void runSpeedTest() {
        if (speedTest == null) {
            speedTest = new Speedtest();
            speedTest.addTestPoint(testPoint);
        } else {
            speedTest.addTestPoint(testPoint);
            getConnection();
            speedTest.start(new Speedtest.SpeedtestHandler() {

                @Override
                public void onDownloadUpdate(double dl, double progress) {
                    if (progress == 0) {
                        requireActivity().runOnUiThread(() -> downloadView());
                    }
                    requireActivity().runOnUiThread(() -> {
                        binding.tvSpeedValue.setText(format((float) dl));
                        binding.speedView.speedTo((float) dl);
                        if (progress >= 1) {
                            requireActivity().runOnUiThread(() -> {
                                binding.tvDownloadValue.clearAnimation();
                                binding.tvDownloadValue.setText(format(dl));
                                binding.tvSpeedValue.setText(0.0 + "");
                            });

                            binding.speedView.speedTo(0);
                            binding.speedView.stop();

                        }
                    });
                }

                @Override
                public void onUploadUpdate(double ul, double progress) {

                    requireActivity().runOnUiThread(() -> {
                        if (progress == 0) {
                            requireActivity().runOnUiThread(() -> uploadView());
                        }
                        binding.speedView.speedTo((float) ul);
                        binding.tvSpeedValue.setText(format((float) ul));
                        if (progress >= 1) {
                            binding.tvUploadValue.clearAnimation();
                            binding.tvUploadValue.setText(format(ul));
                            binding.speedView.speedTo(0);
                            binding.speedView.setWithTremble(false);
                            binding.tvSpeedValue.setText(0.0 + "");
                        }
                    });
                }

                @Override
                public void onPingJitterUpdate(double ping, double jitter, double progress) {
                    requireActivity().runOnUiThread(() -> {
                        binding.tvPingCount.setText(format(ping) + " ms");
                        binding.tvJitterCount.setText(format(jitter) + " ms");
                    });
                }


                @Override
                public void onEnd() {
                    Intent intent = new Intent(getActivity(), ResultActivity.class);

                    requireActivity().runOnUiThread(() -> {
                        if (type.equals("wifi")) {
                            ConnectivityTestModel connectivityTestModel = new ConnectivityTestModel(binding.tvWifiName.getText().toString(),
                                    new Date(),
                                    binding.tvDownloadValue.getText().toString(),
                                    binding.tvUploadValue.getText().toString(),
                                    binding.tvPingCount.getText().toString().replace(" ms", ""),
                                    binding.tvJitterCount.getText().toString().replace(" ms", ""),
                                    binding.tvLossCount.getText().toString().replace("%", ""),
                                    null,
                                    wifi, type);

                            ((MainActivity) requireActivity()).viewModel.insertResultTest(connectivityTestModel);
                            intent.putExtra("test_result", connectivityTestModel);
                            intent.putExtra("EXTRA_MESS_1", "from_scan_activity");

                        } else {
                            ConnectivityTestModel connectivityTestModel = new ConnectivityTestModel(binding.tvWifiName.getText().toString(),
                                    new Date(),
                                    binding.tvDownloadValue.getText().toString(),
                                    binding.tvUploadValue.getText().toString(),
                                    binding.tvPingCount.getText().toString().replace(" ms", ""),
                                    binding.tvJitterCount.getText().toString().replace(" ms", ""),
                                    binding.tvLossCount.getText().toString().replace("%", ""),
                                    mobile,
                                    null, type);
                            ((MainActivity) requireActivity()).viewModel.insertResultTest(connectivityTestModel);
                            intent.putExtra("test_result", connectivityTestModel);
                            intent.putExtra("EXTRA_MESS_1", "from_scan_activity");
                        }
                        application.getShareData().isScanning.postValue(false);
                        startActivityForResult(intent, 1);
                    });
                }

                @Override
                public void onCriticalFailure(String err) {
                    application.getShareData().isScanning.postValue(false);
                }
            });
        }
    }

    private void downloadView() {
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(100); //You can manage the blinking time with this parameter
        anim.setStartOffset(50);
        anim.setRepeatCount(Animation.INFINITE);
        binding.speedView.setIndicatorLightColor(R.color.gradient_green_start);
        binding.speedView.setSpeedometerColor(getResources().getColor(R.color.gradient_green_start));
        binding.tvDownloadValue.setTextColor(getResources().getColor(R.color.gradient_green_start));
        binding.iconSpeedValue.setImageDrawable(getResources().getDrawable(R.drawable.ic_download));
        binding.tvDownloadValue.startAnimation(anim);
    }

    private void uploadView() {
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(100); //You can manage the blinking time with this parameter
        anim.setStartOffset(50);
        anim.setRepeatCount(Animation.INFINITE);
        binding.iconSpeedValue.setImageDrawable(getResources().getDrawable(R.drawable.ic_upload));
        binding.speedView.setIndicatorLightColor(R.color.gradient_orange_start);
        binding.tvUploadValue.setTextColor(getResources().getColor(R.color.gradient_orange_start));
        binding.tvUploadValue.startAnimation(anim);
        binding.speedView.setSpeedometerColor(getResources().getColor(R.color.gradient_orange_start));

    }

    private String format(double d) {
        Locale l;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            l = getResources().getConfiguration().getLocales().get(0);
        } else {
            l = getResources().getConfiguration().locale;
        }
        if (d < 200) return String.format(l, "%.2f", d);
        return "" + Math.round(d);
    }

    private void getConnection() {
        if (NetworkUtils.isWifiConnected(requireContext().getApplicationContext())) {
            WifiInfo wifiInfo = NetworkUtils.getWifiInfo(requireContext().getApplicationContext());
            if (wifiInfo != null) {
                wifi.setWifi_name(wifiInfo.getSSID());
                wifi.setWifi_bssid(wifiInfo.getBSSID());
                wifi.setWifi_level(String.valueOf(wifiInfo.getRssi()));
                wifi.setWifi_frequency(String.valueOf(wifiInfo.getFrequency()));
                double exp = (27.55 - (20 * Math.log10(wifiInfo.getFrequency())) + Math.abs(wifiInfo.getRssi())) / 20.0;
                double distanceM = Math.pow(10.0, exp);
                wifi.setWifi_distance(distanceM + "");
                wifi.setWifi_isConnected(true);
                wifi.setWifi_internal_ip(NetworkUtils.wifiIpAddress(requireContext().getApplicationContext()));
                wifi.setWifi_channel(0 + "");
            }

        } else if (NetworkUtils.isMobileConnected(requireContext().getApplicationContext())) {
            NetworkInfo networkInfo = NetworkUtils.getInforMobileConnected(requireContext().getApplicationContext());
            if (networkInfo != null) {
                String type = networkInfo.getTypeName() + " - " + networkInfo.getSubtypeName();
                mobile.setMobile_name(type);
                mobile.setMobile_external_ip("0.0.0.0");
                mobile.setMobile_internal_ip("0.0.0.0");
                mobile.setMobile_isConnected(true);
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == 100) {
                onClickStartButton();
            }
        }
    }

    public void checkFirstWhenStart() {
        if (NetworkUtils.isWifiConnected(getContext())) {
            type = "wifi";
        } else if (NetworkUtils.isMobileConnected(getContext())) {
            type = "mobile";
        } else {
            type = "no-connection";
        }
    }

    public void setViewWithPermission() {
        if (NetworkUtils.isWifiConnected(getContext())) {
            binding.tvWifiName.setText(NetworkUtils.getNameWifi(getContext().getApplicationContext()));
            type = "wifi";
            loadServer();
        } else if (NetworkUtils.isMobileConnected(getContext())) {
            NetworkInfo info = NetworkUtils.getInforMobileConnected(getContext());
            String name = info.getTypeName() + " - " + info.getSubtypeName();
            binding.tvWifiName.setText(name);
            type = "mobile";
            loadServer();
        } else {
            binding.tvWifiName.setText("No connection");
            binding.tvIspName.setText("No data");
            type = "no-connection";
        }
    }

    public void resetView() {
        if (speedTest != null) {
            speedTest.abort();
        }
        binding.speedView.setSpeedometerColor(getResources().getColor(R.color.gray_400));
        binding.speedView.speedTo(0);
        binding.tvSpeedValue.setText("0");
        binding.tvDownloadValue.clearAnimation();
        binding.tvUploadValue.clearAnimation();
        binding.tvWifiName.setEnabled(true);
        binding.tvDownloadValue.setText("0");
        binding.tvUploadValue.setText("0");
        binding.tvPingCount.setText("0");
        binding.tvJitterCount.setText("0");
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        binding.tvDownloadValue.setTextColor(getResources().getColor(R.color.gray_400));
        binding.tvUploadValue.setTextColor(getResources().getColor(R.color.gray_400));
        YoYo.with(Techniques.FadeOut).onStart(animator -> {
            binding.topView.setVisibility(View.GONE);
        }).playOn(binding.topView);
        YoYo.with(Techniques.FadeIn).playOn(binding.btnStart);
        YoYo.with(Techniques.FadeIn).playOn(binding.tvGo);
        binding.tvGo.setVisibility(VISIBLE);
        binding.btnStart.setVisibility(VISIBLE);
        binding.loading.setVisibility(View.GONE);
        binding.tvConnecting.setVisibility(View.GONE);
        binding.speedView.setVisibility(View.GONE);
        binding.tvSpeedValue.setVisibility(View.GONE);
        binding.containerSpeed.setVisibility(View.GONE);
        binding.btnStart.setEnabled(true);
        binding.tvWifiName.setEnabled(true);
    }
}
