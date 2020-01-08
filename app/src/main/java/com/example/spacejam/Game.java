package com.example.spacejam;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Random;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class Game extends AppCompatActivity implements View.OnClickListener, SensorEventListener {
    private final String CHECKED_RADIO_BUTTON = "Checked radio button";
    private static final String USER_NAME = "Username";
    private static final String Scores = "scores";
    private static final String Player = "player";
    private static final String Lat = "lat";
    private static final String Lng = "lng";
    private static final String basketball = "basketball";
    private static final String monsters = "monsters";

    private int FULL_LIFES = 3;         // life of the player
    // Images of basketball life
    private ImageView life_status1;
    private ImageView life_status2;
    private ImageView life_status3;
    private ImageView jordan;           // Image of player
    private ImageView[] monsterArr;     // Array of all monsters and bonus
    private TextView scoreView;         // TextView that shows player's score

    private Button left;
    private Button right;
    private static int lastDelay = 0;
    private String finalScore;          // score to transfer to next activity
    private double lat;                 // for GPS
    private double lng;                 // for GPS
    int columns;                        // Saves the no' of columns that the user choose
    String name;                        // Usename of player
    boolean basketball_bonus_selected;
    private FrameLayout frame;
    private LinearLayout parentLinearLayout;
    // Array of all creatures in the game
    private final int[] monsterImageArr = {R.drawable.bonus_basketball,R.drawable.monster01, R.drawable.monster02,
            R.drawable.monster03,R.drawable.monster04,R.drawable.monster05,
            R.drawable.monster06,R.drawable.monster07};
    int lifes;                          // lifes during the game
    private FusedLocationProviderClient client;
    // Animations
    private ValueAnimator animation;
    private ValueAnimator[] animations;
    // Class to help save all names,scores
    DatabaseHelper playersDb;

    Intent intnt;
    private ImageView[] lives = new ImageView[FULL_LIFES];

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private boolean regularMode;
    private boolean musicOn;
    private boolean vibrationOn;
    private View icon;
    private LinearLayout mainLayout;
    private Effects effects;
    private Handler handler;
    LinearLayout baseLayout;
    FrameLayout f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        playersDb = new DatabaseHelper(this);
        // Force user to open USER's GPS
        gpsPermission();
        getLocation();
//        baseLayout = (LinearLayout) findViewById(R.id.mainGame);
        f = (FrameLayout) findViewById(R.id.frameLayout);
        effects = new Effects();
        handler = new Handler();
        left = (Button)findViewById(R.id.left_direction);
        right = (Button)findViewById(R.id.right_direction);
        handleData();

        if(regularMode) {
//            addClickListeners();
            findViewById(R.id.left_direction).setOnClickListener(this);
            findViewById(R.id.right_direction).setOnClickListener(this);
//            setInstructionIcon(R.drawable.clickableguide);
        }
        else{
            setUpSensors();
//            setInstructionIcon(R.drawable.shakephone02);
        }
//        findViewById(R.id.configuration).setOnClickListener(this);

        findViewById(R.id.btn_pause).setOnClickListener(this);
        findViewById(R.id.btn_resume).setOnClickListener(this);
        findViewById(R.id.btn_stop).setOnClickListener(this);

        life_status1 = (ImageView) findViewById(R.id.life01);
        life_status2 = (ImageView) findViewById(R.id.life02);
        life_status3 = (ImageView) findViewById(R.id.life03);
        lifes = FULL_LIFES;

        lives[0] = life_status1;
        lives[1] = life_status2;
        lives[2] = life_status3;

        scoreView = findViewById(R.id.score_view);
        scoreView.setText("0"); //initial score

        intnt = getIntent();
        // Getting number of columns that user choose.
        columns = Integer.parseInt(intnt.getStringExtra(CHECKED_RADIO_BUTTON));
        monsterArr = new ImageView[columns];
        jordan =  (ImageView)findViewById(R.id.jordan);
        if (columns % 2 == 0)
            jordan.setX((getResources().getDisplayMetrics().widthPixels / (columns*2)));
        createColumns(columns);
        animations = new ValueAnimator[columns];
        dropping(jordan);
        name = intnt.getStringExtra(USER_NAME);

    }

    private void handleData() {

        Bundle data = getIntent().getExtras();
        regularMode = data.getBoolean(Login.MODE);
        musicOn = data.getBoolean(Login.MUSIC);
        vibrationOn = data.getBoolean(Login.VIBRATION);
    }

    private void setUpSensors(){
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this,accelerometer,SensorManager.SENSOR_DELAY_GAME);
    }

    private void setInstructionIcon(int photo){

        final int DELAY_TIME = 2000; //1 seconds to be on screen

//        RelativeLayout.LayoutParams params = new  RelativeLayout.LayoutParams(300, 300);
        RelativeLayout.LayoutParams params = new  RelativeLayout.LayoutParams(300, 300);
//        params.gravity = Gravity.CENTER;

        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        icon = new View(this);
        icon.setBackgroundResource(photo);
        icon.setAnimation(effects.fadeInEffect());
        icon.setLayoutParams(params);

        f.addView(icon);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                icon.setAnimation(effects.fadeOutEffect());
                icon.setVisibility(View.INVISIBLE);
                icon = null;
            }
        },DELAY_TIME);
    }

    private void createColumns(int col) {
        LayoutInflater inflater;
        int randomIndex;

        basketball_bonus_selected = false;
        parentLinearLayout = findViewById(R.id.dropsLayout);
        for (int i = 0; i < col; i++) {
            inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.field, parentLinearLayout, false);
            ImageView monster = rowView.findViewById(R.id.monster);

            if ((i == col-1)&&(!basketball_bonus_selected)) {
                monster.setTag(basketball);
                randomIndex = 0;
            }

            else if((randomIndex = new Random().nextInt(monsterImageArr.length)) == 0) {
                basketball_bonus_selected = true;
                monster.setTag(basketball);
            }
            else
                monster.setTag(monsters);
            monster.setImageResource(monsterImageArr[randomIndex]);
            parentLinearLayout.addView(monster, parentLinearLayout.getChildCount() - 1);
            monsterArr[i] = monster;
        }
    }

    //Create each monster with its own thread
    private void dropping(ImageView playerLocation) {
        for (int i = 0; i < monsterArr.length; i++) {
            if (i % 2 == 0)
                blocksDropping(monsterArr[i], i, 0);
            else
                blocksDropping(monsterArr[i], i, 1);
        }
    }

    //Monster Drop animation and hit calculation
    private void blocksDropping(final ImageView monster, final int i, final int delayIndex) {
        int delay;
        frame = findViewById(R.id.frameLayout);
        animation = ValueAnimator.ofInt(0, getResources().getDisplayMetrics().heightPixels);
        animation.setInterpolator(new LinearInterpolator());
//        animation.setDuration(2000);
        animation.setDuration(1500);
        if (delayIndex == 0) {
//            delay = 1000 * ((new Random().nextInt(6)) + 1);
            delay = 1000 * ((new Random().nextInt(3)) + 1);
            lastDelay = delay;
            animation.setStartDelay(delay);
        } else {
//            delay = 1000 * (new Random().nextInt((14 - 5) + 1) + 5);
            delay = 1000 * (new Random().nextInt(6) + 5);
            lastDelay = delay;
            animation.setStartDelay(delay);
        }
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                monster.setVisibility(View.VISIBLE);
                int animatedValue = (int) valueAnimator.getAnimatedValue();
                monster.setTranslationY(animatedValue);
                if (hit(monster, jordan)) { // Hit found
                    if (monster.getTag().toString().equals(basketball)) { // Bonus
                        checkLife(false);// Bonus
                        scoreView.setText(String.valueOf(Integer.parseInt(scoreView.getText().toString()) + 25)); // for bonus add +25
                        //monster.setImageResource(monsterImageArr[new Random().nextInt(monsterImageArr.length)]);
                        monster.setY(0);
                        monster.setVisibility(View.INVISIBLE);
                        valueAnimator.start();
                    } else {               // hit monster
                        checkLife(true);
//                        if(lifes == 1)
//                            gameOver();
//                        else {// Enemy
                            int temp_score = Integer.parseInt(scoreView.getText().toString());
                            if( temp_score >= 15)
                                scoreView.setText(String.valueOf(Integer.parseInt(scoreView.getText().toString()) - 15));
                            else
                                scoreView.setText("0");
//                            checkLife(true);
                            //monster.setImageResource(monsterImageArr[new Random().nextInt(monsterImageArr.length)]);
                            monster.setY(0);
                            monster.setVisibility(View.INVISIBLE);
                            valueAnimator.start();
//                        }
                    }
                }
                //Updating Score
                if (monster.getY() > frame.getHeight()) {
                    scoreView.setText(String.valueOf(Integer.parseInt(scoreView.getText().toString()) + 10));
                    int randomIndex = new Random().nextInt(monsterImageArr.length);
                    if (randomIndex == 0) {
                        monster.setTag(basketball);
                    } else {
                        monster.setTag(monsters);
                    }
                    monster.setImageResource(monsterImageArr[randomIndex]);
                    monster.setVisibility(View.INVISIBLE);
                    valueAnimator.start();
                }
            }
        });
        animation.setRepeatCount(ValueAnimator.INFINITE);
        animation.start();
        animations[i] = animation;
    }

    //Check life and game over
    private void checkLife(boolean hitMonster) {
        if (hitMonster) { // monster
            lifes--;
            if (lifes > 0) {
                lives[lifes].setVisibility(View.INVISIBLE);
            }
            else{
                lives[lifes].setVisibility(View.INVISIBLE);
                gameOver();
                return;
            }
        }
        else{
            if (lifes <3 && lifes>0){
                lives[lifes].setVisibility(View.VISIBLE);
                lifes++;
            }
        }
    }

    public void gameOver() {
        TextView currentScore = findViewById(R.id.score_view);
        finalScore = currentScore.getText().toString();
        Intent activityChangeIntent = new Intent(this, EndGame.class);
        activityChangeIntent.putExtra(Scores, finalScore);
        activityChangeIntent.putExtra(Player, name);
        activityChangeIntent.putExtra(Lat, "" + lat);
        activityChangeIntent.putExtra(Lng, "" + lng);
        startActivity(activityChangeIntent);
        AddData();
        finish();
        return;
    }

    private void getLocation() {
        client = LocationServices.getFusedLocationProviderClient(this);
        client.getLastLocation().addOnSuccessListener(Game.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    lat = location.getLatitude();
                    lng = location.getLongitude();
                }
            }
        });

    }

    // GPS Reque
    private void gpsPermission() {
        ActivityCompat.requestPermissions(this,new String[] {ACCESS_FINE_LOCATION}, 1);
    }

    public void resumeAnimations(){
        for (int i = 0; i < animations.length; i++){
            animations[i].resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopAnimations();
        if (regularMode)
            playerPause();
        else
            sensorManager.unregisterListener(this);
    }

    private void playerPause() {
        if (left.isEnabled())
            left.setEnabled(false);
        if (right.isEnabled())
            right.setEnabled(false);
    }
    private void playerResume() {
        if (!left.isEnabled())
            left.setEnabled(true);
        if (!right.isEnabled())
            right.setEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        resumeAnimations();
        if (regularMode)
            playerResume();
        else
            setUpSensors();
//            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onStop() {
        super.onStop();
        gameOver();
    }

    private boolean hit(View m,View j) {
        int[] enemy_locate = new int[2];
        int[] player_locate = new int[2];
        // Computes the coordinates of this view on the screen
        m.getLocationOnScreen(enemy_locate);
        j.getLocationOnScreen(player_locate);
        Rect rect1 = new Rect(enemy_locate[0], enemy_locate[1], (int) (enemy_locate[0] + m.getWidth()), (int) (enemy_locate[1] + m.getHeight()));
        Rect rect2 = new Rect(player_locate[0], player_locate[1], (int) (player_locate[0] + j.getWidth()), (int) (player_locate[1] + j.getHeight()));
        return Rect.intersects(rect1, rect2);
    }

    public  void AddData() {
        playersDb.insertData(name,
                finalScore,
                String.valueOf(lat), String.valueOf(lng));
    }

    public void clickToMoveRight(View view) {
        if (jordan.getX() + getResources().getDisplayMetrics().widthPixels / columns < (getResources().getDisplayMetrics().widthPixels ))
            jordan.setX(jordan.getX() + getResources().getDisplayMetrics().widthPixels / columns);
    }

    public void clickToMoveLeft(View view) {
        if (jordan.getX() >= (getResources().getDisplayMetrics().widthPixels * 1 / columns))
            jordan.setX(jordan.getX() - getResources().getDisplayMetrics().widthPixels / columns);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.configuration:
//                break;
            case R.id.left_direction:
                clickToMoveLeft(v);
                break;
            case R.id.right_direction:
                clickToMoveRight(v);
                break;
            case R.id.btn_pause:
                onPause();
                break;
            case R.id.btn_resume:
                onResume();
                break;
            case R.id.btn_stop:
                onStop();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        stopAnimations();
        gameOver();
//        startActivity(new Intent(this, Login.class));
//        finish();
//        return;
    }

    private void stopAnimations() {
        for (int i = 0; i < animations.length; i++){
            animations[i].pause();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        double screenWidth = getResources().getDisplayMetrics().widthPixels;
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            //Move Left and Right
            if ((jordan.getX() < screenWidth - jordan.getWidth() || (int) sensorEvent.values[0] > 0)
                    && (jordan.getX() > 0 || (int) sensorEvent.values[0] < 0 )) {
                if ((int) sensorEvent.values[0] <= 0)
                    moveLeftWithSensors(sensorEvent, jordan);
                else
                    moveRightWithSensors(sensorEvent, jordan);

            }
        }
    }

    public void moveLeftWithSensors(SensorEvent sensorEvent,ImageView senJordan) {
        senJordan.setX((senJordan.getX() - (int) sensorEvent.values[0] + 10));
    }

    public void moveRightWithSensors(SensorEvent sensorEvent,ImageView senJordan) {
        senJordan.setX((senJordan.getX() - (int) sensorEvent.values[0] - 10));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
