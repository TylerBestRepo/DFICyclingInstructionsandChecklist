package com.example.dficyclinginstructionsandchecklist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

public class testActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }


    public void buttonClick(View V){
        Intent intent = new Intent(this,myForegroundService.class);
        intent.setAction(myForegroundService.ACTION_START_RECORDING_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            startForegroundService(intent);
        }
        else {
            startService(intent);
        }
    }

    public void secondButtonClick(View V) {
        Intent intent = new Intent(this,myForegroundService.class);
        intent.setAction(myForegroundService.ACTION_STOP_RECORDING_SERVICE);
        startForegroundService(intent);
    }

}