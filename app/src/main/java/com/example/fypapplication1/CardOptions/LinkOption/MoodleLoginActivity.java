package com.example.fypapplication1.CardOptions.LinkOption;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.fypapplication1.R;

public class MoodleLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moodle_login);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}