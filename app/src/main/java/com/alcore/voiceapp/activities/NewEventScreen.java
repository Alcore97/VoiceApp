package com.alcore.voiceapp.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.service.autofill.DateTransformation;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alcore.voiceapp.R;
import com.alcore.voiceapp.models.EventModel;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.speech.tts.TextToSpeech.QUEUE_FLUSH;

public class NewEventScreen extends AppCompatActivity implements RecognitionListener {

    private TextToSpeech speaker;
    private static final int RECORD_AUDIO_CODE = 100;
    private ImageView micro;
    private View view;
    private String target;
    private Boolean ENABLED;
    private TextView TextName;
    private TextView TextDate;
    private TextView TextHour;
    private Boolean name = false;
    private Boolean date = false;
    private Boolean hour = false;
    private String finalname;
    private DateTime finaldate;
    private DateTime finalhour;
    private DateTime finaldatehour;
    private Boolean sayinginputs =false;
    private Boolean invalidinput = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        micro = findViewById(R.id.micro);

        ENABLED = getIntent().getBooleanExtra("isEnable", false);

        TextName = findViewById(R.id.text1);
        TextDate = findViewById(R.id.text2);
        TextHour = findViewById(R.id.text3);

        String Text1 = getIntent().getStringExtra("eventname");
        TextName.setText(Text1);

        if(getIntent().hasExtra("eventdate")){
            String date = getIntent().getStringExtra("eventdate");
            DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");
            DateTime aux = formatter.parseDateTime(date);
            TextDate.setText(aux.toString("dd/MM/yyyy"));
            TextHour.setText(aux.toString("hh:mm aa"));
        }else{
            TextDate.setText("");
            TextDate.setText("");
        }




        speaker = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = speaker.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Language not supported");
                    }else{
                        speaker.speak("What you want to do?", QUEUE_FLUSH, null, "aleix");
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

    private String PATRODATE = "(\\d\\d?)(th|nd|st|rd) of (.*)";

    public void changedate(String date){
        String targetday = "";
        String targetmonth = "";
        Pattern object = Pattern.compile(PATRODATE);
        Matcher matcher = object.matcher(date);
        if(matcher.matches()) {
            invalidinput = false;
            targetday = matcher.group(1).trim();
            targetmonth = matcher.group(matcher.groupCount()).trim();
        }else{
            invalidinput = true;
            speaker.speak("Invalid input", QUEUE_FLUSH, null, "aleix");
            return;
        }

        try {

            int day = Integer.parseInt(targetday);
            if (day > 31 || day <= 0) {
                speaker.speak("This day doesn't exist", QUEUE_FLUSH, null, "aleix");
            }
            int month = new DateTime(new SimpleDateFormat("MMMM").parse(targetmonth)).getMonthOfYear();
            if(month <= 0 || month >= 12){
                speaker.speak("This month doesn't exist", QUEUE_FLUSH, null, "aleix");
            }

            finaldate = new DateTime(new DateTime().getYear(), month, day, 0, 0);
            if(new DateTime().isAfter(finaldate)) finaldate = new DateTime(new DateTime().getYear()+1,month,day,0,0);



        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private String PATROHOUR ="(\\d\\d?)(:(\\d\\d))? (a.m.|p.m.)";

    public void changehour(String hour){

        Pattern object = Pattern.compile(PATROHOUR);
        Matcher matcher = object.matcher(hour);
        if(matcher.matches()) {
            invalidinput = false;
            DateTimeFormatter formatter = null;
            if(matcher.groupCount() == 3){
                formatter = DateTimeFormat.forPattern("hh:mm aa");
            }else{
                formatter = DateTimeFormat.forPattern("hh aa");
            }
            finalhour = formatter.parseDateTime(hour.replace(".", ""));
            finaldatehour = new DateTime(finaldate.getYear(), finaldate.getMonthOfYear(), finaldate.getDayOfMonth(), finalhour.getHourOfDay(), finalhour.getMinuteOfHour());
        }else{
            invalidinput = true;
            speaker.speak("Invalid input", QUEUE_FLUSH, null, "aleix");
            return;
        }


    }

    public void showAlertDialogButtonClicked(View view) {
        // create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.activity_info_newevent, null);
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
        if (ContextCompat.checkSelfPermission(NewEventScreen.this, permission)
                == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(NewEventScreen.this,
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
        Log.d("NewEventScreen", "onResults");

        String message = "";
        ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        for (int i =0; i<data.size(); i++) {
            message += data.get(i);
        }
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

        message = message.toLowerCase();

        if(name){
            name = false;
            TextName.setText(message);
            finalname = message;
        }else if(date){
            date = false;
            changedate(message);
            if(!invalidinput){
                TextDate.setText(finaldate.toString("dd/MM/yyyy"));
            }
        }else if(hour){
            hour = false;
            if(TextDate.getText().toString().equals("")){
                speaker.speak("I need a date first!", QUEUE_FLUSH, null, "aleix");
            }else{
                changehour(message);
                if(!invalidinput) {
                    TextHour.setText(finalhour.toString("hh:mm aa"));
                }
            }
        }

        else if (message.contains("name")) {
            name = true;
            speaker.speak("Say the name of the event", QUEUE_FLUSH, null, "aleix");
        } else if (message.contains("help")) {
            showAlertDialogButtonClicked(view);
        } else if (message.contains("date")) {
            date = true;
            speaker.speak("Say the date of the event", QUEUE_FLUSH, null, "aleix");
        } else if (message.contains("hour") || message.contains("our")) {
            hour = true;
            speaker.speak("Say the hour of the event", QUEUE_FLUSH, null, "aleix");
        } else if (message.contains("save") || message.contains("safe")) {
            if (TextName.getText().toString().equals("")) {
                speaker.speak("This event don't have name", QUEUE_FLUSH, null, "aleix");
            } else if (TextDate.getText().toString().equals("")) {
                speaker.speak("This event don't have date", QUEUE_FLUSH, null, "aleix");
            } else if (TextHour.getText().toString().equals("")) {
                speaker.speak("This event don't have date", QUEUE_FLUSH, null, "aleix");
            } else {
                EventModel newmodel = new EventModel();
                newmodel.name = finalname;
                newmodel.date = finaldatehour.toDate();
                newmodel.save(this);
                finish();
            }
        } else {
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