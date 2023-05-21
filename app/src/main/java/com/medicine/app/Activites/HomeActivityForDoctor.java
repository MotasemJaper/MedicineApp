package com.medicine.app.Activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.medicine.app.R;
import com.medicine.app.databinding.ActivityHomeForDoctorBinding;

public class HomeActivityForDoctor extends AppCompatActivity {

    private ActivityHomeForDoctorBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeForDoctorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.btnAddTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivityForDoctor.this,AddTopicActivity.class));
            }
        });
    }
}