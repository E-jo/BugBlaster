package com.codepath.dropgame;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.codepath.simplegame.Velocity;
import com.codepath.simplegame.actors.SpriteMovingActor;

public class PlayerActor extends SpriteMovingActor {
    private int speed = 10;

    // direct from drawable constructor
    public PlayerActor(Context c, int drawable, int x, int y) {
        super(c, drawable, x, y);
        getVelocity().stop().setXDirection(0).setYDirection(0).setXSpeed(0).setYSpeed(0);
    }

    // bitmap constructor
    public PlayerActor(Bitmap bitmap, int x, int y) {
        super(bitmap, x, y);
        getVelocity().stop().setXDirection(0).setYDirection(0).setXSpeed(0).setYSpeed(0);
    }

    @Override
    public void stylePaint(Paint p) {

    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void handleTouchStart(MotionEvent event) {
        // handles left and right player sight movement
        Velocity v = getVelocity();
        if (event.getY() < getHeight() - 150) {
            return;
        }
        if (event.getX() < this.getX()) {
            v.stop().setXDirection(Velocity.DIRECTION_LEFT).setXSpeed(speed);
        } else if (event.getX() > this.getX()) {
            v.stop().setXDirection(Velocity.DIRECTION_RIGHT).setXSpeed(speed);
        }
    }

    public void handleTouchStop(MotionEvent event) {
        Velocity v = getVelocity();
        v.stop();
    }
}
