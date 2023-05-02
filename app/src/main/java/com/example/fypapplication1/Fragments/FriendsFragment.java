//Student Name: Megan Cash
//Student Number: C19317723
package com.example.fypapplication1.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.fypapplication1.Adapters.AdapterUsers;
import com.example.fypapplication1.CreateGroupChatActivity;
import com.example.fypapplication1.HomeActivity;
import com.example.fypapplication1.MainActivity;
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
import java.util.Locale;


public class FriendsFragment extends Fragment {

    //firebase auth
    FirebaseAuth firebaseAuth;

    RecyclerView recyclerView;
    AdapterUsers adapterUsers;
    List<User> userList;

    //Empty constructor
    public FriendsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null ) {
                actionBar.setTitle("Friends");
            }
        firebaseAuth = FirebaseAuth.getInstance();

        recyclerView = view.findViewById(R.id.friends_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //init user list
        userList = new ArrayList<>();

        //get all users
        getAllUsers();

        return view;
    }
    private void getAllUsers() {
        //To get current user
        final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        //Get path of database named 'users' containing users info
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        //get data from the path
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    User modelUser = ds.getValue(User.class);
                    modelUser.setUid(ds.getKey());

                    if (!modelUser.getUid().equals(fUser.getUid())) {
                        userList.add(modelUser);
                    }
                    //adapter
                    adapterUsers = new AdapterUsers(getActivity(), userList);
                    //set adapter to recycler view
                    recyclerView.setAdapter(adapterUsers);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void searchUsers(String query) {
        //To get current user
        final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        //Get path of database named 'users' containing users info
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        //get data from the path
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    User modelUser = ds.getValue(User.class);
                    modelUser.setUid(ds.getKey());

                    //get all search users except for the current user
                    if (!modelUser.getUid().equals(fUser.getUid())) {

                        if (modelUser.getName().toLowerCase().contains(query.toLowerCase()) ||
                                modelUser.getEmail().toLowerCase().contains(query.toLowerCase())){
                            userList.add(modelUser);
                        }
                    }
                    //adapter
                    adapterUsers = new AdapterUsers(getActivity(), userList);
                    //refresh adapter
                    adapterUsers.notifyDataSetChanged();

                    //set adapter to recycler view
                    recyclerView.setAdapter(adapterUsers);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void checkUserStatus() {
        //get current user
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            //User is signed in stay here
            //mProfileTv.setText(user.getEmail());
        } else {
            //user not signed in, go to main activity
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        }
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

        menu.findItem(R.id.action_add_participant).setVisible(false);
        menu.findItem(R.id.action_create_group).setVisible(false);
        menu.findItem(R.id.action_create_event).setVisible(false);
        menu.findItem(R.id.action_information).setVisible(false);


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
                    searchUsers(s);
                }else {
                    //if search is empty, get all users
                    getAllUsers();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //when the user enters on the keyboard
                //if search query isnt empy then search users input
                if (!TextUtils.isEmpty(s.trim())) {
                    //if search contains text
                    searchUsers(s);
                }else {
                    //if search is empty, get all users
                    getAllUsers();
                }
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    //Handle menu item clicks
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //get item id
        int id = item.getItemId();
        if (id == R.id.action_logout){
            firebaseAuth.signOut();
            checkUserStatus();
        }
        else if (id==R.id.action_create_group) {
            startActivity(new Intent(getActivity(), CreateGroupChatActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}