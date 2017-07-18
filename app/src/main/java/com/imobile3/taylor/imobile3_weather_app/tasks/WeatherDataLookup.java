package com.imobile3.taylor.imobile3_weather_app.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.imobile3.taylor.imobile3_weather_app.HttpJSONRequest;
import com.imobile3.taylor.imobile3_weather_app.WeatherDataParser;
import com.imobile3.taylor.imobile3_weather_app.interfaces.WeatherDataTaskListener;
import com.imobile3.taylor.imobile3_weather_app.models.Location;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by taylorp on 7/17/2017.
 */
//Split up the parsing to mLocation object from the return of the JSON data. Shouldnt need to use GSON.
public class WeatherDataLookup extends AsyncTask<Location, String, HashMap<String, JSONObject> > {
    private WeatherDataTaskListener mListener;
    private Context mContext;
    Location mLocation;

    public WeatherDataLookup(WeatherDataTaskListener listener, Context context) {
        this.mListener = listener;
        this.mContext = context;
    }

    @Override
    public void onPreExecute() {
        super.onPreExecute();
        mListener.onTaskStarted();
    }

    @Override
    public HashMap<String, JSONObject> doInBackground(Location... args) {
        HashMap<String, JSONObject> weatherData = new HashMap<>();
        mLocation = args[0];

        //Put these somewhere else later
        final String WUNDERGROUND_API_KEY = "20a88f5fc4c597d7";

            /*
                Do this some cleaner way later, possibly with loop.
            */
        //URL for WUnderground API Call
        String weatherURL_10DAY = "http://api.wunderground.com/api/" + WUNDERGROUND_API_KEY
                + "/" + "forecast10day" + "/q/" + mLocation.getCoordinates() + ".json";
        //URL for WUnderground API Call
        String weatherURL_Conditions = "http://api.wunderground.com/api/" + WUNDERGROUND_API_KEY
                + "/" + "conditions" + "/q/" + mLocation.getCoordinates() + ".json";
        //URL for WUnderground API Call
        String weatherURL_Astronomy = "http://api.wunderground.com/api/" + WUNDERGROUND_API_KEY
                + "/" + "astronomy" + "/q/" + mLocation.getCoordinates() + ".json";
        //URL for WUnderground API Call
        String weatherURL_Hourly10Day = "http://api.wunderground.com/api/" + WUNDERGROUND_API_KEY
                + "/" + "hourly10day" + "/q/" + mLocation.getCoordinates() + ".json";

        try {
            weatherData.put("FORECAST_10DAY", new HttpJSONRequest().getJSONFromUrl(weatherURL_10DAY));
            weatherData.put("FORECAST_OBSERVATION_CURR", new HttpJSONRequest().getJSONFromUrl(weatherURL_Conditions));
            weatherData.put("FORECAST_ASTRONOMY", new HttpJSONRequest().getJSONFromUrl(weatherURL_Astronomy));
            weatherData.put("FORECAST_HOURLY_10DAY", new HttpJSONRequest().getJSONFromUrl(weatherURL_Hourly10Day));
            return weatherData;
        } catch (IOException | JSONException e) {
            //Need to handle this properly. Network may not be enabled/working.
            e.printStackTrace();
        }
        //This causes app to crash. Find a way to handle this.
        //Need to add new exception above?
        return null;
    }

    @Override
    public void onPostExecute(HashMap<String, JSONObject> weatherData) {
        Location location = new WeatherDataParser(mLocation, mContext).parseJSONWeatherData(weatherData);
        mListener.onWeatherDataTaskFinished(location);
    }
}
