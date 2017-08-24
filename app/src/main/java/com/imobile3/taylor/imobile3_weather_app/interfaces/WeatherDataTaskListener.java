package com.imobile3.taylor.imobile3_weather_app.interfaces;

import com.imobile3.taylor.imobile3_weather_app.models.Location;

/**
 * The Interface WeatherDataTaskListener is used by certain fragments
 * to alert when a task has been started or has ended its task.
 *
 * @author Taylor Parrish
 * @since 8/5/2016
 */
public interface WeatherDataTaskListener {
    void onWeatherDataTaskStarted();

    void onWeatherDataTaskFailed(String failureType);

    void onWeatherDataTaskFinished(Location location);
}
