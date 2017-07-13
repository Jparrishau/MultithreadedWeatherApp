package com.imobile3.taylor.imobile3_weather_app.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by taylorp on 7/3/2017.
 */
public class CurrentWeatherForecast implements Parcelable {
    private String mWeatherIcon;
    private String mWeather_description;
    private String mTempText;

    private double mTemp_F;
    private double mTemp_C;
    private String mHumidity;

    private String mWind_text;
    private String mWind_dir;
    private int mWind_degrees;
    private double mWind_mph;
    private double mWind_gust_mph;
    private double mWind_kph;
    private double mWind_gust_kph;


    public CurrentWeatherForecast(String weatherIcon, String weather_description, String tempText,
                                  double temp_F, double temp_C,
                                  String humidity, String wind_text, String wind_dir,
                                  int wind_degrees, double wind_mph, double wind_gust_mph,
                                  double wind_kph, double wind_gust_kph) {
        setWeatherIcon(weatherIcon);
        setWeather_description(weather_description);
        setTempText(tempText);
        setTemp_F(temp_F);
        setTemp_C(temp_C);
        setHumidity(humidity);
        setWind_text(wind_text);
        setWind_dir(wind_dir);
        setWind_degrees(wind_degrees);
        setWind_mph(wind_mph);
        setWind_gust_mph(wind_gust_mph);
        setWind_kph(wind_kph);
        setWind_kph(wind_gust_kph);
    }

    public String getWeatherIcon() {
        return mWeatherIcon;
    }

    public void setWeatherIcon(String weatherIcon) {
        mWeatherIcon = weatherIcon;
    }

    public String getWeather_description() {
        return mWeather_description;
    }

    public void setWeather_description(String weather_description) {
        mWeather_description = weather_description;
    }

    public String getTempText() {
        return mTempText;
    }

    public void setTempText(String tempText) {
        mTempText = tempText;
    }

    public double getTemp_F() {
        return mTemp_F;
    }

    public void setTemp_F(double temp_F) {
        mTemp_F = temp_F;
    }

    public double getTemp_C() {
        return mTemp_C;
    }

    public void setTemp_C(double temp_C) {
        mTemp_C = temp_C;
    }

    public String getHumidity() {
        return mHumidity;
    }

    public void setHumidity(String humidity) {
        mHumidity = humidity;
    }

    public String getWind_dir() {
        return mWind_dir;
    }

    public void setWind_dir(String wind_dir) {
        mWind_dir = wind_dir;
    }

    public String getWind_text() {
        return mWind_text;
    }

    public void setWind_text(String wind_text) {
        mWind_text = wind_text;
    }

    public int getWind_degrees() {
        return mWind_degrees;
    }

    public void setWind_degrees(int wind_degrees) {
        mWind_degrees = wind_degrees;
    }

    public double getWind_mph() {
        return mWind_mph;
    }

    public void setWind_mph(double wind_mph) {
        mWind_mph = wind_mph;
    }

    public double getWind_gust_mph() {
        return mWind_gust_mph;
    }

    public void setWind_gust_mph(double wind_gust_mph) {
        mWind_gust_mph = wind_gust_mph;
    }

    public double getWind_kph() {
        return mWind_kph;
    }

    public void setWind_kph(double wind_kph) {
        mWind_kph = wind_kph;
    }

    public double getWind_gust_kph() {
        return mWind_gust_kph;
    }

    public void setWind_gust_kph(double wind_gust_kph) {
        mWind_gust_kph = wind_gust_kph;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mWeatherIcon);
        dest.writeString(this.mWeather_description);
        dest.writeString(this.mTempText);
        dest.writeDouble(this.mTemp_F);
        dest.writeDouble(this.mTemp_C);
        dest.writeString(this.mHumidity);
        dest.writeString(this.mWind_text);
        dest.writeString(this.mWind_dir);
        dest.writeInt(this.mWind_degrees);
        dest.writeDouble(this.mWind_mph);
        dest.writeDouble(this.mWind_gust_mph);
        dest.writeDouble(this.mWind_kph);
        dest.writeDouble(this.mWind_gust_kph);
    }

    protected CurrentWeatherForecast(Parcel in) {
        this.mWeatherIcon = in.readString();
        this.mWeather_description = in.readString();
        this.mTempText = in.readString();
        this.mTemp_F = in.readDouble();
        this.mTemp_C = in.readDouble();
        this.mHumidity = in.readString();
        this.mWind_text = in.readString();
        this.mWind_dir = in.readString();
        this.mWind_degrees = in.readInt();
        this.mWind_mph = in.readDouble();
        this.mWind_gust_mph = in.readDouble();
        this.mWind_kph = in.readDouble();
        this.mWind_gust_kph = in.readDouble();
    }

    public static final Creator<CurrentWeatherForecast> CREATOR = new Creator<CurrentWeatherForecast>() {
        @Override
        public CurrentWeatherForecast createFromParcel(Parcel source) {
            return new CurrentWeatherForecast(source);
        }

        @Override
        public CurrentWeatherForecast[] newArray(int size) {
            return new CurrentWeatherForecast[size];
        }
    };
}
