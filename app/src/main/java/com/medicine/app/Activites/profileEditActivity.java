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
import com.medicine.app.R;
import com.medicine.app.databinding.ActivityProfileBinding;
import com.squareup.picasso.Picasso;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class profileEditActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    String uId, getExtraString;
    Intent intent;
    private Uri uriImages;
    Bitmap bitmap;
    private ProgressDialog progressDialog;
    private StorageReference mStorageRef;
    private StorageTask task;
    Uri downloadUri;
    private static final int REQUESTCIDE = 100;
    String typeUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait for Update Data...");
        progressDialog.setCancelable(false);
        intent = getIntent();
        getExtraString = intent.getStringExtra("idUser");
        getUserData(getExtraString);

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.choseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getImage = new Intent();
                getImage.setAction(Intent.ACTION_GET_CONTENT);
                getImage.setType("image/*");
                startActivityForResult(getImage, REQUESTCIDE);
            }
        });

        binding.btnSaveChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validAll()) {

                } else {
                    uploadDataUser();
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
            Intent userOut = new Intent(profileEditActivity.this, SignInActivity.class);
            startActivity(userOut);
            finish();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null & REQUESTCIDE == 100) {
            uriImages = data.getData();
            Log.d("ImageUri", "onActivityResult: " + uriImages);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriImages);
                binding.imageProfile.setVisibility(View.VISIBLE);
                binding.imageProfile.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void getUserData(String id) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        reference.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                binding.firstNameDoctor.setText(snapshot.child("firstName").getValue() + "");
                binding.middleNameDoctor.setText(snapshot.child("middleName").getValue() + "");
                binding.lastNameDoctor.setText(snapshot.child("lastName").getValue() + "");
                binding.emailAddressDoctor.setText(snapshot.child("email").getValue() + "");
                binding.addressDoctor.setText(snapshot.child("address").getValue() + "");
                binding.mobileNumberDoctor.setText(snapshot.child("mobileNumber").getValue() + "");
                binding.dateOfBirthDoctor.setText(snapshot.child("birthDoctor").getValue() + "");
                typeUser = snapshot.child("type").getValue()+"";
                try {
                    if (snapshot.child("imageUrl").getValue() != null){
                    }else {
                        Toast.makeText(profileEditActivity.this, "You can select Image!!", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

    public void uploadDataUser() {
        showDialog();
        if (bitmap != null && validAll()) {
            StorageReference storageReference = mStorageRef.child("ImagesProfile/" + UUID.randomUUID().toString());
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
                        saveDataUser(downloadUri+"");
                        Toast.makeText(profileEditActivity.this, "Success Uploaded Image ", Toast.LENGTH_SHORT).show();
                    } else {

                        Toast.makeText(profileEditActivity.this, "Please check Your internet Connection", Toast.LENGTH_SHORT).show();

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(profileEditActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    hideDialog();
                }
            });
        } else {
            saveDataUser(downloadUri+"");

        }

    }

    private void saveDataUser(String downloadUri) {
        showDialog();
        Map<String, Object> user = new HashMap<>();
        user.put("firstName", binding.firstNameDoctor.getText().toString());
        user.put("lastName", binding.lastNameDoctor.getText().toString());
        user.put("middleName", binding.middleNameDoctor.getText().toString());
        user.put("email", binding.emailAddressDoctor.getText().toString());
        user.put("address", binding.addressDoctor.getText().toString());
        user.put("birthDoctor", binding.dateOfBirthDoctor.getText().toString());
        user.put("mobileNumber", binding.mobileNumberDoctor.getText().toString());
        user.put("imageUrl", downloadUri);
        user.put("uId", mAuth.getUid());
        user.put("type", typeUser);
        Log.d("DownloadimageUrl", "saveDataUser:"+downloadUri);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User").child(uId);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reference.updateChildren(user);
                hideDialog();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(profileEditActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                hideDialog();
            }
        });

    }

    boolean validAll() {
        return isEmty(binding.firstNameDoctor, "required") &
                isEmty(binding.middleNameDoctor, "required") &
                isEmty(binding.lastNameDoctor, "required") &
                isEmty(binding.emailAddressDoctor, "required") &
                isEmty(binding.addressDoctor, "required") &
                isEmty(binding.mobileNumberDoctor, "required") &
                isEmty(binding.dateOfBirthDoctor, "required");
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
}

