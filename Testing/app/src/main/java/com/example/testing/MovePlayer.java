package com.example.testing;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MovePlayer extends Activity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor sensor;
    GameView ourView;
    private PlayerSprite p1;
    private double sensorX, sensorY;

    @Override
    public void onSensorChanged(SensorEvent event) {
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sensorX = event.values[1]; //swap x and y event.values since phone in landscape
        sensorY = event.values[0];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //check if accelerometer sensor exists
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if(sensorManager.getSensorList((Sensor.TYPE_ACCELEROMETER)).size()!=0) {
            sensor = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        p1 = new PlayerSprite();
        ourView = new GameView(this);
        ourView.resume();
        setContentView(ourView);

        sensorX = 0; sensorY = 0;
    }
    public class GameView extends SurfaceView implements Runnable {
        SurfaceHolder ourHolder;
        Thread ourThread = null;
        boolean isRunning = true;

        public GameView(Context context) {
            super(context);
            ourHolder = getHolder();
        }
        public void pause() {
            isRunning = false;
            while(true) {
                try {
                    ourThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            }
            ourThread = null;
        }
        public void resume() {
            isRunning = true;
            ourThread = new Thread(this);
            ourThread.start();
        }
        @Override
        public void run() {
            while(isRunning) {
                if (!ourHolder.getSurface().isValid())
                    continue;
                Canvas canvas = ourHolder.lockCanvas();
                canvas.drawColor(Color.DKGRAY);
                p1.update(canvas, sensorX, sensorY);
                ourHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

}
