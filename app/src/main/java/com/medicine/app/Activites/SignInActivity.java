package com.medicine.app.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.medicine.app.R;
import com.medicine.app.databinding.ActivitySignInBinding;

public class SignInActivity extends AppCompatActivity {

    private ActivitySignInBinding binding;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        sharedpreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        binding.txtSignUpClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this,WelcomeActivity.class));
            }
        });

        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validAll()){
                    hideDialog();
                }else {
                    progressDialog.setMessage("Please Wait For Sign In...");
                    showDialog();
                    signUser(binding.emailET.getText().toString().trim()
                            ,binding.passwordlET.getText().toString().trim());
                }
            }
        });


    }

    private void signUser(String email, String password){
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    editor = sharedpreferences.edit();
                    editor.putString("email", binding.emailET.getText().toString());
                    editor.putString("password", binding.passwordlET.getText().toString());
                    editor.putString("Uid", mAuth.getUid());
                    getType(task.getResult().getUser().getUid());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignInActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                hideDialog();
            }
        });
    }

    String UserDoctor = "";
    String type = "";

    public String getType(final String id) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("UserDoctor").child(id);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserDoctor = dataSnapshot.child("type").getValue() + "";
                if (UserDoctor.equals("Doctor")) {
                    type = "UserDoctor";
                    editor.putString("typeUser", UserDoctor);
                    editor.putString("id",id);
                    editor.apply();
                    Intent i = new Intent(getApplicationContext(), HomeActivityForDoctor.class);
                    startActivity(i);
                    finish();
                    hideDialog();

                } else {
                    editor.putString("typeUser","Patient");
                    editor.putString("id",id);
                    editor.apply();
                    Intent i = new Intent(getApplicationContext(), HomeActivityForPatient.class);
                    startActivity(i);
                    finish();
                    hideDialog();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SignInActivity.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                hideDialog();
            }
        });

        return type;


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
        return isEmty(binding.emailET, "Required") &
                isEmty(binding.passwordlET, "Required");
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