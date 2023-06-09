//Student Name: Megan Cash
//Student Number: C19317723
package com.example.fypapplication1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fypapplication1.Adapters.ChatAdapter;
import com.example.fypapplication1.Models.Chat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {

    //Sender and receiver messages
    String hisUid;
    String hisImage;
    String myUid;

    //ArrayLists and Adapters
    List<Chat> chatList;
    ChatAdapter chatAdapter;

    //Views
    ImageView profileIv;
    RecyclerView recyclerView;
    TextView nameTv, userStatusTv;
    EditText messageEt;
    ImageButton sendButton;

    //Firebase
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference usersDatabaseReference;

    //Message status
    ValueEventListener seenListener;
    DatabaseReference usersReferenceForSeen;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        //init views
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");

        recyclerView = findViewById(R.id.chat_recyclerView);
        profileIv = findViewById(R.id.profileIv);
        nameTv = findViewById(R.id.nameTv);
        userStatusTv = findViewById(R.id.userStatusTv);
        messageEt = findViewById(R.id.messageEt);
        sendButton = findViewById(R.id.sendButton);

        //Linearlayout
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);

        //RecyclerView
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        //For the users name to be displayed in the toolbar of the chat screen
        Intent intent = getIntent();
        hisUid =intent.getStringExtra("hisUid");

        //Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        usersDatabaseReference = firebaseDatabase.getReference("Users");


        //To search a user for their user information
        Query userQuery = usersDatabaseReference.orderByChild("uid").equalTo(hisUid);
        userQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    //get data
                    String name =""+ ds.child("name").getValue();
                    hisImage =""+ ds.child("image").getValue();
                    //online status
                    String onlineStatus = ""+ ds.child("onlineStatus").getValue();
                    if (onlineStatus.equals("online")) {
                        userStatusTv.setText(onlineStatus);
                    }
                    else {
                        //set timestamp
                        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
                        calendar.setTimeInMillis(Long.parseLong(onlineStatus));
                        String dateTime = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();
                        userStatusTv.setText("Last seen at "+ dateTime);


                    }

                    //set data
                    nameTv.setText(name);

                    try{
                        //received image
                        Picasso.get().load(hisImage).placeholder(R.drawable.profile_icon).into(profileIv);
                    }
                    catch (Exception e){
                        //no image
                        Picasso.get().load(R.drawable.profile_icon).into(profileIv);
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Send message Icon button onclick
        sendButton.setOnClickListener(v -> {
            //retrieve text from edit text
            String message = messageEt.getText().toString().trim();
            //to check if text is empty or not
            if (TextUtils.isEmpty(message)) {
                //if text is empty
                Toast.makeText(ChatActivity.this, "Error! Unable to send your message", Toast.LENGTH_SHORT).show();
            } else {
                //if text is not empty
                sendMessage(message);
            }
        });
        readMessages();
        seenMessages();
    }
    private void seenMessages() {
        usersReferenceForSeen = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = usersReferenceForSeen.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    Chat chat = ds.getValue(Chat.class);
                    if (chat.getReceiver().equals(myUid) && chat.getSender().equals(hisUid)) {
                        HashMap<String, Object> hasSeenHashMap = new HashMap<>();
                        hasSeenHashMap.put("is seen", true);
                        ds.getRef().updateChildren(hasSeenHashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError DatabaseError) {

            }
        });
    }

    private void readMessages() {
        chatList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Chats");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    Chat chat = ds.getValue(Chat.class);
                    //chat.setMessage(ds.getKey());
                    if (chat.getReceiver().equals(myUid) && chat.getSender().equals(hisUid) ||
                            chat.getReceiver().equals(hisUid) && chat.getSender().equals(myUid)) {
                        chatList.add(chat);
                    }
                    chatAdapter = new ChatAdapter(ChatActivity.this, chatList, hisImage);
                    chatAdapter.notifyDataSetChanged();
                    //set adapter for recyclerview
                    recyclerView.setAdapter(chatAdapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendMessage(String message) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        String timestamp = String.valueOf(System.currentTimeMillis());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", myUid);
        hashMap.put("receiver", hisUid);
        hashMap.put("message", message);
        hashMap.put("timestamp", timestamp);
        hashMap.put("isSeen", false);
        databaseReference.child("Chats").push().setValue(hashMap);

        //Reset the text input after user sends a message within a chat
        messageEt.setText("");



    }

    private void checkUserStatus() {
        //Get current user on the application
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            //current user
            myUid = user.getUid();
        } else {
            //user not signed in, go to the main activity
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    private void checkOnlineStatus(String status){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(myUid);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("onlineStatus", status);
        //to update online status of the user thats logged in
        ref.updateChildren(hashMap);

    }

    @Override
    protected void onStart() {
        checkUserStatus();
        //Set status as online
        checkOnlineStatus("online");
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //To retrieve the time
        String timestamp = String.valueOf(System.currentTimeMillis());

        //For when the users have not been online, time stamp will be displayed for when they were last online
        checkOnlineStatus(timestamp);
        usersReferenceForSeen.removeEventListener(seenListener);
    }

    @Override
    protected void onResume() {
        //Set status as online
        checkOnlineStatus("online");
        super.onResume();
    }

    //On create Options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.action_create_group).setVisible(false);
        menu.findItem(R.id.action_logout).setVisible(false);
        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem(R.id.action_add_participant).setVisible(false);
        menu.findItem(R.id.action_information).setVisible(false);
        menu.findItem(R.id.action_create_event).setVisible(false);

        //hide search bar
        menu.findItem(R.id.action_search).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        //get item id
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            firebaseAuth.signOut();
            checkUserStatus();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}