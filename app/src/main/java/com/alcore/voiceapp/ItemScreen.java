package com.alcore.voiceapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ItemScreen extends AppCompatActivity implements ItemAdapter.ItemController {



    private RecyclerView shopRecyclerView;
    private TextView itemname;
    private String name;
    private ArrayList<ItemModel> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_screen);

        shopRecyclerView = findViewById(R.id.recycleshop);
        shopRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        list.add(new ItemModel("Caprabo"));
        list.add(new ItemModel("Medimark"));
        list.add(new ItemModel("Amazon"));
        list.add(new ItemModel("Tintoreria"));
        list.add(new ItemModel("Glovo"));
        list.add(new ItemModel("Caprabo"));
        list.add(new ItemModel("Medimark"));
        list.add(new ItemModel("Amazon"));
        list.add(new ItemModel("Tintoreria"));
        list.add(new ItemModel("Glovo"));



        shopRecyclerView = findViewById(R.id.recycleshop);
        shopRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        shopRecyclerView.setAdapter(new ItemAdapter(list,this));
    }
    public void showAlertDialogButtonClicked(View view) {
        // create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Name");
        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.custom_layout, null);
        builder.setView(customLayout);
        // add a button
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // send data from the AlertDialog to the Activity
                finish();
            }
        });
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    // do something with the data coming from the AlertDialog

    @Override
    public void OnClickItem(int position) {
        ItemModel imodel = list.get(position);
        Intent myIntent = new Intent(ItemScreen.this, ProductScreen.class);
        myIntent.putExtra("productlist", imodel.name);
        startActivity(myIntent);
    }
}