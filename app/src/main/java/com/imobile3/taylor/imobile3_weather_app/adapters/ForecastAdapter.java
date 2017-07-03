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
 * the SimpleForecastFragment.
 */
public class ForecastAdapter extends BaseAdapter {
    private Location mLocation;
    private LayoutInflater mInflater = null;

    public ForecastAdapter(Activity context, Location location) {
        super();
        mLocation = location;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View convertedView = convertView;
        View convertedCurrentView = convertView;
        if (convertView == null && convertedCurrentView == null) {
            convertedView = mInflater.inflate(R.layout.row_weather_item, null);
            convertedCurrentView = mInflater.inflate(R.layout.forecast_list, null);
        }
        setupWeatherItemListView(position, convertedView, convertedCurrentView);

        return convertedView;
    }

    private void setupWeatherItemListView(int position, View convertView, View convertViewCurrent) {
        String currentTemp = mLocation.getCurrentWeatherForecast().getTemp_F() + " °F";
        String currentWeatherDescr = mLocation.getCurrentWeatherForecast().getWeather_description();
        String currentPressure = "1016.0 hPa";
        String currentHumidity = mLocation.getCurrentWeatherForecast().getHumidity();
        String weekday = mLocation.getDay(position).getTextDay();
        String conditions = mLocation.getDay(position).getWeatherForecast().getConditions();
        String high = mLocation.getDay(position).getWeatherForecast().getHigh();
        String low = mLocation.getDay(position).getWeatherForecast().getLow();

        TextView currentTempText = (TextView) convertViewCurrent.findViewById(R.id.currentTemperature);
        TextView currentWeatherDescrText = (TextView) convertViewCurrent.findViewById(R.id.currentWeatherDescription);
        TextView currentPressureText = (TextView) convertViewCurrent.findViewById(R.id.currentPressure);
        TextView currentHumidityText = (TextView) convertViewCurrent.findViewById(R.id.currentHumidity);

        TextView weekdayText = (TextView) convertView.findViewById(R.id.weekday);
        TextView conditionsText = (TextView) convertView.findViewById(R.id.conditions);
        TextView highText = (TextView) convertView.findViewById(R.id.highDegree);
        TextView lowText = (TextView) convertView.findViewById(R.id.lowDegree);

        /*currentTempText.setText(currentTemp);
        currentWeatherDescrText.setText(currentWeatherDescr);
        currentPressureText.setText(currentPressure);
        currentHumidityText.setText(currentHumidity);*/
        weekdayText.setText(weekday);
        conditionsText.setText(conditions);
        highText.setText(high);
        lowText.setText(low);
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