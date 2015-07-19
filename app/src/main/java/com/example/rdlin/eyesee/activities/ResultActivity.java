package com.example.rdlin.eyesee.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.example.rdlin.eyesee.R;


public class ResultActivity extends Activity {
    Button button;
    TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        button = (Button) findViewById(R.id.restart_button);
        text = (TextView) findViewById(R.id.textViewResult);
        Intent intent = getIntent();
        String extras = getIntent().getStringExtra("picName");
        if (extras != null) {
            String value = extras;
            text.setText(value);
        }
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {

                // Start NewActivity.class
                Intent myIntent = new Intent(ResultActivity.this,
                        MainActivity.class);
                startActivity(myIntent);
            }
        });
    }

}
