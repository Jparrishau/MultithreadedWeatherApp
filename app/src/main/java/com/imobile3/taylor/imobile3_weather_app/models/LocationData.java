package com.imobile3.taylor.imobile3_weather_app.models;

/**
 * Created by Taylor Parrish on 8/27/2016.
 *
 * An object model containing relevant location data used to search for weather data.
 */
public class LocationData {

    private String latitude;
    private String longitude;
    private String city;
    private String state;
    private String formatted_address;

    public LocationData(String latitude, String longitude,
                        String formatted_address) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.formatted_address = formatted_address;
    }

    public LocationData(String latitude, String longitude,
                        String city, String state,
                        String formatted_address) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
        this.state = state;
        this.formatted_address = formatted_address;
    }


    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getFormattedAddress() {
        return formatted_address;
    }
}
