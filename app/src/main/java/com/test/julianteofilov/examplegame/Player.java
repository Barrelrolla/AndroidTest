package com.test.julianteofilov.examplegame;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

/**
 * Created by julian.teofilov on 24/1/2017.
 */

public class Player extends GameObject {
    private final int jumpFrames = 15;
    private Bitmap spritesheet;
    private int score;
    private boolean up;
    private boolean playing;
    private Animation animation = new Animation();
    private long startTime;
    private int lowPoint = GamePanel.DEVICE_HEIGHT - 162;
    private int jumpVelocity;
    private int gravity;
    private int jumpFrameCount;
    private boolean isOnGround = true;
    private int numFrames;
    private boolean isNewJump;

    public Player(Bitmap res, int w, int h, int numFrames, Bitmap jumpImage) {
        x = 100;
        y = lowPoint;
        dy = 0;
        score = 0;
        height = h;
        width = w;
        jumpVelocity = -20;
        gravity = 20;
        jumpFrameCount = jumpFrames;
        this.numFrames = numFrames;

        Bitmap[] image = new Bitmap[numFrames + 1];
        spritesheet = res;

        for (int i = 0; i < image.length; i++) {
            image[i] = Bitmap.createBitmap(spritesheet, (i % 3) * width, (i / 3) * height, width, height);
        }
        image[numFrames] = jumpImage;

        animation.setFrames(image, numFrames);
        animation.setDelay(10);
        startTime = System.nanoTime();
    }

    public void setUp(boolean b) {
        up = b;
    }

    public boolean getIsOnGround() {
        return this.isOnGround;
    }

    public void update() {
        long elapsed = (System.nanoTime() - startTime / 1000000);
        if (elapsed > 100) {
            score++;
            startTime = System.nanoTime();
        }

        animation.update();

        if (isNewJump) {
            isNewJump = false;
            jumpFrameCount = jumpFrames;
        }

        this.isOnGround = false;

        if (up) {
            dy = jumpVelocity;
            jumpFrameCount--;
            if (jumpFrameCount == 0) {
                up = false;
                jumpFrameCount = jumpFrames;
            }
        } else {
            dy = gravity;
        }

        y += dy;
        if (y > lowPoint) {
            y = lowPoint;
            this.isOnGround = true;
        } else if (y < GamePanel.DEVICE_HEIGHT / 2) {
            y = GamePanel.DEVICE_HEIGHT / 2;
        }
    }

    public void draw(Canvas canvas) {
        if (isOnGround) {
            canvas.drawBitmap(animation.getImage(), x, y, null);
        } else {
            canvas.drawBitmap(animation.getImage(numFrames), x, y, null);
        }
    }

    public int getScore() {
        return this.score;
    }

    public boolean getPlaying() {
        return this.playing;
    }

    public void setPlaying(boolean p) {
        this.playing = p;
    }

    public void resetScore() {
        this.score = 0;
    }

    public void setNewJump(boolean j) {
        this.isNewJump = j;
    }
}
