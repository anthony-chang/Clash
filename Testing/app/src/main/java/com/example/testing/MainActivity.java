package com.example.testing;

import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void onButtonTap(View v) {
        Toast test = Toast.makeText(getApplicationContext(), "Hello World!", Toast.LENGTH_LONG);
        test.show();
    }
}
