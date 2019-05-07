package com.lecture.gl.servicesnotifications;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class DownloaderService extends Service {
    public static String TAG = "MYDWN";

    public DownloaderService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Downloader service is started");

        String intentAction = intent.getAction();
        if(intentAction.equals("download_file")) {

            performTask();
        }
        else if(intentAction.equals("send_file")){
            sendPerform();
        }
        Log.d(TAG, "Downloader service is finished");

        return START_STICKY; //Service still running if the app will dissmissed
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void sendPerform(){
        throw new UnsupportedOperationException("sending operation not supported for now!");
    }

    private void performTask(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(5000);
                    Intent intent = new Intent();
                    intent.setAction("download_operation");
                    intent.putExtra("dowload_url", "www.receiver.com");
                    sendBroadcast(intent);

                }
                catch(InterruptedException e){

                }

            }
        });
        thread.start();

        /*This piece of codes will make UI to be freezed.
        try {
            Thread.sleep(5000);

        }
        catch(InterruptedException e){

        }*/


    }
}
