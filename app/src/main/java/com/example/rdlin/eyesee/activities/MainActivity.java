package com.example.rdlin.eyesee.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rdlin.eyesee.R;

import java.util.HashMap;
import java.util.Locale;


public class MainActivity extends Activity implements
        TextToSpeech.OnInitListener {
    TextView text;
    TextToSpeech t1;
    private TextToSpeech tts;
    private boolean instructions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tts = new TextToSpeech(this, this);
        RelativeLayout rlayout = (RelativeLayout) findViewById(R.id.MainActivityLayout);
        text = (TextView) findViewById(R.id.textViewMain);
        Intent intent = getIntent();
        String extras = getIntent().getStringExtra("PicName");
        if (extras != null) {
            String value = extras;
            text.setText(value);
        }

        rlayout.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                    // Start NewActivity.class
                    Intent myIntent = new Intent(MainActivity.this,
                            CameraActivity.class);
                    startActivity(myIntent);
            }
        });
    }

    @Override
    public void onDestroy() {
        // Don't forget to shutdown tts!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                speakOut();
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }

    }

    private void speakOut() {

        String text = "Tap then take a picture of the grocery item.";

        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}
