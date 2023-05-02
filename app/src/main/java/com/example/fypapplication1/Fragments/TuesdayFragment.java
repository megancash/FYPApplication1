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

import com.example.fypapplication1.Adapters.AdapterTimeTable2;
import com.example.fypapplication1.CardOptions.TimetableOptions.AddTimetableActivity2;
import com.example.fypapplication1.Models.Timetable;
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


public class TuesdayFragment extends Fragment {

    //Widgets
    private FloatingActionButton floatingActionButton;
    private ProgressBar progress;
    private DatabaseReference reference;

    //Create instances of the Firebase Database
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;


    private String id;
    RecyclerView recyclerView;
    AdapterTimeTable2 adapterTimetable2;
    private ArrayList<Timetable> timetableList2 = new ArrayList<>();
    FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tuesday, container, false);
        view.setBackgroundResource(R.drawable.home_background);
        mAuth = FirebaseAuth.getInstance();
        recyclerView= view.findViewById(R.id.recyclerView);
        fab = view.findViewById(R.id.floatingActionButton);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapterTimetable2 = new AdapterTimeTable2(getActivity(), timetableList2, id);
        recyclerView.setAdapter(adapterTimetable2);

        mUser=mAuth.getCurrentUser();
        id = mUser.getUid();

        reference = FirebaseDatabase.getInstance().getReference("Timetable").child("Tuesday").child(id);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                timetableList2.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Timetable timetable = snapshot1.getValue(Timetable.class);
                    timetable.setKey(snapshot1.getKey());
                    timetableList2.add(timetable);
                }
                adapterTimetable2.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddTimetableActivity2.class);
                startActivity(intent);
            }
        });
        return view;
    }
}