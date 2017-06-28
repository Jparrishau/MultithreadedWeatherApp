package com.imobile3.taylor.imobile3_weather_app.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.imobile3.taylor.imobile3_weather_app.HttpJSONRequest;
import com.imobile3.taylor.imobile3_weather_app.R;
import com.imobile3.taylor.imobile3_weather_app.activities.DetailedForecastActivity;
import com.imobile3.taylor.imobile3_weather_app.activities.MainActivity;
import com.imobile3.taylor.imobile3_weather_app.adapters.ForecastAdapter;
import com.imobile3.taylor.imobile3_weather_app.interfaces.WeatherDataTaskListener;
import com.imobile3.taylor.imobile3_weather_app.models.DetailedWeatherItem;
import com.imobile3.taylor.imobile3_weather_app.models.WeatherItem;
import com.imobile3.taylor.imobile3_weather_app.utilities.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Taylor Parrish on 8/29/2016
 *
 * SimpleForecastFragment runs a background task using the users location
 * data that searches for relevant weather details to then be displayed to the user.
 */
public class SimpleForecastFragment extends Fragment {
    private static final String CLASS_TAG = SimpleForecastFragment.class.getSimpleName();
    private static final boolean DEBUG = true;

    private boolean mIsTaskRunning = false;

    private Bundle saveWeatherItems;
    public final static String TAG_EXTRA_DETAIL_ITEMS = "detailWeatherItems";
    public final static String TAG_WEATHER_ITEM_BUNDLE = "weatherBundle";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (DEBUG) Log.d(CLASS_TAG, "onCreate(Bundle)");
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        saveWeatherItems = new Bundle();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (DEBUG) Log.d(CLASS_TAG, "onCreateView(LayoutInflater, ViewGroup, Bundle)");

        return inflater.inflate(R.layout.forecast_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (DEBUG) Log.d(CLASS_TAG, "onActivityCreated(Bundle)");
        super.onActivityCreated(savedInstanceState);

        if (!saveWeatherItems.containsKey(TAG_WEATHER_ITEM_BUNDLE)) {
            // Pull in data objects from parceble bundle
        } else {
            ArrayList<WeatherItem> weatherItems =
                    saveWeatherItems.getParcelableArrayList(TAG_WEATHER_ITEM_BUNDLE);
            setupWeatherItemListView(weatherItems);
        }
    }

    private void setupWeatherItemListView(final ArrayList<WeatherItem> weatherItems) {
        ListView simpleForecastListview = (ListView) getActivity().findViewById(R.id.forecastListView);
        simpleForecastListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                Intent detailedforecastIntent = new Intent(getActivity(), DetailedForecastActivity.class);
                ArrayList<DetailedWeatherItem> detailItems = weatherItems.get(position).getDetailWeatherItems();

                detailedforecastIntent.putParcelableArrayListExtra(TAG_EXTRA_DETAIL_ITEMS, detailItems);
                startActivity(detailedforecastIntent);
            }
        });
        ForecastAdapter adapter = new ForecastAdapter(getActivity(), weatherItems);
        simpleForecastListview.setAdapter(adapter);
    }
}
