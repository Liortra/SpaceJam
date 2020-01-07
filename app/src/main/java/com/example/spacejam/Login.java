package com.example.spacejam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;


public class Login extends AppCompatActivity implements View.OnClickListener {

    private final String CHECKED_RADIO_BUTTON = "Checked radio button";
    private final String ERROR_MSG = "Unvalid Username";
    private final String UNRECOGNIZE_CLICK_MSG = "Unrecognize Click";
    private final String USER_NAME = "Username";
    private String usernameInput;
    private TextInputLayout nameOfPlayer;
    private boolean volumeMute;
    private MediaPlayer loginSong;
    private RadioGroup radioG;
    private Button configurations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        volumeMute = false;
        // Play Music
        loginSong = MediaPlayer.create(getApplicationContext(), R.raw.gameon);
        loginSong.setLooping(true);
        //loginSong.start();

        nameOfPlayer = (TextInputLayout) findViewById(R.id.nameOfPlayer);
        usernameInput = "";
        radioG = (RadioGroup)findViewById(R.id.radioGroup);

        configurations = (Button) findViewById(R.id.configuration);
        configurations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonShowPopupWindowClick(view);
            }
        });

        findViewById(R.id.btn_startGame).setOnClickListener(this);
        findViewById(R.id.volume).setOnClickListener(this);
        findViewById(R.id.exit).setOnClickListener(this);
    }

    public void onButtonShowPopupWindowClick(View view) {

    }

    public void clickToPlay() {
        if (validateUsername()) {
            Intent gameActivityIntent = new Intent(Login.this, Game.class);
            gameActivityIntent.putExtra(USER_NAME, nameOfPlayer.getEditText().getText().toString().trim());
            gameActivityIntent.putExtra(CHECKED_RADIO_BUTTON, ((RadioButton) findViewById(radioG.getCheckedRadioButtonId()))
                    .getText().toString());
            startActivity(gameActivityIntent);
        } else
            Toast.makeText(this, ERROR_MSG, Toast.LENGTH_SHORT).show();
    }

    private boolean validateUsername() {
        usernameInput = "";
        usernameInput += nameOfPlayer.getEditText().getText().toString().trim();
        if (usernameInput.isEmpty()) {
            nameOfPlayer.setError("Field can't be empty");
            return false;
        } else if (usernameInput.length() > 11) {
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
