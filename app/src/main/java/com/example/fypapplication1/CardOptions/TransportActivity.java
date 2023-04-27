package com.example.fypapplication1.CardOptions;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.fypapplication1.CardOptions.TransportOptions.AungierStreetActivity;
import com.example.fypapplication1.CardOptions.TransportOptions.BlanchardstownActivity;
import com.example.fypapplication1.CardOptions.TransportOptions.BoltonStreetActivity;
import com.example.fypapplication1.CardOptions.TransportOptions.GrangegormanActivity;
import com.example.fypapplication1.CardOptions.TransportOptions.TallaghtActivity;
import com.example.fypapplication1.R;

public class TransportActivity extends AppCompatActivity {

    //Initialising Variables
    Button GrangegormanButton, AungierStreetButton, BoltonStreetButton, BlanchardstownButton, TallaghtButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport);
        //Action bar and its title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(" ");
        //Enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        GrangegormanButton=findViewById(R.id.GrangegormanButton);
        AungierStreetButton=findViewById(R.id.AungierStreetButton);
        BoltonStreetButton=findViewById(R.id.BoltonStreetButton);
        BlanchardstownButton=findViewById(R.id.BlanchardstownButton);
        TallaghtButton=findViewById(R.id.TallaghtButton);

        GrangegormanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TransportActivity.this, GrangegormanActivity.class));
            }
        });
        AungierStreetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TransportActivity.this, AungierStreetActivity.class));
            }
        });
        BoltonStreetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TransportActivity.this, BoltonStreetActivity.class));
            }
        });
        BlanchardstownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TransportActivity.this, BlanchardstownActivity.class));
            }
        });
        TallaghtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TransportActivity.this, TallaghtActivity.class));
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
