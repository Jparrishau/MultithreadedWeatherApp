package com.imobile3.taylor.imobile3_weather_app.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.imobile3.taylor.imobile3_weather_app.R;
import com.imobile3.taylor.imobile3_weather_app.adapters.DetailedForecastAdapter;
import com.imobile3.taylor.imobile3_weather_app.models.DailyDetailedWeatherItem;

import java.util.ArrayList;

/**
 * Created by Taylor Parrish on 8/29/2016.
 *
 * DetailedForecastFragment is responsible for setting up
 * the detailed weather information into a list and displaying it to the user.
 */
public class DetailedForecastFragment extends Fragment {
    private static final String CLASS_TAG = DetailedForecastFragment.class.getSimpleName();

    private static final boolean DEBUG = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (DEBUG) Log.d(CLASS_TAG, "onCreateView(LayoutInflater, ViewGroup, Bundle)");

        View root = inflater.inflate(R.layout.forecast_details_list, container, false);

        //Get weatherItems arraylist to pass to detailedListview
        ArrayList<DailyDetailedWeatherItem> detailWeatherItems = getActivity().getIntent()
                .getParcelableArrayListExtra(CurrentForecastFragment.TAG_EXTRA_DETAIL_ITEMS);

        ListView detailListview = (ListView) root.findViewById(R.id.detailForecastListView);

        DetailedForecastAdapter adapter =
                new DetailedForecastAdapter(getActivity(), detailWeatherItems);
        detailListview.setAdapter(adapter);

        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (DEBUG) Log.d(CLASS_TAG, "onActivityCreated(Bundle)");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (DEBUG) Log.d(CLASS_TAG, "onConfigurationChanged(Configuration)");
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onDetach() {
        if (DEBUG) Log.d(CLASS_TAG, "onDetach()");
        super.onDetach();
    }

}
