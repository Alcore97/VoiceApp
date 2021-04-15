package com.alcore.voiceapp.models;

import java.io.Serializable;
import java.util.Date;

public class EventModel implements Serializable {

    public int id;
    public String name;
    public String date;

    public EventModel(){

    }
    public EventModel(String name){
        this();
        this.name = name;
    }

    public EventModel(String name, String date,int id){
        this.name = name;
        this.date = date;
        this.id = id;
    }
}
