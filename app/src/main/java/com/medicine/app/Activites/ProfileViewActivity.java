package com.medicine.app.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.PopupMenu;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.medicine.app.R;
import com.medicine.app.databinding.ActivityProfileViewBinding;
import com.squareup.picasso.Picasso;

public class ProfileViewActivity extends AppCompatActivity {

    private ActivityProfileViewBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    String uId, getExtraString;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        intent = getIntent();
        getExtraString = intent.getStringExtra("idUser");
        getUserData(getExtraString);
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    private void getUserData(String id) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        reference.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                binding.firstNameProfile.setText(snapshot.child("firstName").getValue() + "");
                binding.middleNameProfile.setText(snapshot.child("middleName").getValue() + "");
                binding.lastNameProfile.setText(snapshot.child("lastName").getValue() + "");
                binding.emailAddressDoctor.setText(snapshot.child("email").getValue() + "");
                binding.addressProfile.setText(snapshot.child("address").getValue() + "");
                binding.mobileNumberProfile.setText(snapshot.child("mobileNumber").getValue() + "");
                binding.dateOfBirthProfile.setText(snapshot.child("birthDoctor").getValue() + "");
                try {
                    Picasso.get().load(snapshot.child("imageUrl").getValue()+"").placeholder(R.drawable.spinner).into(binding.imageProfile);

                }catch (Exception e){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
            Intent userOut = new Intent(ProfileViewActivity.this, SignInActivity.class);
            startActivity(userOut);
            finish();
        }
    }

    public void onMenuButtonClick(View view) {
        showPopupMenu(view);
    }
    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_msg, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {

                case R.id.menuMsg:
                    Intent msg = new Intent(getApplicationContext(), MessagesActivity.class);
                    msg.putExtra("idUser",getExtraString);
                    startActivity(msg);
                    return true;
            }
            return false;
        });

        popupMenu.show();
    }

}