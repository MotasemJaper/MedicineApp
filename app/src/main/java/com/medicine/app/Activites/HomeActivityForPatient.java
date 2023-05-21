package com.medicine.app.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

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
import com.medicine.app.databinding.ActivityHomeForPatientBinding;

import java.util.ArrayList;
import java.util.List;

import kotlin.collections.ArrayDeque;

public class HomeActivityForPatient extends AppCompatActivity {

    private ActivityHomeForPatientBinding binding;
    private AdapterViewPagerForBunner pagerForBunner;
    List<Integer> imageList;
    AdapterForHomePatientShowTopicOne adapterOne;
    AdapterForHomePatientShowTopicTow adapterTow;
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

        adapterOne = new AdapterForHomePatientShowTopicOne(HomeActivityForPatient.this,listTopic);
        adapterTow = new AdapterForHomePatientShowTopicTow(HomeActivityForPatient.this,listTopic);
        LinearLayoutManager managerOne = new LinearLayoutManager(HomeActivityForPatient.this);
        LinearLayoutManager managerTow = new LinearLayoutManager(HomeActivityForPatient.this);
        managerOne.setOrientation(RecyclerView.HORIZONTAL);
        managerTow.setOrientation(RecyclerView.HORIZONTAL);
        binding.showTopicRecycle.setAdapter(adapterOne);
        binding.showTopicRecycle2.setAdapter(adapterTow);
        binding.showTopicRecycle.setLayoutManager(managerOne);
        binding.showTopicRecycle2.setLayoutManager(managerTow);
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
                    adapterOne.notifyDataSetChanged();
                    adapterTow.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}