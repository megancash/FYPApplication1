package com.example.fypapplication1.CardOptions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;

import com.example.fypapplication1.CardOptions.CalendarOption.AcademicCalendarActivity;
import com.example.fypapplication1.CardOptions.TransportOptions.GrangegormanActivity;
import com.example.fypapplication1.R;

public class CalendarActivity extends AppCompatActivity {

    Button academicCalendarButton;
    private CalendarView calendarView;
    private EditText userInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        //Action bar and its title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(" ");
        //Enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        academicCalendarButton = findViewById(R.id.academicCalendarButton);
        calendarView = findViewById(R.id.calendarView);
        userInput = findViewById(R.id.userInput);

        //To allow user to view academic calendar when button is clicked.
        academicCalendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CalendarActivity.this, AcademicCalendarActivity.class));
            }
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

            }
        });
    }

    public void saveDateButton(View v){

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}