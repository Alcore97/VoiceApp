package com.alcore.voiceapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.DialogInterface;
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
import android.widget.TextView;
import android.widget.Toast;

import com.alcore.voiceapp.adapters.ItemAdapter;
import com.alcore.voiceapp.adapters.ProductAdapter;
import com.alcore.voiceapp.models.ItemModel;
import com.alcore.voiceapp.R;
import com.alcore.voiceapp.models.ProductModel;

import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.speech.tts.TextToSpeech.QUEUE_FLUSH;

public class ItemScreen extends AppCompatActivity implements RecognitionListener, ItemAdapter.ItemController {



    private RecyclerView shopRecyclerView;
    private TextView itemname;
    private String name;
    private ArrayList<ItemModel> list = new ArrayList<>();
    private static final int RECORD_AUDIO_CODE = 100;
    private ImageView micro;
    private TextToSpeech speaker;
    private View view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_screen);

        micro = findViewById(R.id.micro);


        shopRecyclerView = findViewById(R.id.recycleshop);
        shopRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        list.add(new ItemModel("Caprabo"));
        list.add(new ItemModel("Mediamarkt"));
        list.add(new ItemModel("Amazon"));
        list.add(new ItemModel("Tintoreria"));
        list.add(new ItemModel("Glovo"));

        //If list is empty, show a message (PAVEL)

        shopRecyclerView = findViewById(R.id.recycleshop);
        shopRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        shopRecyclerView.setAdapter(new ItemAdapter(list,this));

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
        final View customLayout = getLayoutInflater().inflate(R.layout.activity_info_items, null);
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
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, "com.alcore.voiceapp");
        speechRecognizer.startListening(intent);
    }


    @Override
    public void OnClickItem(int position) {
        ItemModel imodel = list.get(position);
        Intent myIntent = new Intent(ItemScreen.this, ProductScreen.class);
        myIntent.putExtra("productlist", imodel.name);
        startActivity(myIntent);
    }

    public void checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(ItemScreen.this, permission)
                == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(ItemScreen.this,
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
                Toast.makeText(ItemScreen.this,
                        "Audio Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            else {
                Toast.makeText(ItemScreen.this,
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
        Log.d("ItemScreen", "onResults");

        String message = "";
        ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        for (int i =0; i<data.size(); i++) {
            message += data.get(i);
        }
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        message = message.toLowerCase();

        if(message.contains("help")){
            showAlertDialogButtonClicked(view);
        }else if(message.contains("Create") || message.contains("create")){
            String newlist = "";
            newlist = message.substring(message.lastIndexOf(" ")+1);
            list.add(new ItemModel(newlist));
            shopRecyclerView.setAdapter(new ItemAdapter(list,this));
            shopRecyclerView.scrollToPosition(list.size()-1);
        }else if(message.contains("show")){
            String target = "";
            Boolean ended = false;
            int itemp = 0;
            Pattern object = Pattern.compile("show (.*?) item");
            Matcher matcher = object.matcher(message);
            while(matcher.find()){target = matcher.group(1);}
            for(int i = 0; i < list.size() && !ended; ++i){
                if(target.equals(list.get(i).getName().toLowerCase())){
                    ended = true;
                    itemp = i;
                }
            }
            ItemModel imodel = list.get(itemp);
            Intent myIntent = new Intent(ItemScreen.this, ProductScreen.class);
            myIntent.putExtra("productlist", imodel.name);
            startActivity(myIntent);

        }
        else if(message.contains("add")){
            String[] object = message.split("to");
            int prodsize = object[0].length();
            int listsize = object[1].length();
            String prod = object[0].substring(4,prodsize);
            String lists = object[1].substring(0,listsize-5);
            for(int i = 0; i < list.size(); ++i){
                if(lists.equals(list.get(i).getName())) {
                    //completar pavel
                    list.get(i).products.add(prod);
                    shopRecyclerView.setAdapter(new ItemAdapter(list,this));
                }
                }
        }
        else if(message.contains("delete")) {
            String[] object = message.split("delete");
            int length = object[1].length();
            String target = object[1].substring(0, length - 5);
            for (int i = 0; i < list.size(); ++i) {
                if (target.equals(list.get(i).getName())) {
                    list.remove(list.get(i));
                    shopRecyclerView.setAdapter(new ItemAdapter(list,this));
                }
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