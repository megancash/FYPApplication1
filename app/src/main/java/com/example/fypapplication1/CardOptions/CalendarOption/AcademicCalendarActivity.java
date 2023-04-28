//Student Name: Megan Cash
//Student Number: C19317723
package com.example.fypapplication1.CardOptions.CalendarOption;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.fypapplication1.R;

public class AcademicCalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_academic_calendar);
        //Action bar and its title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Academic Calendar 2022-2023");
        //Enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}