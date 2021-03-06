package com.alcore.voiceapp.activities;

import com.alcore.voiceapp.Database.DB;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import com.alcore.voiceapp.R;
import com.alcore.voiceapp.models.TaskModel;

import java.util.ArrayList;
import java.util.Locale;

import static android.speech.tts.TextToSpeech.QUEUE_FLUSH;

public class NewTaskScreen extends AppCompatActivity implements RecognitionListener {

    private TextToSpeech speaker;
    private static final int RECORD_AUDIO_CODE = 100;
    private ImageView micro;
    private String target;
    private Boolean ENABLED;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        micro = findViewById(R.id.micro);

        ENABLED = getIntent().getBooleanExtra("isEnable", false);

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
        Log.d("NewTaskScreen", "onResults");

        String message = "";
        ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        for (int i =0; i<data.size(); i++) {
            message += data.get(i);
        }
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

        message = message.toLowerCase();

        TextView placeholder = findViewById(R.id.text1);
        if(placeholder.getText().toString().equals("")) {
            placeholder.setText(message);
            target = message;
            speaker.speak("Are you sure that you want to create " + message + " task?", QUEUE_FLUSH, null, "aleix");
        }

        if(!placeholder.getText().toString().equals("") && message != target){
            if(message.contains("yes")|| message.contains("ies")){
                Boolean trobat = false;

                for (int i = 0; i < DB.getTaskList().size(); ++i) {
                    if (target.equals(DB.getTaskList().get(i).getName().toLowerCase())) {
                        trobat = true;
                    }
                }
                if(trobat){
                    speaker.speak("This task already exists", QUEUE_FLUSH, null, "aleix");
                    placeholder.setText("");
                }
                else{
                    TaskModel newtask = new TaskModel(target);
                    DB.getTaskList().add(newtask);
                    newtask.save();
                    if (ENABLED) {
                        customsound(NewTaskScreen.this);
                    }
                    finish();
                }
            }else if(message.contains("no")) {
                placeholder.setText("");
               //speaker.speak("Push the button and say the name of your task", QUEUE_FLUSH, null, "aleix");
            }
            else{
                speaker.speak("I don't understand you, could you say it again?", QUEUE_FLUSH, null, "aleix");
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