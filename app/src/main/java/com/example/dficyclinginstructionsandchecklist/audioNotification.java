package com.example.dficyclinginstructionsandchecklist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class audioNotification extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_notification);

        TextView textView = findViewById(R.id.text_message);

        String message = getIntent().getStringExtra("message");

        textView.setText(message);
    }
}