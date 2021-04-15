package com.alcore.voiceapp.activities;

import com.alcore.voiceapp.Database.DB;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alcore.voiceapp.R;
import com.alcore.voiceapp.adapters.TaskAdapter;
import com.alcore.voiceapp.models.TaskModel;

import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static android.speech.tts.TextToSpeech.QUEUE_FLUSH;

public class TaskScreen extends AppCompatActivity implements RecognitionListener {


    private RecyclerView taskRecyclerView;
    private ImageView micro;
    private TextToSpeech speaker;
    private ArrayList<TaskModel> filtertask;
    private View view;
    private static final int RECORD_AUDIO_CODE = 100;
    private int itempdel;
    private String targetdel;
    private Boolean waitdelete = false;
    private Boolean ENABLED;


    @Override
    protected void onStart() {
        super.onStart();
        taskRecyclerView.setAdapter(new TaskAdapter(DB.getTaskList()));
        taskRecyclerView.getAdapter().notifyDataSetChanged();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_screen);

        //final ImageView InfoButton = findViewById(R.id.infoicon);

        taskRecyclerView = findViewById(R.id.recyclestask);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ENABLED = getIntent().getBooleanExtra("isEnable",false);


        micro = findViewById(R.id.micro);


        taskRecyclerView = findViewById(R.id.recyclestask);
        taskRecyclerView.setAdapter(new TaskAdapter(DB.getTaskList()));


        speaker = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = speaker.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Language not supported");
                    }else{
                        speaker.speak("This is your task list", QUEUE_FLUSH, null, "aleix");
                    }
                } else Log.e("TTS", "Unable to initialize speaker - ErrorCode: $status");


            }
        });

    }

    static public void customsound(Context c){
        try {
            MediaPlayer media = MediaPlayer.create(c, R.raw.correct);
            media.start();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void getSpeechInput(View view) {

        checkPermission(Manifest.permission.RECORD_AUDIO, RECORD_AUDIO_CODE);

        SpeechRecognizer speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(this);

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en");
        intent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, true);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, "com.alcore.voiceapp");
        speechRecognizer.startListening(intent);
    }

    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(TaskScreen.this, permission)
                == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(TaskScreen.this,
                    new String[]{permission},
                    requestCode);
        }
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

    @Override
    public void onReadyForSpeech(Bundle params) {

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onRmsChanged(float rmsdB) {
        if (rmsdB > 0) {
            double percentage = (0.5 + ((rmsdB / 10)));

            //Log.d(TAG, "percentage: $percentage")

            Animation anim_waves= new ScaleAnimation(
                    1f, 1f,
                    0.5f, (float) percentage,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f
            );
            anim_waves.setDuration(50);
            micro.startAnimation(anim_waves);
        }

    }

    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onError(int error) {

    }

    @Override
    public void onResults(Bundle results) {
        Log.d("TaskScreen", "onResults");

        String message = "";
        ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        for (int i =0; i<data.size(); i++) {
            message += data.get(i);
        }
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

        message = message.toLowerCase();
        if(!waitdelete) {
            if (message.contains("help")) {
                showAlertDialogButtonClicked(view);

            } else if (message.contains("create")) {
                Intent myIntent = new Intent(TaskScreen.this, NewTaskScreen.class);
                myIntent.putExtra("isEnable", ENABLED);
                startActivity(myIntent);
            } else if (message.contains("filter")) {

                filtertask = (ArrayList<TaskModel>) DB.getTaskList().stream().filter(task -> task.getStatus() == false).collect(Collectors.toList());
                if(filtertask.isEmpty()){
                    speaker.speak("There isn't mark tasks", QUEUE_FLUSH, null, "aleix");
                }else{
                    taskRecyclerView.setAdapter(new TaskAdapter(filtertask));
                    taskRecyclerView.getAdapter().notifyDataSetChanged();
                }


            }else if (message.contains("mark") || message.contains("marc")) {
                String task = "";
                Boolean trobat = false;

                int position = message.lastIndexOf("mark");
                if(position < 0){
                    position = message.lastIndexOf("marc");
                }
                if(message.length() >=5) {
                    task = message.substring(position + 5);
                }



                for (int i = 0; i < DB.getTaskList().size(); ++i) {
                    if (task.equals(DB.getTaskList().get(i).getName().toLowerCase())) {
                        trobat = true;
                        DB.getTaskList().get(i).setStatus(true);
                        DB.getTaskList().get(i).save();
                        taskRecyclerView.getAdapter().notifyDataSetChanged();
                        taskRecyclerView.scrollToPosition(DB.getTaskList().size() - 1);
                    }
                }
                if (!trobat) {
                    speaker.speak("This task doesn't exists", QUEUE_FLUSH, null, "aleix");
                }
            }
            else if (message.contains("delete")) {
                String task = "";
                Boolean trobat = false;

                if(message.length() >= 7) {
                    task = message.substring(message.lastIndexOf("delete") + 1 + "delete".length());
                }

                for (int i = 0; i < DB.getTaskList().size(); ++i) {
                    if (task.equals(DB.getTaskList().get(i).getName().toLowerCase())) {
                        trobat = true;
                        itempdel = i;
                        targetdel = task;
                    }
                }
                if (trobat && !task.equals("")) {
                    waitdelete = true;
                    speaker.speak("Are you sure that you want to remove " + task + "?", QUEUE_FLUSH, null, "aleix");
                } else {
                    speaker.speak("This task doesn't exists", QUEUE_FLUSH, null, "aleix");
                }
            }
            else{
                speaker.speak("I don't understood, could you say it again?", QUEUE_FLUSH, null, "aleix");
            }
        }
        else {
            waitdelete = false;
            if (message.contains("yes") || message.contains("ies")) {
                DB.getTaskList().get(itempdel).delete();
                DB.getTaskList().remove(DB.getTaskList().get(itempdel));
                taskRecyclerView.setAdapter(new TaskAdapter(DB.getTaskList()));
                taskRecyclerView.getAdapter().notifyDataSetChanged();
                taskRecyclerView.scrollToPosition(DB.getTaskList().size() - 1);
                speaker.speak("Succesfully deleted" + targetdel, QUEUE_FLUSH, null, "aleix");
            } else if (message.contains("no")) {
                waitdelete = false;
            } else {
                waitdelete = true;
                speaker.speak("I don't understood, could you say it again?", QUEUE_FLUSH, null, "aleix");
            }
        }
    }


    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }
}

