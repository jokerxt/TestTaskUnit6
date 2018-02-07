package ru.jxt.testtaskunit6.model;

import android.os.Handler;

class AppTimer implements Runnable {

    private long startTime;
    private Handler handler;
    private MainModelImpl.TickCallback tickCallback;

    AppTimer() {
        handler = new Handler();
    }

    void start(MainModelImpl.TickCallback tickCallback) {
        this.tickCallback = tickCallback;
        startTime = System.currentTimeMillis();
        handler.postDelayed(this, 0);
    }

    void stop() {
        handler.removeCallbacks(this);
    }

    @Override
    public void run() {
        handler.postDelayed(this, 1000);

        if (tickCallback != null) {
            long currentMillis = System.currentTimeMillis() - startTime;
            tickCallback.onTickCallback(currentMillis);
        }
    }
}
