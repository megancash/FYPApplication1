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

import com.example.fypapplication1.CardOptions.LinkOption.MoodleLoginActivity;
import com.example.fypapplication1.CardOptions.TransportOptions.AungierStreetActivity;
import com.example.fypapplication1.CardOptions.TransportOptions.BlanchardstownActivity;
import com.example.fypapplication1.CardOptions.TransportOptions.BoltonStreetActivity;
import com.example.fypapplication1.CardOptions.TransportOptions.GrangegormanActivity;
import com.example.fypapplication1.CardOptions.TransportOptions.TallaghtActivity;
import com.example.fypapplication1.R;

public class LinksActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_links);

        //Action bar and its title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Additional Links");
        //Enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        CardView option1=findViewById(R.id.Option1);
        CardView option2=findViewById(R.id.Option2);
        CardView option3=findViewById(R.id.Option3);
        CardView option4=findViewById(R.id.Option4);
        CardView option5=findViewById(R.id.Option5);
        CardView option6=findViewById(R.id.Option6);

        option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://brightspace.tudublin.ie/d2l/home";
                Intent i = new Intent (Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LinksActivity.this, MoodleLoginActivity.class));
            }
        });
        option3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url ="https://www.tudublin.ie/connect/technology-services/wi-fi/";
                Intent i = new Intent (Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        option4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://www.myhome.ie/";
                Intent i = new Intent (Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        option5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://timetables.tudublin.ie/";
                Intent i = new Intent (Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        option6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://studentapps.dit.ie/BAN8L1/twbkwbis.P_WWWLogin";
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