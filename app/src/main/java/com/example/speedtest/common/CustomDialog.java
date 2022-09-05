package com.example.speedtest.common;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;

import com.example.speedtest.R;
import com.example.speedtest.databinding.CustomDialogBoxBinding;
import com.example.speedtest.interfaces.OnDialogClickListener;


public class CustomDialog extends Dialog implements View.OnClickListener {
    CustomDialogBoxBinding binding;
    OnDialogClickListener listener;
    public Activity activity;
    public CustomDialog(@NonNull Context context, OnDialogClickListener listener) {
        super(context);
        this.activity = (Activity) context;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = CustomDialogBoxBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnYes.setOnClickListener(this);
        binding.btnNo.setOnClickListener(this);
//        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        int width = (int)(activity.getResources().getDisplayMetrics().widthPixels*0.90);
        getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }
    public void setTitle(String title){
        binding.tvTitle.setText(title);
    }
    public void setContent(String content){
        binding.tvContent.setText(content);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_yes:
                dismiss();
                listener.onClickPositiveButton();
                break;
            case  R.id.btn_no:
                dismiss();
        }
    }
}
