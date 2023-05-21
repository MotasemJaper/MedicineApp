package com.medicine.app.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.medicine.app.Adapter.AdapterForHomePatientShowTopicOne;
import com.medicine.app.Adapter.AdapterForHomePatientShowTopicTow;
import com.medicine.app.Adapter.AdapterViewPagerForBunner;
import com.medicine.app.Model.TopicPatient;
import com.medicine.app.R;
import com.medicine.app.databinding.ActivityHomeForDoctorBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeActivityForDoctor extends AppCompatActivity {

    private ActivityHomeForDoctorBinding binding;
    private AdapterViewPagerForBunner pagerForBunner;
    List<Integer> imageList;

    AdapterForHomePatientShowTopicOne adapterOne;
    AdapterForHomePatientShowTopicTow adapterTow;
    List<TopicPatient> listTopic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeForDoctorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        listTopic = new ArrayList<>();
        Collections.reverse(listTopic);
        imageList = new ArrayList<>();
        imageList.add(R.drawable.logo_splash);
        imageList.add(R.drawable.get_started);
        imageList.add(R.drawable.skip_one);
        pagerForBunner = new AdapterViewPagerForBunner((ArrayList<Integer>) imageList,this);
        binding.viewPagerBannerDoctor.setAdapter(pagerForBunner);
        binding.pageIndicatorView.setViewPager(binding.viewPagerBannerDoctor);
        binding.pageIndicatorView.setCount(3);
        binding.pageIndicatorView.setSelection(binding.viewPagerBannerDoctor.getCurrentItem());
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

        getTopicData();
        adapterOne = new AdapterForHomePatientShowTopicOne(HomeActivityForDoctor.this,listTopic);
        adapterTow = new AdapterForHomePatientShowTopicTow(HomeActivityForDoctor.this,listTopic);
        LinearLayoutManager managerOne = new LinearLayoutManager(HomeActivityForDoctor.this);
        LinearLayoutManager managerTow = new LinearLayoutManager(HomeActivityForDoctor.this);
        managerOne.setOrientation(RecyclerView.HORIZONTAL);
        managerTow.setOrientation(RecyclerView.HORIZONTAL);
        binding.showTopicRecycleDoctor.setAdapter(adapterOne);
        binding.showTopicRecycle2Doctor.setAdapter(adapterTow);
        binding.showTopicRecycleDoctor.setLayoutManager(managerOne);
        binding.showTopicRecycle2Doctor.setLayoutManager(managerTow);

    }

    private void getTopicData(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Topic");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    TopicPatient topicPatient = new TopicPatient();
                    topicPatient.setId(snapshot.child("idTopic")+"");
                    topicPatient.setFirstName(snapshot.child("firstName")+"");
                    topicPatient.setMiddleName(snapshot.child("middleName")+"");
                    topicPatient.setAddress(snapshot.child("address")+"");
                    topicPatient.setEmail(snapshot.child("email")+"");
                    topicPatient.setTitleTopic(snapshot.child("nameTopic")+"");
                    topicPatient.setImageTopic(snapshot.child("imageUrl")+"");
                    topicPatient.setDetails(snapshot.child("descriptionTopic")+"");
                    topicPatient.setVideoTopic(snapshot.child("videoUrl")+"");
                    listTopic.add(topicPatient);
                    adapterOne.notifyDataSetChanged();
                    adapterTow.notifyDataSetChanged();
                    Log.d("idTopic", "onDataChange: "+snapshot.child("idTopic"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}