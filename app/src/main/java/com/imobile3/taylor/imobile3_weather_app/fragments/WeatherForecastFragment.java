package com.imobile3.taylor.imobile3_weather_app.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.imobile3.taylor.imobile3_weather_app.R;

import com.imobile3.taylor.imobile3_weather_app.adapters.SimpleForecastAdapter;
import com.imobile3.taylor.imobile3_weather_app.models.Location;

/**
 * Created by Taylor Parrish on 7/4/2017.
 */
public class WeatherForecastFragment extends Fragment {
    private static final String CLASS_TAG = WeatherForecastFragment.class.getSimpleName();
    private static final boolean DEBUG = true;

    public final static String TAG_EXTRA_DETAIL_ITEMS = "detailWeatherItems";
    public final static String TAG_LOCATION_BUNDLE = "locationBundle";

    private String title;
    private int page;

    Location mLocation;

    public static WeatherForecastFragment newInstance(int page, String title){
        WeatherForecastFragment weatherForecastFragment = new WeatherForecastFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        weatherForecastFragment.setArguments(args);
        return weatherForecastFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (DEBUG) Log.d(CLASS_TAG, "onCreate(Bundle)");
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mLocation =
                getActivity().getIntent().getParcelableExtra(TAG_LOCATION_BUNDLE);

        //page = getArguments().getInt("someInt", 0);
        //title = getArguments().getString("someTitle");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (DEBUG) Log.d(CLASS_TAG, "onCreateView(LayoutInflater, ViewGroup, Bundle)");

        //ListView simpleForecastListview = (ListView) getActivity().findViewById(R.id.forecastListView);
        View view = inflater.inflate(R.layout.row_weather_item, container, false);
        String weekday = mLocation.getDay(0).getTextDay();
        String conditions = mLocation.getDay(0).getWeatherForecast().getConditions();
        String humidity = mLocation.getCurrentWeatherForecast().getHumidity();
        String high = mLocation.getDay(0).getWeatherForecast().getHigh();
        String low = mLocation.getDay(0).getWeatherForecast().getLow();

        TextView weekdayText = (TextView) view.findViewById(R.id.weekday);
        TextView conditionsText = (TextView) view.findViewById(R.id.conditions);
        TextView humidityText = (TextView) view.findViewById(R.id.humidity);
        TextView highText = (TextView) view.findViewById(R.id.highDegree);
        TextView lowText = (TextView) view.findViewById(R.id.lowDegree);

        weekdayText.setText(weekday);
        conditionsText.setText(conditions);
        humidityText.setText(humidity);
        highText.setText(high);
        lowText.setText(low);
        //SimpleForecastAdapter adapter = new SimpleForecastAdapter(getActivity(), mLocation);
        //simpleForecastListview.setAdapter(adapter);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (DEBUG) Log.d(CLASS_TAG, "onActivityCreated(Bundle)");
        super.onActivityCreated(savedInstanceState);
        //setupWeatherItemListView(mLocation);
    }

/*private void setupWeatherItemListView(final Location location) {
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
}*/
}
