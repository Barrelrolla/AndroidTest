package com.test.julianteofilov.examplegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by julian.teofilov on 23/1/2017.
 */

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    public static final int WIDTH = 1024;
    public static final int HEIGHT = 512;
    public static final int MOVESPEED = -5;
    public static int DEVICE_WIDTH;
    public static int DEVICE_HEIGHT;
    private long enemyStartTime;
    private long enemyElapsed;
    private MainThread thread;
    private Background bg;
    private Player player;
    private ArrayList<Enemy> enemies;
    private Random random = new Random();
    private boolean firstFly = true;

    public GamePanel(Context context) {
        super(context);

        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);

        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        int counter = 0;
        while (retry && counter < 1000) {
            counter++;
            try {
                thread.setRunning(false);
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        DEVICE_HEIGHT = getHeight();
        DEVICE_WIDTH = getWidth();
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.bg_grasslands);
        bg = new Background(Bitmap.createScaledBitmap(b, WIDTH, getHeight(), false));
        Bitmap jump = BitmapFactory.decodeResource(getResources(), R.drawable.p3_jump);
        player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.p3_walk), 66, 92, 5, jump);
        enemies = new ArrayList<Enemy>();
        enemyStartTime = System.nanoTime();

        thread.setRunning(true);
        thread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (!player.getPlaying()) {
                player.setPlaying(true);
            } else {
                if (player.getIsOnGround()) {
                    player.setUp(true);
                    player.setNewJump(true);
                }
            }
            return true;
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (!player.getIsOnGround()) {
                player.setUp(false);
            }
        }

        return super.onTouchEvent(event);
    }

    public void update() {
        if (player.getPlaying()) {
            bg.update();
            player.update();

            long enemiesElapsed = (System.nanoTime() - enemyStartTime) / 1000000;
            if (enemiesElapsed > (500 - player.getScore() / 4)) {
                if (firstFly) {
                    firstFly = false;
                    enemies.add(new Enemy(
                            BitmapFactory.decodeResource(getResources(), R.drawable.fishswim1),
                            BitmapFactory.decodeResource(getResources(), R.drawable.fishswim2),
                            DEVICE_WIDTH + 10,
                            DEVICE_HEIGHT - 162,
                            72, 36, player.getScore(), 2));
                } else {
                    enemies.add(new Enemy(
                            BitmapFactory.decodeResource(getResources(), R.drawable.fishswim1),
                            BitmapFactory.decodeResource(getResources(), R.drawable.fishswim2),
                            DEVICE_WIDTH + 10,
                            (int) (random.nextDouble() * (DEVICE_HEIGHT)),
                            72, 36, player.getScore(), 2));
                }

                enemyStartTime = System.nanoTime();
            }

            for (int i = 0; i < enemies.size(); i++) {
                enemies.get(i).update();
                if (collision(enemies.get(i), player)) {
                    player.setPlaying(false);
                    break;
                }
                if (enemies.get(i).getX() < -100) {
                    enemies.remove(i);
                    break;
                }
            }
        }
    }

    public boolean collision(GameObject a, GameObject b) {
        if (Rect.intersects(a.getRectangle(), b.getRectangle())) {
            return true;
        }
        return false;
    }

    @Override
    public void draw(Canvas canvas) {
        final float scaleFactorX = getWidth() / (WIDTH * 1.0f);
        final float scaleFactorY = getHeight() / (HEIGHT * 1.0f);
        if (canvas != null) {
            final int savedState = canvas.save();
            //canvas.scale(scaleFactorX, scaleFactorY);
            bg.draw(canvas);
            player.draw(canvas);
            for (int i = 0; i < getWidth(); i += 70) {
                canvas.drawBitmap((BitmapFactory.decodeResource(getResources(), R.drawable.grassmid)), i, getHeight() - 70, null);
            }

            for (Enemy e : enemies) {
                e.draw(canvas);
            }

            canvas.restoreToCount(savedState);
        }
    }
}
