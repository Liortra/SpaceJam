package com.example.spacejam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private final String ERROR_MSG = "Unvalid Username";
    private final String UNRECOGNIZE_CLICK_MSG = "Unrecognize Click";
    private final String USER_NAME = "Username";
    String usernameInput = "";
    private TextInputLayout nameOfPlayer;
    //private Button play;
    //private ImageView volume;
    private boolean volumeMute;
    private MediaPlayer loginSong;
    private final String bundleString = "player_name";
    Bundle bundle; // Used like a Pipe to transfer values between windows

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        getSupportActionBar().hide(); // Disappearing of the main bar

        volumeMute = false;
        // Play Music
        loginSong = MediaPlayer.create(getApplicationContext(), R.raw.gameon);
        loginSong.setLooping(true);
        loginSong.start();

        nameOfPlayer = (TextInputLayout) findViewById(R.id.nameOfPlayer);
        bundle = new Bundle();
        findViewById(R.id.btn_startGame).setOnClickListener(this);
        findViewById(R.id.volume).setOnClickListener(this);
        findViewById(R.id.exit).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void clickToPlay() {
        if (validateUsername()) {
            Intent gameActivityIntent = new Intent(Login.this, Game.class);
            //Add your data to bundle
            //bundle.putString(bundleString, nameOfPlayer.getEditText().getText().toString());
            //gameActivityIntent.putExtras(bundle);
            //gameActivityIntent.putExtra()
            gameActivityIntent.putExtra(""+USER_NAME ,nameOfPlayer.getEditText().getText().toString().trim());
            startActivity(gameActivityIntent);
        } else
            Toast.makeText(this, ERROR_MSG, Toast.LENGTH_SHORT).show();
    }

    private boolean validateUsername() {
        usernameInput = "";
        usernameInput = nameOfPlayer.getEditText().getText().toString().trim();
        if (usernameInput.isEmpty()) {
            nameOfPlayer.setError("Field can't be empty");
            return false;
        } else if (usernameInput.length() > 20) {
            nameOfPlayer.setError("Username too long");
            return false;
        } else {
            nameOfPlayer.setError(null);
            return true;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_startGame:
                clickToPlay();
                break;
            case R.id.volume:
                if (!volumeMute) {
                    volumeMute = true;
                    v.setBackgroundResource(R.drawable.volume_off);
                    loginSong.pause();
                } else {
                    volumeMute = false;
                    v.setBackgroundResource(R.drawable.volume_on);
                    loginSong.start();
                }
                break;
            case R.id.exit://press End Game to finish the game and destroy the progress
                moveTaskToBack(true);
                System.exit(0);
                finish();
                break;
            default:
                Toast.makeText(this, UNRECOGNIZE_CLICK_MSG, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
