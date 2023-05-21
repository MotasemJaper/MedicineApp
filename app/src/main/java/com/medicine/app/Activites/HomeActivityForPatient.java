package com.medicine.app.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.medicine.app.Adapter.AdapterForHomeShowTopic;
import com.medicine.app.Adapter.AdapterViewPagerForBunner;
import com.medicine.app.Model.TopicPatient;
import com.medicine.app.R;
import com.medicine.app.databinding.ActivityHomeForPatientBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeActivityForPatient extends AppCompatActivity {

    private ActivityHomeForPatientBinding binding;
    private AdapterViewPagerForBunner pagerForBunner;
    List<Integer> imageList;
    AdapterForHomeShowTopic adapter;
    List<TopicPatient> listTopic;

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

        adapter = new AdapterForHomeShowTopic(HomeActivityForPatient.this,listTopic);
        binding.showTopicRecycle.setAdapter(adapter);
        GridLayoutManager manager = new GridLayoutManager(HomeActivityForPatient.this,2);
        binding.showTopicRecycle.setLayoutManager(manager);
        getTopicData();


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
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}