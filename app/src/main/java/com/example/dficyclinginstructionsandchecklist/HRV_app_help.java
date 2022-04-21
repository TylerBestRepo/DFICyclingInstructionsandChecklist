package com.example.dficyclinginstructionsandchecklist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

public class HRV_app_help extends AppCompatActivity {
    VideoView video;
    MediaController mediaC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hrv_app_help);
        Button play = (Button) (findViewById(R.id.playVideo));
        video = (VideoView) (findViewById(R.id.video));
        mediaC = new MediaController(this);
        video.setVideoPath("android.resource://"+getPackageName()+"/"+ R.raw.hrvappinstructions);
    }
    public void videoPlay(View v){
        video.setMediaController(mediaC);
        mediaC.setAnchorView(video);
        video.start();
    }
}