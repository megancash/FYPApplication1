package com.example.fypapplication1.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fypapplication1.Fragments.MondayFragment;
import com.example.fypapplication1.Models.Timetable;
import com.example.fypapplication1.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.ArrayList;
import java.util.List;

public class AdapterTimetable extends RecyclerView.Adapter<AdapterTimetable.MyVHolder> {
    Context context;
    List<Timetable> timetableList;
    String id;

    //Empty constructor
    public AdapterTimetable() {

    }

    //Constructor
    public AdapterTimetable(Context context, List<Timetable>timetableList, String id){
        this.context = context;
        this.timetableList = timetableList;
        this.id = id;
    }


    @NonNull
    @Override
    public MyVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout from tasks
        View view = LayoutInflater.from(context).inflate(R.layout.row_timetable, parent, false);
        return new MyVHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyVHolder holder, int position) {
        //get data

        Timetable timetable = timetableList.get(position);
        TextView module = holder.mModule;
        if (module != null) {
            module.setText(timetable.getModule());
        }
        TextView lecturer = holder.mLecturer;
        if (lecturer != null) {
            lecturer.setText(timetable.getLecturer());
        }
        TextView room = holder.mRoom;
        if (room != null) {
            room.setText(timetable.getRoom());
        }
        TextView time = holder.mTime;
        if (time != null) {
            time.setText(timetable.getTime());
        }
        //holder.mModule.setText(timetable.getModule());
        //holder.mLecturer.setText(timetable.getLecturer());
        //holder.mRoom.setText(timetable.getRoom());
        //holder.mTime.setText(timetable.getTime());


    }

    @Override
    public int getItemCount() {
        return timetableList.size();
    }
    class MyVHolder extends RecyclerView.ViewHolder{

        TextView mModule, mLecturer, mRoom, mTime;
        Button deleteTaskButton;

        public MyVHolder(@NonNull View itemView) {
            super(itemView);

            //views
            mModule = itemView.findViewById(R.id.Module);
            mLecturer = itemView.findViewById(R.id.Lecturer);
            mRoom = itemView.findViewById(R.id.Room);
            mTime = itemView.findViewById(R.id.Time);



        }
    }
}
