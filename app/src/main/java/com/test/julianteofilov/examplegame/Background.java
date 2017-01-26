package com.test.julianteofilov.examplegame;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by julian.teofilov on 23/1/2017.
 */

public class Background {
    private Bitmap image;
    private int x;
    private int y;
    private int dx;

    public Background(Bitmap res) {
        image = res;
        dx = GamePanel.MOVESPEED;
    }

    public void update() {
        x+=dx;
        if (x < -GamePanel.WIDTH) {
            x = 0;
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, y, null);
        if (x < GamePanel.WIDTH / 2) {
            canvas.drawBitmap(image, x+GamePanel.WIDTH, y, null);
            canvas.drawBitmap(image, x+(GamePanel.WIDTH * 2), y, null);
        }
    }
}
