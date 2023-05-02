package com.example.fypapplication1.CardOptions.TimetableOptions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fypapplication1.MainActivity;
import com.example.fypapplication1.Models.Timetable;
import com.example.fypapplication1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddTimetableActivity2 extends AppCompatActivity {

    private DatabaseReference reference;
    FirebaseDatabase database;
    EditText addModule, addTime, addRoom, addLecturer;
    Button addButton, cancelButton;

    //Create instances of the Firebase Database
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_timetable2);
        //Action bar and its title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(" ");
        //Enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        id = mUser.getUid();

        reference = FirebaseDatabase.getInstance().getReference("Timetable").child("Tuesday").child(id);
        addModule=(EditText)findViewById(R.id.addModule);
        addTime=(EditText)findViewById(R.id.addTime);
        addRoom=(EditText)findViewById(R.id.addRoom);
        addLecturer=(EditText)findViewById(R.id.addLecturer);

        addButton=findViewById(R.id.addButton);
        cancelButton=findViewById(R.id.cancelButton);



        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
    }
    private void addData() {
        Timetable timetable = new Timetable(addModule.getText().toString().trim(), addLecturer.getText().toString().trim(), addRoom.getText().toString().trim(), addTime.getText().toString().trim());
        reference.child(reference.push().getKey()).setValue(timetable).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    Toast.makeText(AddTimetableActivity2.this, "Slot added", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(AddTimetableActivity2.this, "Could not add slot!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}