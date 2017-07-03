package com.imobile3.taylor.imobile3_weather_app.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Taylor Parrish on 8/23/2016.
 *
 * The data object model DailyDetailedWeatherItem is used
 * to hold the detailed weather information of its corresponding DailyWeatherForecast object.
 */
public class DailyDetailedWeatherItem implements Parcelable {
    private String weekday;
    private String description;
    private String pop;


    public DailyDetailedWeatherItem(String weekday, String description, String pop) {
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

    private DailyDetailedWeatherItem(Parcel in) {
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

    public static final Parcelable.Creator<DailyDetailedWeatherItem> CREATOR = new Parcelable.Creator<DailyDetailedWeatherItem>() {
        public DailyDetailedWeatherItem createFromParcel(Parcel in) {
            return new DailyDetailedWeatherItem(in);
        }

        public DailyDetailedWeatherItem[] newArray(int size) {
            return new DailyDetailedWeatherItem[size];
        }
    };
}
