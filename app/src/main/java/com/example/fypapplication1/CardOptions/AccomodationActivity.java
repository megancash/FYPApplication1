//Student Name: Megan Cash
//Student Number: C19317723
package com.example.fypapplication1.CardOptions;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.example.fypapplication1.R;

public class AccomodationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accomodation);

        //Action bar and its title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(" ");
        //Enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        CardView accomodationOption1 = findViewById(R.id.accomodationOption1);
        CardView accomodationOption2 = findViewById(R.id.accomodationOption2);
        CardView accomodationOption3 = findViewById(R.id.accomodationOption3);
        CardView accomodationOption4 = findViewById(R.id.accomodationOption4);
        CardView accomodationOption5 = findViewById(R.id.accomodationOption5);
        CardView accomodationOption6 = findViewById(R.id.accomodationOption6);

        //MyHome.ie
        accomodationOption1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.myhome.ie/";
                Intent i = new Intent (Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        //Homestay.com
        accomodationOption2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.homestay.com/";
                Intent i = new Intent (Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        //Daft.ie
        accomodationOption3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.daft.ie/";
                Intent i = new Intent (Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        //TuDublin StudentPad
        accomodationOption4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.tudublinstudentpad.ie/Accommodation";
                Intent i = new Intent (Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        //Yugo
        accomodationOption5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://yugo.com/en-gb";
                Intent i = new Intent (Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        //Mezzino
        accomodationOption6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.mezzino.com/";
                Intent i = new Intent (Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}