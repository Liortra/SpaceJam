package com.example.spacejam;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import com.google.gson.Gson;


public class Game extends AppCompatActivity {

    private static final String USER_NAME = "Username";
    private int NUM_OF_COL = 3;
    private View hoop;
    private View enemy1;
    private View enemy2;
    private View enemy3;
    private ImageView life_status1;
    private ImageView life_status2;
    private ImageView life_status3;
    private int life = 3;
    private ValueAnimator animation1;
    private ValueAnimator animation2;
    private ValueAnimator animation3;
    private int screenHeight;
    private int score = 0;
    private TextView scoreView;

    Random rand;
    Intent intent;
    //Intent intent = getIntent();
    //Bundle bundle;
    //Bundle b;
    Score s;
    static ArrayList<Score> list = new ArrayList<Score>();
    String name;
    DisplayMetrics mat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        getSupportActionBar().hide(); // Disapiring of the main bar

        hoop = (View) findViewById(R.id.hoop);
        enemy1 = (View) findViewById(R.id.enemy1);
        enemy2 = (View) findViewById(R.id.enemy2);
        enemy3 = (View) findViewById(R.id.enemy3);
        life_status1 = (ImageView) findViewById(R.id.life01);
        life_status2 = (ImageView) findViewById(R.id.life02);
        life_status3 = (ImageView) findViewById(R.id.life03);
        scoreView=findViewById(R.id.score_view);
//        bundle = getIntent().getExtras();
//        name = bundle.getString("player_name");
        name = getIntent().getStringExtra(""+USER_NAME);
        s = new Score(name);
        //b = new Bundle();
        scoreView.setText("SCORE: " + 0); //initial score
        rand = new Random();
        // Start positions
        enemy1.setTranslationY(-150);
        enemy2.setTranslationY(-150);
        enemy3.setTranslationY(-150);

        //get screenHeight
        mat = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mat);
        screenHeight = mat.heightPixels;

        //Create Value Animation
        animation1 = ValueAnimator.ofInt(-150,screenHeight + 400);
        animation1.setDuration(rand.nextInt(3001) + 2000).setRepeatCount(Animation.INFINITE);
        animation1.setStartDelay(rand.nextInt(3500) + 500);
        animation1.start();
        animation1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator updatedAnimation) {
                int animatedValue = (int)updatedAnimation.getAnimatedValue();
                enemy1.setTranslationY(animatedValue);
                if(boom(enemy1,hoop)) {
                    hitCheck();
                    enemy1.setY(-150);
                    updatedAnimation.start();
                }
                addScore(enemy1,updatedAnimation);
            }
        });
        animation2 = ValueAnimator.ofInt(-150,screenHeight + 400);
        animation2.setDuration(rand.nextInt(3001) + 2000).setRepeatCount(Animation.INFINITE);
        animation2.setStartDelay(rand.nextInt(3500) + 500);
        animation2.start();
        animation2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator updatedAnimation) {

                int animatedValue = (int)updatedAnimation.getAnimatedValue();

                enemy2.setTranslationY(animatedValue);
                if(boom(enemy2,hoop)) {
                    hitCheck();
                    enemy2.setY(-150);
                    updatedAnimation.start();
                }
                addScore(enemy2,updatedAnimation);
            }
        });

        animation3 = ValueAnimator.ofInt(-150,screenHeight + 400);
        animation3.setDuration(rand.nextInt(3001) + 2000).setRepeatCount(Animation.INFINITE);
        animation3.setStartDelay(rand.nextInt(3500) + 500);
        animation3.start();
        animation3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator updatedAnimation) {

                int animatedValue = (int)updatedAnimation.getAnimatedValue();
                enemy3.setTranslationY(animatedValue);
                if(boom(enemy3,hoop)) {
                    hitCheck();
                    enemy3.setY(-150);
                    updatedAnimation.start();
                }
                addScore(enemy3,updatedAnimation);
            }
        });
    }

    private synchronized void addScore(View enemy,ValueAnimator updatedAnimation) {
        if (enemy.getY() > hoop.getY() + hoop.getHeight()) {
            score += 15;
            scoreView.setText("SCORE: " + score);
            updatedAnimation.start();
        }
    }

    private void pause() {
        animation1.pause();
        animation2.pause();
        animation3.pause();
    }

    private void resume(){
        animation1.resume();
        animation2.resume();
        animation3.resume();
    }

    private synchronized  void hitCheck() {
        this.life--;
        if (life == 0) {
            life_status1.setVisibility(View.INVISIBLE);
            pause();
            s.setPlayerName(name);
            s.setPlayerScore(score);
            Intent gameActivityIntent = new Intent(Game.this, End.class);
            list.add(s); // adding new user to the list
            Collections.sort(list, new Comparator<Score>() {
                @Override
                public int compare(Score o1, Score o2) {
                    return o2.getPlayerScore() - o1.getPlayerScore();
                }
            });
            saveData();
            startActivity(gameActivityIntent);
        } else if (life == 1)
            life_status2.setVisibility(View.INVISIBLE);
        else
            life_status3.setVisibility(View.INVISIBLE);
        return;
    }
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    Gson gson;
    String json;

    private void saveData() {
        sp = getSharedPreferences("sp",MODE_PRIVATE);
        editor = sp.edit();
        gson = new Gson();
        json = gson.toJson(list);
        editor.putString("listOfScore",json);
        editor.apply();
    }

    private boolean boom(View e,View p) {
        int[] enemy_locate = new int[2];
        int[] player_locate = new int[2];
        // Computes the coordinates of this view on the screen
        e.getLocationOnScreen(enemy_locate);
        p.getLocationOnScreen(player_locate);
        Rect rect1 = new Rect(enemy_locate[0], enemy_locate[1], (int) (enemy_locate[0] + e.getWidth()), (int) (enemy_locate[1] + e.getHeight()));
        Rect rect2 = new Rect(player_locate[0], player_locate[1], (int) (player_locate[0] + p.getWidth()), (int) (player_locate[1] + p.getHeight()));
        return Rect.intersects(rect1, rect2);
    }

    public void clickToPause(View view) {
        pause();
        findViewById(R.id.move_left).setVisibility(View.INVISIBLE);
        findViewById(R.id.move_right).setVisibility(View.INVISIBLE);
    }

    public void clickToResume(View view) {
        resume();
        findViewById(R.id.move_left).setVisibility(View.VISIBLE);
        findViewById(R.id.move_right).setVisibility(View.VISIBLE);
    }

    public void clickToStop(View view) {
        pause();
        // Opening new window
        Intent gameActivityIntent = new Intent(Game.this, End.class);
        s.setPlayerName(name);
        s.setPlayerScore(score);
        list.add(s);
        Collections.sort(list, new Comparator<Score>() {
            @Override
            public int compare(Score o1, Score o2) {
                return o2.getPlayerScore() - o1.getPlayerScore();
            }
        });
        saveData();
        gameActivityIntent.putExtra("score", score);
        startActivity(gameActivityIntent);
    }

    public void clickToMoveRight(View view) {
        if (hoop.getX() < (getResources().getDisplayMetrics().widthPixels * 2 / NUM_OF_COL))
            hoop.setX(hoop.getX() + getResources().getDisplayMetrics().widthPixels / NUM_OF_COL);
    }

    public void clickToMoveLeft(View view) {
        if (hoop.getX() >= (getResources().getDisplayMetrics().widthPixels * 1 / NUM_OF_COL))
            hoop.setX(hoop.getX() - getResources().getDisplayMetrics().widthPixels / NUM_OF_COL);
    }
}
