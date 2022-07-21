package com.example.dficyclinginstructionsandchecklist;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;



import org.w3c.dom.Text;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class AudioRecorder extends AppCompatActivity {

    private static int MICROPHONE_PERMISSION_CODE = 200;

    public static final int MIC = 1;

    // Yoinking things from other apps notifications
    private final static String CHANNEL_ID = "com.com.example.dficyclinginstructionsandchecklist.NotificationId";
    private final static String CHANNEL_NAME = "Default";
    private RemoteViews remoteViewsSmall;
    private PendingIntent contentPendingIntent;
    private boolean started = false;
    private static final int NOTIF_ID = 101;

    private NotificationManager mNotificationManager;
    NotificationCompat.Builder mBuilder;

    private MediaRecorder mRecorder;
    private TextView AudioTopMessage;
    private TextView stopRecordingMessage;
    private Button stopRecording, startRecording;
    private TextView recordingStartTime;
    private TextView elapsedTime;
    private TextView verbalMessage;
    private Timer timer;
    private TimerTask timerTask;
    private Double time = 0.0;

    private Boolean startMessageShown = false;


    private NotificationManager notificationManager;



    // To hopefully keep the audio recording on in the back ground
    private static final String TAG = AudioRecorder.class.getSimpleName();
    private PowerManager.WakeLock mWakeLock = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_recorder);

        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
        mWakeLock.acquire();

        // Defining and linking buttons to the xml code
        stopRecording = (Button)(findViewById(R.id.stopRecording));
        startRecording = (Button)(findViewById(R.id.startRecording));
        stopRecordingMessage = (TextView)(findViewById(R.id.recordingMessage));
        recordingStartTime = (TextView) (findViewById(R.id.recordingStartTime));
        elapsedTime = (TextView)(findViewById(R.id.elapsedTime));
        AudioTopMessage = (TextView)(findViewById(R.id.AudioMessage));

        verbalMessage = (TextView)(findViewById(R.id.verbalMessage));

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        timer = new Timer();

        if (isMicrophonePresent()){
            getMicrophonePermission();
        }

        if(!startMessageShown){
            alertDialog();
        }


    }

    public void btnRecord(View v){
        
        try {
            Intent intent = new Intent(this,myForegroundService.class);
            intent.setAction(myForegroundService.ACTION_START_RECORDING_SERVICE);

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                startForegroundService(intent);
            }
            else {
                startService(intent);
            }

            stopRecording.setVisibility(View.VISIBLE);
            startRecording.setVisibility(View.INVISIBLE);
            recordingStartTime.setVisibility(View.VISIBLE);
            elapsedTime.setVisibility(View.VISIBLE);
            time = 0.0;
            elapsedTime.setText(formatTime(0,0,0));
            startTimer();
            recordingMessage();

            AudioTopMessage.setVisibility(View.INVISIBLE);


            //Keeping the screen on whether it actually keeps the display on or it makes it thinks it does.
            // Keep checking where this is called in the other app!

            verbalMessage.setVisibility(View.VISIBLE);

            Toast.makeText(this, "Recording has been started", Toast.LENGTH_SHORT).show();
        } catch(Exception e) {
            e.printStackTrace();
        }

        /*Probably going to get the app to immediately go to the next "Main Page" after hitting record
         To prevent any annoying coding shenanigans
         */
        Intent straightToMainPage = new Intent(this,MainActivity.class);
        startActivity(straightToMainPage);
        
    }

    private void startTimer() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        time++;
                        elapsedTime.setText(getTimerText());
                    }
                });
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

    public void btnStopRecord(View V){
        Intent intent = new Intent(this,myForegroundService.class);
        intent.setAction(myForegroundService.ACTION_STOP_RECORDING_SERVICE);
        startForegroundService(intent);
        Toast.makeText(this, "Recording has been stopped successfully", Toast.LENGTH_SHORT).show();
        stopRecording.setVisibility(View.GONE);
        startRecording.setVisibility(View.VISIBLE);
        timerTask.cancel();
        stopRecordingMessage.setText("Recording has been saved!");
        verbalMessage.setVisibility(View.INVISIBLE);

    }


    private boolean isMicrophonePresent(){
        return this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE);
    }

    private void getMicrophonePermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
        == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this,new String [] {Manifest.permission.RECORD_AUDIO},MICROPHONE_PERMISSION_CODE);
        }

    }

    private void recordingMessage() {
        Calendar ride_calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String current_date = simpleDateFormat.format(ride_calendar.getTime());

        SimpleDateFormat twenty4HRtime = new SimpleDateFormat("HH-mm-ss");

        String current_time = twenty4HRtime.format(ride_calendar.getTime());
        String date_and_time = current_date + " " + current_time;

        SimpleDateFormat diffFormat = new SimpleDateFormat("HH:mm:ss");
        String current_time_diffFormat = diffFormat.format(ride_calendar.getTime());


        String concatenated_message_time = "Recording started at:\n" + current_time_diffFormat;
        recordingStartTime.setText(concatenated_message_time);

        stopRecordingMessage.setText(date_and_time);
    }


    private Notification buildNotification() {
        // Create notification builder.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);

        builder.setWhen(System.currentTimeMillis());
        builder.setSmallIcon(R.drawable.ic_record_rec);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setPriority(NotificationManager.IMPORTANCE_MAX);
        } else {
            builder.setPriority(Notification.PRIORITY_MAX);
        }
        // Make head-up notification.
        builder.setContentIntent(contentPendingIntent);
        builder.setCustomContentView(remoteViewsSmall);
