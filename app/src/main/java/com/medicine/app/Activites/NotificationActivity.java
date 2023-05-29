package com.medicine.app.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.medicine.app.Adapter.AdapterNotifications;
import com.medicine.app.Model.Notifi;
import com.medicine.app.R;
import com.medicine.app.databinding.ActivityNotificationBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    private ActivityNotificationBinding binding;
    FirebaseAuth mAuth;
    List<Notifi> list;
    AdapterNotifications adapterNotifications;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        list = new ArrayList<>();
        Collections.reverse(list);
        getNotifications();
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void getNotifications(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    Notifi notifi = new Notifi();
                    notifi.setFirstName(ds.child("firstName").getValue()+"");
                    notifi.setIdTopic(ds.child("idTopic").getValue()+"");
                    notifi.setIdNoti(ds.child("idNoti").getValue()+"");
                    notifi.setImageUrl(ds.child("imageUrl").getValue()+"");
                    notifi.setNot(ds.child("Notifications").getValue()+"");
                    list.add(notifi);
                    adapterNotifications = new AdapterNotifications(NotificationActivity.this,list);
                    LinearLayoutManager manager = new LinearLayoutManager(NotificationActivity.this);
                    binding.recyclerNotifications.setAdapter(adapterNotifications);
                    binding.recyclerNotifications.setLayoutManager(manager);
                    adapterNotifications.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}