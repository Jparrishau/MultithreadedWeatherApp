package com.imobile3.taylor.imobile3_weather_app.tasks;

import android.os.AsyncTask;

import com.imobile3.taylor.imobile3_weather_app.HttpJSONRequest;
import com.imobile3.taylor.imobile3_weather_app.interfaces.LocationDataTaskListener;
import com.imobile3.taylor.imobile3_weather_app.models.Location;
import com.imobile3.taylor.imobile3_weather_app.utilities.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by taylorp on 7/17/2017.
 */
public class LocationDataLookup extends AsyncTask<String, String, Location> {
    private final LocationDataTaskListener listener;

    private Location location;
    private String city;
    private String state;

    public LocationDataLookup(LocationDataTaskListener mListener) {
        this.listener = mListener;
    }

    @Override
    public void onPreExecute() {
        super.onPreExecute();
        listener.onTaskStarted();
    }

    @Override
    public Location doInBackground(String... args) {
        //Use user set mLocation string and return geocode mLocation data object model
        String locationString = args[0];
        return invoke(locationString);
    }

    @Override
    public void onPostExecute(Location location) {
        listener.onTaskFinished(location);
    }

    public Location invoke(String locationString) {
        String coordinates;

        coordinates = Utils.removeWhitespace(locationString);
        String geocodeURL = "http://maps.googleapis.com/maps/api/geocode/json?address=" + coordinates + "&sensor=true";

        try {
            parseLocationData(geocodeURL);
        } catch (JSONException | IOException e) {
            //GoogleApi request failed, return user to mLocation page?
            //Internet could be off?
            e.printStackTrace();
        }
        return location;
    }

    private void parseLocationData(String geocodeURL) throws IOException, JSONException {
        //Request geocode for JSONObject containing user input mLocation data
        JSONObject locationLookup = new HttpJSONRequest().getJSONFromUrl(geocodeURL);

        //JSONObject that contains all mLocation data
        JSONObject locationJSON = locationLookup
                .getJSONArray("results").getJSONObject(0);

        //JSONArray that contains the different parts of address
        JSONArray address_compontents = locationJSON.getJSONArray("address_components");

        //JSONObject that contains latitude and longitude information
        JSONObject geometryComponents = locationJSON
                .getJSONObject("geometry").getJSONObject("location");

        for (int i = 0; i < address_compontents.length(); i++) {
            if (address_compontents.getJSONObject(i).getJSONArray("types")
                    .getString(0).equals("locality")) {
                city = address_compontents.getJSONObject(i).getString("long_name");
            }
            if (address_compontents.getJSONObject(i).getJSONArray("types")
                    .getString(0).equals("administrative_area_level_1")) {
                state = address_compontents.getJSONObject(i).getString("short_name");
            }
        }
        String latitude = geometryComponents.getString("lat");
        String longitude = geometryComponents.getString("lng");
        String formatted_address = locationJSON.getString("formatted_address");

        location = new Location(latitude, longitude, city, state, formatted_address);
    }
}