//		builder.setCustomBigContentView(remoteViewsBig);
        builder.setOnlyAlertOnce(true);
        builder.setDefaults(0);
        builder.setSound(null);
        return builder.build();
    }

    private void createNotificationChannel(String channelId, String channelName) {
        NotificationChannel channel = notificationManager.getNotificationChannel(channelId);
        if (channel == null) {
            NotificationChannel chan = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            chan.setLightColor(Color.BLUE);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            chan.setSound(null, null);
            chan.enableLights(false);
            chan.enableVibration(false);

            notificationManager.createNotificationChannel(chan);
        }
    }

    private PendingIntent createContentIntent() {
        // Create notification default intent.
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        return PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
    }

    public void makeNotification(View V) {
        mBuilder =
                new NotificationCompat.Builder(this.getApplicationContext(), "notify_001");

        Context context = this.getApplicationContext();
        Intent ii = new Intent(this.getApplicationContext(), audioNotification.class);
        ii.setAction(Intent.ACTION_MAIN);
        ii.addCategory(Intent.CATEGORY_LAUNCHER);
        ii.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, ii, 0);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText("Running for 0:01:45");
        bigText.setBigContentTitle("Audio recording has started!");
        bigText.setSummaryText("Audio Recorder");

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
        mBuilder.setContentTitle("Recording Started");



        mBuilder.setSilent(true);

        mBuilder.setStyle(bigText);

        mNotificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        ii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

// === Removed some obsoletes
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "Your_channel_id";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }
        mNotificationManager.notify(0, mBuilder.build());
    }

    public void updateBigText(){
        mBuilder.setContentTitle(getTimerText());

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();

        String concat = "Running for " + getTimerText();
        bigText.bigText("Audio recording has started!");
        bigText.setBigContentTitle(concat);
        bigText.setSummaryText("Audio Recorder");

        mBuilder.setStyle(bigText);
        mBuilder.setSilent(true);

        mNotificationManager.notify(0,mBuilder.build());
    }

    public void alertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(AudioRecorder.this);
        builder.setCancelable(true);
        builder.setTitle("Welcome!");
        String messageString = getResources().getString(R.string.AudioPopUpString);

        builder.setMessage(messageString);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startMessageShown = true;
            }
        });
        builder.show();
    }

}