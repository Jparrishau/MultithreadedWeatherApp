package com.imobile3.taylor.imobile3_weather_app.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Taylor Parrish on 8/23/2016.
 *
 * An object model used to contain all the relevant weather data of a specific day.
 */
public class WeatherForecast implements Parcelable {
    private String high;
    private String low;
    private String conditions;
    private ArrayList<DetailedWeatherItem> detailWeatherItems;


    public WeatherForecast(String conditions, String high, String low) {
        this.conditions = conditions;
        this.high = high;
        this.low = low;
        this.detailWeatherItems = new ArrayList<>();
    }

    public String getHigh() {
        return high;
    }

    public String getLow() {
        return low;
    }

    public String getConditions() {
        return conditions;
    }

    public ArrayList<DetailedWeatherItem> getDetailWeatherItems() {
        return detailWeatherItems;
    }


    /*
       Make Parcelable
   */
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.high);
        dest.writeString(this.low);
        dest.writeString(this.conditions);
        dest.writeTypedList(this.detailWeatherItems);
    }

    protected WeatherForecast(Parcel in) {
        this.high = in.readString();
        this.low = in.readString();
        this.conditions = in.readString();
        this.detailWeatherItems = in.createTypedArrayList(DetailedWeatherItem.CREATOR);
    }

    public static final Creator<WeatherForecast> CREATOR = new Creator<WeatherForecast>() {
        @Override
        public WeatherForecast createFromParcel(Parcel source) {
            return new WeatherForecast(source);
        }

        @Override
        public WeatherForecast[] newArray(int size) {
            return new WeatherForecast[size];
        }
    };
    /*
       End Make Parcelable
    */

    @Override
    public String toString() {
        return "WeatherForecast{" +
                ", high='" + high + '\'' +
                ", low='" + low + '\'' +
                ", conditions='" + conditions + '\'' +
                ", detailWeatherItems=" + detailWeatherItems +
                '}';
    }
}
