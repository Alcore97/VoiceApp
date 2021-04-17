package com.alcore.voiceapp.models;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;

import com.orm.SugarRecord;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class EventModel extends SugarRecord implements Serializable {

    public String name;
    public Date date;
    public long eventID;
    public static long mainCalendar =-1;



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


    private long getMainCalendar(Context c){
        if (mainCalendar < 0 && ContextCompat.checkSelfPermission(c, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
            String selection = CalendarContract.Calendars.VISIBLE + "=1 AND " + CalendarContract.Calendars.IS_PRIMARY + "=1";
            final String[] EVENT_PROJECTION = new String[] {
                    CalendarContract.Calendars._ID,                           // 0
            };
            Cursor cur = c.getContentResolver().query(CalendarContract.Calendars.CONTENT_URI, EVENT_PROJECTION, selection, null, null);
            if (cur != null && cur.moveToNext()) {
                mainCalendar = cur.getLong(0);
                cur.close();
            }
        }

        return mainCalendar;
    }

    public void save(Context c){

        String eventTitle = this.name;

        DateTime begindate = new DateTime(this.date);
        DateTime enddate = begindate.plusHours(2);

        Calendar begintime = Calendar.getInstance();
        begintime.set(begindate.getYear(), begindate.getMonthOfYear()-1,begindate.getDayOfMonth(), begindate.getHourOfDay(), begindate.getMinuteOfDay());

        Calendar endtime = Calendar.getInstance();
        endtime.set(enddate.getYear(),enddate.getMonthOfYear()-1,enddate.getDayOfMonth(),enddate.getHourOfDay(), enddate.getMinuteOfDay());


        long startMillis = 0;
        long endMillis = 0;

        ContentResolver cr = c.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.TITLE, eventTitle);
        values.put(CalendarContract.Events.DTSTART, begintime.getTimeInMillis());
        values.put(CalendarContract.Events.DTEND, endtime.getTimeInMillis());
        values.put(CalendarContract.Events.CALENDAR_ID, getMainCalendar(c));
        values.put(CalendarContract.Events.EVENT_TIMEZONE, Calendar.getInstance().getTimeZone().toString());
        values.put(CalendarContract.Events.ORGANIZER, "voiceapp");


        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
        long eventID = Long.parseLong(uri.getLastPathSegment());

    }

    public boolean delete(Context c) {
        Uri deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
        int rows = c.getContentResolver().delete(deleteUri, null, null);
        return rows > 0;
    }
}
