package com.imobile3.taylor.imobile3_weather_app.interfaces;

import com.imobile3.taylor.imobile3_weather_app.models.Location;

/**
 * Created by Taylor Parrish on 8/5/2016.
 *
 * The Interface LocationDataTaskListener is used by certain fragments
 * to alert when a task has been started or has ended its task.
 */
public interface LocationDataTaskListener {
    void onTaskStarted();

    void onTaskFinished(Location location);
}
