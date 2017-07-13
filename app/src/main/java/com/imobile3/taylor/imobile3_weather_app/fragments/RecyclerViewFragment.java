package com.imobile3.taylor.imobile3_weather_app.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.imobile3.taylor.imobile3_weather_app.R;
import com.imobile3.taylor.imobile3_weather_app.adapters.ForecastRecyclerAdapter;
import com.imobile3.taylor.imobile3_weather_app.models.Location;

/**
 * Description.
 *
 * @author Taylor Parrish
 * @since 7/11/2017
 */
public class RecyclerViewFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        String locationJSON = bundle.getString("location");
        Location mLocation = new Gson().fromJson(locationJSON, Location.class);
        View view = inflater.inflate(R.layout.fragment_recycler, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        ForecastRecyclerAdapter mAdapter = new ForecastRecyclerAdapter(mLocation);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter);
        return view;
    }
}