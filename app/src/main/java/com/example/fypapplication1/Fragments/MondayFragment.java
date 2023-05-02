//Student Name: Megan Cash
//Student Number: C19317723
package com.example.fypapplication1.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.fypapplication1.Adapters.AdapterTimetable;
import com.example.fypapplication1.Adapters.ToDoAdapter;
import com.example.fypapplication1.CardOptions.TimetableActivity;
import com.example.fypapplication1.CardOptions.TimetableOptions.AddTimetableActivity;
import com.example.fypapplication1.CardOptions.ToDoListActivity;
import com.example.fypapplication1.CardOptions.ToDoOption.AddTaskActivity;
import com.example.fypapplication1.Models.Timetable;
import com.example.fypapplication1.Models.ToDo;
import com.example.fypapplication1.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MondayFragment extends Fragment {

    //Widgets
    private FloatingActionButton floatingActionButton;
    private ProgressBar progress;
    private DatabaseReference reference;

    //Create instances of the Firebase Database
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;


    private String id;
    RecyclerView recyclerView;
    AdapterTimetable adapterTimetable;
    private ArrayList<Timetable> timetableList = new ArrayList<>();
    FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_monday, container, false);
        view.setBackgroundResource(R.drawable.home_background);
        mAuth = FirebaseAuth.getInstance();
        recyclerView= view.findViewById(R.id.recyclerView);
        fab = view.findViewById(R.id.floatingActionButton);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapterTimetable = new AdapterTimetable(getActivity(), timetableList, id);
        recyclerView.setAdapter(adapterTimetable);

        mUser=mAuth.getCurrentUser();
        id = mUser.getUid();

        reference = FirebaseDatabase.getInstance().getReference("Timetable").child("Monday").child(id);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                timetableList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Timetable timetable = snapshot1.getValue(Timetable.class);
                    timetable.setKey(snapshot1.getKey());
                    timetableList.add(timetable);
                }
                adapterTimetable.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddTimetableActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}


