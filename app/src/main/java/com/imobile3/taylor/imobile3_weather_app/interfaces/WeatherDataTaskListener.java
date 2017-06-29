package com.imobile3.taylor.imobile3_weather_app.interfaces;

import com.imobile3.taylor.imobile3_weather_app.models.WeatherForecast;

import java.util.ArrayList;

/**
 * Created by Taylor Parrish on 8/5/2016.
 *
 * The Interface WeatherDataTaskListener is used by certain fragments
 * to alert when a task has been started or has ended its task.
 */
public interface WeatherDataTaskListener {
    void onTaskStarted();

    void onTaskFinished(ArrayList<WeatherForecast> weatherForecasts);
}
