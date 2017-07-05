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

/**
 * Created by Taylor Parrish on 8/23/2016.
 *
 * The adapter responsible for setting up the listview displayed by
 * the CurrentForecastFragment.
 */
public class SimpleForecastAdapter extends BaseAdapter {
    private Location mLocation;
    private LayoutInflater mInflater = null;

    public SimpleForecastAdapter(Activity context, Location location) {
        super();
        mLocation = location;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View convertedView = convertView;
        if (convertView == null) {
            convertedView = mInflater.inflate(R.layout.row_weather_item, null);
        }
        return setupWeatherItemListView(position, convertedView);
    }

    private View setupWeatherItemListView(int position, View convertView) {
        String weekday = mLocation.getDay(position).getTextDay();
        String conditions = mLocation.getDay(position).getWeatherForecast().getConditions();
        String humidity = mLocation.getCurrentWeatherForecast().getHumidity();
        String high = mLocation.getDay(position).getWeatherForecast().getHigh();
        String low = mLocation.getDay(position).getWeatherForecast().getLow();

        TextView weekdayText = (TextView) convertView.findViewById(R.id.weekday);
        TextView conditionsText = (TextView) convertView.findViewById(R.id.conditions);
        TextView humidityText = (TextView) convertView.findViewById(R.id.humidity);
        TextView highText = (TextView) convertView.findViewById(R.id.highDegree);
        TextView lowText = (TextView) convertView.findViewById(R.id.lowDegree);

        weekdayText.setText(weekday);
        conditionsText.setText(conditions);
        humidityText.setText(humidity);
        highText.setText(high);
        lowText.setText(low);

        return convertView;
    }

    @Override
    public int getCount() {
        return mLocation.getDays().size();
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