package com.alcore.voiceapp.models;

import com.orm.SugarRecord;

import java.io.Serializable;
import java.util.Date;

public class EventModel extends SugarRecord implements Serializable {

    public String name;
    public Date date;



    public EventModel(){

    }
    public EventModel(String name){
        this();
        this.name = name;
    }

    public EventModel(String name, Date date){
        this.name = name;
        this.date = date;
    }

}
