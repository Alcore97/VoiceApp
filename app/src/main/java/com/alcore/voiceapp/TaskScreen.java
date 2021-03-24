package com.alcore.voiceapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class TaskScreen extends AppCompatActivity {


    private RecyclerView taskRecyclerView;
    private TextView itemname;
    private ArrayList<TaskModel> list = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_screen);

        taskRecyclerView = findViewById(R.id.recyclestask);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        list.add(new TaskModel("Pepper"));
        list.add(new TaskModel("Tomato"));
        list.add(new TaskModel("Apple"));
        list.add(new TaskModel("Pear"));
        list.add(new TaskModel("Googles"));
        list.add(new TaskModel("Glass"));
        list.add(new TaskModel("Ice"));
        list.add(new TaskModel("Gas"));
        list.add(new TaskModel("Fuel"));
        list.add(new TaskModel("Water"));



        taskRecyclerView = findViewById(R.id.recyclestask);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskRecyclerView.setAdapter(new TaskAdapter(list));
    }

}

