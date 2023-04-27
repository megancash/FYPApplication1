package com.example.fypapplication1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.fypapplication1.Fragments.ChatListFragment;
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

              /*private void  showMoreOptions() {
                  PopupMenu popupMenu = new PopupMenu(HomeActivity.this, navigationView, Gravity.END);
                  popupMenu.getMenu().add(Menu.NONE, 0,0, "Notifications");
                  popupMenu.getMenu().add(Menu.NONE, 1,0, "Groupchats");

                  //menu clicks
                  popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                      @Override
                      public boolean onMenuItemClick(MenuItem item) {
                          int id = item.getItemId();
                          if (id==0) {
                              //Notifications
                          }else if (id==1){
                              //Group chats
                              actionBar.setTitle("Group chats");
                              GroupChatsFragment fragment6 = new GroupChatsFragment();
                              FragmentTransaction ft6 = getSupportFragmentManager().beginTransaction();
                              ft6.replace(R.id.content, fragment6, "");
                              ft6.commit();
                          }
                          return false;*/
                  //    }
                 // });

            //  }
            };


    private void checkUserStatus() {
        //get current user
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