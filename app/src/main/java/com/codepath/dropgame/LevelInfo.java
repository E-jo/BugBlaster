package com.codepath.dropgame;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.codepath.simplegame.actors.PositionedActor;


public class LevelInfo extends PositionedActor {
    private int gained, lost;

    public LevelInfo(int x, int y, int gained, int lost) {
        super(x, y);
        this.gained = gained;
        this.lost = lost;
    }


    @Override
    public void stylePaint(Paint p) {
        p.setTextSize(30);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawText(String.valueOf(gained) + " total levels gained and " +
                String.valueOf(lost) + " levels lost", getX(), getY(), getPaint());
    }

}