package com.example.rdlin.eyesee.services;

import android.app.Activity;
import android.os.AsyncTask;

import java.io.File;

import com.example.rdlin.eyesee.Constants;
import com.example.rdlin.eyesee.helpers.NotificationHelper;
import com.example.rdlin.eyesee.imgurmodel.ImageResponse;
import com.example.rdlin.eyesee.imgurmodel.ImgurAPI;
import com.example.rdlin.eyesee.imgurmodel.Upload;
import com.example.rdlin.eyesee.utils.NetworkUtils;
import retrofit.RestAdapter;
import retrofit.mime.TypedFile;

/**
 * Created by AKiniyalocts on 1/12/15.
 *
 * Our upload service. This creates our restadapter, uploads our image, and notifies us of the response.
 *
 *
 */
public class UploadService extends AsyncTask<Void, Void, Void> {
  public final static String TAG = UploadService.class.getSimpleName();


  public String title, description, albumId;
  private ImageResponse response;
  private Activity activity;
  private OnImageUploadedListener mUploaded;
  private File image;

  private NotificationHelper notificationHelper;


  public UploadService(Upload upload, Activity activity){
    this.image = upload.image;
    this.title = upload.title;
    this.description = upload.description;
    this.albumId = upload.albumId;
    this.activity = activity;
    mUploaded = (OnImageUploadedListener) activity;

    notificationHelper = new NotificationHelper(activity);

  }

  @Override
  protected void onPreExecute() {
    super.onPreExecute();
    notificationHelper.createUploadingNotification();
  }

  @Override
  protected Void doInBackground(Void... params) {
    if(NetworkUtils.isConnected(activity)) {
      if (NetworkUtils.connectionReachable()) {

        /*
          Create rest adapter using our imgur API
         */
        RestAdapter imgurAdapter = new RestAdapter.Builder()
            .setEndpoint(ImgurAPI.server)
            .build();

        /*
          Set rest adapter logging if we're already logging
         */

        if(Constants.LOGGING)
          imgurAdapter.setLogLevel(RestAdapter.LogLevel.FULL);

        /*
          Upload image, get response for image
         */

        response = imgurAdapter.create(ImgurAPI.class)
            .postImage(
                Constants.getClientAuth(), title, description, albumId, null, new TypedFile("image/*", image)
        );

      }

    }
    return null;
  }

  @Override
  protected void onPostExecute(Void aVoid) {
    super.onPostExecute(aVoid);

    if(response != null){

    /*
      Notfiy image was uploaded sucessfully
     */
      if(response.success) {
        mUploaded.onImageUploaded(response);
        notificationHelper.createUploadedNotification(response);
      }


    /*
      Notfiy image was NOT uploaded sucessfully
     */
      else{
        notificationHelper.createFailedUploadNotification();
      }

    }
  }

}
