package com.example.testing;

import android.app.Activity;
import android.content.Intent;
import android.view.Window;
import android.widget.Toast;
import android.os.Bundle;
import android.view.WindowManager;

public class MainActivity extends Activity {
    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        gameView = new GameView(this);
        setContentView(gameView);
        Intent start = new Intent(this, MovePlayer.class);
        startActivity(start);



    }
}
