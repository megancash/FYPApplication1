package com.example.fypapplication1.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fypapplication1.Models.User;
import com.example.fypapplication1.Notifications.Data;
import com.example.fypapplication1.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterParticipantAdd extends RecyclerView.Adapter<AdapterParticipantAdd.HolderParticipantAdd> {

    private Context context;
    private ArrayList<User> userList;
    private String groupId, myGroupRole;  //Group roles: creater, participant, admin

    public AdapterParticipantAdd(Context context, ArrayList<User> userList, String groupId, String myGroupRole) {
        this.context = context;
        this.userList = userList;
        this.groupId = groupId;
        this.myGroupRole = myGroupRole;
    }

    @NonNull
    @Override
    public HolderParticipantAdd onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //layout inflater
        View view = LayoutInflater.from(context).inflate(R.layout.row_add_participants, parent, false);

        return new HolderParticipantAdd(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderParticipantAdd holder, int position) {
        //get data
        User user = userList.get(position);
        String name = user.getName();
        String email = user.getEmail();
        String image = user.getImage();
        String uid = user.getUid();

        //set data
        holder.nameTv.setText(name);
        holder.emailTv.setText(email);
        try {
            Picasso.get().load(image).placeholder(R.drawable.profile_icon).into(holder.avatarIv);
        } catch (Exception e){
            holder.avatarIv.setImageResource(R.drawable.profile_icon);
        }

        checkIfAlreadyExists(user, holder);

        //handle click
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Groups");
                ref.child(groupId).child("Participants").child(uid)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    //user exists
                                    String hisPreviousRole = ""+dataSnapshot.child("role").getValue();

                                    //Dialog options
                                    String[] options;
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setTitle("Choose an option");
                                    if (myGroupRole.equals("creator")){
                                        if(hisPreviousRole.equals("admin")) {
                                            //if current user is a creator of a group and another user is an admin
                                            options = new String[]{"Remove admin", "Remove User"};
                                            builder.setItems(options, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    //handle item clicks
                                                    if (which ==0){
                                                        //remove selected admin
                                                        removeAdmin(user);
                                                    }else {
                                                        //remove selected user
                                                        removeParticipant(user);

                                                    }
                                                }
                                            }).show();
                                        }
                                        else if (hisPreviousRole.equals("participant")) {
                                            //if current user is a creator of a group and another user is an participant
                                            options=new String[]{"Make admin", "Remove user"};
                                            builder.setItems(options, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    //handle item clicks
                                                    if (which ==0){
                                                        //make an administrator
                                                        makeAdmin(user);
                                                    }else {
                                                        //remove selected user
                                                        removeParticipant(user);

                                                    }
                                                }
                                            }).show();
                                        }
                                    }
                                    else if (myGroupRole.equals("admin")){
                                        if (hisPreviousRole.equals("creator")) {
                                            //Im admin hes creator
                                            Toast.makeText(context, "Creator of the group", Toast.LENGTH_SHORT).show();
                                        }
                                        else if (hisPreviousRole.equals("admin")) {
                                            //If current user is admin and so is selected user.
                                            options = new String[]{"Remove admin", "Remove User"};
                                            builder.setItems(options, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    //handle item clicks
                                                    if (which ==0){
                                                        //remove selected admin
                                                        removeAdmin(user);
                                                    }else {
                                                        //remove selected user
                                                        removeParticipant(user);
                                                    }
                                                }
                                            }).show();
                                        }
                                        else if (hisPreviousRole.equals("participant")){
                                            //if the current user is an admin, and the other user is participant
                                            options = new String[]{"Make Admin", "Remove user"};
                                            builder.setItems(options, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    //handle item clicks
                                                    if (which ==0){
                                                        //remove selected admin
                                                        removeAdmin(user);
                                                    }else {
                                                        //remove selected user
                                                        removeParticipant(user);

                                                    }
                                                }
                                            }).show();
                                        }
                                    }
                                }
                                else {
                                    //user does not exist/ not a participant
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setTitle("Add Participant")
                                            .setMessage("Add this user to this group?")
                                            .setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    //add user
                                                    addParticipant(user);
                                                }
                                            })
                                            .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            }).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }
        });
    }

    private void addParticipant(User user) {
        //add a participant to a group
        String timestamp = ""+System.currentTimeMillis();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("uid", user.getUid());
        hashMap.put("role", "participant");
        hashMap.put("timestamp", ""+timestamp);
        //add the user to the group
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Groups");
        ref.child(groupId).child("Participants").child(user.getUid()).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //added successfully
                        Toast.makeText(context, "User added successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Adding user to the group was unsuccessful
                        Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void makeAdmin(User user) {
        //Change user role to an admin
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("role", "admin");
        //Update role in the database
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Groups");
        reference.child(groupId).child("Participants").child(user.getUid()).updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //made admin
                        Toast.makeText(context, "User is now an admin", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void removeParticipant(User user) {
        //Remove participant from the group
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Groups");
        reference.child(groupId).child("Participants").child(user.getUid()).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //removed successfully
                        Toast.makeText(context, "Participant removed", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failed removing participant from group
                        Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void removeAdmin(User user) {
        //setup data remove admin
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("role", "participant");
        //Update role in the database
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Groups");
        reference.child(groupId).child("Participants").child(user.getUid()).updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //made admin
                        Toast.makeText(context, "User is no longer an admin", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkIfAlreadyExists(User user, HolderParticipantAdd holder) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Groups");
        ref.child(groupId).child("Participants").child(user.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            //already exists
                            String hisRole = "" + dataSnapshot.child("role").getValue();
                            holder.statusTv.setText(hisRole);
                        }else {
                            //doesnt exist
                            holder.statusTv.setText("");
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }


    class HolderParticipantAdd extends RecyclerView.ViewHolder{

        private ImageView avatarIv;
        private TextView nameTv, emailTv, statusTv;


        public HolderParticipantAdd(@NonNull View itemView) {
            super(itemView);

            avatarIv = itemView.findViewById(R.id.avatarIv);
            nameTv = itemView.findViewById(R.id.nameTv);
            emailTv = itemView.findViewById(R.id.emailTv);
            statusTv = itemView.findViewById(R.id.statusTv);

        }
    }
}
