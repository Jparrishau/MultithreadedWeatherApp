package com.imobile3.taylor.imobile3_weather_app.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.imobile3.taylor.imobile3_weather_app.utilities.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
    private Calendar mTimeStamp;

    public HourlyWeatherForecast(String condition, String mTemperature,String feelsLike, String humidity, String icon, Calendar timeStamp) {
        setCondition(condition);
        setTemperature(mTemperature);
        setFeelsLike(feelsLike);
        setHumidity(humidity);
        setWeatherIcon(icon);
        setTimeStamp(timeStamp);
    }

    public String getFormattedTime(String formatString){
        if (mTimeStamp != null)
            return Utils.getFormattedTime(formatString, mTimeStamp);
        else
            return null;
    }

    public Calendar getTimeStamp() {
        return mTimeStamp;
    }

    public void setTimeStamp(Calendar timeStamp) {
        mTimeStamp = timeStamp;
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
        dest.writeSerializable(this.mTimeStamp);
    }

    protected HourlyWeatherForecast(Parcel in) {
        this.mTemperature = in.readString();
        this.mFeelsLike = in.readString();
        this.mCondition = in.readString();
        this.mHumidity = in.readString();
        this.mWeatherIcon = in.readString();
        this.mTimeStamp = (Calendar) in.readSerializable();
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
}