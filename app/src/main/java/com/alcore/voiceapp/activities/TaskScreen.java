package com.alcore.voiceapp.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alcore.voiceapp.R;
import com.alcore.voiceapp.adapters.TaskAdapter;
import com.alcore.voiceapp.models.TaskModel;

import java.util.ArrayList;

public class TaskScreen extends AppCompatActivity {


    private RecyclerView taskRecyclerView;
    private TextView itemname;
    private ArrayList<TaskModel> list = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_screen);

        //final ImageView InfoButton = findViewById(R.id.infoicon);

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

        /*InfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(TaskScreen.this, NewTaskScreen.class);
                startActivity(myIntent);
            }
        });*/
    }
    public void showAlertDialogButtonClicked(View view) {
        // create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.activity_info_task, null);
        builder.setView(customLayout);
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface d) {
                RelativeLayout backbut = dialog.findViewById(R.id.backbutton);
                backbut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
        dialog.show();
    }

}

