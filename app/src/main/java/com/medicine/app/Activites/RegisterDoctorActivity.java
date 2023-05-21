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
import com.medicine.app.databinding.ActivityRegisterDoctorBinding;

import java.util.HashMap;
import java.util.Map;

public class RegisterDoctorActivity extends AppCompatActivity {

    private ActivityRegisterDoctorBinding binding;
    private Intent intent;
    private ProgressDialog progressDialog;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterDoctorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        intent = getIntent();
        binding.txtSignUpClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterDoctorActivity.this,SignInActivity.class));
                finish();
            }
        });

        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validAll()) {

                }else {
                    if (!EmailValidator.isValidEmail(binding.emailAddressDoctor.getText().toString())) {
                        Toast.makeText(RegisterDoctorActivity.this, "Email Valid", Toast.LENGTH_SHORT).show();
                    } else if (binding.confirmpasswordDoctor.getText().toString().equals(binding.passwordDoctor.getText().toString())) {
                        createUser();
                    } else {
                        Toast.makeText(RegisterDoctorActivity.this, "Please Confirm Password", Toast.LENGTH_SHORT).show();
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
        String email = binding.emailAddressDoctor.getText().toString().trim();
        String password = binding.passwordDoctor.getText().toString().trim();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    saveUserData();
                    Intent i = new Intent(RegisterDoctorActivity.this, SignInActivity.class);
                    startActivity(i);
                    finish();
                    Toast.makeText(RegisterDoctorActivity.this, "Task is Successful", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterDoctorActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                hideDialog();
            }
        });

    }

    private void saveUserData() {
        String id = String.valueOf(System.currentTimeMillis());
        String Uid = mAuth.getUid();
        Map<String, Object> user = new HashMap<>();
        user.put("uId", mAuth.getUid());
        user.put("firstName", binding.firstNameDoctor.getText().toString());
        user.put("lastName", binding.lastNameDoctor.getText().toString());
        user.put("middleName", binding.middleNameDoctor.getText().toString());
        user.put("email", binding.emailAddressDoctor.getText().toString());
        user.put("password", binding.passwordDoctor.getText().toString());
        user.put("address", binding.addressDoctor.getText().toString());
        user.put("birthDoctor", binding.dateOfBirthDoctor.getText().toString());
        user.put("type","Doctor");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("UserDoctor");
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
                Toast.makeText(RegisterDoctorActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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

    boolean validAll() {
        return isEmty(binding.firstNameDoctor, "Required") &
                isEmty(binding.lastNameDoctor, "Required") &
                isEmty(binding.middleNameDoctor, "Required") &
                isEmty(binding.emailAddressDoctor, "Required") &
                isEmty(binding.passwordDoctor, "Required") &
                isEmty(binding.confirmpasswordDoctor, "Required") &
                isEmty(binding.dateOfBirthDoctor, "Required") &
                isEmty(binding.addressDoctor, "Required") &
                isEmty(binding.mobileNumberDoctor, "Required");
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