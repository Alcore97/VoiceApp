package com.alcore.voiceapp.activities;

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
import android.widget.TextView;
import android.widget.Toast;

import com.alcore.voiceapp.Database.DB;
import com.alcore.voiceapp.adapters.EventAdapter;
import com.alcore.voiceapp.models.EventModel;
import com.alcore.voiceapp.R;

import org.joda.time.DateTime;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.speech.tts.TextToSpeech.QUEUE_FLUSH;

public class EventScreen extends AppCompatActivity implements RecognitionListener, EventAdapter.EventController {


    private RecyclerView eventRecyclerView;
    private TextView itemname;
    private Boolean ENABLED;
    private static final int RECORD_AUDIO_CODE = 100;
    private ImageView micro;
    private TextToSpeech speaker;
    private View view;
    private EventModel emodel;
    private int itempdel;
    private boolean waitdelete = false;
    private String targetdel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_screen);

        final ImageView InfoButton = findViewById(R.id.infoicon);

        eventRecyclerView = findViewById(R.id.recyclesevent);
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        micro = findViewById(R.id.micro);



        ENABLED = getIntent().getBooleanExtra("isEnable", false);


        eventRecyclerView = findViewById(R.id.recyclesevent);
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventRecyclerView.setAdapter(new EventAdapter(DB.getEventList(this), this));
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

    public void showAlertDialogButtonClicked(View view) {
        // create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.activity_info_event, null);
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

    static public void customsound(Context c) {
        try {
            MediaPlayer media = MediaPlayer.create(c, R.raw.correct);
            media.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class NumberToWords {
        private static final String[] tensNames = {
                "", " ten", " twenty", " thirty", " forty",
                " fifty", " sixty", " seventy", " eighty", " ninety"
        };

        private static final String[] numNames = {
                "", " one", " two", " three", " four", " five", " six",
                " seven", " eight", " nine", " ten", " eleven", " twelve",
                " thirteen", " fourteen", " fifteen", " sixteen", " seventeen",
                " eighteen", " nineteen"
        };

        private NumberToWords() {
        }

        private static String convertLessThanOneThousand(int number) {
            String soFar;

            if (number % 100 < 20) {
                soFar = numNames[number % 100];
                number /= 100;
            } else {
                soFar = numNames[number % 10];
                number /= 10;

                soFar = tensNames[number % 10] + soFar;
                number /= 10;
            }
            if (number == 0) return soFar;
            return numNames[number] + " hundred" + soFar;
        }


        public static String convert(long number) {
            // 0 to 999 999 999 999
            if (number == 0) {
                return "zero";
            }

            String snumber = Long.toString(number);

            // pad with "0"
            String mask = "000000000000";
            DecimalFormat df = new DecimalFormat(mask);
            snumber = df.format(number);

            // XXXnnnnnnnnn
            int billions = Integer.parseInt(snumber.substring(0, 3));
            // nnnXXXnnnnnn
            int millions = Integer.parseInt(snumber.substring(3, 6));
            // nnnnnnXXXnnn
            int hundredThousands = Integer.parseInt(snumber.substring(6, 9));
            // nnnnnnnnnXXX
            int thousands = Integer.parseInt(snumber.substring(9, 12));

            String tradBillions;
            if (billions == 0) {
                tradBillions = "";
            } else {
                tradBillions = convertLessThanOneThousand(billions)
                        + " billion ";
            }
            String result = tradBillions;

            String tradMillions;
            if (millions == 0) {
                tradMillions = "";
            } else {
                tradMillions = convertLessThanOneThousand(millions)
                        + " million ";
            }
            result = result + tradMillions;

            String tradHundredThousands;
            switch (hundredThousands) {
                case 0:
                    tradHundredThousands = "";
                    break;
                case 1:
                    tradHundredThousands = "one thousand ";
                    break;
                default:
                    tradHundredThousands = convertLessThanOneThousand(hundredThousands)
                            + " thousand ";
            }
            result = result + tradHundredThousands;

            String tradThousand;
            tradThousand = convertLessThanOneThousand(thousands);
            result = result + tradThousand;

            // remove extra spaces!
            return result.replaceAll("^\\s+", "").replaceAll("\\b\\s{2,}\\b", " ");
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
        if (ContextCompat.checkSelfPermission(EventScreen.this, permission)
                == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(EventScreen.this,
                    new String[]{permission},
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
    public void OnClickEvent(int position) {
        emodel = DB.getEventList(this).get(position);
        Intent myIntent = new Intent(EventScreen.this, NewEventScreen.class);
        myIntent.putExtra("eventname", emodel.name);
        myIntent.putExtra("eventdate",new DateTime(emodel.date).toString("dd/MM/yyyy HH:mm"));
        startActivity(myIntent);
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

            Animation anim_waves = new ScaleAnimation(
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
        Log.d("ItemScreen", "onResults");

        String message = "";
        ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        for (int i = 0; i < data.size(); i++) {
            message += data.get(i);
        }
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        message = message.toLowerCase();

        if (!waitdelete) {
            if (message.contains("create")) {
                Intent myIntent = new Intent(EventScreen.this, NewEventScreen.class);
                myIntent.putExtra("isEnable", ENABLED);
                startActivity(myIntent);

            } else if (message.contains("delete")) {
                String target = "";

                Boolean trobat = false;
                itempdel = 0;
                Pattern object = Pattern.compile("delete event (.*?)");
                Matcher matcher = object.matcher(message);
                if (matcher.matches()) {
                    target = matcher.group(matcher.groupCount()).trim();
                }


                for (int i = 0; i < DB.getEventList(this).size(); ++i) {
                    if (target.equals(DB.getEventList(this).get(i).getId().toString()) || target.equals(NumberToWords.convert(DB.getEventList(this).get(i).getId()))) {
                        trobat = true;
                        itempdel = i;
                    }
                }

                if (trobat && !target.equals("")) {
                    waitdelete = true;
                    targetdel = target;
                    speaker.speak("Are you sure that you want to remove event number" + target + "?", QUEUE_FLUSH, null, "aleix");
                } else {
                    speaker.speak("This event doesn't exists", QUEUE_FLUSH, null, "aleix");
                }
            }else{
                speaker.speak("I don't understand you, could you say it again?", QUEUE_FLUSH, null, "aleix");
            }
        }else{
            waitdelete = false;
            if(message.contains("yes") || message.contains("ies")) {
                DB.getEventList(this).get(itempdel).delete(this);
                eventRecyclerView.setAdapter(new EventAdapter(DB.getEventList(this),this));
                eventRecyclerView.getAdapter().notifyDataSetChanged();
                speaker.speak("Succesfully deleted" + targetdel, QUEUE_FLUSH, null, "aleix");
                if (ENABLED) {
                    customsound(EventScreen.this);
                }
            }else if(message.contains("no")){
                waitdelete = false;
            }else{
                waitdelete = true;
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

