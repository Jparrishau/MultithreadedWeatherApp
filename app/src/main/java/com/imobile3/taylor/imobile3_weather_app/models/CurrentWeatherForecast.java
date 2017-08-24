package com.imobile3.taylor.imobile3_weather_app.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Description.
 *
 * @author Taylor Parrish
 * @since 7/3/2017
 */
public class CurrentWeatherForecast implements Parcelable {
    private String mWeatherIcon;
    private String mWeather_description;
    private String mTempText;

    private double mTemp_F;
    private double mTemp_C;
    private String mHumidity;

    private String mSunriseTime;
    private String mSunsetTime;
    private String mWindText;
    private String mWindDir;
    private int mWind_degrees;
    private double mPressure_mb;
    private double mPressure_in;
    private double mWind_mph;
    private double mWind_gust_mph;
    private double mWind_kph;
    private double mWind_gust_kph;


    public CurrentWeatherForecast(String weatherIcon, String weather_description, String tempText,
                                  double temp_F, double temp_C,
                                  String humidity, String wind_text, String wind_dir,
                                  int wind_degrees, double pressure_mb, double pressure_in,  double wind_mph, double wind_gust_mph,
                                  double wind_kph, double wind_gust_kph, String sunriseTime, String sunsetTime) {
        setWeatherIcon(weatherIcon);
        setWeather_description(weather_description);
        setTempText(tempText);
        setTemp_F(temp_F);
        setTemp_C(temp_C);
        setHumidity(humidity);
        setSunriseTime(sunriseTime);
        setSunsetTime(sunsetTime);
        setWind_text(wind_text);
        setWind_dir(wind_dir);
        setWind_degrees(wind_degrees);
        setWind_mph(wind_mph);
        setWind_gust_mph(wind_gust_mph);
        setWind_kph(wind_kph);
        setWind_kph(wind_gust_kph);
        setPressure_mb(pressure_mb);
        setPressure_in(pressure_in);
    }

    public double getPressure_mb() {
        return mPressure_mb;
    }

    public void setPressure_mb(double pressure_mb) {
        this.mPressure_mb = pressure_mb;
    }

    public double getPressure_in() {
        return mPressure_in;
    }

    public void setPressure_in(double pressure_in) {
        this.mPressure_in = pressure_in;
    }

    public String getSunriseTime() {
        return mSunriseTime;
    }

    public void setSunriseTime(String mSunriseTime) {
        this.mSunriseTime = mSunriseTime;
    }

    public String getSunsetTime() {
        return mSunsetTime;
    }

    public void setSunsetTime(String mSunsetTime) {
        this.mSunsetTime = mSunsetTime;
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
        return mWindDir;
    }

    public void setWind_dir(String wind_dir) {
        mWindDir = wind_dir;
    }

    public String getWind_text() {
        return mWindText;
    }

    public void setWind_text(String wind_text) {
        mWindText = wind_text;
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
        dest.writeString(this.mSunriseTime);
        dest.writeString(this.mSunsetTime);
        dest.writeString(this.mWindText);
        dest.writeString(this.mWindDir);
        dest.writeInt(this.mWind_degrees);
        dest.writeDouble(this.mPressure_mb);
        dest.writeDouble(this.mPressure_in);
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
        this.mSunriseTime = in.readString();
        this.mSunsetTime = in.readString();
        this.mWindText = in.readString();
        this.mWindDir = in.readString();
        this.mWind_degrees = in.readInt();
        this.mPressure_mb = in.readDouble();
        this.mPressure_in = in.readDouble();
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
