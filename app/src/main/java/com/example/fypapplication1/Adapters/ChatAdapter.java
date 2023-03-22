package com.example.fypapplication1.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fypapplication1.Models.Chat;
import com.example.fypapplication1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyHolder> {

    private static final int MSG_TYPE_SENDER =1;
    private static final int MSG_TYPE_RECEIVER =0;
    Context context;
    List<Chat> chatList;
    String imageUrl;

    FirebaseUser fUser;

    public ChatAdapter(Context context, List<Chat> chatsList, String imageUrl) {
        this.context=context;
        this.chatList=chatsList;
        this.imageUrl=imageUrl;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //Inflate sender and receiver layouts
        if (i==MSG_TYPE_SENDER) {
            View view = LayoutInflater.from(context).inflate(R.layout.row_chat_sender, viewGroup, false);
            return new MyHolder(view);
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.row_chat_receiver, viewGroup, false);
            return new MyHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, @SuppressLint("RecyclerView") final int i) {
        //get data
        String message = chatList.get(i).getMessage();
        String timestamp = chatList.get(i).getTimestamp();

        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        try {
            //set time stamp as dd/mm/yyyy hh:mm
            cal.setTimeInMillis(Long.parseLong(timestamp));
        } catch(NumberFormatException e) {
        }

        String dateTime = DateFormat.format("dd/MM/yyyy hh:mm aa", cal).toString();
        //set data
        myHolder.messageTv.setText(message);
        myHolder.timeTv.setText(dateTime);
        try {
            Picasso.get().load(imageUrl).into(myHolder.profileIv);
        } catch (Exception e) {
        }

        //Delete pop up screen
        myHolder.messageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete");
                builder.setMessage("Delete this message");
                //delete button
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteMessage(i);
                    }
                });
                //cancel delete action
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                //Display delete dialog
                builder.create().show();
            }
        });

        //Set message status
        if (i==chatList.size()-1) {
            if (chatList.get(i).isSeen()){
                myHolder.isSeenTv.setText("Seen");
            } else {
                myHolder.isSeenTv.setText("Delivered");
            }
        } else{
            myHolder.isSeenTv.setVisibility(View.GONE);
        }

    }

    private void deleteMessage(int position) {
        final String myUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        String msgTimeStamp = chatList.get(position).getTimestamp();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Chats");
        Query query = dbRef.orderByChild("timestamp").equalTo(msgTimeStamp);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    if (ds.child("sender").getValue().equals(myUID)) {
                        //remove from database path
                        ds.getRef().removeValue();

                        Toast.makeText(context, "Message has been deleted.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "You can only delete a message that you have sent.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    @Override
    public int getItemViewType(int position) {
        //get current user
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        if (chatList.get(position).getSender().equals(fUser.getUid())) {
            return MSG_TYPE_SENDER;
        }else {
            return MSG_TYPE_RECEIVER;
        }
    }

    //view holder class
    class MyHolder extends RecyclerView.ViewHolder{

        //views
        ImageView profileIv;
        TextView messageTv, timeTv, isSeenTv;
        LinearLayout messageLayout;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            //views
            profileIv = itemView.findViewById(R.id.profileIv);
            messageTv = itemView.findViewById(R.id.messageTv);
            timeTv = itemView.findViewById(R.id.timeTv);
            isSeenTv = itemView.findViewById(R.id.isSeenTv);
            messageLayout = itemView.findViewById(R.id.messageLayout);

        }

    }
}
