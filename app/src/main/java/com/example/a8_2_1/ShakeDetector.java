package com.example.a8_2_1;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class ShakeDetector implements SensorEventListener {

    private static final float DEFAULT_ACCELERATION_THRESHOLD = 2.0f;
    private static final int DEFAULT_SHAKE_WINDOW_TIME_INTERVAL = 500; // milliseconds
    private static final int DEFAULT_SHAKE_COUNT_THRESHOLD = 2;

    private float accelerationThreshold;
    private int shakeWindowTimeInterval;
    private int shakeCountThreshold;
    private long lastShakeTimestamp;
    private int shakeCount;
    private ShakeListener shakeListener;

    public ShakeDetector() {
        accelerationThreshold = DEFAULT_ACCELERATION_THRESHOLD;
        shakeWindowTimeInterval = DEFAULT_SHAKE_WINDOW_TIME_INTERVAL;
        shakeCountThreshold = DEFAULT_SHAKE_COUNT_THRESHOLD;
    }

    public void registerListener(ShakeListener listener) {
        shakeListener = listener;
    }

    public void unregisterListener(ShakeListener listener) {
        shakeListener = null;
    }

    public void setSensitivity(float accelerationThreshold) {
        this.accelerationThreshold = accelerationThreshold;
    }

    public void setShakeWindowTimeInterval(int shakeWindowTimeInterval) {
        this.shakeWindowTimeInterval = shakeWindowTimeInterval;
    }

    public void setShakeCountThreshold(int shakeCountThreshold) {
        this.shakeCountThreshold = shakeCountThreshold;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (shakeListener == null) {
            return;
        }

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        float acceleration = (float) Math.sqrt(x * x + y * y + z * z) - SensorManager.GRAVITY_EARTH;

        if (acceleration > accelerationThreshold) {
            long now = System.currentTimeMillis();

            if (lastShakeTimestamp == 0) {
                lastShakeTimestamp = now;
                shakeCount = 1;
            } else {
                long timeDiff = now - lastShakeTimestamp;

                if (timeDiff > shakeWindowTimeInterval) {
                    shakeCount = 1;
                } else {
                    shakeCount++;
                }

                lastShakeTimestamp = now;

                if (shakeCount >= shakeCountThreshold) {
                    shakeListener.onShake();
                    shakeCount = 0;
                    lastShakeTimestamp = 0;
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}