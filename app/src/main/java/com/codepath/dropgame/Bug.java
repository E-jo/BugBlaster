package com.codepath.dropgame;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;

import com.codepath.simplegame.Velocity;

import java.util.Random;

public class Bug extends PlayerActor {
    private int bugType, hits, reversedCounter;
    private boolean reversed = false;
    private boolean wackyMode = DropGamePanel.wackyMode;
    private boolean horizontalMode = DropGamePanel.horizontalMode;
    private boolean randomMode = DropGamePanel.randomMode;

    @SuppressLint("NonConstantResourceId")
    public Bug(Context c, int drawable, int x, int y) {
        super(c, drawable, x, y);
        if (wackyMode || horizontalMode || randomMode) {
            Random random = new Random();
            int change = random.nextInt(2);
            if (change == 0) {
                getVelocity().setXDirection(Velocity.DIRECTION_RIGHT).setXSpeed(10);
            } else {
                getVelocity().setXDirection(Velocity.DIRECTION_LEFT).setXSpeed(10);
            }
        }
        bugType = drawable;

        // incremented each time direction is reversed
        reversedCounter = 0;

        switch (bugType) {
            case R.drawable.bug1:
                getVelocity()
                        .setYDirection(Velocity.DIRECTION_DOWN)
                        .setYSpeed(3 + DropGamePanel.speed);
                hits = 2;
                break;
            case R.drawable.bug2:
                getVelocity()
                        .setYDirection(Velocity.DIRECTION_DOWN)
                        .setYSpeed(5 + DropGamePanel.speed);
                hits = 1;
                break;
            default:
                getVelocity()
                        .setYDirection(Velocity.DIRECTION_DOWN)
                        .setYSpeed(DropGamePanel.speed);
                hits = 3;
                break;
        }
    }

    public Bug(Bitmap bitmap, int x, int y) {
        super(bitmap, x, y);
    }

    public void setReversed(boolean reversed) {
        this.reversed = reversed;
    }

    public boolean getReversed() {
        return this.reversed;
    }

    public void setReversedCounter (int step) {
        this.reversedCounter += step;
    }

    public int getReversedCounter() {
        return this.reversedCounter;
    }

    public void addHit() {
        this.hits--;
    }

    public int getHits() {
        return hits;
    }

    public int getBugType() { return bugType;}
}
