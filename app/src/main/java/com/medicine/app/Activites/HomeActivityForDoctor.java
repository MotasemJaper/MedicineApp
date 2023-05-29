package com.medicine.app.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.PopupMenu;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.medicine.app.Adapter.AdapterForHomeShowTopic;
import com.medicine.app.Adapter.AdapterViewPagerForBunner;
import com.medicine.app.Model.TopicPatient;
import com.medicine.app.R;
import com.medicine.app.databinding.ActivityHomeForDoctorBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeActivityForDoctor extends AppCompatActivity {

    public static ActivityHomeForDoctorBinding binding;
    private AdapterViewPagerForBunner pagerForBunner;
    AdapterForHomeShowTopic adapter;
    List<TopicPatient> listTopic;
    List<Integer> imageList;
    FirebaseAuth mAuth;
    FirebaseUser user;
    SharedPreferences settings;
    String uId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeForDoctorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        listTopic = new ArrayList<>();
        Collections.reverse(listTopic);

//        getNot();
        imageList = new ArrayList<>();
        imageList.add(R.drawable.nnnn);
        imageList.add(R.drawable.bbb);
        imageList.add(R.drawable.medium);
        imageList.add(R.drawable.t);
        binding.imageNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(HomeActivityForDoctor.this,NotificationActivity.class));
                binding.notificationsID.setVisibility(View.GONE);
            }
        });
        pagerForBunner = new AdapterViewPagerForBunner((ArrayList<Integer>) imageList,this);
        binding.viewPagerBannerDoctor.setAdapter(pagerForBunner);
        binding.pageIndicatorView.setViewPager(binding.viewPagerBannerDoctor);
        binding.pageIndicatorView.setCount(4);
        binding.pageIndicatorView.setSelection(binding.viewPagerBannerDoctor.getCurrentItem());
      binding.viewAllDoctor.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent i = new Intent(HomeActivityForDoctor.this,ViewAllTopicForDoctorActivity.class);
              i.putExtra("Reference","UserDoctor");
              startActivity(i);
          }
      });
        binding.viewPagerBannerDoctor.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
        binding.btnAddTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivityForDoctor.this,AddTopicActivity.class));
            }
        });

        adapter = new AdapterForHomeShowTopic(HomeActivityForDoctor.this,listTopic);
        GridLayoutManager manager = new GridLayoutManager(HomeActivityForDoctor.this,2);
        binding.showTopicRecycleDoctor.setAdapter(adapter);
        binding.showTopicRecycleDoctor.setLayoutManager(manager);
        getTopicData();

        binding.searchHomeDoctor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }

            public void filter(String text){
                ArrayList<TopicPatient> listDB = new ArrayList<>();
                for (TopicPatient dbb : listTopic){
                    if (dbb.getTitleTopic().toLowerCase().contains(text.toLowerCase())){
                        listDB.add(dbb);
                    }
                }
                adapter.filterList(listDB);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        user = mAuth.getCurrentUser();
        if (user != null) {
            uId = user.getUid();
        } else {
            Intent userOut = new Intent(HomeActivityForDoctor.this,SignInActivity.class);
            startActivity(userOut);
            finish();
        }
    }

    public void onMenuButtonClick(View view) {
        showPopupMenu(view);
    }
    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.profile:
                    Intent profile = new Intent(getApplicationContext(), profileEditActivity.class);
                    profile.putExtra("idUser", mAuth.getCurrentUser().getUid());
                    startActivity(profile); // Handle menu item 1 click
                    return true;
                case R.id.message:
                    Intent msg = new Intent(getApplicationContext(), MessageListActivity.class);
                    msg.putExtra("idUser", mAuth.getCurrentUser().getUid());
                    startActivity(msg);
                    return true;
                // Handle other menu items as needed
                case R.id.signOut:
                    if (getSharedPreferences("MyPrefs", MODE_PRIVATE) != null) {
                        settings = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                        settings.getString("typeUser","").equals("typeUser");
                        settings.edit().clear().apply();
                        Intent signOut = new Intent(HomeActivityForDoctor.this, SignInActivity.class);
                        startActivity(signOut);
                        finish();

                        return true;
                    }
            }
            return false;
        });

        // Show the popup menu
        popupMenu.show();
    }

    private void getTopicData(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Topic");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    TopicPatient topicPatient = new TopicPatient();
                    topicPatient.setId(snapshot.child("idTopic").getValue()+"");
                    topicPatient.setFirstName(snapshot.child("firstName").getValue()+"");
                    topicPatient.setMiddleName(snapshot.child("middleName").getValue()+"");
                    topicPatient.setAddress(snapshot.child("address").getValue()+"");
                    topicPatient.setEmail(snapshot.child("email").getValue()+"");
                    topicPatient.setTitleTopic(snapshot.child("nameTopic").getValue()+"");
                    topicPatient.setImageTopic(snapshot.child("imageUrl").getValue()+"");
                    topicPatient.setDetails(snapshot.child("descriptionTopic").getValue()+"");
                    topicPatient.setVideoTopic(snapshot.child("videoUrl").getValue()+"");
                    listTopic.add(topicPatient);
                    adapter.notifyDataSetChanged();
                    // adapterTow.notifyDataSetChanged();
                    Log.d("idTopic", "onDataChange: "+snapshot.child("idTopic").getValue());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void onBackPressed() {
        // Ask the user if they want to save their changes.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure to Exits?");
        builder.setMessage("You have made changes to the data. Do you want to save them before exiting?");
        builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                builder.getContext();
            }
        });
        builder.show();

    }

    private void getNot(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                binding.notificationsID.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}