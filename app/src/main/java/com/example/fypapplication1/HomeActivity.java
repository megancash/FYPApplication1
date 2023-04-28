//Student Name: Megan Cash
//Student Number: C19317723
package com.example.fypapplication1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.fypapplication1.Fragments.FriendsFragment;
import com.example.fypapplication1.Fragments.GroupChatsFragment;
import com.example.fypapplication1.Fragments.HomeFragment;
import com.example.fypapplication1.Fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {

    //Firebase Auth
    FirebaseAuth firebaseAuth;


    ActionBar actionBar;
    //views

    BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Action bar and its title
        actionBar = getSupportActionBar();
        actionBar.setTitle("Profile");

        //init
        firebaseAuth = FirebaseAuth.getInstance();

        //bottom navigation
        navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(selectedListener);

        //Default fragment transaction
        actionBar.setTitle("Home");
        HomeFragment fragment1 = new HomeFragment();
        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
        ft1.replace(R.id.content, fragment1,"");
        ft1.commit();

    }
    private BottomNavigationView.OnNavigationItemSelectedListener selectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        //Home fragment
                        case R.id.nav_home:
                            actionBar.setTitle("Home");
                            HomeFragment fragment1 = new HomeFragment();
                            FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
                            ft1.replace(R.id.content, fragment1, "");
                            ft1.commit();
                            return true;
                        //Profile fragment
                        case R.id.nav_profile:
                            actionBar.setTitle("Profile");
                            ProfileFragment fragment2 = new ProfileFragment();
                            FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                            ft2.replace(R.id.content, fragment2, "");
                            ft2.commit();
                            return true;
                        //User Fragment
                        case R.id.nav_users:
                            actionBar.setTitle("Friends");
                            FriendsFragment fragment3 = new FriendsFragment();
                            FragmentTransaction ft3 = getSupportFragmentManager().beginTransaction();
                            ft3.replace(R.id.content, fragment3, "");
                            ft3.commit();
                            return true;
                        case R.id.nav_groupchat:
                            //showMoreOptions();
                            //Group chats
                            actionBar.setTitle("Group chats");
                            GroupChatsFragment fragment6 = new GroupChatsFragment();
                            FragmentTransaction ft6 = getSupportFragmentManager().beginTransaction();
                            ft6.replace(R.id.content, fragment6, "");
                            ft6.commit();
                            return true;
                    }
                    return false;
                };
            };


    private void checkUserStatus() {
        //to get the  current user
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            //User is signed in stay here
            //mProfileTv.setText(user.getEmail());
        } else {
            //user not signed in, go to main activity
            startActivity(new Intent(HomeActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    @Override
    protected void onStart() {
        //check on start of the app
        checkUserStatus();
        super.onStart();
    }
}