package com.imobile3.taylor.imobile3_weather_app.utilities;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by Taylor Parrish on 8/24/2016.
 *
 * Contains helper methods to perform certain tasks
 */
public class Utils {

    public static String getFirstWord(String text) {
        if (text.indexOf(' ') > -1) { // Check if there is more than one word.
            return text.substring(0, text.indexOf(' ')); // Extract first word.
        } else {
            return text; // Text is the first word itself.
        }
    }
    public static String removeWhitespace(String text) {
        return text.replaceAll("\\s+","");
    }

    public static void showToast(Activity activity, String message){
        Toast.makeText(activity,
                message,
                Toast.LENGTH_LONG).show();
    }
}
