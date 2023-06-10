package com.codepath.dropgame;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.codepath.simplegame.AbstractGamePanel;
import com.codepath.simplegame.actors.PositionedActor;


public class Display extends PositionedActor {
    private int score;

    public int getScore() {
        return score;
    }

    public Display(AbstractGamePanel context, int y) {
        super(context.getWidth() - 150, y);
        score = 0;
    }

    public Display(int x, int y) {
        super(x, y);
    }

    public Display(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void stylePaint(Paint p) {
        p.setTextSize(75);
    }

    public void addScore(int points) {
        score += points;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawText(String.valueOf(score), getX(), getY(), getPaint());
    }

}