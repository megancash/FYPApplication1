//Student Name: Megan Cash
//Student Number: C19317723
package com.example.fypapplication1.CardOptions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.example.fypapplication1.Adapters.ToDoAdapter;
import com.example.fypapplication1.CardOptions.ToDoOption.AddTaskActivity;
import com.example.fypapplication1.Models.ToDo;
import com.example.fypapplication1.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ToDoListActivity extends AppCompatActivity {


    //Widgets
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private ProgressBar progress;
    private DatabaseReference reference;

    //Create instances of the Firebase Database
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    //Initialising Global Variables
    private String id;
    private String key ="";
    private String category;
    private String description;
    private String deadline;
    private ArrayList<ToDo> toDoList = new ArrayList<>();
    private ToDoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);

        //Action bar and its title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("To-do List");
        //Enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.recyclerView);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ToDoAdapter(this, toDoList, id);
        recyclerView.setAdapter(adapter);

        mUser=mAuth.getCurrentUser();
        id = mUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference("Tasks").child(id);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                toDoList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    ToDo toDo = snapshot1.getValue(ToDo.class);
                    toDo.setId(snapshot1.getKey());
                    toDoList.add(toDo);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  Intent i = new Intent(ToDoListActivity.this, AddTaskActivity.class);
                  i.putExtra("uid", id);
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