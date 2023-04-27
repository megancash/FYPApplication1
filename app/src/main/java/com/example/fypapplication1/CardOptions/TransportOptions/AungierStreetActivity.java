package com.example.fypapplication1.CardOptions.TransportOptions;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.fypapplication1.R;

import java.util.ArrayList;

public class AungierStreetActivity extends AppCompatActivity {

    EditText etUserLocation, etUserDestination;
    Button btTrack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aungier_street);

        //Action bar and its title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(" ");
        //Enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        //Initialise variables
        etUserLocation = findViewById(R.id.et_userlocation);
        etUserDestination = findViewById(R.id.et_userdestination);
        btTrack = findViewById(R.id.bt_track);
        ImageSlider imageSlider = findViewById(R.id.imageSlider);
        ArrayList<SlideModel> slideModels = new ArrayList<>();

        slideModels.add(new SlideModel(R.drawable.aungier1, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.aungier2, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.aungier3, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.aungier4, ScaleTypes.FIT));

        imageSlider.setImageList(slideModels, ScaleTypes.FIT);

        btTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get value from edittext
                String userLocation = etUserLocation.getText().toString().trim();
                String userDestination = etUserDestination.getText().toString().trim();

                //check condition
                if (userLocation.equals("") && userDestination.equals("")) {
                    //when values are blank
                    Toast.makeText(AungierStreetActivity.this, "Enter both location", Toast.LENGTH_SHORT).show();
                }else {
                    //if both values are full display the track
                    DisplayTrack(userLocation, userDestination);
                }
            }
        });
    }

    private void DisplayTrack(String userLocation, String userDestination) {
        //If users android device does not contain google maps, redirect used to android play store
        try {
            //if google maps is installed, go to:
            Uri uri = Uri.parse("https://www.google.co.in/maps/dir/" + userLocation + "/" + userDestination);
            //Initialise intent with action view
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            //Set package
            intent.setPackage("com.google.android.apps.maps");
            //Set flag
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //Start activity
            startActivity(intent);
        }catch (ActivityNotFoundException e) {
            //If google maps is not installed
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");
            //Initialise intent with action view
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            //Set flag
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //Start activity
            startActivity(intent);
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}