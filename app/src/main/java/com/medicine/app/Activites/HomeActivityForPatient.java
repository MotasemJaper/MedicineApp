package com.medicine.app.Activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.medicine.app.Adapter.AdapterViewPagerForBunner;
import com.medicine.app.R;
import com.medicine.app.databinding.ActivityHomeForPatientBinding;

import java.util.ArrayList;
import java.util.List;

import kotlin.collections.ArrayDeque;

public class HomeActivityForPatient extends AppCompatActivity {

    private ActivityHomeForPatientBinding binding;
    private AdapterViewPagerForBunner pagerForBunner;
    List<Integer> imageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeForPatientBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        imageList = new ArrayList<>();
        imageList.add(R.drawable.logo_splash);
        imageList.add(R.drawable.get_started);
        imageList.add(R.drawable.skip_one);
        pagerForBunner = new AdapterViewPagerForBunner((ArrayList<Integer>) imageList,this);
        binding.viewPagerBanner.setAdapter(pagerForBunner);
        binding.pageIndicatorView.setViewPager(binding.viewPagerBanner);
        binding.pageIndicatorView.setCount(3);
        binding.pageIndicatorView.setSelection(binding.viewPagerBanner.getCurrentItem());
        binding.viewPagerBanner.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                binding.pageIndicatorView.setSelection(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });








    }
}