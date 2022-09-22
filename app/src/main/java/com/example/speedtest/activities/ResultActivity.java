package com.example.speedtest.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.lifecycle.ViewModelProvider;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;

import com.example.speedtest.R;
import com.example.speedtest.common.CustomDialog;
import com.example.speedtest.databinding.ActivityResultBinding;
import com.example.speedtest.interfaces.OnDialogClickListener;
import com.example.speedtest.model.ConnectivityTestModel;
import com.example.speedtest.utils.DateTimeUtils;
import com.example.speedtest.view_model.WifiTestViewModel;

public class ResultActivity extends AppCompatActivity implements OnDialogClickListener {
    ActivityResultBinding binding;
    WifiTestViewModel viewModel;
    ConnectivityTestModel connectivityTestModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(WifiTestViewModel.class);
        connectivityTestModel = (ConnectivityTestModel) getIntent().getSerializableExtra("test_result");
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        if (connectivityTestModel == null) {
            return;
        }
        String afterScan = getIntent().getStringExtra("EXTRA_MESS_1");
        int level = connectivityTestModel.getType().equals("wifi") ? Integer.parseInt(connectivityTestModel.getWifi().getWifi_level()) : 0;
        int progress = level >= -60 ? 100 : level < -60 && level >= -90 ? 50 : 0;
        ValueAnimator anim = ValueAnimator.ofInt(0, progress);
        anim.setDuration(600);
        anim.addUpdateListener(animation -> {
            int animProgress = (Integer) animation.getAnimatedValue();
            binding.pbSignal.setProgress(animProgress);
        });
        anim.start();
        String status = level >= -60 ? getString(R.string.signal_strong) :
                level < -60 && level >= -90 ? getString(R.string.signal_normal) : getString(R.string.signal_weak);
//        binding.pbSignal.setProgress(progress);
        binding.pbSignal.setEnabled(false);
        binding.tvSignalStrength.setTextColor(level >= -60 ? getResources().getColor(R.color.signal_good) :
                level < -60 && level >= -90 ? getResources().getColor(R.color.signal_normal) : getResources().getColor(R.color.signal_poor));
        setProgressBarColor(binding.pbSignal, level >= -60 ? getResources().getColor(R.color.signal_good) :
                level < -60 && level >= -90 ? getResources().getColor(R.color.signal_normal) : getResources().getColor(R.color.signal_poor));
        binding.tvTime.setText(DateTimeUtils.getDateConvertedToResult(connectivityTestModel.getDate()));
        binding.tvSignalStrength.setText(status);
        binding.tvDownloadValue.setText(connectivityTestModel.getDownloadSpeed());
        binding.tvUploadValue.setText(connectivityTestModel.getUpLoadSpeed());
        binding.tvPingCount.setText(connectivityTestModel.getPing() + " ms");
        binding.tvJitterCount.setText(connectivityTestModel.getJitter() + " ms");
        binding.tvLossCount.setText(connectivityTestModel.getLoss() + " %");
        binding.tvConnectNameValue.setText(connectivityTestModel.getName());
        String type = connectivityTestModel.getType();
        if (type.equals("wifi")) {
            binding.tvIspValue.setText(connectivityTestModel.getWifi().getWifi_ISP());
            binding.tvInternalIpValue.setText(connectivityTestModel.getWifi().getWifi_internal_ip());
            binding.tvExternalIpValue.setText(connectivityTestModel.getWifi().getWifi_external_ip());
        } else {
            binding.tvIspValue.setText(connectivityTestModel.getMobile().getMobile_isp());
            binding.tvInternalIpValue.setText(connectivityTestModel.getMobile().getMobile_internal_ip());
            binding.tvExternalIpValue.setText(connectivityTestModel.getMobile().getMobile_external_ip());
        }

        binding.btnClose.setOnClickListener(view -> {
            finish();
        });
        binding.btnDelete.setOnClickListener(view -> {
            if (connectivityTestModel == null) {
                return;
            }

            CustomDialog customDialog = new CustomDialog(this, this);
            customDialog.show();
            customDialog.setTitle("DELETE RESULT");
            customDialog.setContent("This result will be deleted from your history");
        });

        if (afterScan == null) {
            binding.btnScanAgain.setVisibility(View.GONE);
        } else {
            binding.btnScanAgain.setVisibility(View.VISIBLE);
            binding.btnScanAgain.setOnClickListener(view -> {
//                Intent intent = new Intent(ResultActivity.this,MainActivity.class);
//                intent.putExtra("start_scan","start_scan");
//                setResult(RESULT_OK,intent);
                setResult(100);
                finish();
            });
        }
    }

    public void setProgressBarColor(SeekBar progressBar, int newColor) {
        LayerDrawable ld = (LayerDrawable) progressBar.getProgressDrawable();
        ClipDrawable d1 = (ClipDrawable) ld.findDrawableByLayerId(android.R.id.progress);
        d1.setColorFilter(newColor, PorterDuff.Mode.SRC_IN);
        LayerDrawable thumb = (LayerDrawable) getResources().getDrawable(R.drawable.custom_thumb);
        Drawable bgThumb = thumb.findDrawableByLayerId(R.id.bg_thumb);
        bgThumb.setColorFilter(newColor, PorterDuff.Mode.SRC_IN);
        progressBar.setThumb(thumb);
//        LayerDrawable thumb = (LayerDrawable) progressBar.getThumb();
//        thumb.setColorFilter(newColor, PorterDuff.Mode.SRC_IN);

    }


    @Override
    public void onClickPositiveButton() {
        viewModel.deleteResultTest(connectivityTestModel);
        finish();
    }
}