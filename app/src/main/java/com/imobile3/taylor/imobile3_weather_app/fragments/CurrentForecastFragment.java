package com.imobile3.taylor.imobile3_weather_app.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.imobile3.taylor.imobile3_weather_app.R;
import com.imobile3.taylor.imobile3_weather_app.activities.DetailedWeatherForecastActivity;
import com.imobile3.taylor.imobile3_weather_app.adapters.SimpleForecastAdapter;
import com.imobile3.taylor.imobile3_weather_app.models.DailyDetailedWeatherItem;
import com.imobile3.taylor.imobile3_weather_app.models.Location;

import java.util.ArrayList;

/**
 * Created by Taylor Parrish on 8/29/2016
 *
 * CurrentForecastFragment runs a background task using the users location
 * data that searches for relevant weather details to then be displayed to the user.
 */
public class CurrentForecastFragment extends Fragment {
    private static final String CLASS_TAG = CurrentForecastFragment.class.getSimpleName();
    private static final boolean DEBUG = true;

    public final static String TAG_EXTRA_DETAIL_ITEMS = "detailWeatherItems";
    public final static String TAG_LOCATION_BUNDLE = "locationBundle";

    Location mLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (DEBUG) Log.d(CLASS_TAG, "onCreate(Bundle)");
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mLocation =
                getActivity().getIntent().getParcelableExtra(TAG_LOCATION_BUNDLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (DEBUG) Log.d(CLASS_TAG, "onCreateView(LayoutInflater, ViewGroup, Bundle)");

        String currentTemp = mLocation.getCurrentWeatherForecast().getTemp_F() + " °F";
        String currentWeatherDescr = mLocation.getCurrentWeatherForecast().getWeather_description();
        String currentPressure = "1016.0 hPa";
        String currentHumidity = mLocation.getCurrentWeatherForecast().getHumidity();
        Double currentWindSpeed = mLocation.getCurrentWeatherForecast().getWind_mph();

        View view = inflater.inflate(R.layout.forecast_fragment, container, false);
        TextView currentTempText = (TextView) view.findViewById(R.id.currentTemperature);
        TextView currentWeatherDescrText = (TextView) view.findViewById(R.id.currentWeatherDescription);
        TextView currentPressureText = (TextView) view.findViewById(R.id.currentPressure);
        TextView currentHumidityText = (TextView) view.findViewById(R.id.currentHumidity);
        TextView currentWindSpeedText = (TextView) view.findViewById(R.id.currentWind);
        TextView sunriseText = (TextView) view.findViewById(R.id.todaySunrise);
        TextView sunsetText = (TextView) view.findViewById(R.id.todaySunset);

        currentTempText.setText(currentTemp);
        currentWeatherDescrText.setText(currentWeatherDescr);
        currentWindSpeedText.setText("Wind: " + currentWindSpeed + " Mph");
        currentPressureText.setText("Pressure: " + currentPressure);
        currentHumidityText.setText("Humidity: " + currentHumidity);
        sunriseText.setText("Sunrise: " + "5:42am");
        sunsetText.setText("Sunset: " + "8:00pm");
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (DEBUG) Log.d(CLASS_TAG, "onActivityCreated(Bundle)");
        super.onActivityCreated(savedInstanceState);
        setupWeatherItemListView(mLocation);
    }
    private void setupWeatherItemListView(final Location location) {
        ListView simpleForecastListview = (ListView) getActivity().findViewById(R.id.forecastListView);
        simpleForecastListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                Intent detailedforecastIntent = new Intent(getActivity(), DetailedWeatherForecastActivity.class);
                ArrayList<DailyDetailedWeatherItem> detailItems = location.getDay(position).getWeatherForecast().getDetailWeatherItems();

                detailedforecastIntent.putParcelableArrayListExtra(TAG_EXTRA_DETAIL_ITEMS, detailItems);
                startActivity(detailedforecastIntent);
            }
        });
        SimpleForecastAdapter adapter = new SimpleForecastAdapter(getActivity(), location);
        simpleForecastListview.setAdapter(adapter);
    }
}
