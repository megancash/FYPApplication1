//Student Name: Megan Cash
//Student Number: C19317723
package com.example.fypapplication1.CardOptions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fypapplication1.Models.ToDo;
import com.example.fypapplication1.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class ToDoListActivity extends AppCompatActivity implements OnDialogCloseListener{


    //Widgets
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private ProgressDialog progress;
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
    private ArrayList<ToDo> taskDataSet = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);

        //Action bar and its title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(" ");
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
        progress= new ProgressDialog(this);

        mUser=mAuth.getCurrentUser();
        id = mUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("Tasks").child(id);
        Query query = reference.orderByKey();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTask();  //addTask method
            }
        });
    }
    //Add task method - To allow user to add a new task to the to-do list.
    private void addTask() {
        AlertDialog.Builder taskDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View taskView = inflater.inflate(R.layout.add_task, null);
        taskDialog.setView(taskView);

        final AlertDialog dialog = taskDialog.create();
        dialog.setCancelable(false);

        final EditText title = taskView.findViewById(R.id.inputTitle);
        final EditText description = taskView.findViewById(R.id.inputDescription);
        final EditText deadline = taskView.findViewById(R.id.inputDeadline);
        Button save = taskView.findViewById(R.id.addTaskButton);
        Button cancel = taskView.findViewById(R.id.cancelButton);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mTitle = title.getText().toString().trim();
                String mDescription = description.getText().toString().trim();
                String mDeadline = deadline.getText().toString().trim();


                if (TextUtils.isEmpty(mTitle)) {
                    title.setError("Error! Input task"); //Validation - if title is empty
                    return;
                }
                if (TextUtils.isEmpty(mDescription)) { //Validation - if description is empty
                    description.setError("Error! Input description");
                    return;
                }
                if (TextUtils.isEmpty(mDeadline)) { //Validation - if deadline is empty
                    deadline.setError("Error! Input deadline!");
                    return;
                }
                else {
                    progress.setMessage("Adding your task");
                    progress.setCanceledOnTouchOutside(false);
                    progress.show();

                    ToDo toDoItem = new ToDo(mTitle, mDescription, mDeadline);
                    reference.child(id).setValue(toDoItem).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                //taskDataSet.add(toDoItem);
                                Toast.makeText(ToDoListActivity.this, "Your task has been added to the To do list!", Toast.LENGTH_SHORT).show();
                                progress.dismiss();
                            } else {
                                String error = task.getException().toString();
                                Toast.makeText(ToDoListActivity.this, "Error! Task could not be added to the To do List.", Toast.LENGTH_SHORT).show();
                                progress.dismiss();
                            }
                        }
                    });
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<ToDo> options = new FirebaseRecyclerOptions.Builder<ToDo>()
                .setQuery(reference, ToDo.class)
                .build();

        FirebaseRecyclerAdapter<ToDo, taskViewHolder> adapter = new FirebaseRecyclerAdapter<ToDo, taskViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull taskViewHolder holder, @SuppressLint("RecyclerView") final int position, @NonNull ToDo toDoItem) {
                holder.setCategory(toDoItem.getTaskTitle());
                holder.setDescription(toDoItem.getTaskDescription());
                holder.setDeadline(toDoItem.getTaskDeadline());

                //Creating an OnClickListener to be able to update or delete a task when a task is selected.
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        key = getRef(position).getKey();
                        category = toDoItem.getTaskTitle();
                        description = toDoItem.getTaskDescription();
                        deadline = toDoItem.getTaskDeadline();

                        updateTask();
                    }
                });

            }

            @NonNull
            @Override
            public taskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task, parent, false);
                return new taskViewHolder(v);
            }

        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();


    }


    //Creating an adapter which will allow us to retrieve task data
    public static class taskViewHolder extends RecyclerView.ViewHolder{
        View view;
        //Constructor matching super
        public taskViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
        }
        public void setCategory(String category) {
            TextView taskTextView = view.findViewById(R.id.titleTv);
            taskTextView.setText(category);
        }
        public void setDescription(String description) {
            TextView taskTextView = view.findViewById(R.id.descriptionTv);
            taskTextView.setText(description);
        }
        public void setDeadline(String deadline) {
            TextView taskTextView = view.findViewById(R.id.deadline1);
            taskTextView.setText(deadline);
        }
    }
    //To allow user to update their tasks.
    private void updateTask() {
        AlertDialog.Builder taskDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.update_task, null);
        taskDialog.setView(v);

        final AlertDialog dialog = taskDialog.create();

        final EditText category1 = v.findViewById(R.id.updateTitleField);
        final EditText description1 = v.findViewById(R.id.updateDescriptionField);
        final EditText deadline1 = v.findViewById(R.id.updateDeadlineField);



        category1.setText(category);
        category1.setSelection(category.length());

        description1.setText(description);
        description1.setSelection(description.length());

        deadline1.setText(deadline);
        deadline1.setSelection(deadline.length());


        //Update Button
        Button updateButton = v.findViewById(R.id.updateButton);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category = category1.getText().toString().trim();
                description = description1.getText().toString().trim();
                deadline = deadline1.getText().toString().trim();

                ToDo toDoItem = new ToDo(category, description, deadline);

                reference.child(key).setValue(toDoItem).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ToDoListActivity.this, "Your task has been updated successfully!", Toast.LENGTH_SHORT).show();
                        } else {
                            String error = task.getException().toString();
                            Toast.makeText(ToDoListActivity.this, "Error! Task could not be updated." + error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                dialog.dismiss();
            }
        });

        //Delete Button
        Button deleteButton = v.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference.child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ToDoListActivity.this, "Task deleted from To do List.", Toast.LENGTH_SHORT).show();
                        } else {
                            String error = task.getException().toString();
                            Toast.makeText(ToDoListActivity.this, "Error! Task not deleted" + error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}