package com.codepath.dropgame;

import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;

import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

import android.media.SoundPool;
import android.media.AudioManager;

import com.codepath.simplegame.AbstractGamePanel;
import com.codepath.simplegame.Velocity;
import com.codepath.simplegame.threads.BaseGameThread;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Random;

@SuppressWarnings("ALL")
public class DropGamePanel extends AbstractGamePanel {
    static public boolean wackyMode, dblWacky, horizontalMode, randomMode, slantyMode;
    static public int maxBugs, maxBullets, speed;

    private PlayerActor player, left, right, fire;
    private List<PlayerActor> bullets;
    private List<Bug> bugs;
    private final Bitmap playerIcon;
    private Display hits, damage, speedLevel;
    private Victory victory;
    private VictoryInfo victoryInfo;
    private BulletInfo bulletInfo;
    private LevelInfo levelInfo;
    private ReturnToMenu returnToMenu;
    private int hit, gotHit, bulletSpeed, fireFx, hitFx, destroyedFx, randomness,
            levelUp, levelCount, targetLevel, startLevel;
    private SoundPool dropFx;
    private SharedPreferences prefs;
    private boolean lvlBugs, lvlBullets, lvlSpeed, gameOver;
    private long startTime, endTime, gameTime;
    private int levelsGained, bulletsFired, bugsKilled, gained, lost;

