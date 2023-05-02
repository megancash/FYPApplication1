//Student Name: Megan Cash
//Student Number: C19317723
package com.example.fypapplication1.CardOptions.LinkOption;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.fypapplication1.R;

public class MoodleLoginActivity extends AppCompatActivity {

    //Initialising Variables
    Button MoodleTallaghtLogin, MoodleBlanchardstownLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moodle_login);
        //Action bar and its title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(" ");
        //Enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        MoodleTallaghtLogin=findViewById(R.id.MoodleTallaghtButton);
        MoodleBlanchardstownLogin=findViewById(R.id.MoodleBlanchButton);

    MoodleTallaghtLogin.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String url = "https://elearning-ta.tudublin.ie/login/index.php";
            Intent i = new Intent (Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }
    });
    MoodleBlanchardstownLogin.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String url = "https://vle-bn.tudublin.ie/login/index.php";
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