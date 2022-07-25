package com.example.dficyclinginstructionsandchecklist;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.os.Process;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import org.w3c.dom.Text;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class myForegroundService extends Service
{
    public static final String ACTION_START_RECORDING_SERVICE = "ACTION_START_RECORDING_SERVICE";
    public static final String ACTION_STOP_RECORDING_SERVICE = "ACTION_STOP_RECORDING_SERVICE";

    private MediaRecorder mRecorder;



    private static final String TAG = "V.General";
    private TimerTask timerTask;
    private Timer timer;
    private Notification notification;

    private Double time = 0.0;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        createNotificationChannel();

        String action = intent.getAction();
        if (action != null && !action.isEmpty()) {
            switch (action) {
                case ACTION_START_RECORDING_SERVICE:
                     startRecording();
                    break;
                case ACTION_STOP_RECORDING_SERVICE:
                    stopRecording();
                    break;
            }
        }


        Intent intent1 = new Intent(this, AudioRecorder.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent1,0);

        notification = new NotificationCompat.Builder(this, "ChannelId1")
                .setContentTitle("My Audio recorder")
                .setContentText("Audio is recording")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent).build();

        startForeground(1,notification);



        return START_STICKY;
    }


    private void createNotificationChannel() {
        // We have to check if OS is oreo or above

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(
                    "ChannelId1", "Foreground notification", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);

        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "Just checking that the foreground service is properly being destroyed");
        stopForeground(true);
        stopSelf();
        super.onDestroy();
    }

    public void changeText(){
        Log.i(TAG, "precursor: Cant change the text but this is something isn't it?");
    }

    private void changeTextv2() {
        Log.i(TAG, "precursor: Saying something else different now. This is the button that would end the" +
                "foreground service!");
    }


    public void startRecording(){
        Thread recordThread = new Thread(() -> {
            try {
                android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_AUDIO);
                mRecorder = new MediaRecorder();
                mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.HE_AAC);
                mRecorder.setAudioEncodingBitRate(384000);
                mRecorder.setAudioSamplingRate(44100);

                mRecorder.setOutputFile(getRecordingFilePath());
                mRecorder.prepare();
                mRecorder.start();

                //makeNotification();
                Log.i(TAG, "precursor: Recording should be starting now!");
                Toast.makeText(this, "Recording has been started", Toast.LENGTH_SHORT).show();

                // Starting the timer in the background too so the notification can be easily updated
                startTimer();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // start the thread
        recordThread.start();

    }




    public void stopRecording(){
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
        Toast.makeText(this, "Recording has been stopped successfully", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "precursor: Recording should be stopping now!");
        onDestroy();

    }

    private String getRecordingFilePath() {
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath();
        Calendar ride_calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String current_date = simpleDateFormat.format(ride_calendar.getTime());

        SimpleDateFormat twenty4HRtime = new SimpleDateFormat("HH-mm-ss");

        String current_time = twenty4HRtime.format(ride_calendar.getTime());
        String date_and_time = current_date + " " + current_time;
        File file = new File(path,date_and_time + ".m4a");

        SimpleDateFormat diffFormat = new SimpleDateFormat("HH:mm:ss");
        String current_time_diffFormat = diffFormat.format(ride_calendar.getTime());


        return file.getPath();
    }

    private void startTimer() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                time++;
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0 , 1000);
    }
    private String getTimerText() {
        int rounded = (int) Math.round(time);
        int seconds = ((rounded % 86400) % 3600 ) % 60;
        int minutes = ((rounded % 86400) % 3600 ) / 60;
        int hours = ((rounded % 86400) / 3600 );

        return formatTime(seconds, minutes, hours);
    }

    private String formatTime(int seconds, int minutes, int hours) {
        return String.format("%02d", hours) + " : " + String.format("%02d", minutes) + " : " + String.format("%02d", seconds);
    }


}
