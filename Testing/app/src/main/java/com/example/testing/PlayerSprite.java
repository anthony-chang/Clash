package com.example.testing;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class PlayerSprite {
    private int x, y, width, height;
    private int xVelocity = 10;
    private int yVelocity = 5;
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    public PlayerSprite() {
        x=100;
        y = 100;
        width = 100;
        height = 100;
    }
    public void update() {
        x += xVelocity;
        y += yVelocity;
        if (x > screenWidth - width || x < 0) {
            xVelocity = xVelocity * -1;
        }
        if (y > screenHeight - height || y < 0) {
            yVelocity = yVelocity * -1;
        }

    }
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.rgb(0, 100, 100));
        canvas.drawRect(x, y, x+width, y+height, paint);
    }
}
