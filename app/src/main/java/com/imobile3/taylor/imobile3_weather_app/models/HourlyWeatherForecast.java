package com.imobile3.taylor.imobile3_weather_app.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * An object model used to contain all the relevant weather data of a specific day.
 *
 * @author Taylor Parrish
 * @since 7/12/2017
 */
public class HourlyWeatherForecast implements Parcelable {
    private String mTemperature;
    private String mFeelsLike;
    private String mCondition;
    private String mHumidity;
    private String mWeatherIcon;

    public HourlyWeatherForecast(String condition, String mTemperature,String feelsLike, String humidity, String icon) {
        setCondition(condition);
        setTemperature(mTemperature);
        setFeelsLike(feelsLike);
        setHumidity(humidity);
        setWeatherIcon(icon);
    }

    public String getHumidity() {
        return mHumidity;
    }

    public void setHumidity(String humidity) {
        this.mHumidity = humidity;
    }

    public String getWeatherIcon() {
        return mWeatherIcon;
    }

    public void setWeatherIcon(String weatherIcon) {
        this.mWeatherIcon = weatherIcon;
    }

    public String getFeelsLike() {
        return mFeelsLike;
    }

    public void setFeelsLike(String feelsLike) {
        mFeelsLike = feelsLike;
    }

    public void setTemperature(String temperature) {
        mTemperature = temperature;
    }

    public String getTemperature() {
        return mTemperature;
    }

    public void setCondition(String condition) {
        mCondition = condition;
    }

    public String getCondition() {
        return mCondition;
    }

    /*
       Begin Make Parcelable
    */
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mTemperature);
        dest.writeString(this.mFeelsLike);
        dest.writeString(this.mCondition);
        dest.writeString(this.mHumidity);
        dest.writeString(this.mWeatherIcon);
    }

    protected HourlyWeatherForecast(Parcel in) {
        this.mTemperature = in.readString();
        this.mFeelsLike = in.readString();
        this.mCondition = in.readString();
        this.mHumidity = in.readString();
        this.mWeatherIcon = in.readString();
    }

    public static final Creator<HourlyWeatherForecast> CREATOR = new Creator<HourlyWeatherForecast>() {
        @Override
        public HourlyWeatherForecast createFromParcel(Parcel source) {
            return new HourlyWeatherForecast(source);
        }

        @Override
        public HourlyWeatherForecast[] newArray(int size) {
            return new HourlyWeatherForecast[size];
        }
    };
    /*
       End Make Parcelable
    */

    @Override
    public String toString() {
        return "HourlyWeatherForecast{" +
                "mTemperature='" + mTemperature + '\'' +
                ", mFeelsLike='" + mFeelsLike + '\'' +
                ", mCondition='" + mCondition + '\'' +
                ", mHumidity='" + mHumidity + '\'' +
                ", mWeatherIcon='" + mWeatherIcon + '\'' +
                '}';
    }
}