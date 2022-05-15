package com.example.dficyclinginstructionsandchecklist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity_page_2 extends AppCompatActivity {
    private TextView participantName, foolCheck;
    private Boolean save_audio_pressed = false, save_vid_pressed = false, save_GPS_pressed = false;
    private String audio_dateTime, video_dateTime, GPS_dateTime;
    private Button audio_button, vid_button, save_finish_button, gps_button;
    private LocationManager locationManager;
    private String latitude_num = " ",longitude_num = " ";
    private String temperature,weatherDescription, city_location, wind_1;

    private TextView temp_audio, temp_video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page2);


        Intent intent = getIntent ();
        Bundle extras = intent.getExtras();
        //String name = extras.getString(MainActivity.EXTRA_TEXT);

        ArrayList<String> test = extras.getStringArrayList("forwarding");

        participantName = (TextView)(findViewById(R.id.p_name));
        audio_button = (Button)(findViewById(R.id.saveAudio));
        vid_button = (Button)(findViewById(R.id.saveVideo2));
        save_finish_button = (Button)(findViewById(R.id.finished));
        gps_button = (Button)(findViewById(R.id.saveGPS));

        participantName.setText("Hi " + test.get(0));

        getGPS();

        save_finish_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(save_audio_pressed && save_vid_pressed && save_GPS_pressed){
                    finished_and_save(test);
                }else if(!save_audio_pressed){
                    Toast.makeText(getApplicationContext(), "You haven't synced the audio time", Toast.LENGTH_LONG).show();
                }else if(!save_GPS_pressed){
                    Toast.makeText(getApplicationContext(), "You haven't synced the GPS time", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(), "You haven't synced the video time", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    public void saveAudio(View v){
        Calendar audio_calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        audio_dateTime = simpleDateFormat.format(audio_calendar.getTime());
        audio_button.setEnabled(false);
        save_audio_pressed = true;
        getWeather();

    }

    public void saveGPS(View v){
        Calendar audio_calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        GPS_dateTime = simpleDateFormat.format(audio_calendar.getTime());
        gps_button.setEnabled(false);
        save_GPS_pressed = true;
    }
    public void audioSyncHelp(View v){
        Intent audioSync_help = new Intent(this,save_audio_help.class);
        startActivity(audioSync_help);
    }

    public void videoSyncHelp(View v){
        Intent vidSync_help = new Intent(this, save_video_help.class);
        startActivity(vidSync_help);
    }
    public void saveVideo(View v){
        Calendar video_calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        video_dateTime = simpleDateFormat.format(video_calendar.getTime());
        vid_button.setEnabled(false);
        save_vid_pressed = true;

    }

    public void finished_and_save(ArrayList<String> information){
        //Toast.makeText(getApplicationContext(), "Definitely enters the saving function", Toast.LENGTH_SHORT).show();
        StringBuilder textToSave = new StringBuilder(100);
        textToSave.append("Participant ID, ").append(information.get(0)).append("\n");
        textToSave.append("Date of ride, ").append(information.get(1)).append("\n");
        textToSave.append("Empatica baseline recording completed, ").append(information.get(11)).append("\n");
        textToSave.append("Attached face angled GoPro, ").append(information.get(2)).append("\n");
        textToSave.append("Forward facing GoPro attached,").append(information.get(3)).append("\n");
        textToSave.append("Microphone attached,").append(information.get(4)).append("\n");
        textToSave.append("GPS + mount attached,").append(information.get(5)).append("\n");
        textToSave.append("GoPro angle has been checked,").append(information.get(6)).append("\n");
        textToSave.append("HRV monitor has been attached,").append(information.get(7)).append("\n");
        textToSave.append("Empatica device properly attached,").append(information.get(8)).append("\n");
        textToSave.append("HRV activity started,").append(information.get(9)).append("\n");
        //textToSave.append("Bike computer activity started,").append(information.get(10)).append("\n");
        //Saving the video and audio start times
        textToSave.append("GPS started at,").append(GPS_dateTime).append('\n');
        textToSave.append("Audio started at,").append(audio_dateTime).append('\n');
        textToSave.append("Video started at,").append(video_dateTime).append('\n');
        textToSave.append("The temp in degrees is,").append(temperature).append('\n');
        textToSave.append("The description is,").append(weatherDescription).append('\n');
        textToSave.append("The wind levels are (km/h),").append(wind_1).append('\n');
        textToSave.append("The location is,").append(city_location).append('\n');

        //Saving to the documents folder of the phones storage
        try{
            String downloadDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath();
            String rider_name = information.get(0);
            String date = information.get(1);
            String time = information.get(12);
            String date_and_time = date + " " + time;
            String path = downloadDirectory + '/' + rider_name + " " + date_and_time + ".txt";

            FileOutputStream fos = new FileOutputStream(path);
            fos.write(textToSave.toString().getBytes(StandardCharsets.UTF_8));
            fos.flush();
            fos.close();

            Toast.makeText(getApplicationContext(), "Data saved", Toast.LENGTH_SHORT).show();
            save_finish_button.setEnabled(false);

        }catch(IOException e){
            e.printStackTrace();
        }
    }


    public void getGPS(){
        locationManager = (LocationManager) (getSystemService(LOCATION_SERVICE));
        if(ContextCompat.checkSelfPermission(MainActivity_page_2.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(MainActivity_page_2.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity_page_2.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1, new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                longitude_num = String.valueOf(location.getLongitude());
                latitude_num = String.valueOf(location.getLatitude());
                Toast.makeText(getApplicationContext(), "Location found", Toast.LENGTH_SHORT).show();

            }
        });
    }
    public void getWeather() {
        Toast.makeText(getApplicationContext(), "Pulling weather", Toast.LENGTH_SHORT).show();

        String url = "https://api.weatherapi.com/v1/current.json?key=3a374bcf175a428d9b270415210712 &q=" + latitude_num + "," + longitude_num + "&aqi=no";
        String urlBackup = "https://api.weatherapi.com/v1/current.json?key=3a374bcf175a428d9b270415210712%20&q=melbourne&aqi=no";
        RequestQueue queue = Volley.newRequestQueue(this);
        if (latitude_num.equals(" ")) {
            url = urlBackup;
        }
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject response) {
                try {
                    JSONObject main_object = response.getJSONObject("current");
                    JSONObject description_object = main_object.getJSONObject("condition");
                    JSONObject location_object = response.getJSONObject("location");
                    temperature = String.valueOf(main_object.getDouble("temp_c"));
                    weatherDescription = description_object.getString("text");
                    city_location = location_object.getString("name");
                    wind_1 = String.valueOf(main_object.getDouble("wind_kph"));
                    Toast.makeText(getApplicationContext(), "Weather data found", Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_LONG).show();
            }
        }
        );
        queue.add(jor);
    }

}