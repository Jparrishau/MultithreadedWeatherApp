package com.imobile3.taylor.imobile3_weather_app;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Created by Taylor Parrish on 8/23/2016.
 *
 * A location listner used to listen for location events.
 */
public final class ForecastLocationListener implements LocationListener{

    @Override
    public void onLocationChanged(Location location) {
        //Location changed, do something?
        //Store in cache possibly
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    @Override
    public void onProviderEnabled(String s) {
    }

    @Override
    public void onProviderDisabled(String s) {
    }
}
