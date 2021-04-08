package com.alcore.voiceapp.models;

import java.io.Serializable;
import java.util.Date;

public class EventModel implements Serializable {

    public String name;
    public String date;
    public String hour;

    public EventModel(){
        this.name = "";
        this.date = "";
        this.hour = "";
    }
    public EventModel(String name){
        this();
        this.name = name;
    }

    public EventModel(String name, String date,String hour){
        this.name = name;
        this.date = date;
        this.hour = hour;
    }
}
