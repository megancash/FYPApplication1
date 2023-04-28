package com.example.fypapplication1.CardOptions.TimetableOptions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fypapplication1.MainActivity;
import com.example.fypapplication1.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddTimetableActivity extends AppCompatActivity {

    EditText addModule, addTime, addRoom, addLecturer;
    Button addButton, cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_timetable);

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
        Map<String,Object> map = new HashMap<>();
        map.put("Module", addModule.getText().toString());
        map.put("Lecturer", addLecturer.getText().toString());
        map.put("Room", addRoom.getText().toString());
        map.put("Time", addTime.getText().toString());
        FirebaseDatabase.getInstance().getReference().child("Timetable").push()
                .setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        addModule.setText("");
                        addLecturer.setText("");
                        addRoom.setText("");
                        addTime.setText("");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddTimetableActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}