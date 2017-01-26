package com.test.julianteofilov.examplegame;

import android.graphics.Rect;

/**
 * Created by julian.teofilov on 24/1/2017.
 */

public abstract class GameObject {
    protected int x;
    protected int y;
    protected int dy;
    protected int dx;
    protected int width;
    protected int height;

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public Rect getRectangle() {
        return new Rect(x + 2, y + 2, x + width - 2, y + height - 2);
    }
}
