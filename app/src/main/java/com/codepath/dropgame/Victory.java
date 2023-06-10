package com.codepath.dropgame;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.codepath.simplegame.AbstractGamePanel;
import com.codepath.simplegame.actors.PositionedActor;


public class Victory extends PositionedActor {
    public Victory(AbstractGamePanel context, int y) {
        super(context.getWidth() - 125, y);
    }

    public Victory(int x, int y) {
        super(x, y);
    }

    public Victory(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void stylePaint(Paint p) {
        p.setTextSize(75);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawText("Victory!", getX(), getY(), getPaint());
    }

}