package com.imobile3.taylor.imobile3_weather_app.models;

import java.util.ArrayList;

/**
 * Created by Taylor Parrish on 8/27/2016.
 *
 * An object model containing relevant location data used to search for weather data.
 */
public class Location {
    private String mLatitude;
    private String mLongitude;
    private String mCoordinates;
    private String mCity;
    private String mState;
    private String mFormatted_address;
    private ArrayList<Day> mDays = new ArrayList<>();

    public Location(String latitude, String longitude,
                    String formatted_address) {
        setLatitude(latitude);
        setLatitude(longitude);
        setFormatted_address(formatted_address);
        setCoordinates(latitude + "," + longitude);
    }

    public Location(String latitude, String longitude,
                    String city, String state,
                    String formatted_address) {
        setLatitude(latitude);
        setLongitude(longitude);
        setFormatted_address(formatted_address);
        setCoordinates(latitude + "," + longitude);
        setCity(city);
        setState(state);
    }

    public String getCoordinates() {
        return mCoordinates;
    }

    public void setCoordinates(String coordinates) {
        this.mCoordinates = coordinates;
    }

    public String getLatitude() {
        return mLatitude;
    }

    public void setLatitude(String mLatitude) {
        this.mLatitude = mLatitude;
    }

    public String getLongitude() {
        return mLongitude;
    }

    public void setLongitude(String longitude) {
        this.mLongitude = longitude;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        this.mCity = city;
    }

    public String getState() {
        return mState;
    }

    public void setState(String state) {
        this.mState = state;
    }

    public String getFormatted_address() {
        return mFormatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.mFormatted_address = formatted_address;
    }


}
