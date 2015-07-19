package com.example.rdlin.eyesee.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.JsonReader;
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
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;


public class Result2Activity extends Activity implements OnImageUploadedListener {
    Button button;
    TextView text;
    TextView finished;
    ImageView uploadImage;
    private Upload upload; // Upload object containging image and meta data
    private File chosenFile; //chosen file from intent
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
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
                Intent myIntent = new Intent(Result2Activity.this,
                        MainActivity.class);
                startActivity(myIntent);
            }
        });
    }

    public void onImageUploaded(ImageResponse response){
    /*
      Logging the response from the image upload.
     */
        aLog.w("LOGGING", response.toString());
        finished.setText("Upload finished: " + response.data.link);
        String cutLink = response.data.link.substring(Math.max(0, response.data.link.length() - 11));
        HttpResponse resp = null;
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI("https://sleepy-plateau-3785.herokuapp.com/url?imgur=" + cutLink));
            resp = client.execute(request);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            resp = client.execute(request);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String respString = "";
        try {
            respString = convertStreamToString(resp.getEntity().getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();

        String json = gson.toJson(respString);
        JsonObject jsonObject = gson.fromJson(respString, JsonObject.class);
        String asd = jsonObject.get("response").toString();
        finished.setText("Upload finished: " + "\n" + asd + "\n" + response.data.link);

    }

    public static String convertStreamToString(InputStream inputStream) throws IOException {
        if (inputStream != null) {
            Writer writer = new StringWriter();

            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"),1024);
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                inputStream.close();
            }
            return writer.toString();
        } else {
            return "";
        }
    }

    private void createUpload(File image){
        upload = new Upload();

        upload.image = image;

    }

}