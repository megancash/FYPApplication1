package com.example.fypapplication1.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.fypapplication1.Adapters.ChatListAdapter;
import com.example.fypapplication1.CreateGroupChatActivity;
import com.example.fypapplication1.MainActivity;
import com.example.fypapplication1.Models.Chat;
import com.example.fypapplication1.Models.ChatList;
import com.example.fypapplication1.Models.User;
import com.example.fypapplication1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ChatListFragment extends Fragment {

    //Firebase auth
    FirebaseAuth firebaseAuth;
    RecyclerView recyclerView;
    List<ChatList> chatListList;
    List<User> userList;
    DatabaseReference reference;
    FirebaseUser currentUser;
    ChatListAdapter chatListAdapter;


    //Empty constructor
    public ChatListFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat_list, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
       // recyclerView= view.findViewById(R.id.recyclerView);

        chatListList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chatlist").child(currentUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatListList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    ChatList chatList = ds.getValue(ChatList.class);
                    chatListList.add(chatList);
                }
                loadChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }
    private void loadChats() {
        userList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    User user = ds.getValue(User.class);
                    for (ChatList chatList: chatListList) {
                        /*if (user.getUid() != null && user.getUid().equals(chatList.getId())) {
                            userList.add(user);
                            break;
                        }*/
                    }
                  /*  //adapter
                    chatListAdapter = new ChatListAdapter(getContext(), userList);
                    //setadapter
                    recyclerView.setAdapter(chatListAdapter);*/
                    //set last message
                    for (int i=0; i<userList.size(); i++) {
                        lastMessage(userList.get(i).getUid());
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void lastMessage(final String userId) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String theLastMessage = "default";
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    Chat chat = ds.getValue(Chat.class);
                    if (chat==null) {
                        continue;
                    }
                    String sender = chat.getSender();
                    String receiver = chat.getReceiver();
                    if (sender == null || receiver ==null) {
                        continue;
                    }
                    if (chat.getReceiver().equals(currentUser.getUid()) &&
                            chat.getSender().equals(userId) ||
                    chat.getReceiver().equals(userId) && chat.getSender().equals(currentUser.getUid())) {
                        //instead of displaying url in message show 'sent photo'
                        /*if (chat.getType().equals("image")) {
                            theLastMessage = "Sent a photo";
                        }
                        else {
                            theLastMessage=chat.getMessage();
                        }*/
                    }
                }
                /*chatListAdapter.setLastMessageMap(userId, theLastMessage);
                chatListAdapter.notifyDataSetChanged();*/

            }

            @Override
            public void onCancelled(@NonNull DatabaseError DatabaseError) {

            }
        });
    }

    private void checkUserStatus() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user !=null) {

        }else {
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    //Inflating options menu

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //inflating menu
        inflater.inflate(R.menu.menu_main, menu);

        //hide menu option
        menu.findItem(R.id.action_add_participant).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }
    //menu onClick options

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            firebaseAuth.signOut();
            checkUserStatus();
        }
        else if (id==R.id.action_create_group) {
            startActivity(new Intent(getActivity(), CreateGroupChatActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}