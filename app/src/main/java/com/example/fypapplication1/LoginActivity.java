//Student Name: Megan Cash
//Student Number: C19317723
package com.example.fypapplication1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    //Views
    EditText EmailEt, PasswordEt;
    TextView notHaveAccountTv, RecoverPasswordTv;
    Button LoginButton;

    //Firebase
    private FirebaseAuth Auth;

    //Progress dialog
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Action bar and its title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(" ");
        //Enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        //Firebase
        Auth = FirebaseAuth.getInstance();

        EmailEt = findViewById(R.id.emailEt);
        PasswordEt = findViewById(R.id.passwordEt);
        notHaveAccountTv = findViewById(R.id.nothave_accountTv);
        RecoverPasswordTv = findViewById(R.id.recoverPasswordTv);
        LoginButton = findViewById(R.id.loginButton);

        //Login Button
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Input data
                String email = EmailEt.getText().toString();
                String password = PasswordEt.getText().toString().trim();
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    //Validation if email pattern is incorrect
                    EmailEt.setError("Error!Invalid email");
                    EmailEt.setFocusable(true);
                } else {
                    //If the email pattern is correct
                    loginUser(email, password);
                }
            }
        });
        //If the user does not have an account, go to register activity
        notHaveAccountTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });
        //Recover users password
        RecoverPasswordTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRecoverPasswordDialog();
            }
        });

        //init progress
        pd= new ProgressDialog(this);

    }

    private void showRecoverPasswordDialog() {
        //Alertdialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Recover your password");
        LinearLayout linearLayout = new LinearLayout(this);
        EditText emailEt = new EditText(this);
        emailEt.setHint("Email");
        emailEt.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        emailEt.setMinEms(16);

        linearLayout.addView(emailEt);
        linearLayout.setPadding(10, 10, 10, 10);

        builder.setView(linearLayout);

        //Buttons recover
        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email = emailEt.getText().toString().trim();
                beginRecovery(email);
            }
        });
        //Cancel
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        //Show dialog
        builder.create().show();
    }

    private void beginRecovery(String email) {
        pd.setMessage("Sending the password recovery email..");
        pd.show();
        Auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                pd.dismiss();
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Email has successfully been sent!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Error! Email failed to send!", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(LoginActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void loginUser(String email, String password) {
        //Show progress dialog
        pd.setMessage("Logging in..");
        pd.show();
        Auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            pd.dismiss();
                            FirebaseUser user = Auth.getCurrentUser();
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            Toast.makeText(LoginActivity.this, "Authentication successful!", Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            pd.dismiss();
                            Toast.makeText(LoginActivity.this, "Error! Authentication Failed", Toast.LENGTH_SHORT).show();

                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(LoginActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
