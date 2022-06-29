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
import android.widget.Toast;

import java.io.File;

public class AudioRecorder extends AppCompatActivity {

    private static int MICROPHONE_PERMISSION_CODE = 200;

    public static final int MIC = 1;

    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_recorder);

        if (isMicrophonePresent()){
            getMicrophonePermission();
        }
    }

    public void btnRecord(View v){
        
        try {
            mediaRecorder = new MediaRecorder();
            //mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setOutputFile(getRecordingFilePath());
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.prepare();
            mediaRecorder.start();

            Toast.makeText(this, "Recording has been started", Toast.LENGTH_SHORT).show();
        } catch(Exception e) {
            e.printStackTrace();
        }
        
    }

    public void btnStopRecord(View V){
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;

        Toast.makeText(this, "Recording has been stopped successfully", Toast.LENGTH_SHORT).show();

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
        ContextWrapper contextWrapper = new ContextWrapper((getApplicationContext()));
        File downloadsDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        // Will need to pass through the name of the file aka the time and date properly when doing this for real
        File file = new File(downloadsDirectory,"testRecordingFile" + ".mp3");
        return file.getPath();
    }
}