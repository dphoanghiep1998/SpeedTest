package com.example.speedtest.fragments;

import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.speedtest.R;
import com.example.speedtest.adapter.ViewPagerAdapter;
import com.example.speedtest.adapter.ViewPagerLocationAdapter;
import com.example.speedtest.databinding.FragmentChangeLocationBinding;
import com.google.android.material.tabs.TabLayout;

public class ChangeLocationFragment extends Fragment {
    FragmentChangeLocationBinding binding;


    public ChangeLocationFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentChangeLocationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initView();

        super.onViewCreated(view, savedInstanceState);
    }



    public void initView() {
        TextView tv=(TextView)LayoutInflater.from(requireContext()).inflate(R.layout.custom_tabbar_item,null);
        Shader shader = new LinearGradient(0, 0, 0, tv.getLineHeight(),
                getResources().getColor(R.color.gradient_text_premium_start), getResources().getColor(R.color.gradient_text_premium_end), Shader.TileMode.REPEAT);
        tv.getPaint().setShader(shader);
        tv.setText("Premium");
        binding.tabLayout.getTabAt(1).setCustomView(tv);
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d("TAG", "onTabSelected: " + tab.getPosition());
                binding.viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position));
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        });
    }
}
