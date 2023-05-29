package com.medicine.app.Activites;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
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
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.medicine.app.databinding.ActivityAddTopicBinding;
import com.medicine.app.databinding.ActivityHomeForDoctorBinding;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddTopicActivity extends AppCompatActivity {

    private ActivityAddTopicBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private static final int REQUESTCIDE = 100;
    private Uri uriImages;
    Bitmap bitmap;
    private ProgressDialog progressDialog;
    private StorageReference mStorageRef;
    private StorageTask task;
    Uri downloadUri;
    String firstName, lastName, middleName, email, address, imageUser;
    String uId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddTopicBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        getUserData();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        binding.imageTopic.setVisibility(View.GONE);
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.btnAddTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validAll() && uriImages == null){
                    Toast.makeText(AddTopicActivity.this, "Please select Image", Toast.LENGTH_SHORT).show();
                }else {
                    uploadTopic();
                }
            }
        });
        binding.selectPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getImage = new Intent();
                getImage.setAction(Intent.ACTION_GET_CONTENT);
                getImage.setType("image/*");
                startActivityForResult(getImage, REQUESTCIDE);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null & REQUESTCIDE == 100) {
            uriImages = data.getData();
            Log.d("ImageUri", "onActivityResult: "+uriImages);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriImages);
                binding.imageTopic.setVisibility(View.VISIBLE);
                binding.imageTopic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        user = mAuth.getCurrentUser();
        if (user != null) {
             uId = user.getUid();
        } else {
            startActivity(new Intent(AddTopicActivity.this, SignInActivity.class));
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
        return isEmty(binding.topicName, "required") &
                isEmty(binding.descriptionTopic, "required") &
                isEmty(binding.urlVideo, "required");
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

    public void uploadTopic() {
        progressDialog.setMessage("Please Wait for Upload Topic...");
        showDialog();
        if (bitmap != null && validAll()) {
            StorageReference storageReference = mStorageRef.child("Images/" + UUID.randomUUID().toString());
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            bitmap.recycle();
            task = storageReference.putBytes(byteArray);
            Task<Uri> urlTask = task.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return storageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        downloadUri = task.getResult();
                        saveDataTopic();
                        Toast.makeText(AddTopicActivity.this, "Success Upload Topic ", Toast.LENGTH_SHORT).show();
                    } else {

                        Toast.makeText(AddTopicActivity.this, "Please check Your internet Connection", Toast.LENGTH_SHORT).show();

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddTopicActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    hideDialog();
                }
            });
        }else {
            Toast.makeText(this, "Please Select Image !!", Toast.LENGTH_SHORT).show();
            hideDialog();
        }

    }
    private void saveDataTopic() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateTopic = dateFormat.format(new Date());
        String idTopic = String.valueOf(System.currentTimeMillis());

        Map<String, Object> dataTopic = new HashMap<>();
        dataTopic.put("dateTopic", dateTopic);
        dataTopic.put("imageUrl", "" + downloadUri);
        dataTopic.put("Uid", mAuth.getUid());
        dataTopic.put("idTopic", idTopic);
        dataTopic.put("nameTopic", binding.topicName.getText().toString());
        dataTopic.put("descriptionTopic", binding.descriptionTopic.getText().toString());
        dataTopic.put("videoUrl", binding.urlVideo.getText().toString());
        dataTopic.put("firstName",firstName);
        dataTopic.put("middleName",middleName);
        dataTopic.put("lastName",lastName);
        dataTopic.put("address",address);
        dataTopic.put("email",email);
        dataTopic.put("imageUser",imageUser);


        Map<String,Object> noti = new HashMap<>();
        String idNoti = String.valueOf(System.currentTimeMillis());
        noti.put("firstName",firstName);
        noti.put("middleName",middleName);
        noti.put("lastName",lastName);
        noti.put("idTopic", idTopic);
        noti.put("Notifications",firstName+" Add new Topic");
        noti.put("idNoti",idNoti);
        noti.put("imageUrl",imageUser);
        noti.put("dateTopic", idNoti);
        DatabaseReference not = FirebaseDatabase.getInstance().getReference("Notifications");
        not.child(idNoti).setValue(noti);
        HomeActivityForDoctor.binding.notificationsID.setVisibility(View.VISIBLE);
     //   HomeActivityForPatient.binding.notificationsIDD.setVisibility(View.VISIBLE);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Topic");
        reference.child(idTopic).setValue(dataTopic).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    hideDialog();
                    Intent i = new Intent(AddTopicActivity.this, HomeActivityForDoctor.class);
                    startActivity(i);
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                hideDialog();
                Toast.makeText(AddTopicActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }
    private void getUserData(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        reference.child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                firstName = snapshot.child("firstName").getValue()+"";
                middleName = snapshot.child("middleName").getValue()+"";
                lastName = snapshot.child("lastName").getValue()+"";
                email = snapshot.child("email").getValue()+"";
                address = snapshot.child("address").getValue()+"";
                imageUser = snapshot.child("imageUrl").getValue()+"";

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}