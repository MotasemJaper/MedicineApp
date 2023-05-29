package com.medicine.app.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.medicine.app.Adapter.AdapterShowComments;
import com.medicine.app.Model.Comments;
import com.medicine.app.R;
import com.medicine.app.databinding.ActivityDetailsTopicBinding;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailsTopicActivity extends AppCompatActivity {


    private ActivityDetailsTopicBinding binding;
    private AdapterShowComments adapter;
    private List<Comments> comments;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private String firstName, middleName, lastName, email, address, imageUrl, idUserForTopic;
    private Intent intent;
    private String idTopic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsTopicBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        intent = getIntent();
        idTopic = intent.getStringExtra("idTopic");
        viewTopic(idTopic);
        getUserData();
        comments = new ArrayList<>();
        Collections.reverse(comments);
        adapter = new AdapterShowComments(DetailsTopicActivity.this, comments);
        progressDialog = new ProgressDialog(DetailsTopicActivity.this);
        progressDialog.setCancelable(false);

        LinearLayoutManager manager = new LinearLayoutManager(DetailsTopicActivity.this);
        manager.setStackFromEnd(true);
        binding.recyclerShowComments.setAdapter(adapter);
        binding.recyclerShowComments.setLayoutManager(manager);
        adapter.notifyDataSetChanged();
        getComments();
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailsTopicActivity.this, ProfileViewActivity.class);
                i.putExtra("idUser", idUserForTopic);
                i.putExtra("idTopic", idTopic);
                startActivity(i);
            }
        });

        binding.namePerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailsTopicActivity.this, ProfileViewActivity.class);
                i.putExtra("idUser", idUserForTopic);
                i.putExtra("idTopic", idTopic);
                startActivity(i);
            }
        });
        binding.btnSendComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validAll()) {

                } else {
                    addComments();
                }
            }
        });
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();

    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.hide();
    }

    private void viewTopic(String id) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Topic");
        reference.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Picasso.get().load(snapshot.child("imageUrl").getValue() + "").placeholder(R.drawable.spinner).into(binding.viewImageDetails);
                binding.titleTopic.setText(snapshot.child("nameTopic").getValue() + "");
                binding.descriptionDetails.setText(snapshot.child("descriptionTopic").getValue() + "");
                binding.videoUrl.setText(snapshot.child("videoUrl").getValue() + "");
                binding.namePerson.setText(snapshot.child("firstName").getValue() + " " +
                        snapshot.child("middleName").getValue() + " " +
                        snapshot.child("lastName").getValue() + "");
                binding.videoUrl.setText(snapshot.child("videoUrl").getValue() + "");
                Picasso.get().load(snapshot.child("imageUser").getValue() + "").placeholder(R.drawable.spinner).into(binding.imageProfile);
                idUserForTopic = snapshot.child("Uid").getValue() + "";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getComments() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Topic");
        reference.child(idTopic).child("Comments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                comments.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Comments comments1 = new Comments();
                    comments1.setUidUser(ds.child("Uid").getValue() + "");
                    comments1.setIdTopic(ds.child("idComments").getValue() + "");
                    comments1.setComments(ds.child("comments").getValue() + "");
                    comments1.setFirstName(ds.child("firstName").getValue() + "");
                    comments1.setMiddleName(ds.child("middleName").getValue() + "");
                    comments1.setLastName(ds.child("lastName").getValue() + "");
                    comments1.setTimeStamp(ds.child("timeStamp").getValue() + "");
                    comments1.setImageView(ds.child("imageUrl").getValue() + "");
                    comments1.setUidUser(ds.child("Uid").getValue() + "");

                    Log.d("getComments", "onDataChange: " + ds.child("comments").getValue());
                    binding.recyclerShowComments.setVisibility(View.VISIBLE);
                    comments.addAll(Collections.singleton(comments1));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void addComments() {
        progressDialog.setMessage("Waiting For Add Comment...");
        showDialog();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateComments = dateFormat.format(new Date());
        String idComments = String.valueOf(System.currentTimeMillis());

        Map<String, Object> comment = new HashMap<>();
        comment.put("comments", binding.addComments.getText().toString().trim());
        comment.put("firstName", firstName);
        comment.put("middleName", middleName);
        comment.put("lastName", lastName);
        comment.put("timeStamp", idComments);
        comment.put("email", email);
        comment.put("address", address);
        comment.put("idComments", idComments);
        comment.put("imageUrl", imageUrl);
        comment.put("Uid", mAuth.getCurrentUser().getUid());


        Map<String,Object> noti = new HashMap<>();
        String idNoti = String.valueOf(System.currentTimeMillis());
        noti.put("firstName",firstName);
        noti.put("middleName",middleName);
        noti.put("lastName",lastName);
        noti.put("idTopic", idTopic);
        noti.put("Notifications",firstName+" Add new Comments");
        noti.put("idNoti",idNoti);
        noti.put("imageUrl",imageUrl);
        noti.put("dateNotifications", idNoti);
        DatabaseReference not = FirebaseDatabase.getInstance().getReference("Notifications");
        not.child(idNoti).setValue(noti);

        HomeActivityForDoctor.binding.notificationsID.setVisibility(View.VISIBLE);
      //  HomeActivityForPatient.binding.notificationsIDD.setVisibility(View.VISIBLE);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Topic").child(idTopic);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reference.child("Comments").child(idComments).setValue(comment);
                binding.addComments.getText().clear();
                hideDialog();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DetailsTopicActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                hideDialog();
            }
        });


    }

    boolean validAll() {
        return isEmty(binding.addComments, "Required");
    }

    boolean isEmty(TextView editText, String msg) {
        boolean isDone = true;
        if (editText != null) {
            if (editText.getText().toString().isEmpty()) {
                editText.setError(msg);
                isDone = false;
            }
        }
        return isDone;
    }

    private void getUserData() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        reference.child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                firstName = snapshot.child("firstName").getValue() + "";
                middleName = snapshot.child("middleName").getValue() + "";
                lastName = snapshot.child("lastName").getValue() + "";
                email = snapshot.child("email").getValue() + "";
                address = snapshot.child("address").getValue() + "";
                imageUrl = snapshot.child("imageUrl").getValue() + "";
                String type = snapshot.child("type").getValue() + "";
                if (type != null) {
                    if (type.equals("Doctor")) {
                        binding.moreMenu.setVisibility(View.VISIBLE);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void onMenuButtonClick(View view) {
        showPopupMenu(view);
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.pop_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {

                case R.id.delete:

                    deleteTopic();

                    return true;

                case R.id.edit:
                    Intent i = new Intent(DetailsTopicActivity.this, EditTopicActivity.class);
                    i.putExtra("idUser",idTopic);
                    startActivity(i);
                    return true;
            }
            return false;
        });

        popupMenu.show();
    }

    private void deleteTopic() {
        progressDialog.setMessage("Waiting For delete Topic...");
        showDialog();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Topic");
        reference.child(idTopic).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(DetailsTopicActivity.this, "Delete Topic Successfully", Toast.LENGTH_SHORT).show();
                    hideDialog();
                    Intent i = new Intent(DetailsTopicActivity.this, HomeActivityForDoctor.class);
                    startActivity(i);
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DetailsTopicActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                hideDialog();
            }
        });
    }

}