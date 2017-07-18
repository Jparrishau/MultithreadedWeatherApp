package com.imobile3.taylor.imobile3_weather_app.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.imobile3.taylor.imobile3_weather_app.utilities.Utils;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * An object model containing relevant location data used to search for weather data.
 *
 * @author Taylor Parrish
 * @since 8/27/2016
 */
public class Location implements Parcelable, Serializable {
    private Calendar mTimeStamp;
    private String mLatitude;
    private String mLongitude;
    private String mCoordinates;
    private String mCity;
    private String mState;
    private String mFormatted_address;

    //Convert this to hash table where keys are the dates?
    private ArrayList<Day> mDays = new ArrayList<>();

    //Convert to map later if we can make it parcelable/serializable
    private ArrayList<HourlyWeatherForecast> mHourlyWeatherForecastsToday = new ArrayList<>();
    private ArrayList<HourlyWeatherForecast> mHourlyWeatherForecastsTomorrow = new ArrayList<>();
    private ArrayList<HourlyWeatherForecast> mHourlyWeatherForecastsLater = new ArrayList<>();

    private CurrentWeatherForecast mCurrentWeatherForecast;

    public Location(String latitude, String longitude,
                    String formatted_address) {
        setTimeStamp(Calendar.getInstance());
        setLatitude(latitude);
        setLatitude(longitude);
        setFormatted_address(formatted_address);
        setCoordinates(latitude + "," + longitude);
    }

    public Location(String latitude, String longitude,
                    String city, String state,
                    String formatted_address) {
        setTimeStamp(Calendar.getInstance());
        setLatitude(latitude);
        setLongitude(longitude);
        setFormatted_address(formatted_address);
        setCoordinates(latitude + "," + longitude);
        setCity(city);
        setState(state);
    }

    public String getFormattedTime(String formatString){
        if (mTimeStamp != null)
            return Utils.getFormattedTime(formatString, mTimeStamp);
        else
            return null;
    }

    public void updateTimeStamp(){
        setTimeStamp(Calendar.getInstance());
    }

    public Calendar getTimeStamp() {
        return mTimeStamp;
    }

    public void setTimeStamp(Calendar timeStamp) {
        this.mTimeStamp = timeStamp;
    }

    public  ArrayList<HourlyWeatherForecast> getHourlyWeatherForecastsToday() {
        return mHourlyWeatherForecastsToday;
    }
    public Location setHourlyWeatherForecastsToday(ArrayList<HourlyWeatherForecast> hourlyWeatherForecastsToday) {
        mHourlyWeatherForecastsToday = hourlyWeatherForecastsToday;
        return this;
    }
    public ArrayList<HourlyWeatherForecast> getHourlyWeatherForecastsTomorrow() {
        return mHourlyWeatherForecastsTomorrow;
    }
    public Location setHourlyWeatherForecastsTomorrow(ArrayList<HourlyWeatherForecast> hourlyWeatherForecastsTomorrow) {
        mHourlyWeatherForecastsTomorrow = hourlyWeatherForecastsTomorrow;
        return this;
    }
    public ArrayList<HourlyWeatherForecast> getHourlyWeatherForecastsLater() {
        return mHourlyWeatherForecastsLater;
    }
    public Location setHourlyWeatherForecastsLater(ArrayList<HourlyWeatherForecast> hourlyWeatherForecastsLater) {
        mHourlyWeatherForecastsLater = hourlyWeatherForecastsLater;
        return this;
    }

    public CurrentWeatherForecast getCurrentWeatherForecast() {
        return mCurrentWeatherForecast;
    }

    public void setCurrentWeatherForecast(CurrentWeatherForecast currentWeatherForecast) {
        this.mCurrentWeatherForecast = currentWeatherForecast;
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
    public void setDays(ArrayList<Day> days){
        mDays = days;
    }

    public Day getDay(int index){
        return mDays.get(index);
    }

    public ArrayList<Day> getDays(){
        return mDays;
    }

    /*
       End Make Parcelable
    */

    @Override
    public String toString() {
        return "Location{" +
                ", Latitude='" + getLatitude() + '\'' +
                ", Longitude='" + getLongitude() + '\'' +
                ", Coordinates='" + getCoordinates() + '\'' +
                 ", Address='" + getFormatted_address() + '\'' +
                ", City=" + getCity() + '\'' +
                ", State='" + getState() +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.mTimeStamp);
        dest.writeString(this.mLatitude);
        dest.writeString(this.mLongitude);
        dest.writeString(this.mCoordinates);
        dest.writeString(this.mCity);
        dest.writeString(this.mState);
        dest.writeString(this.mFormatted_address);
        dest.writeTypedList(this.mDays);
        dest.writeTypedList(this.mHourlyWeatherForecastsToday);
        dest.writeTypedList(this.mHourlyWeatherForecastsTomorrow);
        dest.writeTypedList(this.mHourlyWeatherForecastsLater);
        dest.writeParcelable(this.mCurrentWeatherForecast, flags);
    }

    protected Location(Parcel in) {
        this.mTimeStamp = (Calendar) in.readSerializable();
        this.mLatitude = in.readString();
        this.mLongitude = in.readString();
        this.mCoordinates = in.readString();
        this.mCity = in.readString();
        this.mState = in.readString();
        this.mFormatted_address = in.readString();
        this.mDays = in.createTypedArrayList(Day.CREATOR);
        this.mHourlyWeatherForecastsToday = in.createTypedArrayList(HourlyWeatherForecast.CREATOR);
        this.mHourlyWeatherForecastsTomorrow = in.createTypedArrayList(HourlyWeatherForecast.CREATOR);
        this.mHourlyWeatherForecastsLater = in.createTypedArrayList(HourlyWeatherForecast.CREATOR);
        this.mCurrentWeatherForecast = in.readParcelable(CurrentWeatherForecast.class.getClassLoader());
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel source) {
            return new Location(source);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };
}
