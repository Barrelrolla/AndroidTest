package com.test.julianteofilov.examplegame;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

/**
 * Created by julian.teofilov on 24/1/2017.
 */

public class Enemy extends GameObject {
    private int score;
    private int speed;
    private Random rand = new Random();
    private Animation animation = new Animation();

    public Enemy(Bitmap image1, Bitmap image2, int x, int y, int w, int h, int s, int numFrames) {
        this.x = x;
        this.y = y;
        width = w;
        height = h;
        score = s;

        speed = 30 + (int)(rand.nextDouble() * score) / 30;

        Bitmap[] images = new Bitmap[numFrames];
        images[0] = image1;
        images[1] = image2;

        animation.setFrames(images, 2);
        animation.setDelay(100 - speed);
    }

    public void update() {
        x -= speed;
        animation.update();
    }

    public void draw(Canvas canvas) {
        try {
            canvas.drawBitmap(animation.getImage(), x, y, null);
        } catch (Exception e) {

        }
    }
}
