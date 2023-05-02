package com.example.fypapplication1.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fypapplication1.Models.Timetable;
import com.example.fypapplication1.R;

import java.util.List;

public class AdapterTimetable4 extends RecyclerView.Adapter<AdapterTimetable4.MyVHolder> {
    Context context;
    List<Timetable> timetableList4;
    String id;

    //Empty constructor
    public AdapterTimetable4() {

    }

    //Constructor
    public AdapterTimetable4(Context context, List<Timetable>timetableList4, String id){
        this.context = context;
        this.timetableList4 = timetableList4;
        this.id = id;
    }


    @NonNull
    @Override
    public AdapterTimetable4.MyVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_timetable, parent, false);
        return new AdapterTimetable4.MyVHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterTimetable4.MyVHolder holder, int position) {
        //get data

        Timetable timetable = timetableList4.get(position);
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

    }

    @Override
    public int getItemCount() {
        return timetableList4.size();
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

