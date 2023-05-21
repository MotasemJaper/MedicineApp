package com.medicine.app.Activites;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.medicine.app.databinding.ActivitySplashBinding;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    private ActivitySplashBinding binding;
    public static final int MILLSCAND = 1000;
    SharedPreferences settings ;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (!isNetworkConnectoin()) {
                    Toast.makeText(SplashActivity.this, "Please check your internet connection", Toast.LENGTH_LONG).show();

                }else {
                    if (getSharedPreferences("MyPrefs",MODE_PRIVATE) != null) {
                        settings = getSharedPreferences("MyPrefs", MODE_PRIVATE);

                        if (settings.getString("typeUser", "").equals("Doctor")){
                            Intent i = new Intent(getApplicationContext(), HomeActivityForDoctor.class);
                            startActivity(i);
                            finish();

                        } else if (settings.getString("typeUser", "").equals("Patient")) {
                            Intent i = new Intent(getApplicationContext(), HomeActivityForPatient.class);
                            startActivity(i);
                            finish();

                        }else{
                            Intent i = new Intent(getApplicationContext(), SkipActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }

                }

            }
        },MILLSCAND);

    }

    private boolean isNetworkConnectoin() {
        ConnectivityManager cm;
        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;

    }
}