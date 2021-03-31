package com.alcore.voiceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

public class NewEventScreen extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        final EditText TextName = findViewById(R.id.text1);
        final EditText TextDate = findViewById(R.id.text2);
        final EditText TextHour = findViewById(R.id.text3);

        String Text1 = getIntent().getStringExtra("eventname");
        TextName.setText(Text1);
        String Text2 = getIntent().getStringExtra("eventdate");
        TextDate.setText(Text2);
        String Text3 = getIntent().getStringExtra("eventhour");
        TextHour.setText(Text3);

    }
}