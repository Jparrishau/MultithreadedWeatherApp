package com.imobile3.taylor.imobile3_weather_app.models;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by taylorp on 6/28/2017.
 *
 * An object model that represents a physical day of the week. (I.E: Mon-Sun)
 */

public class Day {
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
}
