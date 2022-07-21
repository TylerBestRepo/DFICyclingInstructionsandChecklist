package com.example.dficyclinginstructionsandchecklist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;

import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.io.FileOutputStream;
import java.io.IOException;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<String> textToForward;
    //GPS variables
    private String latitude_num = " ",longitude_num = " ";

    private CheckBox lapelMicCheck;
    private CheckBox heartRateCheck;
    private CheckBox goProFaceCheck;
    private CheckBox goProForwardCheck;
    private CheckBox GPSCheck;
    private CheckBox HRVPairedCheck;
    private CheckBox empaticaCheck;
    private CheckBox bikeActivityStartCheck;
    private CheckBox empaticaAttachCheck;
    private CheckBox baselineCheck;
    private CheckBox bikeComputerModeCheck;
    private CheckBox GPSSignalCheck;
    //Boolean variables for if the check marks have been ticked
    private Boolean lapelMicCheckBool = false;
    private Boolean heartRateBool = false;
    private Boolean goProFaceBool = false;
    private Boolean goProForwardBool = false;
    private Boolean GPSCheckBool = false;
    private Boolean hrvAPPstartBool = false;
    private Boolean empaticaBool = false;
    private Boolean bikeActivityStartBool = false;
    private Boolean empaticaActivityBool = false;
    private Boolean baselineBool = false;
    private Boolean bikeComputerModeBool = false;
    private EditText IDinput;
    private Boolean HRVPairedBool = false;
    private Boolean GPSSignalBool = false;
    private TextView ride_date_time;
    private String current_date;

    //Storage Buttons
    String video_dateTime;
    String audio_dateTime;

    //Make sure the pop up message at the start doesn't show up more than once per use
    private Boolean startMessageShown = false;
    private Boolean secondStartMessageShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //GPS and weather
        // Getting the checkbox stuff properly
        lapelMicCheck = (CheckBox)(findViewById(R.id.lapelMicCheck));
        heartRateCheck = (CheckBox)(findViewById(R.id.heartRateCheck));
        goProFaceCheck = (CheckBox)(findViewById(R.id.goProFaceCheck));
        goProForwardCheck = (CheckBox)(findViewById(R.id.goProForwardCheck));
        GPSCheck = (CheckBox)(findViewById(R.id.GPSCheck));
        HRVPairedCheck = (CheckBox)(findViewById(R.id.HRVPairedCheck));
        empaticaCheck = (CheckBox)(findViewById(R.id.empaticaCheck));
        empaticaAttachCheck = (CheckBox)(findViewById(R.id.empaticaCheck));
        baselineCheck = (CheckBox)(findViewById(R.id.baselineCheck));
        bikeComputerModeCheck = (CheckBox)(findViewById(R.id.bikeComputerModeCheck));
        GPSSignalCheck = (CheckBox)(findViewById(R.id.GPSSignalCheck));

        IDinput = (EditText)(findViewById(R.id.IDinput));
        IDinput.setText("");
        //save_data = (Button)(findViewById(R.id.save_data));

        if (!isTaskRoot()
                && getIntent().hasCategory(Intent.CATEGORY_LAUNCHER)
                && getIntent().getAction() != null
                && getIntent().getAction().equals(Intent.ACTION_MAIN)) {
            finish();
            return;
        }


        //Pulling and displaying the current date
        ride_date_time = (TextView)(findViewById(R.id.ride_date));
        Calendar ride_calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        current_date = simpleDateFormat.format(ride_calendar.getTime());
        ride_date_time.setText(current_date);

        if(!startMessageShown){
            alertDialog();
        }

        if(!secondStartMessageShown){
            alertDialogNumberTwo();
        }

        lapelMicCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lapelMicCheckBool = !lapelMicCheckBool;
            }
        });
        empaticaCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                empaticaBool = !empaticaBool;
            }
        });
        heartRateCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                heartRateBool = !heartRateBool;
            }
        });
        goProForwardCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goProForwardBool = !goProForwardBool;
            }
        });
        GPSCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GPSCheckBool = !GPSCheckBool;
            }
        });

        bikeActivityStartCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bikeActivityStartBool = !bikeActivityStartBool;
            }
        });
        empaticaAttachCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                empaticaActivityBool = !empaticaActivityBool;
            }
        });
        baselineCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baselineBool = !baselineBool;
            }
        });

        goProFaceCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goProFaceBool = !goProFaceBool;
            }
        });
        ;
        bikeComputerModeCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bikeComputerModeBool = !bikeComputerModeBool;
            }
        });
        HRVPairedCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HRVPairedBool = !HRVPairedBool;
            }
        });
        ;
        GPSSignalCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GPSSignalBool = !GPSSignalBool;
            }
        });


    }


    public void alertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(true);
        builder.setTitle("Please Read before continuing");
        builder.setMessage("1.It's extremely important you perform the baseline reading before you go on your ride\n"
                +"\n2.Ensure you have replaced the GoPro SD card if necessary\n\n3.Also check to see if the" +
                "GoPro has enough battery to last the rides you plan on doing today"
                + "\n\n4.Make sure not to leave the equipment lying around please! It is expensive and people" +
                " will want to steal it if you leave it out\n\n" + "If you've already done this you can skip"+
                "straight to the incident recording page");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startMessageShown = true;
            }
        });
        builder.setNeutralButton("Incident Recording", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startMessageShown = true;
                openIncidentPage();
            }
        });

        builder.show();
    }

    public void alertDialogNumberTwo(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(true);
        builder.setTitle("Baseline readings");

        String messageString = getResources().getString(R.string.fiveMinutePrepInstructions);
        builder.setMessage(messageString);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startMessageShown = true;
            }
        });
        builder.setNeutralButton("Incident Recording", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startMessageShown = true;
                openIncidentPage();
            }
        });

        builder.show();
    }

    public void bikeComputerMountHelp(View v){
        Intent computerMountHelp = new Intent(this,BikeComputerMountHelp.class);
        startActivity(computerMountHelp);
    }

    public void HRV_attach_help(View v){
        Intent hrvHelp = new Intent(this, HRV_help.class);
        startActivity(hrvHelp);
    }
    public void  hrv_recording_help(View v){
        Intent hrv_app_instructions = new Intent(this, HRV_app_help.class);
        startActivity(hrv_app_instructions);
    }

    public void goProAttachHelp(View v){
        Intent openGoProAttach = new Intent(this,Attach_GoPro.class);
        startActivity(openGoProAttach);
    }

    public void goproforwardHelp(View v){
        Intent frontGoProAttach = new Intent(this,forwardGoProHelp.class);
        startActivity(frontGoProAttach);
    }

    public void micAttachHelp(View v){
        Intent openMicAttachHelp = new Intent(this,Mic_Attach.class);
        startActivity(openMicAttachHelp);
    }
    public void goProAnglingHelp(View v){
        Intent openGoProAngleHelp = new Intent(this,GoPro_Angling.class);
        startActivity(openGoProAngleHelp);
    }

    public void bikeComputerHelp(View v){
        Intent bikeCompHelp = new Intent(this,bike_activity_start_help.class);
        startActivity(bikeCompHelp);
    }

    public void IncidentRecordingPage(View v){
        Intent incident_record = new Intent(this,Incident_recording.class);
        startActivity(incident_record);
    }

    public void main_page_2(View v){
        String current_time;
        String participant_name = IDinput.getText().toString();
        if (participant_name.matches("")){
            Toast.makeText(getApplicationContext(), "You didn't input your name", Toast.LENGTH_SHORT).show();
        }else {
            Intent main_page_2 = new Intent(this, MainActivity_page_2.class);

            textToForward = new ArrayList<String>();
            String date = ride_date_time.getText().toString();


            textToForward.add(participant_name);
            textToForward.add(date);

            if (baselineBool) {
                //textToSave.append("Attached Baseline check, True\n");
                textToForward.add("True");
            } else {
                textToForward.add("False");
            }
            if (lapelMicCheckBool) {
                //textToSave.append("Attached lapel microphone, True\n");
                textToForward.add("True");
            } else {
                textToForward.add("False");
            }
            if (empaticaBool) {
                //textToSave.append("attached Empatica, True\n");
                textToForward.add("True");
            } else {
                textToForward.add("False");
            }
            if (heartRateBool) {
                //textToSave.append("attahced hrv to chest, True\n");
                textToForward.add("True");
            } else {
                textToForward.add("False");
            }
            if (goProFaceBool) {
                //textToSave.append("face angled gopro attached, True\n");
                textToForward.add("True");
            } else {
                textToForward.add("False");
            }
            if (goProForwardBool) {
                //textToSave.append("attached forward facing gopro, True\n");
                textToForward.add("True");
            } else {
                textToForward.add("False");
            }
            if(GPSCheckBool){
                textToForward.add("True");
                //textToSave.append("attached bike computer to bike, True\n");

            }else{
                textToForward.add("False");
            }
            if (bikeComputerModeBool) {
                //textToSave.append("bike computer set to outdoor cycling mode, True\n");
                textToForward.add("True");
            } else {
                textToForward.add("False");
            }
            if (HRVPairedBool) {
                //textToSave.append("HRV paired to bike computer, True\n");
                textToForward.add("True");
            } else {
                textToForward.add("False");
            }
            if (GPSSignalBool){
                textToForward.add("True");
                //textToSave.append("Bike computer paired to gps, True\n");
            }else {
                textToForward.add("False");
            }
            Calendar ride_calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH-mm-ss");
            current_time = simpleDateFormat.format(ride_calendar.getTime());

            textToForward.add(current_time);

            //Adding the list we just generated to the activity
            main_page_2.putStringArrayListExtra("forwarding", (ArrayList<String>) textToForward);
            //Should pass through a list of 11 strings, 2 being actual and 9 strings representing Booleans
            startActivity(main_page_2);
        }
    }
    public void openIncidentPage(){
        Intent incident_record = new Intent(this,Incident_recording.class);
        startActivity(incident_record);
    }

    public void openEmpaticaBaselineHelp(View v){
        Intent baseline_help = new Intent(this, EmapticaBaselineHelp.class);
        startActivity(baseline_help);
    }


}














