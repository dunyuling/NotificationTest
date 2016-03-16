package com.example.notificationtest;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        findViewById(R.id.regular_notification_btn).setOnClickListener(onClickListener());
        findViewById(R.id.special_notification_btn).setOnClickListener(onClickListener());
        findViewById(R.id.progress_notification_btn).setOnClickListener(onClickListener());
        findViewById(R.id.custom_view_notification_btn).setOnClickListener(onClickListener());
    }

    private View.OnClickListener onClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.regular_notification_btn:
                        triggerRegularNotification();
                        break;
                    case R.id.special_notification_btn:
                        triggerSpecialNotification();
                        break;
                    case R.id.progress_notification_btn:
                        triggerProgressNotification();
                        break;
                    case R.id.custom_view_notification_btn:
                        triggerCustomNotification();
//                        custom2();
                        break;

                }
            }
        };
    }

    private void triggerRegularNotification() {
        Intent resultIntent = new Intent(this, RegularNotificationActivity.class);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addParentStack(RegularNotificationActivity.class);
        taskStackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("title")
                .setContentText("regular")
                .setAutoCancel(true);
        builder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }


    private void triggerSpecialNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("title")
                .setContentText("special")
                .setAutoCancel(true);
        ;

        Intent notifyIntent = new Intent(this, SpecialNotificationActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(notifyPendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());

    }

    private void triggerProgressNotification() {
        final NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("Picture Download")
                .setContentText("Download in progress")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_MIN);

        new Thread(new Runnable() {
            @Override
            public void run() {
                int incr;
                for (incr = 0; incr <= 100; incr += 5) {
//                    builder.setProgress(100, incr, false);
                    builder.setProgress(0, 0, true);
                    notificationManager.notify(0, builder.build());
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                builder.setContentText("Download complete")
                        // Removes the progress bar
                        .setProgress(0, 0, false);
                notificationManager.notify(0, builder.build());
            }
        }).start();
    }

    int PAUSE = 0;
    int PLAY = 1;
    String action = "action";
    private void triggerCustomNotification() {

        Intent resultIntent = new Intent(this, RegularNotificationActivity.class);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addParentStack(RegularNotificationActivity.class);
        taskStackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent pauseIntent = new Intent(getApplicationContext(), MusicService.class);
        Intent playIntent = new Intent(getApplicationContext(), MusicService.class);
        pauseIntent.putExtra(action, PAUSE);
        playIntent.putExtra(action, PLAY);
        PendingIntent pause = PendingIntent.getService(getApplicationContext(), PAUSE, pauseIntent, 0);
        PendingIntent play = PendingIntent.getService(getApplicationContext(), PLAY, playIntent, 0);
        RemoteViews remoteViews = new RemoteViews(getPackageName(),
                R.layout.custom_notification);
        remoteViews.setOnClickPendingIntent(R.id.btn_1,pause);
        remoteViews.setOnClickPendingIntent(R.id.btn_2,play);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContent(remoteViews)
                .setTicker("aa")
                .setDefaults(Notification.DEFAULT_SOUND)
                .setLights(Color.WHITE, 500, 500)
                .setOngoing(false)
                .setAutoCancel(true);


        builder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
