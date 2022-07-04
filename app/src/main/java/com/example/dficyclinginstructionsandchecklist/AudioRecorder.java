package com.example.dficyclinginstructionsandchecklist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AudioRecorder extends AppCompatActivity {

    private static int MICROPHONE_PERMISSION_CODE = 200;

    public static final int MIC = 1;

    private MediaRecorder mediaRecorder, mRecorder;
    private MediaPlayer mediaPlayer;
    private TextView stopRecordingMessage;


    private Button stopRecording, startRecording;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_recorder);


        stopRecording = (Button)(findViewById(R.id.stopRecording));
        startRecording = (Button)(findViewById(R.id.startRecording));
        stopRecordingMessage = (TextView)(findViewById(R.id.recordingMessage));

        if (isMicrophonePresent()){
            getMicrophonePermission();
        }

    }

    public void btnRecord(View v){
        
        try {
            /*mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setAudioEncodingBitRate(16*44100);
            mediaRecorder.setAudioSamplingRate(44100);
            mediaRecorder.setOutputFile(getRecordingFilePath());
            mediaRecorder.prepare();
            mediaRecorder.start();
            stopRecording.setVisibility(View.VISIBLE);
            startRecording.setVisibility(View.INVISIBLE);


             */

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

            Toast.makeText(this, "Recording has been started", Toast.LENGTH_SHORT).show();
        } catch(Exception e) {
            e.printStackTrace();
        }
        
    }

    public void btnStopRecord(View V){
        /*mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null; */
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
        Toast.makeText(this, "Recording has been stopped successfully", Toast.LENGTH_SHORT).show();
        stopRecording.setVisibility(View.GONE);
        startRecording.setVisibility(View.VISIBLE);
    }

    public void btnPlayRecording(View v){
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(getRecordingFilePath());
            mediaPlayer.prepare();
            mediaPlayer.start();

            Toast.makeText(this, "Recording is playing", Toast.LENGTH_SHORT).show();

        } catch(Exception e) {
            e.printStackTrace();
        }
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

        // Might need or want to change this path method too. TBD after testing
        String downloadDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath();

        String path = downloadDirectory;
        //ContextWrapper contextWrapper = new ContextWrapper((getApplicationContext()));
        //File downloadsDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        // Will need to pass through the name of the file aka the time and date properly when doing this for real
        Calendar ride_calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String current_date = simpleDateFormat.format(ride_calendar.getTime());

        SimpleDateFormat twenty4HRtime = new SimpleDateFormat("HH-mm-ss");
        String current_time = twenty4HRtime.format(ride_calendar.getTime());

        String date_and_time = current_date + " " + current_time;
        File file = new File(path,date_and_time + ".m4a");

        stopRecordingMessage.setText(date_and_time);

        return file.getPath();
    }
}