    public DropGamePanel(Context context) {
        super(context);
        setPanelColor(Color.LTGRAY);
        this.playerIcon = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.ss);
        this.bugs = new ArrayList<Bug>();
        this.bullets = new ArrayList<PlayerActor>();
        this.dropFx = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
    }

    public DropGamePanel(Class<? extends BaseGameThread> loopClass, Context context,
                         boolean wackyMode, boolean dblWacky, boolean slantyMode,
                         boolean horizontalMode, boolean randomMode, int maxBugs,
                         int maxBullets, int speed, int randomness) {
        super(loopClass, context);
        this.wackyMode = wackyMode;
        this.dblWacky = dblWacky;
        this.horizontalMode = horizontalMode;
        this.randomMode = randomMode;
        this.maxBugs = maxBugs;
        this.maxBullets = maxBullets;
        this.slantyMode = slantyMode;
        this.speed = speed;
        this.startLevel = speed;
        this.randomness = randomness;
        this.bugs = new ArrayList<Bug>();
        this.bullets = new ArrayList<PlayerActor>();
        this.playerIcon = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.ss);
        this.bugs = new ArrayList<>();
        this.bullets = new ArrayList<>();
        this.dropFx = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
    }

    @Override
    public void onStart() {
        Context c = getContext();

        startTime = System.currentTimeMillis();
        lvlBugs = MainActivity.lvlBugs;
        lvlBullets = MainActivity.lvlBullets;
        lvlSpeed = MainActivity.lvlSpeed;
        levelUp = MainActivity.lvlChange;
        targetLevel = MainActivity.targetLevel;
        bugsKilled = 0;
        bulletsFired = 0;
        gained = 0;
        lost = 0;
        gameOver = false;
        levelCount = 0;
        bulletSpeed = speed + 10;
        fireFx = dropFx.load(c, R.raw.gunfire, 1);
        hitFx = dropFx.load(c, R.raw.thwank, 1);
        destroyedFx = dropFx.load(c, R.raw.splat, 1);

        hits = new Display(this, 150);
        damage = new Display(this, 300);
        speedLevel = new Display (0, 150);
        speedLevel.addScore(speed);

        left = new PlayerActor(c, R.drawable.left, 0, getHeight() - 100);
        right = new PlayerActor(c, R.drawable.right,getWidth() - 100, getHeight() - 100);

        fire = new PlayerActor(c, R.drawable.bullet, getWidth() / 2, getHeight() - 100);

        player = new PlayerActor(c, R.drawable.sight, getWidth() / 2,
                getHeight() - 200);
        player.setSpeed(speed + 10);

        spawnBug();
    }

    private int randomBug() {
        int bug;
        Random random = new Random();
        switch (random.nextInt(3)) {
            case 0:
                bug = R.drawable.bug1;
                break;
            case 1:
                bug = R.drawable.bug2;
                break;
            case 2:
                bug = R.drawable.bug3;
                break;
            default:
                bug = R.drawable.bug1;
                break;
        }
        return bug;
    }

    private void spawnBug() {
        int xSpawn = (int) (Math.random() * getWidth());
        xSpawn = xSpawn > getWidth() - 100 ? xSpawn - 100 : xSpawn;
        bugs.add(new Bug(getContext(), randomBug(), xSpawn, 0));
    }

    @Override
    public void onTimer() {
        // remove bullets from top of screen
        if (bullets.size() > 0) {
            if (bullets.get(0).getY() < 0) {
                bullets.remove(0);
            }
        }

        // move the player sights
        player.move();

        // move all bullets
        for (PlayerActor bullet : bullets) {
            if (dblWacky) {
                Random random = new Random();
                int change = random.nextInt(2);
                if (change == 0) {
                    bullet.getVelocity().setXDirection(Velocity.DIRECTION_RIGHT);
                } else {
                    bullet.getVelocity().setXDirection(Velocity.DIRECTION_LEFT);
                }
            }
            // keep bullets within screen constraints
            if (bullet.getX() > getWidth() - bullet.getWidth() / 2) {
                bullet.getVelocity().setXDirection(Velocity.DIRECTION_LEFT);
            }
            if (bullet.getX() < 0 + bullet.getWidth() / 2) {
                bullet.getVelocity().setXDirection(Velocity.DIRECTION_RIGHT);
            }
            bullet.move();
        }

        // apply random movement to bugs
        applyRandom();

        // check for any bullet hits
        detectHits();

        // if all bugs have been shot down, respawn one to avoid index error on checks
        if (bugs.size() == 0) {
            spawnBug();
        }

        // check if a bug has gotten through
        if (bugs.get(0).getY() > getHeight()) {
            bugThrough();
        }

        // spawn another bug after one is disabled
        try {
            if (!bugs.get(0).isEnabled()) {
                bugs.remove(0);
                if (bugs.size() < maxBugs) {
                    spawnBug();
                }
            }
        } catch (Exception e) {
            Log.println(1, "Error", "Error spawning bug");
        }

        if (bugs.get(0).getY() > getHeight() / (maxBugs) && bugs.size() < maxBugs) {
            spawnBug();
        }
    }

    @Override
    public void redrawCanvas(Canvas canvas) {
        // check for victory
        if (targetLevel >= 0 && speed >= targetLevel) {
            stopGameLoop();
            gameOver = true;
            canvas.drawColor(Color.LTGRAY);

            endTime = System.currentTimeMillis();
            gameTime = (endTime - startTime) / 1000;

            levelsGained = targetLevel - startLevel;

            victory = new Victory (getWidth() / 2 - 125, getHeight() / 2);
            victoryInfo = new VictoryInfo(20, getHeight() / 2 + 125,
                    levelsGained, gameTime);
            levelInfo = new LevelInfo(20, getHeight() / 2 + 175,
                    gained, lost);
            bulletInfo = new BulletInfo(20, getHeight() / 2 + 225,
                    bulletsFired, bugsKilled);
            returnToMenu = new ReturnToMenu(getWidth() / 2 - 175, 200);

            victory.draw(canvas);
            victoryInfo.draw(canvas);
            levelInfo.draw(canvas);
            bulletInfo.draw(canvas);
            returnToMenu.draw(canvas);

            Log.d("threadz", "Game loop stopped");
        } else {  // no victory, normal redraw
            player.draw(canvas);
            try {
                for (PlayerActor bug : bugs) {
                    bug.draw(canvas);
                }
            } catch (ConcurrentModificationException e) {
                Log.d("error", "bug render error");
            }

            try {
                for (PlayerActor bullet : bullets) {
                    bullet.draw(canvas);
                }
            } catch (ConcurrentModificationException e) {
                Log.d("error", "bullet render error");
            }

            speedLevel.setScore(speed);
            left.draw(canvas);
            right.draw(canvas);
            fire.draw(canvas);
            hits.draw(canvas);
            speedLevel.draw(canvas);
            damage.draw(canvas);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (gameOver) {
                if (event.getX() >= 0 && event.getY() <= 300) {
                    returnToMain();
                }
            }
            if (event.getX() >= getWidth() / 3 && event.getX() <= getWidth() / 3 * 2
                    && event.getY() >= getHeight() / 3) {
                fireBullet(0);
            } else if (event.getY() < (getHeight() / 3) * 2) {
                if (event.getX() < player.getX()) {
                    fireBullet(1);
                } else {
                    fireBullet(2);
                }
            } else {
                player.handleTouchStart(event);
            }
            return true;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            player.handleTouchStop(event);
            return true;
        }
        return false;
    }

    private void returnToMain() {
        Intent intent = new Intent(getContext(), Launcher.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(intent);
    }

    private void fireBullet(int direction) {
        if (bullets.size() <= maxBullets - 1) {
            dropFx.play(fireFx, .5f, .5f, 1, 0, .91875f);
            bulletsFired++;
            bullets.add(new PlayerActor(getContext(), R.drawable.bullet,
                    player.getX(), player.getY()));
            bullets.get(bullets.size()-1).getVelocity()
                    .setYDirection(Velocity.DIRECTION_UP)
                    .setYSpeed(bulletSpeed);
            if (dblWacky || slantyMode) {
                bullets.get(bullets.size()-1).getVelocity()
                        .setXSpeed(bulletSpeed);
            }
            if (slantyMode) {
                if (direction == 1) {
                    bullets.get(bullets.size() - 1).getVelocity()
                            .setXDirection(Velocity.DIRECTION_LEFT);
                } else if (direction == 2) {
                    bullets.get(bullets.size() - 1).getVelocity()
                            .setXDirection(Velocity.DIRECTION_RIGHT);
                }
            }
        }
    }

    private void detectHits() {
        // check for bullet hits
        for (int i = 0; i < bullets.size(); i++) {
            for (int j = 0; j < bugs.size(); j++) {
                try {
                    if (bullets.get(i).intersect(bugs.get(j))) {
                        bugs.get(j).addHit();
                        bullets.remove(i);
                        //dropFx.play(hitFx, 3, 3, 1, 0, .91875f);

                        if (bugs.get(j).getHits() == 0) {
                            bugsKilled++;
                            dropFx.play(destroyedFx, 3, 3, 1, 0, .91875f);
                            switch (bugs.get(j).getBugType()) {
                                case R.drawable.bug1:
                                    hits.addScore(2);
                                    levelCount += 2;
                                    break;
                                case R.drawable.bug2:
                                    hits.addScore(1);
                                    levelCount++;
                                    break;
                                case R.drawable.bug3:
                                    hits.addScore(3);
                                    levelCount += 3;
                            }

                            if (levelCount >= levelUp) {
                                gained++;
                                if (lvlSpeed) {
                                    speed++;
                                }
                                if (lvlBugs) {
                                    maxBugs++;
                                }
                                if (lvlBullets) {
                                    maxBullets++;
                                }
                                player.setSpeed(speed + 10);
                                bulletSpeed = speed + 10;
                                levelCount = 0;
                            }
                            bugs.get(j).setEnabled(false);
                            bugs.remove(j);
                        }
                        break;
                    }
                } catch (IndexOutOfBoundsException e) {
                    Log.d("error", "error processing bullets");
                }

            }
        }
    }

    private void bugThrough() {
        bugs.get(0).setEnabled(false);
        switch (bugs.get(0).getBugType()) {
            case R.drawable.bug1:
                damage.addScore(-2);
                gotHit += 2;
                break;
            case R.drawable.bug2:
                damage.addScore(-1);
                gotHit += 1;
                break;
            case R.drawable.bug3:
                damage.addScore(-3);
                gotHit += 3;
        }
        if (gotHit >= levelUp && speed >= 1) {
            lost++;
            if (lvlSpeed) {
                speed--;
            }
            if (lvlBugs) {
                maxBugs--;
                if (maxBugs < 1) {
                    maxBugs++;
                }
            }
            if (lvlBullets) {
                maxBullets--;
                if (maxBullets < 1) {
                    maxBullets++;
                }
            }
            gotHit = 0;
            speed = speed < 1 ? speed++ : speed;
            player.setSpeed(speed + 10);
            bulletSpeed = speed + 10;
        }
    }

    private void applyRandom() {
        try {
            for (Bug bug : bugs) {
                if (wackyMode) {
                    Random random = new Random();
                    if (bug.getReversed()) {
                        bug.setReversedCounter(1);
                    }
                    int change = random.nextInt(randomness);
                    if (change == 0) {
                        bug.getVelocity().setXDirection(Velocity.DIRECTION_RIGHT);
                    } else if (change == 1) {
                        bug.getVelocity().setXDirection(Velocity.DIRECTION_LEFT);
                    }
                    if (bug.getY() < 0 || bug.getReversedCounter() > randomness) {
                        bug.getVelocity().setYDirection(Velocity.DIRECTION_DOWN);
                        bug.setReversed(false);
                        bug.setReversedCounter(0);
                    }
                    change = random.nextInt(randomness);
                    if (change == 1 || change == 2) {
                        bug.getVelocity().setYDirection(Velocity.DIRECTION_UP);
                        bug.setReversed(true);
                    }
                    if (bug.getY() < 0) {
                        bug.getVelocity().setYDirection(Velocity.DIRECTION_DOWN);
                    }
                }
                if (randomMode) {
                    Random random = new Random();
                    int change = random.nextInt(randomness);
                    if (change == 0) {
                        bug.getVelocity().setXDirection(Velocity.DIRECTION_RIGHT);
                    } else if (change == 1) {
                        bug.getVelocity().setXDirection(Velocity.DIRECTION_LEFT);
                    }
                }

                // keep bugs within screen constraints
                if (bug.getX() > getWidth() - bug.getWidth() / 2) {
                    bug.getVelocity().setXDirection(Velocity.DIRECTION_LEFT);
                }
                if (bug.getX() < 0 + bug.getWidth() / 2) {
                    bug.getVelocity().setXDirection(Velocity.DIRECTION_RIGHT);
                }

                bug.move();
            }
        } catch (Exception e) {
            Log.d("Error", "Error spawning bug");
        }
    }

    public List<Bug> getBugs() {
        return this.bugs;
    }
}
