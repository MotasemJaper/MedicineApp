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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.medicine.app.R;
import com.medicine.app.databinding.ActivityAddTopicBinding;

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
    private static final int REQUESTCIDE = 1;
    private Uri uriImages;
    Bitmap bitmap;
    private ProgressDialog progressDialog;
    private StorageReference mStorageRef;
    private StorageTask task;
    Uri downloadUri ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddTopicBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
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
        if (data != null & requestCode == 100) {
            uriImages = data.getData();
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

    }

    private void showDialog () {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog () {
        if (progressDialog.isShowing())
            progressDialog.hide();
    }

    boolean validAll(){
        return  isEmty(binding.topicName,"required") &&
          isEmty(binding.descriptionTopic,"required") &&
          isEmty(binding.urlVideo,"required");
    }

    boolean isEmty(TextView editText, String msg){
        boolean isDone=true;
        if (editText!=null){
            if (editText.getText().toString().isEmpty()){
                editText.setError(msg);
                isDone=false;
            }
        }
        return  isDone;
    }

    public void UploadImage() {

        if (bitmap != null) {
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

                        Toast.makeText(AddTopicActivity.this, "Success Upload Post ", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), HomeActivityForDoctor.class);
                        startActivity(i);
                        finish();
                    } else {

                        Toast.makeText(AddTopicActivity.this, "Please check Your internet Connection", Toast.LENGTH_SHORT).show();
                        // Handle failures
                        // ...
                    }
                }
            });
        }else {
            showDialog();

            Intent i =new Intent(getApplicationContext(), HomeActivityForDoctor.class);
            startActivity(i);
            finish();

        }

    }

    private void addTopic(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateTopic  = dateFormat.format(new Date());
        String idTopic = String.valueOf(System.currentTimeMillis());

        Map<String, Object> dataTopic = new HashMap<>();
        dataTopic.put("dateTopic",dateTopic);
        dataTopic.put("imageUrl",""+downloadUri);
        dataTopic.put("Uid",mAuth.getUid());
        dataTopic.put("idTopic", idTopic);
        dataTopic.put("nameTopic", binding.topicName.getText().toString() );
        dataTopic.put("descriptionTopic",binding.descriptionTopic.getText().toString());

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Topic");
        reference.child(idTopic).setValue(dataTopic).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    hideDialog();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                hideDialog();
                Toast.makeText(AddTopicActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

}