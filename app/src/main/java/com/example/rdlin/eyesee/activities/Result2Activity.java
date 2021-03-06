package com.example.rdlin.eyesee.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.speech.tts.TextToSpeech;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.rdlin.eyesee.R;
import com.example.rdlin.eyesee.helpers.DocumentHelper;
import com.example.rdlin.eyesee.imgurmodel.ImageResponse;
import com.example.rdlin.eyesee.imgurmodel.Upload;
import com.example.rdlin.eyesee.services.OnImageUploadedListener;
import com.example.rdlin.eyesee.services.UploadService;
import com.example.rdlin.eyesee.utils.VenmoLibrary;
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
import java.util.Locale;


public class Result2Activity extends Activity implements
        OnImageUploadedListener{
    Button button1;
    Button button2;
    TextView text;
    TextView finished;
    ImageView uploadImage;
    private Upload upload; // Upload object containging image and meta data
    private File chosenFile; //chosen file from intent
    TextToSpeech tts;
    String asd;
    RelativeLayout rlayout;
    private static final int REQUEST_CODE_VENMO_APP_SWITCH = 2782;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_result2);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);

        asd = "Done! Please tap again.";
        rlayout = (RelativeLayout) findViewById(R.id.RelativeActivityLayout);
        tts=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {

                    int result = tts.setLanguage(Locale.US);

                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "This Language is not supported");
                    } else {
                        speakOut(asd);
                    }

                } else {
                    Log.e("TTS", "Initilization Failed!");
                }
            }
        });
        finished = (TextView) findViewById(R.id.upload_finish);

        Intent intent = getIntent();
        Uri returnUri = getIntent().getData();
        String extrasString = returnUri.toString();
        if (extrasString != null) {
            String value = extrasString;
        }
        chosenFile = new File(DocumentHelper.getPath(this, returnUri));
        createUpload(chosenFile);
        new UploadService(upload, this).execute();
    }

    public void onImageUploaded(ImageResponse response){
    /*
      Logging the response from the image upload.
     */
        aLog.w("LOGGING", response.toString());
        finished.setText("Upload finished: " + response.data.link);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /*String cutLink = response.data.link.substring(Math.max(0, response.data.link.length() - 11));
        HttpResponse resp = null;
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI("https://sleepy-plateau-3785.herokuapp.com/url?imgur=" + cutLink));
            resp = client.execute(request);
            try {
                Thread.sleep(5000);
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
        }*/
        /*String respString = "";
        try {
            respString = convertStreamToString(resp.getEntity().getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        /*Gson gson = new Gson();

        String json = gson.toJson(respString);
        JsonObject jsonObject = gson.fromJson(respString, JsonObject.class);

        if (jsonObject.has("expiry") && jsonObject.get("expiry").toString().equals("")) {
            asd = "The expiry date is " + jsonObject.get("expiry").toString() + " . Tap the bottom button to tip with Venmo. ";
            finished.setText(asd);
        }
        else {*/
            asd = "We were not able to find the expiry date. Sorry";
            finished.setText(asd);
        //}
        asd = asd + "." + " Thank you for using eyesee. Tap the top to try for another product.";
        speakOut(asd);
        button1.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                // Start NewActivity.class
                Intent myIntent = new Intent(Result2Activity.this,
                        MainActivity.class);
                startActivity(myIntent);
            }
        });
        button2.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                // Start NewActivity.class
                Intent venmoIntent = VenmoLibrary.openVenmoPayment("2782", "EyeSee", "eyesee.venmo@gmail.com", "1", "EyeSee donation", "pay");
                startActivityForResult(venmoIntent, REQUEST_CODE_VENMO_APP_SWITCH);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        /*switch(requestCode) {
            case REQUEST_CODE_VENMO_APP_SWITCH: {
                if(resultCode == RESULT_OK) {
                    String signedrequest = data.getStringExtra("signedrequest");
                    if(signedrequest != null) {
                        VenmoLibrary.VenmoResponse response = (new VenmoLibrary()).validateVenmoPaymentResponse(signedrequest, "AKKmruwRq4bHYRNxqs6LcUA879PbEzja");
                        if(response.getSuccess().equals("1")) {
                            //Payment successful.  Use data from response object to display a success message
                            String note = response.getNote();
                            String amount = response.getAmount();
                        }
                    }
                    else {
                        String error_message = data.getStringExtra("error_message");
                        //An error ocurred.  Make sure to display the error_message to the user
                    }
                }
                else if(resultCode == RESULT_CANCELED) {
                    //The user cancelled the payment
                }

                break;
            }

        }*/
        speakOut("Thank you. Redirecting to the start page.");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intent myIntent = new Intent(Result2Activity.this,
                MainActivity.class);
        startActivity(myIntent);
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

    private void createUpload(File image) {
        upload = new Upload();

        upload.image = image;

    }

    private void speakOut(String text) {

        //String text = "Tap then take a picture of the grocery item.";

        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

}
