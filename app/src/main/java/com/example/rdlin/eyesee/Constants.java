package com.example.rdlin.eyesee;

/**
 * Created by AKiniyalocts on 2/23/15.
 */
public class Constants {
    /*
      Logging flag
     */
    public static final boolean LOGGING = true;

    /*
      Your imgur client id. You need this to upload to imgur.

      More here: https://api.imgur.com/
     */
    public static final String MY_IMGUR_CLIENT_ID = "358dc3c4793fd69";
    public static final String MY_IMGUR_CLIENT_SECRET = "c0b273cce7ca5f85cb32e04024cccb9cc4e0039a";

    /*
      Redirect URL for android.
     */
    public static final String MY_IMGUR_REDIRECT_URL = "http://android";

    /*
      Client Auth
     */
    public static String getClientAuth(){
        return "Client-ID " + MY_IMGUR_CLIENT_ID;
    }

}