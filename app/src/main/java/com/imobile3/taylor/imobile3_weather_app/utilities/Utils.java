package com.imobile3.taylor.imobile3_weather_app.utilities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.gson.Gson;
import com.imobile3.taylor.imobile3_weather_app.models.Location;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Contains helper methods to perform certain tasks
 *
 * @author Taylor Parrish
 * @since 8/24/2016
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

    public static String militaryToStandard(String rawTimestamp) throws ParseException {
        SimpleDateFormat inputFormatter = new SimpleDateFormat("HHmm");
        SimpleDateFormat outputFormatter = new SimpleDateFormat("h:mm a");

        Date dateToFormat = inputFormatter.parse(rawTimestamp);
        String formattedTimestamp = outputFormatter.format(dateToFormat);

        return formattedTimestamp;
    }

    public static String getFormattedTime(String formatString, Calendar calendar){
        SimpleDateFormat dateFormater = new SimpleDateFormat(formatString);
        Date date = calendar.getTime();
        String formattedTime =  dateFormater.format(date);
        return formattedTime;
    }

    public static String getStandardTimeFormat(Calendar calendar){
        SimpleDateFormat outputFormatter = new SimpleDateFormat("h:mm a");
        Date dateToFormat = calendar.getTime();

        return outputFormatter.format(dateToFormat);
    }

    public static void showToast(Activity activity, String message){
        Toast.makeText(activity,
                message,
                Toast.LENGTH_LONG).show();
    }
}
