package com.imobile3.taylor.imobile3_weather_app.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.imobile3.taylor.imobile3_weather_app.R;
import com.imobile3.taylor.imobile3_weather_app.models.DailyDetailedWeatherItem;

import java.util.ArrayList;

/**
 * Adapter responsible for setting up the listview displayed by
 * the DetailedForecastFragment.
 *
 * @author Taylor Parrish
 * @since 8/23/2016
 */
public class DetailedForecastAdapter extends BaseAdapter {
    ArrayList<DailyDetailedWeatherItem> detailWeatherItems;
    private LayoutInflater inflater = null;

    public DetailedForecastAdapter(Activity context, ArrayList<DailyDetailedWeatherItem> detailWeatherItems) {
        super();
        setDetailWeatherItems(detailWeatherItems);
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return detailWeatherItems.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View convertedView = convertView;

        if (convertView == null) {
            convertedView = inflater.inflate(R.layout.row_detail_weather_item, null);
        }
        setupWeatherItemView(position, convertedView);

        return convertedView;
    }

    private void setupWeatherItemView(int position, View convertView) {
        TextView weekdayText = (TextView) convertView.findViewById(R.id.detailedWeekday);
        TextView descriptionText = (TextView) convertView.findViewById(R.id.forecastDescription);
        TextView popText = (TextView) convertView.findViewById(R.id.pop);

        String weekday = detailWeatherItems.get(position).getWeekday();
        String description = detailWeatherItems.get(position).getDescription();
        String pop = detailWeatherItems.get(position).getPOP();

        weekdayText.setText(weekday);
        descriptionText.setText(description);
        popText.setText(pop + "%");
    }

    public void setDetailWeatherItems(ArrayList<DailyDetailedWeatherItem> detailWeatherItems) {
        this.detailWeatherItems = detailWeatherItems;
    }
}