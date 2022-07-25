package com.example.dficyclinginstructionsandchecklist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.widget.TextView;

public class warningPage extends AppCompatActivity {

    private Boolean startMessageShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warning_page);

        if (!startMessageShown){
            warningAlertDialog();
        }
    }

    public void warningAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(warningPage.this);
        builder.setCancelable(true);
        builder.setTitle("Please read before continuing");
        builder.setMessage("1.Its very important you perform the baseline reading before you go on your ride\n" +
                "2.Ensure you have replaced the GoPro SD card if necessary\n\n" +
                "3.Also check to see if the GoPro has enough battery to last the rides you plan on doing today\n\n" +
                "4.Make sure not to leave the equipment lying around please! It is expensive and people" +
                "will want to steal it if you leave it out.");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startMessageShown = true;
                new CountDownTimer(250, 1000) {
                    public void onFinish() {
                        // When timer is finished
                        // Execute your code here
                        openAudioPage();
                    }

                    public void onTick(long millisUntilFinished) {
                        // millisUntilFinished    The amount of time until finished.
                    }
                }.start();

            }
        });
        builder.show();

    }

    public void openAudioPage(){
        Intent openAudio = new Intent(this, AudioRecorder.class);
        startActivity(openAudio);
    }
}