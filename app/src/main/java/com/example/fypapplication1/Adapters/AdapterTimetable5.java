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

public class AdapterTimetable5 extends RecyclerView.Adapter<AdapterTimetable5.MyVHolder> {
    Context context;
    List<Timetable> timetableList5;
    String id;

    //Empty constructor
    public AdapterTimetable5() {

    }

    //Constructor
    public AdapterTimetable5(Context context, List<Timetable>timetableList5, String id){
        this.context = context;
        this.timetableList5 = timetableList5;
        this.id = id;
    }


    @NonNull
    @Override
    public AdapterTimetable5.MyVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout from tasks
        View view = LayoutInflater.from(context).inflate(R.layout.row_timetable, parent, false);
        return new AdapterTimetable5.MyVHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterTimetable5.MyVHolder holder, int position) {
        //get data

        Timetable timetable = timetableList5.get(position);
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
        return timetableList5.size();
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
