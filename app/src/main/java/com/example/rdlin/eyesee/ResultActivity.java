package com.example.rdlin.eyesee;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.TextView;


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
