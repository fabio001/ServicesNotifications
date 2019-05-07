package com.lecture.gl.servicesnotifications;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("download_operation");//Should be the same action in service
        registerReceiver(new DownloaderBroadcastListener(), intentFilter);

    }

    public void downloaderServiceRun(View view) {
        Intent serviceIntent = new Intent(this, DownloaderService.class);
        serviceIntent.putExtra("the_url_download", "http://www.google.com");
        serviceIntent.setAction("download_file");
        startService(serviceIntent);

    }


    private class DownloaderBroadcastListener extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String the_url = intent.getStringExtra("dowload_url");
            Log.d(DownloaderService.TAG, "The file in "+ the_url+ " finished");
            NotificationManager nm =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ) {
                NotificationChannel notificationChannel =
                        new NotificationChannel("my_channel", "downloader",
                                NotificationManager.IMPORTANCE_DEFAULT);
                nm.createNotificationChannel(notificationChannel);
            }


            Intent resultIntent = new Intent(MainActivity.this, MainActivity.class);
            // Create the TaskStackBuilder and add the intent, which inflates the back stack
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(MainActivity.this);
            stackBuilder.addNextIntentWithParentStack(resultIntent);
            // Get the PendingIntent containing the entire back stack
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);



            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(MainActivity.this,"my_channel" )
                    .setContentTitle("ServiceNot's")
                    .setAutoCancel(true)
                    .setContentText(the_url + " downloaded")
                    .setContentIntent(resultPendingIntent)
                    .setSmallIcon(R.drawable.ic_file_download_black_24dp);

            Notification notification = notificationBuilder.build();
            nm.notify(1234, notification);

        }
    }

}
