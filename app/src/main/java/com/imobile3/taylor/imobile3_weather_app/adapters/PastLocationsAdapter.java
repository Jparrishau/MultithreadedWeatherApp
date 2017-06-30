package com.imobile3.taylor.imobile3_weather_app.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.imobile3.taylor.imobile3_weather_app.R;
import com.imobile3.taylor.imobile3_weather_app.models.Location;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Taylor Parrish on 8/2/2016.
 *
 * Adapter for setting up the listview displayed by
 * the pastLocastionsListView
 */
public class PastLocationsAdapter extends BaseAdapter {
    private ArrayList<Location> mPastLocations;
    private LayoutInflater mInflater;

    public PastLocationsAdapter(Activity context, ArrayList<Location> locations) {
        super();
        mPastLocations = locations;

        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View convertedView = convertView;
        if (convertView == null) {
            convertedView = mInflater.inflate(R.layout.row_pastlocation_list, null);
        }
        setupLocationListView(position, convertedView);

        return convertedView;
    }

    private void setupLocationListView(int position, View convertView) {
        TextView cityText = (TextView) convertView.findViewById(R.id.cityText);
        TextView degreeText = (TextView) convertView.findViewById(R.id.degreeText);

        String city = mPastLocations.get(position).getCity();
        String degree = mPastLocations.get(position).getDay(0).getWeatherForecast().getHigh();

        cityText.setText(city);
        degreeText.setText(degree);
    }

    @Override
    public int getCount() {
        return mPastLocations.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
}
