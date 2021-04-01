package com.alcore.voiceapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ProductScreen extends AppCompatActivity {



    private RecyclerView productRecyclerView;
    private TextView itemname;
    private ArrayList<ProductModel> list = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_screen);

        itemname = findViewById(R.id.ItemName);
        String name ="Your " + getIntent().getStringExtra("productlist") + " list";
        itemname.setText(name);

        productRecyclerView = findViewById(R.id.recyclesproduct);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        list.add(new ProductModel("Pepper"));
        list.add(new ProductModel("Tomato"));
        list.add(new ProductModel("Apple"));
        list.add(new ProductModel("Pear"));
        list.add(new ProductModel("Googles"));
        list.add(new ProductModel("Glass"));
        list.add(new ProductModel("Ice"));
        list.add(new ProductModel("Gas"));
        list.add(new ProductModel("Fuel"));
        list.add(new ProductModel("Water"));



        productRecyclerView = findViewById(R.id.recyclesproduct);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        productRecyclerView.setAdapter(new ProductAdapter(list));
    }
    public void showAlertDialogButtonClicked(View view) {
        // create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.activity_info_products, null);
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

