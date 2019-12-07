package com.example.spacejam;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class End extends AppCompatActivity {
    private ListView list_view;
    private ArrayList<Score> list;
    private EditText et;

    SharedPreferences sp;
    Gson gson;
    String json;
    Type type;

    TextView t1_name;
    TextView t1_score;
    TextView t2_name;
    TextView t2_score;
    TextView t3_name;
    TextView t3_score;
    TextView t4_name;
    TextView t4_score;
    TextView t5_name;
    TextView t5_score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.end);
        getSupportActionBar().hide(); // Disapiring of the main bar

        t1_name = (TextView) findViewById(R.id.name_01);
        t1_score = (TextView) findViewById(R.id.score_01);
        t2_name = (TextView) findViewById(R.id.name_02);
        t2_score = (TextView) findViewById(R.id.score_02);
        t3_name = (TextView) findViewById(R.id.name_03);
        t3_score = (TextView) findViewById(R.id.score_03);
        t4_name = (TextView) findViewById(R.id.name_04);
        t4_score = (TextView) findViewById(R.id.score_04);
        t5_name = (TextView) findViewById(R.id.name_05);
        t5_score = (TextView) findViewById(R.id.score_05);
        loadData();
        updateTopFive();
    }

    TableLayout table;
    TableRow tr;
    TextView tv;
    TableRow tr2;
    TextView tv2;
    private void updateTopFive() {
        if(list.size() > 0) {
            t1_name.setText(list.get(0).getPlayerName() + "");
            t1_score.setText(list.get(0).getPlayerScore() + "");
        }
        if(list.size() > 1) {
            t2_name.setText(list.get(1).getPlayerName() + "");
            t2_score.setText(list.get(1).getPlayerScore() + "");
        }
        if(list.size() > 2) {
            t3_name.setText(list.get(2).getPlayerName() + "");
            t3_score.setText(list.get(2).getPlayerScore() + "");
        }
        if(list.size() > 3) {
            t4_name.setText(list.get(3).getPlayerName() + "");
            t4_score.setText(list.get(3).getPlayerScore() + "");
        }
        if(list.size() > 4) {
            t5_name.setText(list.get(4).getPlayerName() + "");
            t5_score.setText(list.get(4).getPlayerScore() + "");
        }
    }

    //press End Game to finish the game and destroy the progress
    public void clickExit(View view) {
        finish();
        System.exit(0);
    }

    public void cliclToRestart(View view) {
        Intent gameActivityIntent = new Intent(End.this,Login.class);
        startActivity(gameActivityIntent);
    }

    private void loadData(){
        sp = getSharedPreferences("sp",MODE_PRIVATE);
        gson = new Gson();
        json = sp.getString("listOfScore",null);
        type = new TypeToken<ArrayList<Score>>(){}.getType();
        list = gson.fromJson(json,type);
        if (list == null){
            list = new ArrayList<>();
        }
    }
}

