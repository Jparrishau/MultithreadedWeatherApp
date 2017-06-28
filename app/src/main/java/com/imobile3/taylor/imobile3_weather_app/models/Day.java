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
    private int mTime;
    private Date mDate;
    private String mTextDay;
    private int[] hour = new int[24];

    public Day(){
        setDate(Calendar.getInstance().getTime());
        setDay(Calendar.DAY_OF_MONTH);
        setMonth(Calendar.MONTH);
        setYear(Calendar.YEAR);
        setTime(Calendar.AM_PM);
        setTextDay(new SimpleDateFormat("EEEE").format(Calendar.getInstance().getTime()));

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

    public Integer getTime() {
        return mTime;
    }

    public void setTime(Integer time) {
        this.mTime = time;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date fullDate) {
        this.mDate = fullDate;
    }

    public int[] getHour() {
        return hour;
    }

}
