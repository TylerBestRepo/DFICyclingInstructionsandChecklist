package com.example.dficyclinginstructionsandchecklist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AudioRecorder extends AppCompatActivity {

    private static int MICROPHONE_PERMISSION_CODE = 200;

    public static final int MIC = 1;

    private MediaRecorder mediaRecorder, mRecorder;
    private MediaPlayer mediaPlayer;
    private TextView stopRecordingMessage;
    private TextView participantName;


    private Button stopRecording, startRecording;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_recorder);

        // Getting the name of the participant from the previous page
        participantName = (TextView)(findViewById(R.id.namePassThrough));

        Intent intent = getIntent ();
        Bundle extras = intent.getExtras();
        //String name = extras.getString(MainActivity.EXTRA_TEXT);

        ArrayList<String> test = extras.getStringArrayList("nameForward");

        participantName.setText(test.get(0));

        stopRecording = (Button)(findViewById(R.id.stopRecording));
        startRecording = (Button)(findViewById(R.id.startRecording));
        stopRecordingMessage = (TextView)(findViewById(R.id.recordingMessage));

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

            Toast.makeText(this, "Recording has been started", Toast.LENGTH_SHORT).show();
        } catch(Exception e) {
            e.printStackTrace();
        }
        
    }

    public void btnStopRecord(View V){
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
        Toast.makeText(this, "Recording has been stopped successfully", Toast.LENGTH_SHORT).show();
        stopRecording.setVisibility(View.GONE);
        startRecording.setVisibility(View.VISIBLE);
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