package com.imobile3.taylor.imobile3_weather_app.tasks;

import android.os.AsyncTask;

import com.imobile3.taylor.imobile3_weather_app.LocationLookup;
import com.imobile3.taylor.imobile3_weather_app.interfaces.LocationDataTaskListener;
import com.imobile3.taylor.imobile3_weather_app.models.Location;

/**
 * Created by taylorp on 7/17/2017.
 */
public class LocationResponse extends AsyncTask<String, String, Location> {
    private final LocationDataTaskListener listener;

    public LocationResponse(LocationDataTaskListener mListener) {
        this.listener = mListener;
    }

    @Override
    public void onPreExecute() {
        super.onPreExecute();
        listener.onTaskStarted();
    }

    @Override
    public Location doInBackground(String... args) {
        //Use user set location string and return geocode location data object model
        String locationString = args[0];
        return new LocationLookup(locationString).invoke();
    }

    @Override
    public void onPostExecute(Location location) {
        listener.onTaskFinished(location);
    }
}