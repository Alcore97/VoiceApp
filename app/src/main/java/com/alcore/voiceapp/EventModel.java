package com.alcore.voiceapp;

import java.io.Serializable;
import java.util.Date;

public class EventModel implements Serializable {

    public String name;
    public String date;

    public EventModel(){
        this.name = "";
        this.date = "";
    }
    public EventModel(String name){
        this();
        this.name = name;
    }

    public EventModel(String name, String date){
        this.name = name;
        this.date = date;
    }
}
