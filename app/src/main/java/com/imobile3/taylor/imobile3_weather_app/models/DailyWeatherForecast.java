package com.imobile3.taylor.imobile3_weather_app.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * An object model used to contain all the relevant weather data of a specific day.
 *
 * @author Taylor Parrish
 * @since 8/23/2016
 */
public class DailyWeatherForecast implements Parcelable {
    private String high;
    private String low;
    private String conditions;
    private String humidity;
    private String weatherIcon;
    private ArrayList<DailyDetailedWeatherItem> detailWeatherItems;


    public DailyWeatherForecast(String conditions, String high, String low, String humidity, String icon) {
        this.conditions = conditions;
        this.high = high;
        this.low = low;
        this.humidity = humidity;
        this.weatherIcon = icon;
        this.detailWeatherItems = new ArrayList<>();
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getWeatherIcon() {
        return weatherIcon;
    }

    public void setWeatherIcon(String weatherIcon) {
        this.weatherIcon = weatherIcon;
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

    public ArrayList<DailyDetailedWeatherItem> getDetailWeatherItems() {
        return detailWeatherItems;
    }


    /*
       End Make Parcelable
    */
    @Override
    public String toString() {
        return "DailyWeatherForecast{" +
                ", high='" + high + '\'' +
                ", low='" + low + '\'' +
                ", conditions='" + conditions + '\'' +
                ", detailWeatherItems=" + detailWeatherItems +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.weatherIcon);
        dest.writeString(this.high);
        dest.writeString(this.low);
        dest.writeString(this.conditions);
        dest.writeString(this.humidity);
        dest.writeTypedList(this.detailWeatherItems);
    }

    protected DailyWeatherForecast(Parcel in) {
        this.weatherIcon = in.readString();
        this.high = in.readString();
        this.low = in.readString();
        this.conditions = in.readString();
        this.humidity = in.readString();
        this.detailWeatherItems = in.createTypedArrayList(DailyDetailedWeatherItem.CREATOR);
    }

    public static final Creator<DailyWeatherForecast> CREATOR = new Creator<DailyWeatherForecast>() {
        @Override
        public DailyWeatherForecast createFromParcel(Parcel source) {
            return new DailyWeatherForecast(source);
        }

        @Override
        public DailyWeatherForecast[] newArray(int size) {
            return new DailyWeatherForecast[size];
        }
    };
}
