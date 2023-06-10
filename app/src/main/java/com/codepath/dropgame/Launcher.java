package com.codepath.dropgame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

public class Launcher extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private Button regularBtn, wackyBtn, dblWackyBtn, horizontalBtn, randomBtn, levelingBtn;
    private TextView textViewBugs, textViewBullets, textViewSpeed, textViewRandomness;
    private SeekBar seekBarBugs, seekBarBullets, seekBarSpeed, seekBarRandomness;
    private CheckBox wackyBullets, slantyBullets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        levelingBtn = (Button) findViewById(R.id.levelBtn);
        regularBtn = (Button) findViewById(R.id.reg);
        wackyBtn = (Button) findViewById(R.id.wacky);
        wackyBullets = (CheckBox) findViewById(R.id.checkBox);
        slantyBullets = (CheckBox) findViewById(R.id.checkBox2);
        horizontalBtn = (Button) findViewById(R.id.horizontalBtn);
        randomBtn = (Button) findViewById(R.id.randomBtn);
        textViewBugs = (TextView) findViewById(R.id.textViewBugs);
        textViewBullets = (TextView) findViewById(R.id.textViewBullets);
        textViewSpeed = (TextView) findViewById(R.id.textViewSpeed);
        textViewRandomness = (TextView) findViewById(R.id.textViewRnd);
        seekBarSpeed = (SeekBar) findViewById(R.id.seekBarSpeed);
        seekBarBugs = (SeekBar) findViewById(R.id.seekBarBugs);
        seekBarBullets = (SeekBar) findViewById(R.id.seekBarBullets);
        seekBarRandomness = (SeekBar) findViewById(R.id.seekBarRandomness);

        seekBarBugs.setOnSeekBarChangeListener(this);
        seekBarBullets.setOnSeekBarChangeListener(this);
        seekBarSpeed.setOnSeekBarChangeListener(this);
        seekBarRandomness.setOnSeekBarChangeListener(this);

        levelingBtn.setOnClickListener(this);
        regularBtn.setOnClickListener(this);
        wackyBtn.setOnClickListener(this);
        horizontalBtn.setOnClickListener(this);
        randomBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("maxBugs", seekBarBugs.getProgress());
        intent.putExtra("maxBullets", seekBarBullets.getProgress());
        intent.putExtra("speed", seekBarSpeed.getProgress());
        intent.putExtra("dblWacky", wackyBullets.isChecked());
        intent.putExtra("slanty", slantyBullets.isChecked());



        switch (v.getId()) {
            case R.id.reg:
                intent.putExtra("wackyMode", false);
                break;
            case R.id.wacky:
                intent.putExtra("wackyMode", true);
                break;

            case R.id.horizontalBtn:
                intent.putExtra("horizontalMode", true);
                intent.putExtra("wackyMode", false);
                break;
            case R.id.randomBtn:
                intent.putExtra("randomMode", true);
                intent.putExtra("wackyMode", false);
                intent.putExtra("randomness", seekBarRandomness.getProgress());
                break;
            case R.id.levelBtn:
                intent = new Intent(this, LevelSettingsActivity.class);
        }

        try {
            startActivity(intent);
        } catch (Exception e) {
            Log.d("ac2", "error starting activity intent");
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.seekBarBugs:
                textViewBugs.setText("Maximum Bugs: " + String.valueOf(progress) + "/10");
                break;
            case R.id.seekBarBullets:
                textViewBullets.setText("Maximum Bullets: " + String.valueOf(progress) + "/10");
                break;
            case R.id.seekBarSpeed:
                textViewSpeed.setText("Speed: " + String.valueOf(progress) + "/10");
                break;
            case R.id.seekBarRandomness:
                textViewRandomness.setText("Randomness: " + String.valueOf(progress) + "/100");
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}