package com.alcore.voiceapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

public class EventScreen extends AppCompatActivity implements EventAdapter.EventController {


    private RecyclerView eventRecyclerView;
    private TextView itemname;
    private ArrayList<EventModel> list = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_screen);

        final ImageView InfoButton = findViewById(R.id.infoicon);

        eventRecyclerView = findViewById(R.id.recyclesevent);
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        list.add(new EventModel("Pepper","12:00AM"));
        list.add(new EventModel("Tomato", "13:00AM"));
        list.add(new EventModel("Apple","13:00AM"));
        list.add(new EventModel("Pear","13:00AM"));
        list.add(new EventModel("Googles","13:00AM"));
        list.add(new EventModel("Glass","13:00AM"));
        list.add(new EventModel("Ice","13:00AM"));
        list.add(new EventModel("Gas","13:00AM"));
        list.add(new EventModel("Fuel","13:00AM"));
        list.add(new EventModel("Water","13:00AM"));



        eventRecyclerView = findViewById(R.id.recyclesevent);
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventRecyclerView.setAdapter(new EventAdapter(list,this));

        InfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(EventScreen.this, NewEventScreen.class);
                startActivity(myIntent);
            }
        });
    }

    @Override
    public void OnClickEvent(int position) {
        EventModel emodel = list.get(position);
        Intent myIntent = new Intent(EventScreen.this, NewEventScreen.class);
        //myIntent.putExtra("eventname", emodel.name);
        //myIntent.putExtra("eventdate", emodel.date);
        startActivity(myIntent);
    }
}

