//Student Name: Megan Cash
//Student Number: C19317723
package com.example.fypapplication1.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fypapplication1.Adapters.AdapterTimetable;
import com.example.fypapplication1.CardOptions.TimetableActivity;
import com.example.fypapplication1.CardOptions.TimetableOptions.AddTimetableActivity;
import com.example.fypapplication1.Models.Timetable;
import com.example.fypapplication1.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;


public class MondayFragment extends Fragment {

    RecyclerView recyclerView;
    AdapterTimetable adapterTimetable;
    FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_monday, container, false);

        recyclerView= view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        FirebaseRecyclerOptions<Timetable> options = new FirebaseRecyclerOptions.Builder<Timetable>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Timetable"), Timetable.class)
                .build();

        adapterTimetable = new AdapterTimetable(options);
        recyclerView.setAdapter(adapterTimetable);

        fab= view.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddTimetableActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        adapterTimetable.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        adapterTimetable.stopListening();
    }
}