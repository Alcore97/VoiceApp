package com.alcore.voiceapp.activities;

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
import android.widget.EditText;
import android.widget.ImageView;

import com.alcore.voiceapp.R;

import java.util.Locale;

import static android.speech.tts.TextToSpeech.QUEUE_FLUSH;

public class NewTaskScreen extends AppCompatActivity implements RecognitionListener {

    private TextToSpeech speaker;
    private static final int RECORD_AUDIO_CODE = 100;
    private ImageView micro;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        final EditText input = findViewById(R.id.text1);

        speaker = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = speaker.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Language not supported");
                    }else{
                        speaker.speak("Push the button and say the name of your task", QUEUE_FLUSH, null, "aleix");
                    }
                } else Log.e("TTS", "Unable to initialize speaker - ErrorCode: $status");


            }
        });
    }

    public void getSpeechInput(View view) {

        checkPermission(Manifest.permission.RECORD_AUDIO, RECORD_AUDIO_CODE);

        SpeechRecognizer speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(this);

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, "com.alcore.voiceapp");
        speechRecognizer.startListening(intent);
    }
    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(NewTaskScreen.this, permission)
                == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(NewTaskScreen.this,
                    new String[]{permission},
                    requestCode);
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

    }

    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }
}