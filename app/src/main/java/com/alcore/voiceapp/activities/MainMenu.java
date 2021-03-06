package com.alcore.voiceapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.Image;
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
import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.Locale;

import static android.speech.tts.TextToSpeech.QUEUE_FLUSH;

public class MainMenu extends AppCompatActivity implements RecognitionListener {

    private static final int RECORD_AUDIO_CODE = 100;
    private static final int READ_CALENDAR_CODE = 200;
    private static final int WRITE_CALENDAR_CODE = 300;
    private ImageView micro;
    private ImageView microalert;
    private TextToSpeech speaker;
    private View view;
    public Boolean ENABLED;
    private ImageView arrowon;
    private ImageView arrowoff;
    private Boolean openeddialog = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        micro = findViewById(R.id.micro);

        checkPermission(Manifest.permission.READ_CALENDAR, READ_CALENDAR_CODE);
        checkPermission(Manifest.permission.WRITE_CALENDAR, WRITE_CALENDAR_CODE);


        cargarPreferencias();


        final RelativeLayout ButtonToShop = findViewById(R.id.l2);
        final RelativeLayout ButtonToToDo = findViewById(R.id.l4);
        final RelativeLayout ButtonToEvent = findViewById(R.id.l6);
        final ImageView ButtonToSet = findViewById(R.id.setting);

        speaker = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = speaker.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Language not supported");
                    }else{
                        speaker.speak("", QUEUE_FLUSH, null, "aleix");
                    }
                } else Log.e("TTS", "Unable to initialize speaker - ErrorCode: $status");


            }
        });






        ButtonToShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainMenu.this, ItemScreen.class);
                myIntent.putExtra("isEnable", ENABLED);
                startActivity(myIntent);
            }
        });

        ButtonToToDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainMenu.this, TaskScreen.class);
                myIntent.putExtra("isEnable", ENABLED);
                startActivity(myIntent);
            }
        });
        ButtonToEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainMenu.this, EventScreen.class);
                myIntent.putExtra("isEnable", ENABLED);
                startActivity(myIntent);
            }
        });
        ButtonToSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialogButtonClicked(view, ENABLED);
            }
        });
    }

    private void cargarPreferencias() {
        SharedPreferences preferences = getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        ENABLED = preferences.getBoolean("activado", false);
    }

    public void showAlertDialogButtonClicked(View view, Boolean ENABLED) {
        // create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.activity_settings, null);
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
        arrowoff = (ImageView) dialog.findViewById(R.id.arrowoff);
        arrowon = (ImageView) dialog.findViewById(R.id.arrowon);
        microalert = (ImageView) dialog.findViewById(R.id.microalert);
        if(ENABLED == null){
            ENABLED = false;
        }

        if(ENABLED == false){
            arrowoff.setColorFilter(Color.argb(255,0,0,0));
            arrowon.setColorFilter(Color.argb(255,126,120, 210));
        }
        if(ENABLED == true){
            arrowon.setColorFilter(Color.argb(255,0,0,0));
            arrowoff.setColorFilter(Color.argb(255,126,120, 210));
        }
        openeddialog = true;
    }


    public void Explaining(View view){
        speaker.speak("Hi, i'm Aisha, your voice assistant." + "Every time that you want to say something, push microphone button." +
                "If you don't really know want to do in any screen, you can say: Help." + "If you want to modify sound options, say: Settings." +
                "Enjoy.", QUEUE_FLUSH, null, "aleix");
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

    public void checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(MainMenu.this, permission)
                == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(MainMenu.this,
                    new String[] { permission },
                    requestCode);
        }
       /* else {
            Toast.makeText(ItemScreen.this,
                    "Permission already granted",
                    Toast.LENGTH_SHORT)
                    .show();
        }*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super
                .onRequestPermissionsResult(requestCode,
                        permissions,
                        grantResults);

        if (requestCode == RECORD_AUDIO_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainMenu.this,
                        "Audio Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            else {
                Toast.makeText(MainMenu.this,
                        "Audio Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }

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
            if(openeddialog) {
                microalert.startAnimation(anim_waves);
            }
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
        Log.d("MainMenu", "onResults");

        String message = "";
        ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        for (int i =0; i<data.size(); i++) {
            message += data.get(i);
        }
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

        message = message.toLowerCase();

        if(message.contains("shopping")){
            Intent myIntent = new Intent(MainMenu.this, ItemScreen.class);
            myIntent.putExtra("isEnable", ENABLED);
            startActivity(myIntent);
        }else if(message.contains("todo") || message.contains("to-do") || message.contains("to do")){
            Intent myIntent = new Intent(MainMenu.this, TaskScreen.class);
            myIntent.putExtra("isEnable", ENABLED);
            startActivity(myIntent);
        }else if(message.contains("event")){
            Intent myIntent = new Intent(MainMenu.this, EventScreen.class);
            myIntent.putExtra("isEnable", ENABLED);
            startActivity(myIntent);
        }else if(message.contains("help")){
            Explaining(view);
        }else if(message.contains("settings") || message.contains("setings")) {
            showAlertDialogButtonClicked(view, ENABLED);
        }else if(message.contains("enable") && !ENABLED){
            ENABLED = true;
            arrowon.setColorFilter(Color.argb(255,0,0,0));
            arrowoff.setColorFilter(Color.argb(255,126,120, 210));
            SharedPreferences preferences = getSharedPreferences("preferencias", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("activado", ENABLED);
            editor.commit();
        }else if(message.contains("enable") && ENABLED){
            speaker.speak("Sound already enabled", QUEUE_FLUSH, null, "aleix");

        }else if(message.contains("disable") && !ENABLED){
            speaker.speak("Sound already disabled", QUEUE_FLUSH, null, "aleix");
        }else if(message.contains("disable") && ENABLED){
            ENABLED = false;
            arrowoff.setColorFilter(Color.argb(255,0,0,0));
            arrowon.setColorFilter(Color.argb(255,126,120, 210));
            SharedPreferences preferences = getSharedPreferences("preferencias", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("activado", ENABLED);
            editor.commit();
        }else{
            speaker.speak("I don't understand you, could you say it again?", QUEUE_FLUSH, null, "aleix");
        }


    }

    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }
}