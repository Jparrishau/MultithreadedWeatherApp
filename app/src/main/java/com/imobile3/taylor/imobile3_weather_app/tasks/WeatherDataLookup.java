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
public class WeatherDataLookup extends AsyncTask<Location, String, Location> {
    private WeatherDataTaskListener mListener;
    private Context mContext;
    private String mFailureType;

    public WeatherDataLookup(WeatherDataTaskListener listener, Context context) {
        this.mListener = listener;
        this.mContext = context;
    }

    @Override
    public void onPreExecute() {
        super.onPreExecute();
        mListener.onWeatherDataTaskStarted();
    }

    @Override
    public Location doInBackground(Location... args) {
        Location location = args[0];
        HashMap<String, JSONObject> weatherData = new HashMap<>();
        String[] dataFeatures = {"conditions", "astronomy", "hourly10day"};

        final String WUNDERGROUND_API_KEY = "20a88f5fc4c597d7"; //Put this somewhere else global, resource string?
        try {
            for(int i = 0; i < dataFeatures.length; i++){
               String url = "http://api.wunderground.com/api/" + WUNDERGROUND_API_KEY
                        + "/" + dataFeatures[i] + "/q/" + location.getCoordinates() + ".json";
                weatherData.put(dataFeatures[i],
                        new HttpJSONRequest().getJSONFromUrl(url));
            }
            return  new WeatherDataParser(location, mContext).parseJSONWeatherData(weatherData);
        } catch (JSONException e) {
            setFailureType("JSONException");
        }
        catch (IOException e){
            setFailureType("IOException");
        }
        return null;
    }

    @Override
    public void onPostExecute(Location location) {
        if (location != null) {
            mListener.onWeatherDataTaskFinished(location);
        }
        else {
            mListener.onWeatherDataTaskFailed(getFailureType());
        }
    }

    public String getFailureType() {
        return mFailureType;
    }

    public void setFailureType(String failureType) {
        mFailureType = failureType;
    }
}
