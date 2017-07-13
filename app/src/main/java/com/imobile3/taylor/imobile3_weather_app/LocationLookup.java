package com.imobile3.taylor.imobile3_weather_app;

import com.imobile3.taylor.imobile3_weather_app.models.Location;
import com.imobile3.taylor.imobile3_weather_app.utilities.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Class converts a String either City,State,Zipcode, Latitude & Longitude
 * into location data. Used for verification and fine tuning.
 *
 * Issues: If no internet is enabled will throw exception. UnkownHostException or JSONException probably.
 * Solution: Send exception back to calling method and allow to be delt with.
 *
 * @author Taylor Parrish
 * @since 8/23/2016
 */
public class LocationLookup {
    private String latitude;
    private String longitude;
    private String formatted_address;
    private Location location;
    private String coordinates;
    private String city;
    private String state;

    public LocationLookup(String location) {
        this.coordinates = Utils.removeWhitespace(location);
    }

    //Request location data from the google geocode api
    //and returns the relevant data as a Location object
    public Location invoke() {
        String geocodeURL = "http://maps.googleapis.com/maps/api/geocode/json?address=" + coordinates + "&sensor=true";

        try {
            //Request geocode for JSONObject containing user input location data
            JSONObject locationLookup = new HttpJSONRequest().getJSONFromUrl(geocodeURL);

            //JSONObject that contains all location data
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
            latitude = geometryComponents.getString("lat");
            longitude = geometryComponents.getString("lng");
            formatted_address = locationJSON.getString("formatted_address");

            location = new Location(latitude, longitude, city, state, formatted_address);
        } catch (JSONException | IOException e) {
            //GoogleApi request failed, return user to location page?
            //Internet could be off?
            e.printStackTrace();
        }
            return location;
    }
}