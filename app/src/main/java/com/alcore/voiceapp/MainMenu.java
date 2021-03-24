package com.alcore.voiceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RelativeLayout ButtonToShop = findViewById(R.id.l2);
        final RelativeLayout ButtonToToDo = findViewById(R.id.l4);
        final RelativeLayout ButtonToEvent = findViewById(R.id.l6);


        ButtonToShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainMenu.this, ItemScreen.class);
                startActivity(myIntent);
            }
        });

        ButtonToToDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainMenu.this, TaskScreen.class);
                startActivity(myIntent);
            }
        });
        ButtonToEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainMenu.this, EventScreen.class);
                startActivity(myIntent);
            }
        });
    }


}