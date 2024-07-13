package com.ppngp.ppn;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {


    @SuppressLint({"HardwareIds", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tr(LoginPage.class);
            }
        }, 2000);
    }

    private void tr(Class c) {
        Intent i = new Intent(this, c);
        startActivity(i);
        this.finish();
    }
}