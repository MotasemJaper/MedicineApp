package com.medicine.app.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.medicine.app.Adapter.AdapterForHomeShowTopic;
import com.medicine.app.Model.TopicPatient;
import com.medicine.app.R;
import com.medicine.app.databinding.ActivityViewAllTopicForDoctorBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ViewAllTopicForDoctorActivity extends AppCompatActivity {

    private ActivityViewAllTopicForDoctorBinding binding;
    AdapterForHomeShowTopic adapter;
    List<TopicPatient> listTopic;
    Intent intent;
    String reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewAllTopicForDoctorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        intent = getIntent();
        reference = intent.getStringExtra("Reference");
        listTopic = new ArrayList<>();
        Collections.reverse(listTopic);

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        adapter = new AdapterForHomeShowTopic(ViewAllTopicForDoctorActivity.this,listTopic);
        binding.recyclerViewAllTopic.setAdapter(adapter);
        GridLayoutManager manager = new GridLayoutManager(ViewAllTopicForDoctorActivity.this,2);
        binding.recyclerViewAllTopic.setLayoutManager(manager);
        getTopicData();

        binding.searchHomePatient.addTextChangedListener(new TextWatcher() {
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

    private void getTopicData(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Topic");
        reference.addValueEventListener(new ValueEventListener() {
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
                    Log.d("idTopic", "onDataChange: "+snapshot.child("idTopic").getValue());
                    binding.prgresspar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}