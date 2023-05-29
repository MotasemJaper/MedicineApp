package com.medicine.app.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.storage.FirebaseStorage;
import com.medicine.app.R;
import com.medicine.app.databinding.ActivityEditTopicBinding;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class EditTopicActivity extends AppCompatActivity {

    private ActivityEditTopicBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    String uId, getExtraString;
    Intent intent;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditTopicBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait for Update Topic...");
        progressDialog.setCancelable(false);
        intent = getIntent();
        getExtraString = intent.getStringExtra("idUser");
        getTopicData(getExtraString);

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.btnUpdateTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validAll()){

                }else {
                    updateTopic(getExtraString);
                }
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
            Intent userOut = new Intent(EditTopicActivity.this, SignInActivity.class);
            startActivity(userOut);
            finish();
        }
    }
    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.hide();
    }

    boolean validAll() {
        return isEmty(binding.editNameTopic, "required") &
                isEmty(binding.descriptionTopicEdit, "required") &
                isEmty(binding.urlVideoEdit, "required");
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

    private void getTopicData(String id) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Topic");
        reference.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                binding.editNameTopic.setText(snapshot.child("nameTopic").getValue() + "");
                binding.descriptionTopicEdit.setText(snapshot.child("descriptionTopic").getValue() + "");
                binding.urlVideoEdit.setText(snapshot.child("videoUrl").getValue() + "");
                try {
                    Picasso.get().load(snapshot.child("imageUrl").getValue().toString()).placeholder(R.drawable.spinner).into(binding.imageTopicEdit);
                }catch (Exception e){
                    Toast.makeText(EditTopicActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditTopicActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTopic(String getExtraString){
        showDialog();
        Map<String,Object> update = new HashMap<>();
        update.put("videoUrl",binding.urlVideoEdit.getText().toString());
        update.put("nameTopic",binding.editNameTopic.getText().toString());
        update.put("descriptionTopic",binding.descriptionTopicEdit.getText().toString());
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Topic");
        reference.child(getExtraString).updateChildren(update).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    hideDialog();
                    Intent i =new Intent(EditTopicActivity.this,HomeActivityForDoctor.class);
                    startActivity(i);
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditTopicActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}