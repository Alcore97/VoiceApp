package com.alcore.voiceapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import java.util.ArrayList;
import java.util.Locale;

import static android.speech.tts.TextToSpeech.QUEUE_FLUSH;

public class MainMenu extends AppCompatActivity implements RecognitionListener {

    private static final int RECORD_AUDIO_CODE = 100;
    private ImageView micro;
    private TextToSpeech speaker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        micro = findViewById(R.id.micro);

        final RelativeLayout ButtonToShop = findViewById(R.id.l2);
        final RelativeLayout ButtonToToDo = findViewById(R.id.l4);
        final RelativeLayout ButtonToEvent = findViewById(R.id.l6);

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
                startActivity(myIntent);
            }
        });

        ButtonToToDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainMenu.this, TaskScreen.class);
                startActivity(myIntent);
            }
        });
        ButtonToEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainMenu.this, EventScreen.class);
                startActivity(myIntent);
            }
        });
    }

    public void Explaining(View view){
        speaker.speak("Hi, i'm Aisha, your voice assistant." +
                "This is Main Menu Screen, if you want to go to your Shopping List, you can say: Aisha, go to my Shopping List." +
                "If you want to go to your To-Do List, you can say: Aisha, go to my To-Do List." +
                "If you want to go to your Event List, you can say: Aisha, go to my Event List." +
                "If you don't really know want to do in any screen, you can say: Help" +
                "If you want to modify sound options, say: Settings" +
                "Every time that you want to say something, push microphone button." +
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
            startActivity(myIntent);
        }else if(message.contains("todo")){
            Intent myIntent = new Intent(MainMenu.this, TaskScreen.class);
            startActivity(myIntent);
        }else if(message.contains("event")){
            Intent myIntent = new Intent(MainMenu.this, EventScreen.class);
            startActivity(myIntent);
        }


    }

    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }
}