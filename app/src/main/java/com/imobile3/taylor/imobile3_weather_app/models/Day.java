package com.imobile3.taylor.imobile3_weather_app.models;


import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by taylorp on 6/28/2017.
 *
 * An object model that represents a physical day of the week. (I.E: Mon-Sun)
 */

public class Day implements Parcelable{
    private int mDay;
    private int mMonth;
    private int mYear;
    private long mTime;
    private Date mDate;
    private String mTextDay;
    private WeatherForecast mWeatherForecast;


    public Day(){
        setDate(Calendar.getInstance().getTime());
        setDay(Calendar.DAY_OF_MONTH);
        setMonth(Calendar.MONTH);
        setYear(Calendar.YEAR);
        setTime(Calendar.AM_PM);
        setTextDay(new SimpleDateFormat("EEEE").format(Calendar.getInstance().getTime()));
    }

    public Day(Date date, int day, int month, int year, long time, String mTextDay, WeatherForecast weatherForecast){
        setDate(date);
        setDay(day);
        setMonth(month);
        setYear(year);
        setTime(time);
        setTextDay(mTextDay);
        setWeatherForecast(weatherForecast);
    }

    public Day(int day, int month, int year, long time, String mTextDay, WeatherForecast weatherForecast){
        setDay(day);
        setMonth(month);
        setYear(year);
        setTime(time);
        setTextDay(mTextDay);
        setWeatherForecast(weatherForecast);
    }

    public String getTextDay() {
        return mTextDay;
    }

    public void setTextDay(String simpleDay) {
        this.mTextDay = simpleDay;
    }

    public Integer getDay() {
        return mDay;
    }

    public void setDay(Integer day) {
        this.mDay = day;
    }

    public Integer getMonth() {
        return mMonth;
    }

    public void setMonth(Integer month) {
        this.mMonth = month;
    }

    public Integer getYear() {
        return mYear;
    }

    public void setYear(Integer year) {
        this.mYear = year;
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        this.mTime = time;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date fullDate) {
        this.mDate = fullDate;
    }

    public void setWeatherForecast(WeatherForecast weatherForecast){
        this.mWeatherForecast = weatherForecast;
    }

    public WeatherForecast getWeatherForecast(){
        return mWeatherForecast;
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
        dest.writeInt(this.mDay);
        dest.writeInt(this.mMonth);
        dest.writeInt(this.mYear);
        dest.writeLong(this.mTime);
        dest.writeLong(this.mDate != null ? this.mDate.getTime() : -1);
        dest.writeString(this.mTextDay);
        dest.writeParcelable(this.mWeatherForecast, flags);
    }

    protected Day(Parcel in) {
        this.mDay = in.readInt();
        this.mMonth = in.readInt();
        this.mYear = in.readInt();
        this.mTime = in.readLong();
        long tmpMDate = in.readLong();
        this.mDate = tmpMDate == -1 ? null : new Date(tmpMDate);
        this.mTextDay = in.readString();
        this.mWeatherForecast = in.readParcelable(WeatherForecast.class.getClassLoader());
    }

    public static final Creator<Day> CREATOR = new Creator<Day>() {
        @Override
        public Day createFromParcel(Parcel source) {
            return new Day(source);
        }

        @Override
        public Day[] newArray(int size) {
            return new Day[size];
        }
    };
     /*
        End Make Parcelable
    */

    @Override
    public String toString() {
        return "Day{" +
                "Day=" + mDay +
                ", Month=" + mMonth +
                ", Year=" + mYear +
                ", Time=" + mTime +
                ", Date=" + mDate +
                ", TextDay='" + mTextDay + '\'' +
                ", WeatherForecast=" + mWeatherForecast +
                '}';
    }
}
