package com.alcore.voiceapp.models;

import android.content.ContentUris;
import android.content.Context;
import android.net.Uri;
import android.provider.CalendarContract;

import com.orm.SugarRecord;

import java.io.Serializable;
import java.util.Date;

public class EventModel extends SugarRecord implements Serializable {

    public String name;
    public Date date;
    public long eventID;



    public EventModel(){

    }
    public void EventModel(String name){
        this.name = name;
    }

    public void EventModel(String name, Date date){
        this.name = name;
        this.date = date;
    }


    public void setIdCalendar(long id){
        this.eventID = id;
    }


    /*public void save(){

    }*/

    public boolean delete(Context c) {
        Uri deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
        int rows = c.getContentResolver().delete(deleteUri, null, null);
        return rows > 0;
    }
}
