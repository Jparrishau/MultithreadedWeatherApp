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
    private String weekday;
    private String high;
    private String low;
    private String conditions;
    private ArrayList<DetailedWeatherItem> detailWeatherItems;


    public WeatherForecast(String weekday, String conditions, String high,
                           String low) {
        this.weekday = weekday;
        this.conditions = conditions;
        this.high = high;
        this.low = low;
        this.detailWeatherItems = new ArrayList<>();
    }

    private WeatherForecast(Parcel in) {
        this.weekday = in.readString();
        this.high = in.readString();
        this.low = in.readString();
        this.conditions = in.readString();
        this.detailWeatherItems = in.readArrayList(null);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(weekday);
        out.writeString(high);
        out.writeString(low);
        out.writeString(conditions);
        out.writeList(detailWeatherItems);
    }

    public void addDetailWeatherItem(DetailedWeatherItem detailWeatherItem) {
        this.detailWeatherItems.add(detailWeatherItem);
    }

    public static final Parcelable.Creator<WeatherForecast> CREATOR = new Parcelable.Creator<WeatherForecast>() {
        public WeatherForecast createFromParcel(Parcel in) {
            return new WeatherForecast(in);
        }

        public WeatherForecast[] newArray(int size) {
            return new WeatherForecast[size];
        }
    };

    public String getWeekday() {
        return weekday;
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

    @Override
    public String toString() {
        return "WeatherForecast{" +
                "weekday='" + weekday + '\'' +
                ", high='" + high + '\'' +
                ", low='" + low + '\'' +
                ", conditions='" + conditions + '\'' +
                ", detailWeatherItems=" + detailWeatherItems +
                '}';
    }
}
