package com.medicine.app.Activites;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.github.marlonlom.utilities.timeago.TimeAgoMessages;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.medicine.app.Adapter.MessageAdapter;
import com.medicine.app.Model.Chat;
import com.medicine.app.R;
import com.medicine.app.databinding.ActivityMessagesBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MessagesActivity extends AppCompatActivity {

    private ActivityMessagesBinding binding;
    MessageAdapter adapterMessage;
    List<Chat> chats;
    String imageUrl, getExtraString,uId;
    Intent intent;
    boolean notify = false;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMessagesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        intent = getIntent();
        getExtraString = intent.getStringExtra("idUser");
        getImageAndNameForUser(getExtraString);
        ReadMessage(mAuth.getUid(),getExtraString,imageUrl);

        chats = new ArrayList<>();
//        Collections.reverse(chats);
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify = true;
                String msg = binding.txtSend.getText().toString();
                if (!msg.equals("")){
                    SendMessage(uId,getExtraString,msg);
                }else {
                    Toast.makeText(MessagesActivity.this, "You cant send empty message!", Toast.LENGTH_SHORT).show();
                }
                binding.txtSend.getText().clear();
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            uId = currentUser.getUid();
        }
    }

    public void ReadMessage(String myId, String userId, String imageUri){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chat");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chats.clear();
                if (dataSnapshot != null){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Chat chat = snapshot.getValue(Chat.class);
                        Log.d("Messgae", "onDataChange: "+chat.getMessage());
                        Long time = Long.parseLong(snapshot.child("timestamp").getValue()+"");
                        Locale LocaleBylanguageTag = Locale.forLanguageTag("ps");
                        TimeAgoMessages messages = new TimeAgoMessages.Builder().withLocale(LocaleBylanguageTag).build();
                        String text1 = TimeAgo.using(time, messages);
                        chat.setTimeStamp(text1);
                        if (chat.getReceiver().equals(myId) &&
                                chat.getSender().equals(userId) ||

                                chat.getReceiver().equals(userId) &&
                                        chat.getSender().equals(myId)){

                            chats.add(chat);
                        }
                        adapterMessage = new MessageAdapter(MessagesActivity.this,chats,imageUri);
                        LinearLayoutManager manager = new LinearLayoutManager(MessagesActivity.this);
                        manager.setStackFromEnd(true);
                        binding.recyclerChatingMsg.setLayoutManager(manager);
                        binding.recyclerChatingMsg.setAdapter(adapterMessage);
                        binding.prgressparChating.setVisibility(View.GONE);
                        adapterMessage.notifyDataSetChanged();

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void SendMessage(String sender, String receiver, String message){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Map<String,Object> chatUser = new HashMap<>();
        String timeStamp = String.valueOf(System.currentTimeMillis());
        chatUser.put("sender",sender);
        chatUser.put("receiver",receiver);
        chatUser.put("message",message);
        chatUser.put("timestamp",timeStamp);
        reference.child("Chat").push().setValue(chatUser);

        DatabaseReference chatList1 = FirebaseDatabase.getInstance().getReference("ChatList")
                .child(sender)
                .child(receiver);
        chatList1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    chatList1.child("id").setValue(receiver);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference chatList2 = FirebaseDatabase.getInstance().getReference("ChatList")
                .child(receiver)
                .child(sender);
        chatList2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    chatList2.child("id").setValue(sender);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getImageAndNameForUser(String id){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User").child(id);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                binding.firstNameChating.setText(
                        snapshot.child("firstName").getValue()+" "+
                        snapshot.child("middleName").getValue()+" "+
                        snapshot.child("lastName").getValue()+" "

                );
                imageUrl = snapshot.child("imageUrl").getValue()+"";
                Log.d("ImageUrl", "onDataChange: "+imageUrl);
                Picasso.get().load(snapshot.child("imageUrl").getValue()+"").placeholder(R.drawable.spinner).into(binding.imagetoolbarChating);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}