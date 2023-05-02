package com.example.fypapplication1.CardOptions.ToDoOption;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fypapplication1.CardOptions.ToDoListActivity;
import com.example.fypapplication1.Models.ToDo;
import com.example.fypapplication1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddTaskActivity extends AppCompatActivity {

    private DatabaseReference reference;
    private EditText taskTitle, taskDescription, taskDeadline;
    Button addButton, cancelButton;

    //Create instances of the Firebase Database
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        //Action bar and its title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(" ");
        //Enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        id = mUser.getUid();

        //reference = FirebaseDatabase.getInstance().getReference(getIntent().getStringExtra("uid")).child("Tasks");
        reference = FirebaseDatabase.getInstance().getReference("Tasks").child(id);
        taskTitle = findViewById(R.id.inputTitle);
        taskDescription = findViewById(R.id.inputDescription);
        taskDeadline = findViewById(R.id.inputDeadline);
        addButton = findViewById(R.id.addTaskButton);
        cancelButton = findViewById(R.id.cancelButton);



        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToDo toDo = new ToDo(taskTitle.getText().toString().trim(), taskDescription.getText().toString().trim(), taskDeadline.getText().toString().trim());
                reference.child(reference.push().getKey()).setValue(toDo).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                            Toast.makeText(AddTaskActivity.this, "Task added", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(AddTaskActivity.this, "Could not add task!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddTaskActivity.this, ToDoListActivity.class);
                startActivity(i);
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}