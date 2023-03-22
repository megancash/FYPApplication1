package com.example.fypapplication1.Adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fypapplication1.Models.GroupChat;
import com.example.fypapplication1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AdapterGroupChat extends RecyclerView.Adapter<AdapterGroupChat.HolderGroupChat> {

    private static final int MSG_TYPE_RECIEVER=0;
    private static final int MSG_TYPE_SENDER=1;

    private Context context;
    private ArrayList<GroupChat> groupChatList;

    private FirebaseAuth firebaseAuth;

    public AdapterGroupChat(Context context, ArrayList<GroupChat> groupChatList) {
        this.context = context;
        this.groupChatList = groupChatList;

        firebaseAuth = FirebaseAuth.getInstance();
}

    @NonNull
    @Override
    public HolderGroupChat onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //layout inflaters
        if (viewType == MSG_TYPE_RECIEVER){
            View view = LayoutInflater.from(context).inflate(R.layout.row_groupchat_receiver, parent, false);
            return new HolderGroupChat(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.row_groupchat_sender, parent, false);
            return new HolderGroupChat(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull HolderGroupChat holder, int position) {
       //get data
        GroupChat groupChat = groupChatList.get(position);
        String message = groupChat.getMessage();
        String timestamp = groupChat.getTimestamp();
        String senderUid = groupChat.getSender();

        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        try {
        //set time stamp as dd/mm/yyyy hh:mm
        cal.setTimeInMillis(Long.parseLong(timestamp));
        } catch(NumberFormatException e) {
        }

        String dateTime = DateFormat.format("dd/MM/yyyy hh:mm aa", cal).toString();

        //set data
        holder.messageTv.setText(message);
        holder.timeTv.setText(dateTime);

        setUserName(groupChat, holder);

    }

    private void setUserName(GroupChat groupChat, HolderGroupChat holder) {
        //get sender info
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.orderByChild("uid").equalTo(groupChat.getSender())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds: dataSnapshot.getChildren()) {
                            String name =""+ ds.child("name").getValue();

                            holder.nameTv.setText(name);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return groupChatList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (groupChatList.get(position).getSender().equals(firebaseAuth.getUid())) {
           return MSG_TYPE_SENDER;
        } else {
            return MSG_TYPE_RECIEVER;
        }
    }

    class HolderGroupChat extends RecyclerView.ViewHolder{

        private TextView nameTv, messageTv, timeTv;


         public HolderGroupChat(@NonNull View itemView) {
             super(itemView);

             nameTv = itemView.findViewById(R.id.nameTv);
             messageTv = itemView.findViewById(R.id.messageTv);
             timeTv = itemView.findViewById(R.id.timeTv);

         }
    }
}