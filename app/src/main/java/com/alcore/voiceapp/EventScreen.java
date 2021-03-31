package com.alcore.voiceapp;

import androidx.appcompat.app.AlertDialog;
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
        list.add(new EventModel("Pepper","12/02/20", "12:00AM"));
        list.add(new EventModel("Pepper","12/02/20", "12:00AM"));
        list.add(new EventModel("Pepper","12/02/20", "12:00AM"));
        list.add(new EventModel("Pepper","12/02/20", "12:00AM"));
        list.add(new EventModel("Pepper","12/02/20", "12:00AM"));
        list.add(new EventModel("Pepper","12/02/20", "12:00AM"));
        list.add(new EventModel("Pepper","12/02/20", "12:00AM"));
        list.add(new EventModel("Pepper","12/02/20", "12:00AM"));
        list.add(new EventModel("Pepper","12/02/20", "12:00AM"));
        list.add(new EventModel("Pepper","12/02/20", "12:00AM"));




        eventRecyclerView = findViewById(R.id.recyclesevent);
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventRecyclerView.setAdapter(new EventAdapter(list,this));

    }
    public void showAlertDialogButtonClicked(View view) {
        // create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.activity_info_event, null);
        builder.setView(customLayout);
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void OnClickEvent(int position) {
        EventModel emodel = list.get(position);
        Intent myIntent = new Intent(EventScreen.this, NewEventScreen.class);
        myIntent.putExtra("eventname", emodel.name);
        myIntent.putExtra("eventdate", emodel.date);
        myIntent.putExtra("eventhour", emodel.hour);
        startActivity(myIntent);
    }
}

