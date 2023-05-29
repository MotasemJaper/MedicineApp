package com.medicine.app.Activites;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.github.marlonlom.utilities.timeago.TimeAgoMessages;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.medicine.app.Adapter.AdapterChatList;
import com.medicine.app.Model.Chat;
import com.medicine.app.Model.ModelChatList;
import com.medicine.app.Model.TopicPatient;
import com.medicine.app.databinding.ActivityMessageListBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MessageListActivity extends AppCompatActivity {

    private ActivityMessageListBinding binding;
    FirebaseUser fuser;
    RecyclerView recyclerChatList;
    List<TopicPatient> topicPatinent;
    List<ModelChatList> chatLists;
    AdapterChatList adapterList;
    ProgressBar progressBarChatList;
    FirebaseAuth mAuth;
    Intent intent;
    String hisUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMessageListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        fuser = mAuth.getCurrentUser();
        topicPatinent = new ArrayList<>();
        chatLists = new ArrayList<>();
        adapterList = new AdapterChatList(MessageListActivity.this,topicPatinent);
        LinearLayoutManager manager = new LinearLayoutManager(MessageListActivity.this);
        binding.recyclerChatList.setAdapter(adapterList);
        binding.recyclerChatList.setLayoutManager(manager);
        lastChat(mAuth.getUid());

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }


    private void lastChat(String id) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ChatList").child(id);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatLists.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                   // ModelChatList chatModel = new ModelChatList();
                    ModelChatList chatModel = snapshot.getValue(ModelChatList.class);
                  //  chatModel.setId(snapshot.child("id").getValue() + "");
                    Log.d("ID", "onDataChange: "+chatModel.getId());
                    Collections.reverse(chatLists);
                    chatLists.add(chatModel);

                }

                readChat();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readChat() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                topicPatinent.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    TopicPatient topic = new TopicPatient();
                    topic.setId(snapshot.child("uId").getValue() + "");
                    topic.setFirstName(snapshot.child("firstName").getValue() + "");
                    topic.setImageTopic(snapshot.child("imageUrl").getValue() + "");
                    for (ModelChatList chat : chatLists) {
                        if (topic.getId() != null && topic.getId().equals(chat.getId())) {
                            Collections.reverse(topicPatinent);
                            topicPatinent.add(topic);
                            break;
                        }
                    }
                    adapterList = new AdapterChatList(getApplicationContext(), topicPatinent);
                    binding.recyclerChatList.setAdapter(adapterList);
                    binding.progressBarChatList.setVisibility(View.GONE);
                    for (int i = 0; i < topicPatinent.size(); i++) {
                        hisUid = topicPatinent.get(i).getId();
                        LastMessage(topicPatinent.get(i).getId());
                    }
                    adapterList.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void LastMessage(final String uid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chat");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String lastMessage = "default";
                String time2 = "default";
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    chat.setTimeStamp(snapshot.child("timestamp").getValue() + "");
                    if (chat == null) {
                        continue;
                    }
                    String sender = chat.getSender();
                    String receiver = chat.getReceiver();
                    if (sender == null || receiver == null) {
                        continue;

                    }
                    if (chat.getReceiver().equals(fuser.getUid()) && chat.getSender().equals(uid) ||
                            chat.getReceiver().equals(uid) && chat.getSender().equals(fuser.getUid())) {
                        lastMessage = chat.getMessage();
                        time2 = chat.getTimeStamp();
                        Log.d("RTRTRTRTRT", "onDataChange: " + chat.getMessage());

                    }
                }

                Long time = Long.parseLong(time2);
                Locale LocaleBylanguageTag = Locale.forLanguageTag("ps");
                TimeAgoMessages messages = new TimeAgoMessages.Builder().withLocale(LocaleBylanguageTag).build();
                String text1 = TimeAgo.using(time, messages);
                adapterList.LastMessage(uid, lastMessage);
                adapterList.LastTimeStampMessagee(uid, text1);
                adapterList.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}