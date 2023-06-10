package com.codepath.dropgame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.codepath.simplegame.GameActivity;
import com.codepath.simplegame.threads.BaseGameThread;
import com.codepath.simplegame.threads.FrameGameLoopThread;

public class MainActivity extends GameActivity {
    public static boolean lvlBugs, lvlBullets, lvlSpeed;
    public static int lvlChange, targetLevel;
    private SharedPreferences prefs;

    public void startVictoryActivity() {
        Intent intent = new Intent(this, VictoryActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switchFullscreen();
        Intent intent = getIntent();
        boolean wackyMode = intent.getBooleanExtra("wackyMode", false);
        boolean dblWacky = intent.getBooleanExtra("dblWacky", false);
        boolean slantyMode = intent.getBooleanExtra("slanty", false);
        boolean horizontalMode = intent.getBooleanExtra("horizontalMode", false);
        boolean randomMode = intent.getBooleanExtra("randomMode", false);
        int maxBugs = intent.getIntExtra("maxBugs", 3);
        int maxBullets = intent.getIntExtra("maxBullets", 3);
        int speed = intent.getIntExtra("speed", 5);
        int randomness = intent.getIntExtra("randomness", 50);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        lvlBugs = prefs.getBoolean("pref_progressive_bugs", true);
        lvlBullets = prefs.getBoolean("pref_progressive_bullets", true);
        lvlSpeed = prefs.getBoolean("pref_progressive_speed", true);
        lvlChange = Integer.valueOf(prefs.getString("pref_level_up_count", "10"));
        targetLevel = Integer.valueOf(prefs.getString("pref_victory_target", "20"));

        DropGamePanel gameView = new DropGamePanel(FrameGameLoopThread.class, this,
                wackyMode, dblWacky, slantyMode, horizontalMode, randomMode, maxBugs, maxBullets, speed, randomness);
        setContentView(gameView);

        /*
        new Thread(new Runnable(){
            public void run(){
                try {
                    running.join();
                } catch(Exception e){

                }
                finally{

                }}
            }).start();
        */
        Log.d("threadz", "main thread");

    }


}