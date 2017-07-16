package com.imobile3.taylor.imobile3_weather_app.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.imobile3.taylor.imobile3_weather_app.R;
import com.imobile3.taylor.imobile3_weather_app.adapters.DailyForecastRecyclerAdapter;
import com.imobile3.taylor.imobile3_weather_app.adapters.HourlyForecastRecyclerAdapter;
import com.imobile3.taylor.imobile3_weather_app.models.HourlyWeatherForecast;
import com.imobile3.taylor.imobile3_weather_app.models.Location;

import java.util.ArrayList;

/**
 * Description.
 *
 * @author Taylor Parrish
 * @since 7/11/2017
 */
public class RecyclerViewFragment extends Fragment {
    private static final String CLASS_TAG = RecyclerViewFragment.class.getSimpleName();
    private static final boolean DEBUG = true;
    private Location mLocation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (DEBUG) Log.d(CLASS_TAG, "onCreateView(LayoutInflater, ViewGroup, Bundle)");

        Bundle bundle = this.getArguments();
        String locationJSON = bundle.getString("locationData");
        mLocation = new Gson().fromJson(locationJSON, Location.class);
        View view = inflater.inflate(R.layout.fragment_recycler, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        HourlyForecastRecyclerAdapter mAdapter = new HourlyForecastRecyclerAdapter(getForecastData(bundle.getString("type")));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter);
        return view;
    }

    public ArrayList<HourlyWeatherForecast> getForecastData(String type){
        ArrayList<HourlyWeatherForecast> forecastData = new ArrayList<>();

        switch (type){
            case "today":
                forecastData = mLocation.getHourlyWeatherForecastsToday();
                break;
            case "tomorrow":
                forecastData = mLocation.getHourlyWeatherForecastsTomorrow();
                break;
            case "later":
                forecastData = mLocation.getHourlyWeatherForecastsLater();
                break;
        }

        return forecastData;
    }
}