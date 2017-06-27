package com.imobile3.taylor.imobile3_weather_app.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Taylor Parrish on 8/23/2016.
 *
 * The data object model DetailedWeatherItem is used
 * to hold the detailed weather information of its corresponding WeatherItem object.
 */
public class DetailedWeatherItem implements Parcelable {
    private String weekday;
    private String description;
    private String pop;


    public DetailedWeatherItem(String weekday, String description, String pop) {
        this.weekday = weekday;
        this.description = description;
        this.pop = pop;
    }

    public String getWeekday() {
        return weekday;
    }

    public String getDescription() {
        return description;
    }

    public String getPOP() {
        return pop;
    }

    private DetailedWeatherItem(Parcel in) {
        weekday = in.readString();
        description = in.readString();
        pop = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(weekday);
        out.writeString(description);
        out.writeString(pop);
    }

    public static final Parcelable.Creator<DetailedWeatherItem> CREATOR = new Parcelable.Creator<DetailedWeatherItem>() {
        public DetailedWeatherItem createFromParcel(Parcel in) {
            return new DetailedWeatherItem(in);
        }

        public DetailedWeatherItem[] newArray(int size) {
            return new DetailedWeatherItem[size];
        }
    };
}
