package com.codepath.dropgame;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.codepath.simplegame.actors.PositionedActor;


public class BulletInfo extends PositionedActor {
    private int bullets, bugs;

    public BulletInfo(int x, int y, int bullets, int bugs) {
        super(x, y);
        this.bullets = bullets;
        this.bugs = bugs;
    }


    @Override
    public void stylePaint(Paint p) {
        p.setTextSize(30);
    }

    @Override
    public void draw(Canvas canvas) {

        canvas.drawText(String.valueOf(bullets) + " bullets fired to kill " +
                String.valueOf(bugs) + " total bugs", getX(), getY(), getPaint());
    }

}