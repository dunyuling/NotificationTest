package com.example.notificationtest;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MusicService extends Service {
    final String TAG = "MusicService";
    public MusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int data = (int) intent.getExtras().get("action");
        Log.i(TAG,data + " \t " );
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(0);
        return super.onStartCommand(intent, flags, startId);
    }
}
