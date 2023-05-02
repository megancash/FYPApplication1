//Student Name: Megan Cash
//Student Number: C19317723
package com.example.fypapplication1.GroupEvent;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.Toast;

import com.example.fypapplication1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/*
public class ViewEvent extends AppCompatActivity {

    //Firebase
    private DatabaseReference dbReference;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private String id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);

        //Action bar and its title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Meetup Event");
        //Enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        id = mUser.getUid();

        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);

        dbReference = FirebaseDatabase.getInstance().getReference("Events").child(id);

    }


    public void onEdit(View view) {
        Intent intent = new Intent(this, CreateEventActivity.class);
        intent.putExtra("event", eventViewModel.getEvent().getValue());
        startActivity(intent);
    }

    //Participant response
    public void ParticipantRespond(View view) {
        if (view == acceptInviteButton) {
            eventViewModel.acceptInvite();
        } else if (view == rejectInviteButton) {
            if (!eventViewModel.rejectInvite()) {
                finish();
            }
        }
    }

    //Add event to calender
    public void AddToCalendar(View view) {
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setData(CalendarContract.Events.CONTENT_URI);
        intent.putExtra(CalendarContract.Events.TITLE, eventViewModel.getEventNameValue());
        intent.putExtra(CalendarContract.Events.DESCRIPTION, eventViewModel.getEventDescriptionValue());
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, eventViewModel.getEventLocationValue());
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, eventViewModel.getTimeStamp());

        if (intent.resolveActivity(getPackageManager()) != null) {
            //To launch calendar application
            startActivity(intent);
        } else {
            Toast.makeText(ViewEvent.this, "Error! Could not launched Calendar application", Toast.LENGTH_SHORT).show();
        }
    }
}*/
