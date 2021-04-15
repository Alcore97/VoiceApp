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
import android.widget.TextView;
import android.widget.Toast;

import com.alcore.voiceapp.adapters.ProductAdapter;
import com.alcore.voiceapp.models.ItemModel;
import com.alcore.voiceapp.models.ProductModel;
import com.alcore.voiceapp.R;

import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.speech.tts.TextToSpeech.QUEUE_FLUSH;

public class ProductScreen extends AppCompatActivity implements RecognitionListener {



    private RecyclerView productRecyclerView;
    private TextView itemname;
    private ImageView micro;
    private TextToSpeech speaker;
    private View view;
    private int listid;
    private int itempdel;
    private String targetdel;
    private Boolean waitdelete = false;
    private  ItemModel list = null;
    private Boolean ENABLED;
    private static final int RECORD_AUDIO_CODE = 100;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_screen);

        itemname = findViewById(R.id.ItemName);
        String name ="Your " + getIntent().getStringExtra("productlist") + " list";
        itemname.setText(name);

        listid = (int) getIntent().getLongExtra("listID",0);
        ENABLED = getIntent().getBooleanExtra("isEnable",false);



        micro = findViewById(R.id.micro);

        productRecyclerView = findViewById(R.id.recyclesproduct);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        for(ItemModel model:DB.getShoppingList()){
            if(model.getId() == listid){
                list = model;
                break;
            }
        }

        productRecyclerView.setAdapter(new ProductAdapter(list.getProducts()));

        speaker = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = speaker.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Language not supported");
                    }else{
                        speaker.speak("This is your " + name , QUEUE_FLUSH, null, "aleix");
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


    public void showAlertDialogButtonClicked(View view) {
        // create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.activity_info_products, null);
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
        if (ContextCompat.checkSelfPermission(ProductScreen.this, permission)
                == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(ProductScreen.this,
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

        Log.d("ProductScreen", "onResults");

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

            } else if (message.contains("mark") || message.contains("marc")) {
                String prod = "";
                Boolean trobat = false;

                prod = message.substring(message.lastIndexOf(" ") + 1);

                for (int i = 0; i < list.getProducts().size(); ++i) {
                    if (prod.equals(list.getProducts().get(i).getName().toLowerCase())) {
                        trobat = true;
                        list.getProducts().get(i).setStatus(true);
                        list.getProducts().get(i).save();
                        productRecyclerView.getAdapter().notifyDataSetChanged();
                        productRecyclerView.scrollToPosition(list.getProducts().size() - 1);
                    }
                }
                if (!trobat) {
                    speaker.speak("This product doesn't exists", QUEUE_FLUSH, null, "aleix");
                }
            } else if (message.contains("insert")) {
                String prod = "";
                Boolean trobat = false;

                Pattern object = Pattern.compile("insert (.*?) item");
                Matcher matcher = object.matcher(message);
                while (matcher.find()) {
                    prod = matcher.group(1);
                }
                for (int i = 0; i < list.getProducts().size(); ++i) {
                    if (prod.equals(list.getProducts().get(i).getName().toLowerCase())) {
                        trobat = true;
                    }
                }
                if (!trobat && prod != "") {
                    ProductModel product = new ProductModel(prod);
                    list.getProducts().add(product);
                    list.save();
                    product.save();
                    productRecyclerView.setAdapter(new ProductAdapter(list.getProducts()));
                    productRecyclerView.getAdapter().notifyDataSetChanged();
                    productRecyclerView.scrollToPosition(list.getProducts().size() - 1);
                    if (ENABLED) {
                        customsound(ProductScreen.this);
                    }
                } else {
                    speaker.speak("This product already exist", QUEUE_FLUSH, null, "aleix");
                }
            } else if (message.contains("delete")) {
                String target = "";
                Boolean trobat = false;
                itempdel = 0;
                target = message.substring(message.lastIndexOf(" ") + 1);

                for (int i = 0; i < list.getProducts().size(); ++i) {
                    if (target.equals(list.getProducts().get(i).getName().toLowerCase())) {
                        trobat = true;
                        itempdel = i;
                        targetdel = target;
                    }
                }
                if(trobat){
                    waitdelete = true;
                    speaker.speak("Are you sure that you want to remove " + target + "?", QUEUE_FLUSH, null, "aleix");
                }else{
                    speaker.speak("This product doesn't exists", QUEUE_FLUSH, null, "aleix");
                }
            }
            else{
                speaker.speak("I doesn't undestood, could you say it again?", QUEUE_FLUSH, null, "aleix");
            }
        }else{
            waitdelete = false;
            if(message.contains("yes")) {
                list.getProducts().get(itempdel).delete();
                list.getProducts().remove(list.getProducts().get(itempdel));
                productRecyclerView.getAdapter().notifyDataSetChanged();
                speaker.speak("Succesfully deleted" + targetdel, QUEUE_FLUSH, null, "aleix");
                if (ENABLED) {
                    customsound(ProductScreen.this);
                }
            }else if(message.contains("no")){
                waitdelete = false;
            }else{
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

