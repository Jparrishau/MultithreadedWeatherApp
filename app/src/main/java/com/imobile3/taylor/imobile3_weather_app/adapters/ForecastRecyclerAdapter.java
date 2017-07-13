package com.imobile3.taylor.imobile3_weather_app.adapters;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.imobile3.taylor.imobile3_weather_app.R;
import com.imobile3.taylor.imobile3_weather_app.models.Day;
import com.imobile3.taylor.imobile3_weather_app.models.Location;

/**
 * Description.
 *
 * @author Taylor Parrish
 * @since 7/11/2017
 */
public class ForecastRecyclerAdapter extends RecyclerView.Adapter<ForecastRecyclerAdapter.ForecastRecyclerViewHolder> {
    private Location mLocation;

    public ForecastRecyclerAdapter(Location location){
        setLocation(location);
    }

    @Override
    public ForecastRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_weather_item, parent, false);
        ForecastRecyclerViewHolder recyclerViewHolder = new ForecastRecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(ForecastRecyclerViewHolder holder, int position) {
        Day day = mLocation.getDay(position);

        holder.weatherIconText.setText(day.getWeatherForecast().getWeatherIcon());
        holder.weekdayText.setText(day.getTextDay());
        holder.conditionsText.setText(day.getWeatherForecast().getConditions());
        holder.humidityText.setText(day.getWeatherForecast().getHumidity() + " %");
        holder.highText.setText(day.getWeatherForecast().getHigh());
        holder.lowText.setText(day.getWeatherForecast().getLow());
    }

    @Override
    public int getItemCount() {
        return mLocation.getDays().size();
    }

    public static class ForecastRecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView weatherIconText;
        TextView weekdayText;
        TextView conditionsText;
        TextView humidityText;
        TextView highText;
        TextView lowText;

        public ForecastRecyclerViewHolder(View view) {
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

    public Location getLocation() {
        return mLocation;
    }

    public void setLocation(Location location) {
        mLocation = location;
    }

}