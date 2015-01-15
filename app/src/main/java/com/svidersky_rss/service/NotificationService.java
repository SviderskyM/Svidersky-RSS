package com.svidersky_rss.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.svidersky_rss.R;
import com.svidersky_rss.activity.MainActivity;
import com.svidersky_rss.fragments.BaseFragment;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Eren on 18.12.2014.
 */
public class NotificationService extends Service {

    private NotificationManager notifyManager;
    private static boolean first = true;
    private int count = 20;
    private Intent resultIntent;
    private PendingIntent resultPendingIntent;
    private Resources res;
    private NotificationCompat.Builder builder;
    private String lastVideo;
    private Context ctx;
    private int temp = 0;


    Timer timer;
    TimerTask tTask;
    long interval = 7200000;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (first) {
            showNotification();
            first = false;

        }
        ctx = this;
        timer = new Timer();
        schedule();
    }

    private void schedule() {
        tTask = new TimerTask() {
            @Override
            public void run() {
                BaseFragment.GetData data = new BaseFragment.GetData(ctx);
                data.execute();
                temp = data.getCount();
                if (temp != count) {
                    count = temp;
                    lastVideo = data.getLastNews();
                    updateNotification();
                }
            }
        };
        timer.schedule(tTask, 20000, interval);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        notifyManager.cancel(99);
    }


    public void showNotification() {
        resultIntent = new Intent(this, MainActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, 0);
        res = this.getResources();
        int numMessage = 0;
        builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.ic_launcher))
                .setTicker(res.getString(R.string.news))
                .setWhen(System.currentTimeMillis())
                .setNumber(++numMessage)
                        //.setOngoing(true)
                .setAutoCancel(true)
                .setContentTitle(res.getString(R.string.app_name))
                .setContentText(res.getString(R.string.start))
                .setContentIntent(resultPendingIntent);
        notifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notifyManager.notify(99, builder.build());
    }

    private void updateNotification() {
        resultIntent = new Intent(this, MainActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, 0);
        builder.setTicker(count + " " + res.getString(R.string.news))
                .setWhen(System.currentTimeMillis())
                .setNumber(count)
                .setContentTitle(res.getString(R.string.app_name))
                .setContentText(lastVideo)
                .setContentIntent(resultPendingIntent);
        notifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notifyManager.notify(111, builder.build());

    }
}
