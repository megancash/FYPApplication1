package com.example.fypapplication1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    //Views
    EditText mEmailEt, mPasswordEt;
    Button mRegisterBtn;
    TextView mHaveAccountTv;

    //Progress bar
    ProgressDialog progressDialog;

    //Firebase
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Action bar and its title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Create Account");
        //Enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        //init
        mEmailEt = findViewById(R.id.emailEt);
        mPasswordEt = findViewById(R.id.passwordEt);
        mRegisterBtn = findViewById(R.id.registerBtn);
        mHaveAccountTv = findViewById(R.id.have_accountTv);

        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering..");

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Input email, password
                String email = mEmailEt.getText().toString().trim();
                String password = mPasswordEt.getText().toString().trim();
                //Validate
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    //Set error and focus to email edit text
                    mEmailEt.setError("Invalid email");
                    mEmailEt.setFocusable(true);
                } else if (password.length() < 6) {
                    //Set error and focus to password edit text
                    mPasswordEt.setError("Invalid password");
                    mPasswordEt.setFocusable(true);
                } else {
                    registerUser(email, password); //register
                }
            }
        });
        //Handle login textview click listener
        mHaveAccountTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });
    }


    private void registerUser(String email, String password) {
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                       if (task.isSuccessful()) {
                           progressDialog.dismiss();
                           FirebaseUser user = mAuth.getCurrentUser();

                           String email=user.getEmail();
                           String uid = user.getUid();

                           //Store user info in a hashmap
                           HashMap<Object, String> hashMap = new HashMap<>();
                           //Put info in a hashmap
                           hashMap.put("email", email);
                           hashMap.put("uid", uid);
                           hashMap.put("name", "");
                           hashMap.put("onlineStatus", "online");
                           hashMap.put("phone", "");
                           hashMap.put("image", "");

                           FirebaseDatabase database = FirebaseDatabase.getInstance();
                           //Store user data path
                           DatabaseReference reference = database.getReference("Users");
                           //Put data in database
                           reference.child(uid).setValue(hashMap);

                           Toast.makeText(RegisterActivity.this, "Registered \n"+user.getEmail(), Toast.LENGTH_SHORT).show();
                           startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                           finish();
                       } else {
                           progressDialog.dismiss();
                           Toast.makeText(RegisterActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                       }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}

