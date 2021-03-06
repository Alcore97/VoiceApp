package com.alcore.voiceapp.Database;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.EventLog;
import android.util.Log;

import com.alcore.voiceapp.models.EventModel;
import com.alcore.voiceapp.models.ItemModel;
import com.alcore.voiceapp.models.ProductModel;
import com.alcore.voiceapp.models.TaskModel;

import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class DB  {


    private DB(){}

    private static ArrayList<ItemModel> listitems;
    private static ArrayList<TaskModel> listproducts;
    private static ArrayList<EventModel> listevents;
    private static long count = 0;



    public static ArrayList<ItemModel> getShoppingList(){

            /*ItemModel.deleteAll(ItemModel.class);

            ItemModel lista1 = new ItemModel("Mediamarkt");
            lista1.getProducts().add(new ProductModel("Computer"));
            lista1.save();*/

            listitems = (ArrayList<ItemModel>) ItemModel.listAll(ItemModel.class);

        return listitems;
    }
    public static  ArrayList<TaskModel> getTaskList(){
            /*TaskModel.deleteAll(TaskModel.class);

            TaskModel lista1 = new TaskModel("Estudiar");
            lista1.save();*/

            listproducts = (ArrayList<TaskModel>) TaskModel.listAll(TaskModel.class);

        return listproducts;
    }

    public static ArrayList<EventModel> getEventList(Context c){
            final String[] INSTANCE_PROJECTION = new String[] {
                    CalendarContract.Instances.EVENT_ID,       // 0
                    CalendarContract.Instances.BEGIN,         // 1
                    CalendarContract.Instances.TITLE,        // 2
            };

            // The indices for the projection array above.
            final int PROJECTION_ID_INDEX = 0;
            final int PROJECTION_BEGIN_INDEX = 1;
            final int PROJECTION_TITLE_INDEX = 2;

            DateTime currenttime = new DateTime();

            DateTime weektime = currenttime.plusDays(7);


            // Specify the date range you want to search for recurring event instances
            Calendar beginTime = Calendar.getInstance();
            beginTime.set(currenttime.getYear(), currenttime.getMonthOfYear()-1, currenttime.getDayOfMonth(), 0, 0);
            long startMillis = beginTime.getTimeInMillis();
            Calendar endTime = Calendar.getInstance();
            endTime.set(weektime.getYear(), weektime.getMonthOfYear()-1, weektime.getDayOfMonth(), 23, 59);
            long endMillis = endTime.getTimeInMillis();


            // The ID of the recurring event whose instances you are searching for in the Instances table
            //String selection = CalendarContract.Instances.TITLE + " = ?";
            //String[] selectionArgs = new String[] {eventTitle};

            // Construct the query with the desired date range.
            Uri.Builder builder = CalendarContract.Instances.CONTENT_URI.buildUpon();
            ContentUris.appendId(builder, startMillis);
            ContentUris.appendId(builder, endMillis);

            // Submit the query
            Cursor cur =  c.getContentResolver().query(builder.build(), INSTANCE_PROJECTION, CalendarContract.Instances.ALL_DAY + "= 0", null, null);


            listevents = new ArrayList<>();
            count = 0;
            while (cur.moveToNext()) {
                ++count;

                long eventID = cur.getLong(PROJECTION_ID_INDEX);
                long beginVal = cur.getLong(PROJECTION_BEGIN_INDEX);
                String title = cur.getString(PROJECTION_TITLE_INDEX);

                Calendar datatime = Calendar.getInstance();
                datatime.setTimeInMillis(beginVal);

                EventModel event = new EventModel();
                event.setId(count);
                event.setIdCalendar(eventID);
                event.name = title;
                event.date = datatime.getTime();


                listevents.add(event);
            }
            return listevents;
    }


}
