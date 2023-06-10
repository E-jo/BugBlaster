package com.codepath.dropgame;

        import android.graphics.Canvas;
        import android.graphics.Paint;

        import com.codepath.simplegame.AbstractGamePanel;
        import com.codepath.simplegame.actors.PositionedActor;


public class ReturnToMenu extends PositionedActor {
    public ReturnToMenu(AbstractGamePanel context, int y) {
        super(context.getWidth() - 200, y);
    }

    public ReturnToMenu(int x, int y) {
        super(x, y);
    }

    public ReturnToMenu(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void stylePaint(Paint p) {
        p.setTextSize(50);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawText("Return To Menu", getX(), getY(), getPaint());
    }

}