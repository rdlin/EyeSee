package com.example.rdlin.eyesee.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rdlin.eyesee.R;
import com.example.rdlin.eyesee.helpers.DocumentHelper;
import com.example.rdlin.eyesee.imgurmodel.ImageResponse;
import com.example.rdlin.eyesee.imgurmodel.Upload;
import com.example.rdlin.eyesee.services.OnImageUploadedListener;
import com.example.rdlin.eyesee.services.UploadService;
import com.example.rdlin.eyesee.utils.aLog;
import com.squareup.picasso.Picasso;

import java.io.File;


public class ResultActivity extends Activity implements OnImageUploadedListener {
    Button button;
    TextView text;
    TextView finished;
    ImageView uploadImage;
    private Upload upload; // Upload object containging image and meta data
    private File chosenFile; //chosen file from intent
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        button = (Button) findViewById(R.id.restart_button);
        text = (TextView) findViewById(R.id.textViewResult);
        finished = (TextView) findViewById(R.id.upload_finish);
        uploadImage = (ImageView) findViewById(R.id.upload_image);

        Intent intent = getIntent();
        Uri returnUri = getIntent().getData();
        String extrasString = returnUri.toString();
        if (extrasString != null) {
            String value = extrasString;
            text.setText(value);
        }
        chosenFile = new File(DocumentHelper.getPath(this, returnUri));
        Picasso.with(this).load(chosenFile).fit().into(uploadImage);
        createUpload(chosenFile);
        new UploadService(upload, this).execute();
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                // Start NewActivity.class
                Intent myIntent = new Intent(ResultActivity.this,
                        MainActivity.class);
                startActivity(myIntent);
            }
        });
    }

    public void onImageUploaded(ImageResponse response) {
    /*
      Logging the response from the image upload.
     */
        aLog.w("LOGGING", response.toString());
        finished.setText("Upload finished: " + response.data.link);
    }

    private void createUpload(File image){
        upload = new Upload();

        upload.image = image;

    }

}
