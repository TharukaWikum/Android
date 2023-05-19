package com.example.a8_2_1;


import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.Bundle;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ShakeDetector mShakeDetector;
    private ShakeListener mShakeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mShakeDetector = new ShakeDetector();
        mShakeListener = new ShakeListener() {
            @Override
            public void onShake() {
                changeBackgroundColor();
            }
        };
        mShakeDetector.registerListener(mShakeListener);

        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);

        BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                    mShakeDetector.unregisterListener(mShakeListener);
                } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                    mShakeDetector.registerListener(mShakeListener);
                }
            }
        };

        registerReceiver(mReceiver, filter);
    }

    private void changeBackgroundColor() {
        int color = Color.rgb((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256));
        findViewById(android.R.id.content).setBackgroundColor(color);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mShakeDetector.unregisterListener(mShakeListener);
    }
}

