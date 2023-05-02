//Student Name: Megan Cash
//Student Number: C19317723
package com.example.fypapplication1.CardOptions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fypapplication1.CardOptions.CalendarOption.AcademicCalendarActivity;
import com.example.fypapplication1.CardOptions.TransportOptions.GrangegormanActivity;
import com.example.fypapplication1.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CalendarActivity extends AppCompatActivity {

    //Firebase
    private DatabaseReference dbReference;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    Button academicCalendarButton, addButton;
    private CalendarView calendarView;
    private TextInputEditText userInput, title, location, description;
    private String inputtedDate;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        //Action bar and its title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Calendar");
        //Enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        academicCalendarButton = findViewById(R.id.academicCalendarButton);
        calendarView = findViewById(R.id.calendarView);
        userInput = findViewById(R.id.userInput);
        //title = findViewById(R.id.title);
        //location = findViewById(R.id.location);
        //description = findViewById(R.id.description);
        addButton = findViewById(R.id.addButton);

        /*addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!title.getText().toString().isEmpty() && !location.getText().toString().isEmpty()
                        && !description.getText().toString().isEmpty()) {

                    Intent intent = new Intent(Intent.ACTION_INSERT);
                    intent.setData(CalendarContract.Events.CONTENT_URI);
                    intent.putExtra(CalendarContract.Events.TITLE, title.getText().toString());
                    intent.putExtra(CalendarContract.Events.EVENT_LOCATION, location.getText().toString());
                    intent.putExtra(CalendarContract.Events.DESCRIPTION, description.getText().toString());
                    intent.putExtra(CalendarContract.Events.ALL_DAY, true);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    } else {
                        Toast.makeText(CalendarActivity.this, "Error! There is no calendar application on your device to add to", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(CalendarActivity.this, "Please add an input to each field", Toast.LENGTH_SHORT).show();
                }
            }
        });
*/
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        id = mUser.getUid();

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
                inputtedDate = Integer.toString(year) + Integer.toString(month +1) + Integer.toString(dayOfMonth);
                whenCalendarIsClicked();
            }
        });

        dbReference = FirebaseDatabase.getInstance().getReference("Calendar").child(id);
    }

    private void whenCalendarIsClicked() {
        dbReference.child(inputtedDate).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                //If this is not null
                if (datasnapshot.getValue() != null) {
                    userInput.setText(datasnapshot.getValue().toString());
                }
                else {
                  userInput.setText("no value");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseerror) {

            }
        });

    }

    public void saveDateButton(View v){
        dbReference.child(inputtedDate).setValue(userInput.getText().toString());
        Toast.makeText(this, "Successfully added to the calendar!", Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}