//Student Name: Megan Cash
//Student Number: C19317723
package com.example.fypapplication1.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fypapplication1.CardOptions.ToDoListActivity;
import com.example.fypapplication1.Models.ToDo;
import com.example.fypapplication1.R;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.MyHolder> {
    Context context;
    List<ToDo> toDoList;
    String id;

    //Empty constructor
    public ToDoAdapter() {

    }

    //Constructor
    public ToDoAdapter(Context context, List<ToDo>toDoList, String id){
        this.context = context;
        this.toDoList = toDoList;
        this.id = id;
    }
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout from tasks
        View view = LayoutInflater.from(context).inflate(R.layout.task, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        //get data
        ToDo toDo = toDoList.get(position);
        holder.mTitleTv.setText(toDo.getTaskTitle());
        holder.mDescriptionTv.setText(toDo.getTaskDescription());
        holder.mDeadlineTv.setText(toDo.getTaskDeadline());

    }

    @Override
    public int getItemCount() {
        return toDoList.size();
    }
    class MyHolder extends RecyclerView.ViewHolder{

        TextView mTitleTv, mDescriptionTv, mDeadlineTv;
        Button deleteTaskButton;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            //views
            mTitleTv = itemView.findViewById(R.id.titleTv);
            mDescriptionTv = itemView.findViewById(R.id.descriptionTv);
            mDeadlineTv = itemView.findViewById(R.id.deadlineTv);
            deleteTaskButton = itemView.findViewById(R.id.deleteTask);

            if (deleteTaskButton != null ) {
                deleteTaskButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                       // String taskId = toDoList.get(position).getId();
                       // ToDo toDo = toDoList.get(position);
                       // FirebaseDatabase.getInstance().getReference(id).child(toDo.getId()).removeValue();
                      //  toDoList.remove(position);
                       // notifyItemRemoved(position);
                       // notifyItemRangeChanged(position, toDoList.size());
                    }
                });
            }


        }
    }
}
