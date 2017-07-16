package com.imobile3.taylor.imobile3_weather_app.adapters;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.imobile3.taylor.imobile3_weather_app.R;
import com.imobile3.taylor.imobile3_weather_app.models.HourlyWeatherForecast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by taylorp on 7/14/2017.
 *
 * @author Taylor Parrish
 * @since 7/14/2017
 */
public class HourlyForecastRecyclerAdapter extends RecyclerView.Adapter<com.imobile3.taylor.imobile3_weather_app.adapters.HourlyForecastRecyclerAdapter.HourlyForecastRecyclerViewHolder> {
    private ArrayList<HourlyWeatherForecast> mHourlyWeatherForecasts;

    public HourlyForecastRecyclerAdapter(ArrayList<HourlyWeatherForecast> hourlyWeatherForecasts){
        setHourlyWeatherForecasts(hourlyWeatherForecasts);
    }

    @Override
    public HourlyForecastRecyclerAdapter.HourlyForecastRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_weather_item, parent, false);
        HourlyForecastRecyclerAdapter.HourlyForecastRecyclerViewHolder recyclerViewHolder =
                new HourlyForecastRecyclerAdapter.HourlyForecastRecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(HourlyForecastRecyclerAdapter.HourlyForecastRecyclerViewHolder holder, int position) {
        String formattedTime = mHourlyWeatherForecasts.get(position).getFormattedTime("EEEE - h:mm a");

        holder.weatherIconText.setText(mHourlyWeatherForecasts.get(position).getWeatherIcon());
        holder.weekdayText.setText(formattedTime);
        holder.conditionsText.setText(mHourlyWeatherForecasts.get(position).getCondition());
        holder.humidityText.setText(mHourlyWeatherForecasts.get(position).getHumidity()  + " %");
        holder.highText.setText(mHourlyWeatherForecasts.get(position).getTemperature() + " ˚F");
        holder.lowText.setText(mHourlyWeatherForecasts.get(position).getFeelsLike() + " ˚F");
    }

    public ArrayList<HourlyWeatherForecast> getHourlyWeatherForecasts() {
        return mHourlyWeatherForecasts;
    }

    public void setHourlyWeatherForecasts(ArrayList<HourlyWeatherForecast> hourlyWeatherForecasts) {
        mHourlyWeatherForecasts = hourlyWeatherForecasts;
    }


    @Override
    public int getItemCount() {
        return mHourlyWeatherForecasts.size();
    }

    public static class HourlyForecastRecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView weatherIconText;
        TextView weekdayText;
        TextView conditionsText;
        TextView humidityText;
        TextView highText;
        TextView lowText;

        public HourlyForecastRecyclerViewHolder(View view) {
            super(view);
            Typeface weatherFont
                    = Typeface.createFromAsset(view.getContext().getAssets(), "font/weathericons.ttf");

            weatherIconText = (TextView) view.findViewById(R.id.weatherIcon);
            weatherIconText.setTypeface(weatherFont);
            weekdayText = (TextView) view.findViewById(R.id.weekday);
            conditionsText = (TextView) view.findViewById(R.id.condition);
            humidityText = (TextView) view.findViewById(R.id.humidity);
            highText = (TextView) view.findViewById(R.id.highDegree);
            lowText = (TextView) view.findViewById(R.id.lowDegree);
        }
    }

}