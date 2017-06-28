package com.imobile3.taylor.imobile3_weather_app.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.imobile3.taylor.imobile3_weather_app.R;
import com.imobile3.taylor.imobile3_weather_app.models.WeatherForecast;

import java.util.ArrayList;

/**
 * Created by Taylor Parrish on 8/23/2016.
 *
 * The adapter responsible for setting up the listview displayed by
 * the SimpleForecastFragment.
 */
public class ForecastAdapter extends BaseAdapter {
    private ArrayList<WeatherForecast> mWeatherForecasts;
    private LayoutInflater mInflater = null;

    public ForecastAdapter(Activity context, ArrayList<WeatherForecast> weatherForecasts) {
        super();
        mWeatherForecasts = weatherForecasts;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View convertedView = convertView;
        if (convertView == null) {
            convertedView = mInflater.inflate(R.layout.row_weather_item, null);
        }
        setupWeatherItemListView(position, convertedView);

        return convertedView;
    }

    private void setupWeatherItemListView(int position, View convertView) {
        TextView weekdayText = (TextView) convertView.findViewById(R.id.weekday);
        TextView condtionsText = (TextView) convertView.findViewById(R.id.conditions);
        TextView highText = (TextView) convertView.findViewById(R.id.highDegree);
        TextView lowText = (TextView) convertView.findViewById(R.id.lowDegree);

        String weekday = mWeatherForecasts.get(position).getWeekday();
        String conditions = mWeatherForecasts.get(position).getConditions();
        String high = mWeatherForecasts.get(position).getHigh();
        String low = mWeatherForecasts.get(position).getLow();

        weekdayText.setText(weekday);
        condtionsText.setText(conditions);
        highText.setText(high);
        lowText.setText(low);
    }

    @Override
    public int getCount() {
        return mWeatherForecasts.size();
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