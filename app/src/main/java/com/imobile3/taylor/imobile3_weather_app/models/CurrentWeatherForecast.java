package com.imobile3.taylor.imobile3_weather_app.models;

/**
 * Created by taylorp on 7/3/2017.
 */

public class CurrentWeatherForecast {
    private String mWeather_description;
    private String mCurrentTempText;

    private double mCurrentTemp_F;
    private double mCurrentTemp_C;
    private String mHumidity;

    private String mWind_text;
    private String mWind_dir;
    private int mWind_degrees;
    private double mWind_mph;
    private double mWind_gust_mph;
    private double mWind_kph;
    private double mWind_gust_kph;


    public CurrentWeatherForecast(String weather_description, String currentTempText,
                                  double currentTemp_F, double currentTemp_C,
                                  String humidity, String wind_text, String wind_dir,
                                  int wind_degrees, double wind_mph, double wind_gust_mph,
                                  double wind_kph, double wind_gust_kph) {
        mWeather_description = weather_description;
        mCurrentTempText = currentTempText;
        mCurrentTemp_F = currentTemp_F;
        mCurrentTemp_C = currentTemp_C;
        mHumidity = humidity;
        mWind_text = wind_text;
        mWind_dir = wind_dir;
        mWind_degrees = wind_degrees;
        mWind_mph = wind_mph;
        mWind_gust_mph = wind_gust_mph;
        mWind_kph = wind_kph;
        mWind_gust_kph = wind_gust_kph;
    }

    public String getWeather_description() {
        return mWeather_description;
    }

    public void setWeather_description(String weather_description) {
        mWeather_description = weather_description;
    }

    public String getCurrentTempText() {
        return mCurrentTempText;
    }

    public void setCurrentTempText(String currentTempText) {
        mCurrentTempText = currentTempText;
    }

    public double getCurrentTemp_F() {
        return mCurrentTemp_F;
    }

    public void setCurrentTemp_F(double currentTemp_F) {
        mCurrentTemp_F = currentTemp_F;
    }

    public double getCurrentTemp_C() {
        return mCurrentTemp_C;
    }

    public void setCurrentTemp_C(double currentTemp_C) {
        mCurrentTemp_C = currentTemp_C;
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

}
