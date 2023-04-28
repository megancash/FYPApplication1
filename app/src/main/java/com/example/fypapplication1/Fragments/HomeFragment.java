//Student Name: Megan Cash
//Student Number: C19317723
package com.example.fypapplication1.Fragments;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fypapplication1.CardOptions.AccomodationActivity;
import com.example.fypapplication1.CardOptions.CalendarActivity;
import com.example.fypapplication1.CardOptions.LinksActivity;
import com.example.fypapplication1.CardOptions.TimetableActivity;
import com.example.fypapplication1.CardOptions.ToDoListActivity;
import com.example.fypapplication1.CardOptions.TransportActivity;
import com.example.fypapplication1.MainActivity;
import com.example.fypapplication1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class HomeFragment extends Fragment {

    //Firebase
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference storageReference;

    //Views from XML
    TextView nameTv;


    //Empty constructor
    public HomeFragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        //Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        storageReference = getInstance().getReference(); //Firebase storage reference

        //Find views by their ids using findViewById
        CardView friendsOption = view.findViewById(R.id.friendsOption);
        CardView groupchatOption = view.findViewById(R.id.groupchatOption);
        CardView accomodationOption = view.findViewById(R.id.accomodationOption);
        CardView toDoOption = view.findViewById(R.id.ToDoOption);
        CardView timetableOption = view.findViewById(R.id.timetableOption);
        CardView transportOption= view.findViewById(R.id.transportOption);
        CardView calendarOption = view.findViewById(R.id.calendarOption);
        CardView additionalLinksOption = view.findViewById(R.id.additionalLinksOption);
        nameTv = view.findViewById(R.id.userName);

        //Friends and chat Option
        friendsOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FriendsFragment friendsFragment = new FriendsFragment();
                FragmentManager fm = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.content, friendsFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });
        //Groupchat Option
        groupchatOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupChatsFragment groupchatsFragment = new GroupChatsFragment();
                FragmentManager fm = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.content, groupchatsFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        //Transport Option
       transportOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TransportActivity.class);
                startActivity(intent);
            }
        });
        //Accomodation Option
        accomodationOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent = new Intent(getActivity(), AccomodationActivity.class);
              startActivity(intent);
            }
        });
        //Calendar Option
        calendarOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CalendarActivity.class);
                startActivity(intent);
            }
        });
        //Timetable Option
        timetableOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TimetableActivity.class);
                startActivity(intent);
            }
        });
        //Messenger Option
        additionalLinksOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LinksActivity.class);
                startActivity(intent);
            }
        });
        //TodoList Option
        toDoOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ToDoListActivity.class);
                startActivity(intent);
            }
        });

        //Get user info
        Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    //get data
                    String name = ""+ ds.child("name").getValue();

                    //set data
                    nameTv.setText(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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

    //Inflate the options home menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home_menu, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }
    //To Handle menu item clicks
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