package com.example.fypapplication1.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.fypapplication1.MainActivity;
import com.example.fypapplication1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeFragment extends Fragment {

    FirebaseAuth firebaseAuth;


    //Empty constructor
    public HomeFragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        return view;
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
        return super.onOptionsItemSelected(item);
    }
}