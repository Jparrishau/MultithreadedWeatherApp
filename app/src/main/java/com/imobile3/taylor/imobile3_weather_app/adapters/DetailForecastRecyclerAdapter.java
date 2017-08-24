package com.imobile3.taylor.imobile3_weather_app.adapters;

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
 * @since 7/13/2017
 */
public class DetailForecastRecyclerAdapter extends RecyclerView.Adapter<DetailForecastRecyclerAdapter.DetailForecastViewHolder> {
    private Location mLocation;

    public DetailForecastRecyclerAdapter(Location location){
        setLocation(location);
    }

    @Override
    public DetailForecastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_detail_weather_item, parent, false);
        DetailForecastViewHolder recyclerViewHolder = new DetailForecastViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(DetailForecastViewHolder holder, int position) {
        Day day = mLocation.getDay(position);

        holder.detailWeekdayText.setText(day.getWeatherForecast().getDetailWeatherItems().get(position).getWeekday());
        holder.detailDescriptionText.setText(day.getWeatherForecast().getDetailWeatherItems().get(position).getDescription());
        holder.detailPOPText.setText(day.getWeatherForecast().getDetailWeatherItems().get(position).getPOP());
    }

    @Override
    public int getItemCount() {
        return mLocation.getDays().size();
    }

    public Location getLocation() {
        return mLocation;
    }

    public void setLocation(Location location) {
        mLocation = location;
    }

    public static class DetailForecastViewHolder extends RecyclerView.ViewHolder {

        TextView detailWeekdayText;
        TextView detailDescriptionText;
        TextView detailPOPText;


        public DetailForecastViewHolder(View view) {
            super(view);

            detailWeekdayText = (TextView) view.findViewById(R.id.detailWeekday);
            detailDescriptionText = (TextView) view.findViewById(R.id.detailForecastDescription);
            detailPOPText = (TextView) view.findViewById(R.id.detailPop);
        }
    }
}