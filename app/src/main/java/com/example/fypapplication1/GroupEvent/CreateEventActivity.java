//Student Name: Megan Cash
//Student Number: C19317723
package com.example.fypapplication1.GroupEvent;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fypapplication1.Models.Event;
import com.example.fypapplication1.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;


public class CreateEventActivity extends AppCompatActivity {

    private Event event;
    ScrollView EventScrollView;
    TextInputLayout EventName, EventDescription, Location, informationAddInputLayout ;
    TextInputEditText inputEventName, inputEventDescription,inputDateChosen, locationChosen, TimeChosen, textInputInformation;
    Button createButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        //Action bar and its title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Create Event");
        //Enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        inputEventName = findViewById(R.id.InputEventName);
        inputEventDescription = findViewById(R.id.inputEventDescription);
        locationChosen = findViewById(R.id.inputLocationChosen);
        inputDateChosen = findViewById(R.id.inputDateChosen);
        TimeChosen = findViewById(R.id.inputTimeChosen);
        textInputInformation = findViewById(R.id.textInputInformation);
        createButton =findViewById(R.id.createButton);
        EventScrollView =findViewById(R.id.EventScrollView);




        if (getIntent().hasExtra("event")) {
            event = getIntent().getParcelableExtra("event");
        } else {
            event = new Event();
            String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
            event.getOrganisersId().add(userId);
            event.getParticipantsId().add(userId);
        }

    }

    public void onDateTime(View view) {
        if (view == inputDateChosen) {
            handleDate();
        } else if (view == TimeChosen) {
            handleTime();
        }
    }


    public void onCreate(View view) {
        if (inputIsValid()) {

        }
    }

    private void handleDate() {
        // Get current date or set one chosen previously
        final Calendar calendar = Calendar.getInstance();
        if (event.getTimestamp() != null) {
            calendar.setTimeInMillis(event.getTimestamp());
        }
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    calendar.set(year, monthOfYear, dayOfMonth);
                    event.setTimestamp(calendar.getTimeInMillis());
                },
                currentYear, currentMonth, currentDay
        );
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
        inputDateChosen.setError(null);
    }

    private void handleTime() {
        // Get current time or set one chosen previously
        final Calendar c = Calendar.getInstance();
        if (event.getTimestamp() != null) {
            c.setTimeInMillis(event.getTimestamp());
        }
        int currentHour = c.get(Calendar.HOUR_OF_DAY);
        int currentMinute = c.get(Calendar.MINUTE);

        new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> {
                    c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    c.set(Calendar.MINUTE, minute);
                    event.setTimestamp(c.getTimeInMillis());
                },
                currentHour, currentMinute, true
        ).show();
    }




    private boolean inputIsValid() {
        validateEventName();
        validateDate();
        if (inputEventName.getError() != null || inputDateChosen.getError() != null) {
            return false;
        }

        return true;
    }
    private void validateDate() {
        if (inputDateChosen.getText().toString().trim().isEmpty()) {
            inputDateChosen.setError("Choose date for the event");
            EventScrollView.smoothScrollTo(0, 0);
        } else {
            inputDateChosen.setError(null);
        }
    }

    private void validateEventName() {
        if (inputEventName.getText().toString().trim().isEmpty()) {
            inputEventName.setError("Error! Please input a name for the event");
            EventScrollView.smoothScrollTo(0, 0);
        } else {
            inputEventName.setError(null);
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
