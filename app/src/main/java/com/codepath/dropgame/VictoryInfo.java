package com.codepath.dropgame;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.codepath.simplegame.actors.PositionedActor;


public class VictoryInfo extends PositionedActor {
    private int levels;
    private long time;

    public VictoryInfo(int x, int y, int levels, long time) {
        super(x, y);
        this.levels = levels;
        this.time = time;
    }

    public VictoryInfo(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void stylePaint(Paint p) {
        p.setTextSize(30);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawText(String.valueOf(levels) + " net levels gained in " +
                String.valueOf(time) + " seconds", getX(), getY(), getPaint());
    }

}