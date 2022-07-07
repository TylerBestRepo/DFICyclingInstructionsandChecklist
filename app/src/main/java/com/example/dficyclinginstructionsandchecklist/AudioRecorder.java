package com.example.dficyclinginstructionsandchecklist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.view.View;
import android.widget.Button;
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

    private MediaRecorder mRecorder;
    private TextView stopRecordingMessage;
    private Button stopRecording, startRecording;
    private TextView recordingStartTime;
    private TextView elapsedTime;
    private TextView verbalMessage;
    private Timer timer;
    private TimerTask timerTask;
    private Double time = 0.0;

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

        verbalMessage = (TextView)(findViewById(R.id.verbalMessage));

        timer = new Timer();

        if (isMicrophonePresent()){
            getMicrophonePermission();
        }

    }

    public void btnRecord(View v){
        
        try {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.HE_AAC);
            mRecorder.setAudioEncodingBitRate(384000);
            mRecorder.setAudioSamplingRate(44100);

            mRecorder.setOutputFile(getRecordingFilePath());
            mRecorder.prepare();
            mRecorder.start();

            stopRecording.setVisibility(View.VISIBLE);
            startRecording.setVisibility(View.INVISIBLE);
            recordingStartTime.setVisibility(View.VISIBLE);
            elapsedTime.setVisibility(View.VISIBLE);
            time = 0.0;
            elapsedTime.setText(formatTime(0,0,0));
            startTimer();

            verbalMessage.setVisibility(View.VISIBLE);

            Toast.makeText(this, "Recording has been started", Toast.LENGTH_SHORT).show();
        } catch(Exception e) {
            e.printStackTrace();
        }
        
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
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
        Toast.makeText(this, "Recording has been stopped successfully", Toast.LENGTH_SHORT).show();
        stopRecording.setVisibility(View.GONE);
        startRecording.setVisibility(View.VISIBLE);
        timerTask.cancel();
        stopRecordingMessage.setText("Recording has been saved!");
        verbalMessage.setVisibility(View.INVISIBLE);
        // Releasing the wake lock to prevent battery drain
        mWakeLock.release();
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


        String concatenated_message_time = "Recording started at:\n" + current_time_diffFormat;
        recordingStartTime.setText(concatenated_message_time);

        stopRecordingMessage.setText(date_and_time);

        return file.getPath();
    }
}