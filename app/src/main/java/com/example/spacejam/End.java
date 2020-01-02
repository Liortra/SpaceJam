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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.end);
        getSupportActionBar().hide(); // Disapiring of the main bar

        //updateTopFive();

        View showScores = findViewById(R.id.high_score);
        showScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        View restart = findViewById(R.id.restart);
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gameActivityIntent = new Intent(End.this,Login.class);
                startActivity(gameActivityIntent);
            }
        });

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

