package com.medicine.app.Activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.medicine.app.databinding.ActivitySkipBinding;

public class SkipActivity extends AppCompatActivity {

    private ActivitySkipBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySkipBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.pageIndicatorView.setCount(3);
        binding.pageIndicatorView.setSelection(1);

        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SkipActivity.this, GetStartedActivity.class));
                finish();
            }
        });

    }
}