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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.medicine.app.databinding.ActivityRegisterPatientBinding;

import java.util.HashMap;
import java.util.Map;

public class RegisterPatientActivity extends AppCompatActivity {
    private ActivityRegisterPatientBinding binding;

    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterPatientBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);


        binding.txtSignUpClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterPatientActivity.this,SignInActivity.class));
                finish();
            }
        });
        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validAll()) {

                }else {
                    if (!EmailValidator.isValidEmail(binding.emailAddressPatient.getText().toString())) {
                        Toast.makeText(RegisterPatientActivity.this, "Email Valid", Toast.LENGTH_SHORT).show();
                    } else if (binding.confirmpasswordPatient.getText().toString().equals(binding.passwordPatient.getText().toString())) {
                        createUser();
                    } else {
                        Toast.makeText(RegisterPatientActivity.this, "Please Confirm Password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void createUser() {
        progressDialog.setMessage("Please Wait..");
        showDialog();
        String email = binding.emailAddressPatient.getText().toString().trim();
        String password = binding.passwordPatient.getText().toString().trim();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    saveUserData();
                    Intent i = new Intent(RegisterPatientActivity.this, SignInActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterPatientActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void saveUserData() {
        String id = String.valueOf(System.currentTimeMillis());
        String Uid = mAuth.getUid();
        Map<String, Object> user = new HashMap<>();
        user.put("uId", mAuth.getUid());
        user.put("firstName", binding.firstNamePatient.getText().toString());
        user.put("lastName", binding.lastNamePatient.getText().toString());
        user.put("middleName", binding.middleNamePatient.getText().toString());
        user.put("email", binding.emailAddressPatient.getText().toString());
        user.put("password", binding.passwordPatient.getText().toString());
        user.put("address", binding.addressPatient.getText().toString());
        user.put("birthDoctor", binding.dateOfBirthPatient.getText().toString());
        user.put("mobileNumber", binding.mobileNumberPatient.getText().toString());
        user.put("imageUrl","");
        user.put("type","Patient");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        reference.child(Uid).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    hideDialog();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                hideDialog();
                Toast.makeText(RegisterPatientActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    boolean validAll() {
        return isEmty(binding.firstNamePatient, "Required") &
                isEmty(binding.lastNamePatient, "Required") &
                isEmty(binding.middleNamePatient, "Required") &
                isEmty(binding.emailAddressPatient, "Required") &
                isEmty(binding.passwordPatient, "Required") &
                isEmty(binding.confirmpasswordPatient, "Required") &
                isEmty(binding.dateOfBirthPatient, "Required") &
                isEmty(binding.addressPatient, "Required") &
                isEmty(binding.mobileNumberPatient, "Required");
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

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.hide();
    }
}