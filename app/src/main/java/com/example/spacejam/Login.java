package com.example.spacejam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Login extends AppCompatActivity {

    private EditText nameOfPlayer;
    private Button play;
    private Button exit;

    private final String bundleString = "player_name";
    Bundle bundle; // Used like a Pipe to transfer values between windows

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        getSupportActionBar().hide(); // Disappearing of the main bar

        nameOfPlayer = (EditText) findViewById(R.id.editText);
        play = (Button) findViewById(R.id.btn_startGame);
        exit = (Button) findViewById(R.id.exit);
        bundle = new Bundle();

        nameOfPlayer.addTextChangedListener(watcher);
    }

    // play and enable/disable button
    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String playerName = nameOfPlayer.getText().toString().trim();
            play.setEnabled(!playerName.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public void clickToPlay(View view) {
        Intent gameActivityIntent = new Intent(Login.this, Game.class);
        //Add your data to bundle
        bundle.putString(bundleString, nameOfPlayer.getText().toString());
        gameActivityIntent.putExtras(bundle);
        startActivity(gameActivityIntent);
    }


    //press End Game to finish the game and destroy the progress
    public void clickToExit(View v) {
        moveTaskToBack(true);
        System.exit(0);
    }
}
