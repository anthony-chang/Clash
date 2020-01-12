package com.example.testing;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class PlayerSprite {
    private int width, height;
    private double x, y, vx, vy, ax, ay;
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels-100;

    public PlayerSprite() {
        width = 100;
        height = 100;
        x=screenWidth/2;
        y=screenHeight/2;
        vx = vy = ax = ay = 0;
    }
    public void update(Canvas canvas, double sensorX, double sensorY) {
        ax = sensorX;
        ay = sensorY;
        vx = (vx + ax)*0.85;
        vy = (vy + ay)*0.85;
        x+=vx;
        y+=vy;
        if (x > screenWidth - width || x < 0) {
            vx *= -1;
            if (x<0) x = 0;
            if (x > screenWidth - width) x = screenWidth - width;
        }
        if (y > screenHeight - height || y < 0) {
            vy *= -1;
            if (y<0) y = 0;
            if (y > screenHeight - height) y = screenHeight - height;
        }
        draw(canvas);

    }
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.rgb(0, 100, 255));
        canvas.drawRect((int)x, (int)y, (int)x+width, (int)y+height, paint);

        Paint textpaint = new Paint();
        textpaint.setColor(Color.BLACK);
        textpaint.setTextSize(50);
        canvas.drawText(x+", "+y, 10, 40, textpaint);
    }
}
