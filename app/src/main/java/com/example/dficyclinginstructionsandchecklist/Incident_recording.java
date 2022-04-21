package com.example.dficyclinginstructionsandchecklist;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Incident_recording extends AppCompatActivity {
    EditText temporary;
    int counter = 0;
    private String current_date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident_recording);

        //Date retrieving
        Calendar ride_calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy 'at' HHmmss");
        current_date = simpleDateFormat.format(ride_calendar.getTime());


        final LinearLayout linearLayout = findViewById(R.id.linear_layout);
        //Name input box
        EditText name_input = new EditText(getApplicationContext());
        name_input.setHint("e.g Ben");
        name_input.setText("");
        name_input.setId(R.id.incident_name_input);
        linearLayout.addView(name_input);

        //Adding in starting text
        TextView opening_message = new TextView(getApplicationContext());
        opening_message.setText(getResources().getString(R.string.message_1));
        opening_message.setTextSize(20);
        opening_message.setTypeface(null, Typeface.BOLD);
        linearLayout.addView(opening_message);
        //Adding in edittext
        EditText incident_input_new = new EditText(getApplicationContext());
        incident_input_new.setText("");
        String hint_get = getResources().getString(R.string.input_hint);
        incident_input_new.setHint(hint_get);
        incident_input_new.setGravity(Gravity.TOP);
        incident_input_new.setId(R.id.input_1);
        incident_input_new.setMaxLines(12);
        linearLayout.addView(incident_input_new);
        //Adding in the spacer
        Button buttonSpacer = new Button(getApplicationContext());
        buttonSpacer.setVisibility(View.INVISIBLE);
        //linearLayout.addView(buttonSpacer);
        //Adding in next button
        Button replacement_button_original = new Button(getApplicationContext());
        replacement_button_original.setText("Another incident input");
        replacement_button_original.setBackgroundColor(Color.RED);
        replacement_button_original.setTextColor(Color.WHITE);
        replacement_button_original.setId(R.id.incident_replacement_button);
        linearLayout.addView(replacement_button_original);
        //Adding in the spacer
        Button buttonSpacer2 = new Button(getApplicationContext());
        buttonSpacer2.setVisibility(View.INVISIBLE);
        linearLayout.addView(buttonSpacer2);
        // Gotta input the save button in
        Button save_incidents = new Button(getApplicationContext());
        save_incidents.setText("Save and Finish");
        save_incidents.setBackgroundColor(Color.GREEN);
        save_incidents.setTextColor(Color.WHITE);
        save_incidents.setId(R.id.save_incidents);
        save_incidents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save_incidents_data();
            }
        });
        linearLayout.addView(save_incidents);

        replacement_button_original.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replace_next_button_step_1();
            }
        });

    }
    private void replace_next_button_step_1(){
        final LinearLayout linearLayout = (LinearLayout) (findViewById(R.id.linear_layout));
        //Getting input from first input to make sure they've actually input something
        String temp = "";
        if (counter == 2){
            Toast.makeText(getApplicationContext(), "You can only input your top 3 incidents", Toast.LENGTH_SHORT).show();
        }else{
            if (counter == 0){
                temporary = (EditText) (findViewById(R.id.input_1));
                temp = temporary.getText().toString();
            } else if (counter == 1) {
                EditText temporary_2 = (EditText) (findViewById(R.id.input_2));
                //temporary_2.setText("");
                temp = temporary_2.getText().toString();
            }else if (counter == 2) {
                EditText temporary_3 = (EditText) (findViewById(R.id.input_3));
                temp = temporary_3.getText().toString();

            }
            if (temp.matches("")){
                Toast.makeText(getApplicationContext(), "Input your current incident before adding another", Toast.LENGTH_SHORT).show();
            }else{
                counter++;
                TextView textView = new TextView(getApplicationContext());
                int display_num = counter + 1;
                textView.setText("Incident no." + display_num + " description:");
                textView.setTypeface(null, Typeface.BOLD);
                textView.setTextSize(20);
                linearLayout.addView(textView);
                Button buttonSpacer = new Button(getApplicationContext());
                buttonSpacer.setVisibility(View.INVISIBLE);
                //linearLayout.addView(buttonSpacer);
                //These edit texts may need to have some coding built in based on names and the counter
                EditText incident_input_new = new EditText(getApplicationContext());
                if (counter == 1){
                    incident_input_new.setId(R.id.input_2);
                }else if(counter == 2){
                    incident_input_new.setId(R.id.input_3);
                }
                String hint_get = getResources().getString(R.string.input_hint);
                incident_input_new.setHint(hint_get);
                incident_input_new.setGravity(Gravity.TOP);
                incident_input_new.setMaxLines(12);
                linearLayout.addView(incident_input_new);
                replace_next_button();
            }
        }

    }


    private void replace_next_button(){
        Button replace_button = (Button) (findViewById(R.id.incident_replacement_button));
        Button remove_save = (Button) (findViewById(R.id.save_incidents));
        final LinearLayout linearLayout = (LinearLayout) (findViewById(R.id.linear_layout));
        linearLayout.removeView(replace_button);
        linearLayout.removeView(remove_save);
        Button replacement_button = new Button(getApplicationContext());
        replacement_button.setText("Another incident input");
        replacement_button.setBackgroundColor(Color.RED);
        replacement_button.setTextColor(Color.WHITE);
        replacement_button.setId(R.id.incident_replacement_button);
        replacement_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replace_next_button_step_1();
            }
        });
        Button buttonSpacer2 = new Button(getApplicationContext());
        buttonSpacer2.setVisibility(View.INVISIBLE);

        //Dont want to have another incident input button when the max limit is reached
        if(counter != 2){
            linearLayout.addView(replacement_button);
            linearLayout.addView(buttonSpacer2);
        }

        // Gotta input the save button in
        Button save_incidents = new Button(getApplicationContext());
        save_incidents.setText("Save and Finish");
        save_incidents.setBackgroundColor(Color.GREEN);
        save_incidents.setTextColor(Color.WHITE);
        save_incidents.setId(R.id.save_incidents);
        save_incidents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save_incidents_data();
            }
        });
        linearLayout.addView(save_incidents);
    }

    public void save_incidents_data(){
        EditText name_get;
        Boolean name_input_done = false;
        name_get = (EditText)(findViewById(R.id.incident_name_input));
        String name_check = name_get.getText().toString();
        if(name_check.matches("")){
            Toast.makeText(getApplicationContext(), "You didn't enter your name", Toast.LENGTH_SHORT).show();
            name_input_done = false;
        }else{
            name_input_done = true;
        }

        EditText temp_1, temp_2,temp_3;
        String string_1, string_2,string_3;
        temp_1 = (EditText)(findViewById(R.id.input_1));
        string_1 = temp_1.getText().toString();
        Button save_button;
        save_button = (Button)(findViewById(R.id.save_incidents));
        StringBuilder textToSave = new StringBuilder(100);
        Boolean writingFile = false;

        if (counter == 2){
            temp_3 = (EditText)(findViewById(R.id.input_3));
            string_3 = temp_3.getText().toString();
        }

        if(counter == 0 && string_1.matches("")){
            Toast.makeText(getApplicationContext(), "You didn't enter an incident to save", Toast.LENGTH_SHORT).show();
        }else{
            //Saving to a text file
            textToSave.append("Incident 1: ").append(string_1).append("\n");
            writingFile = true;
            save_button.setEnabled(false);
        }
        if (counter == 1){
            temp_2 = (EditText)(findViewById(R.id.input_2));
            string_2 = temp_2.getText().toString();
            if (counter == 1 && string_2.matches("")){
                textToSave.append("Incident 1: ").append(string_1).append("\n");
            }else{
                textToSave.append("Incident 1: ").append(string_1).append("\n");
                textToSave.append("Incident 2: ").append(string_2).append("\n");
                writingFile = true;
            }
        }

        if(writingFile && name_input_done){ // and name_input_done
            try{
                //Getting file saving ready
                //Saving to the documents folder of the phones storage
                String downloadDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath();
                String rider_name = name_check;
                String date = current_date;
                String path = downloadDirectory + "/IncidentReport " + rider_name + " " + date + ".txt";
                FileOutputStream fos = new FileOutputStream(path);

                fos.write(textToSave.toString().getBytes(StandardCharsets.UTF_8));
                fos.flush();
                fos.close();
                Toast.makeText(getApplicationContext(), "You have saved the file", Toast.LENGTH_SHORT).show();
                save_button.setEnabled(false);
                save_button.setBackgroundColor(Color.GRAY);
                Button replace_button = (Button) (findViewById(R.id.incident_replacement_button));

                if (counter != 2){
                    replace_button.setEnabled(false);
                    replace_button.setBackgroundColor(Color.GRAY);
                }
            }catch(IOException e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Didn't save file for some reason", Toast.LENGTH_SHORT).show();
            }
        }
    }



}