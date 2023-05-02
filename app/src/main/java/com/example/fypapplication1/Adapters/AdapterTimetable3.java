//Wednesday
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

public class AdapterTimetable3 extends RecyclerView.Adapter<AdapterTimetable3.MyVHolder> {
    Context context;
    List<Timetable> timetableList3;
    String id;

    //Empty constructor
    public AdapterTimetable3() {

    }

    //Constructor
    public AdapterTimetable3(Context context, List<Timetable>timetableList3, String id){
        this.context = context;
        this.timetableList3 = timetableList3;
        this.id = id;
    }


    @NonNull
    @Override
    public AdapterTimetable3.MyVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout from tasks
        View view = LayoutInflater.from(context).inflate(R.layout.row_timetable, parent, false);
        return new AdapterTimetable3.MyVHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterTimetable3.MyVHolder holder, int position) {
        //get data

        Timetable timetable = timetableList3.get(position);
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
        return timetableList3.size();
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

