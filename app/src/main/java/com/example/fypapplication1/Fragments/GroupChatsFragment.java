package com.example.fypapplication1.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.fypapplication1.Adapters.AdapterGroupChatList;
import com.example.fypapplication1.AddParticipantsActivity;
import com.example.fypapplication1.CreateGroupChatActivity;
import com.example.fypapplication1.MainActivity;
import com.example.fypapplication1.Models.GroupChatList;
import com.example.fypapplication1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class GroupChatsFragment extends Fragment {

    private RecyclerView groupsRv;

    private FirebaseAuth firebaseAuth;

    private String groupId, myGroupRole="";


    private ArrayList<GroupChatList> groupChatLists;
    private AdapterGroupChatList adapterGroupChatList;


    public GroupChatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_group_chats, container, false);

        groupsRv = view.findViewById(R.id.groupsRv);

        firebaseAuth = FirebaseAuth.getInstance();

        loadGroupChatsList();

        return view;
    }

    private void loadGroupChatsList() {
        groupChatLists = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Groups");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                groupChatLists.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //if current users id exists in participants
                    if (ds.child("Participants").child(firebaseAuth.getUid()).exists()) {
                        GroupChatList model = ds.getValue(GroupChatList.class);
                        groupChatLists.add(model);

                    }
                }
                adapterGroupChatList = new AdapterGroupChatList(getActivity(), groupChatLists);
                groupsRv.setAdapter(adapterGroupChatList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void searchGroupChatsList(final String query) {
        groupChatLists = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Groups");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                groupChatLists.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //if current users id exists in participants
                    if (ds.child("Participants").child(firebaseAuth.getUid()).exists()) {

                        //Search by title of group
                        if (ds.child("groupTitle").toString().toLowerCase().contains(query.toLowerCase())) {
                            GroupChatList model = ds.getValue(GroupChatList.class);
                            groupChatLists.add(model);
                        }
                    }
                }
                adapterGroupChatList = new AdapterGroupChatList(getActivity(), groupChatLists);
                groupsRv.setAdapter(adapterGroupChatList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true); //to show menu option in fragment
        super.onCreate(savedInstanceState);
    }

    //Inflate options menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        


        //searchview
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);

        //search listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //when user presses the search icon
                //if search query isnt empy then search users input
                if (!TextUtils.isEmpty(s.trim())) {
                    //if search contains text
                    searchGroupChatsList(s);
                } else {
                    //if search is empty, get all users
                    loadGroupChatsList();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //when the user enters on the keyboard
                //if search query isnt empy then search users input
                if (!TextUtils.isEmpty(s.trim())) {
                    //if search contains text
                    searchGroupChatsList(s);
                } else {
                    //if search is empty, get all users
                    loadGroupChatsList();
                }
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    //Handle menu item clicks
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //get item id
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            firebaseAuth.signOut();
            checkUserStatus();
        } else if (id == R.id.action_create_group) {
            startActivity(new Intent(getActivity(), CreateGroupChatActivity.class));

        }
        return super.onOptionsItemSelected(item);
    }

    private void checkUserStatus() {
        //get current user
        FirebaseUser user = firebaseAuth.getCurrentUser();

            if (user == null) {
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            }
        }

}