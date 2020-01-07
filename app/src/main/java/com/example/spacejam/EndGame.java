package com.example.spacejam;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;


public class EndGame extends AppCompatActivity {

    private final String Scores = "scores";
    private static final String Lat = "lat";
    private static final String Lng = "lng";
    FragmentManager scoresFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamover);
        Intent intent = getIntent();
        String score = intent.getStringExtra(Scores);
        String lat = intent.getStringExtra(Lat);
        String lng = intent.getStringExtra(Lng);
        Intent scoreIntent = new Intent(EndGame.this, ScoresFragment.class);
        scoreIntent.putExtra(Scores, score);
        scoreIntent.putExtra(Lat, lat);
        scoreIntent.putExtra(Lng, lng);

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, Login.class));
        finish();
        return;
    }

}

