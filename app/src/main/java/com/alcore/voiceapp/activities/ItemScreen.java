package com.alcore.voiceapp.activities;

import com.alcore.voiceapp.Database.DB;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ClipData;
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
    private static final int RECORD_AUDIO_CODE = 100;
    private ImageView micro;
    private TextToSpeech speaker;
    private View view;
    private int itemp;
    private int itempdel;
    private String targetdel;
    private Boolean waitdelete = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_screen);

        micro = findViewById(R.id.micro);


        shopRecyclerView = findViewById(R.id.recycleshop);
        shopRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //If list is empty, show a message (PAVEL)

        shopRecyclerView = findViewById(R.id.recycleshop);
        shopRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        shopRecyclerView.setAdapter(new ItemAdapter(DB.getShoppingList(),this));

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
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en");
        intent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, true);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, "com.alcore.voiceapp");
        speechRecognizer.startListening(intent);
    }


    @Override
    public void OnClickItem(int position) {
        ItemModel imodel = DB.getShoppingList().get(position);
        Intent myIntent = new Intent(ItemScreen.this, ProductScreen.class);
        myIntent.putExtra("productlist", imodel.name);
        myIntent.putExtra("listID", imodel.getId());
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

        if(!waitdelete) {
            if (message.contains("help")) {
                showAlertDialogButtonClicked(view);
            } else if (message.contains("create")) {
                String newlist = "";

                Pattern object = Pattern.compile("create (.*?) list");
                Matcher matcher = object.matcher(message);
                while (matcher.find()) {
                    newlist = matcher.group(1);
                }
                if(newlist != "") {
                    ItemModel list = new ItemModel(newlist);
                    DB.getShoppingList().add(list);
                    list.save();
                    shopRecyclerView.getAdapter().notifyDataSetChanged();
                    shopRecyclerView.scrollToPosition(DB.getShoppingList().size() - 1);
                }
            } else if (message.contains("show")) {
                String target = "";
                Boolean ended = false;
                itemp = 0;
                Pattern object = Pattern.compile("show (.*?) list");
                Matcher matcher = object.matcher(message);
                while (matcher.find()) {
                    target = matcher.group(1);
                }
                for (int i = 0; i < DB.getShoppingList().size() && !ended; ++i) {
                    if (target.equals(DB.getShoppingList().get(i).getName().toLowerCase())) {
                        ended = true;
                        itemp = i;
                    }
                }

                Intent myIntent = new Intent(ItemScreen.this, ProductScreen.class);
                myIntent.putExtra("productlist", DB.getShoppingList().get(itemp).getName());
                myIntent.putExtra("listID", DB.getShoppingList().get(itemp).getId());
                startActivity(myIntent);

            } else if (message.contains("put")) {
                String targetlist = "";
                String target = "";

                Boolean trobatllista = false;
                Boolean trobatprod = false;
                Pattern object = Pattern.compile("put (.*?) to");
                Pattern objectlist = Pattern.compile("to (.*?) list");
                Matcher matcher = object.matcher(message);
                Matcher matcherlist = objectlist.matcher(message);
                while (matcher.find()) {
                    target = matcher.group(1);
                }
                while (matcherlist.find()) {
                    targetlist = matcherlist.group(1);
                }

                ProductModel newprod = new ProductModel();
                newprod.setName(target);
                newprod.setStatus(false);
                int itemp = 0;

                for (int i = 0; i < DB.getShoppingList().size() && !trobatllista; ++i) {
                    if (targetlist.equals(DB.getShoppingList().get(i).getName().toLowerCase())) {
                        trobatllista = true;
                        itemp = i;
                        for (int j = 0; j < DB.getShoppingList().get(i).getProducts().size(); ++j) { // La llista es buida perque no entro a la vista.... mai entrara aqui... persistencia? Preguntar PAVEL
                            if (target.equals(DB.getShoppingList().get(i).getProducts().get(j).getName().toLowerCase())) {
                                trobatprod = true;
                            }
                        }
                    }
                }
                if(!trobatprod && target != "" && targetlist != ""){
                    DB.getShoppingList().get(itemp).getProducts().add(newprod);
                    DB.getShoppingList().get(itemp).save();
                    newprod.save();
                    shopRecyclerView.getAdapter().notifyDataSetChanged();
                    shopRecyclerView.scrollToPosition(DB.getShoppingList().size() - 1);
                    speaker.speak("Succesfully added" + newprod.getName(), QUEUE_FLUSH, null, "aleix");
                }else if(!trobatllista && targetlist != ""){
                    speaker.speak("This list doesn't exists ", QUEUE_FLUSH, null, "aleix");
                }
                else if(trobatprod){
                    speaker.speak("This product already exist ", QUEUE_FLUSH, null, "aleix");
                }else{
                    speaker.speak("Say it again", QUEUE_FLUSH, null, "aleix");
                }

            } else if (message.contains("delete")) {
                String target = "";

                Boolean trobat = false;
                itempdel = 0;
                Pattern object = Pattern.compile("delete (.*?) list");
                Matcher matcher = object.matcher(message);
                while (matcher.find()) {
                    target = matcher.group(1);
                }

                for (int i = 0; i < DB.getShoppingList().size(); ++i) {
                    if (target.equals(DB.getShoppingList().get(i).getName().toLowerCase())) {
                        trobat = true;
                        itempdel = i;
                    }
                }

                if (trobat) {
                    waitdelete = true;
                    targetdel = target;
                    speaker.speak("Are you sure that you want to remove " + target + "?", QUEUE_FLUSH, null, "aleix");
                } else speaker.speak("This list doesn't exists", QUEUE_FLUSH, null, "aleix");
            }
        }else{
            waitdelete = false;
            if(message.contains("yes")) {
                DB.getShoppingList().get(itempdel).delete();
                DB.getShoppingList().remove(DB.getShoppingList().get(itempdel));
                shopRecyclerView.getAdapter().notifyDataSetChanged();
                shopRecyclerView.scrollToPosition(DB.getShoppingList().size() - 1);
                speaker.speak("Succesfully deleted" + targetdel, QUEUE_FLUSH, null, "aleix");
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