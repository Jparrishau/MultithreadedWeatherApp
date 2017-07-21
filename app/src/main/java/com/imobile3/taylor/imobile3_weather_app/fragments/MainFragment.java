package com.imobile3.taylor.imobile3_weather_app.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;

import com.imobile3.taylor.imobile3_weather_app.GPS;
import com.imobile3.taylor.imobile3_weather_app.R;
import com.imobile3.taylor.imobile3_weather_app.activities.WeatherForecastActivity;
import com.imobile3.taylor.imobile3_weather_app.adapters.PastLocationsAdapter;
import com.imobile3.taylor.imobile3_weather_app.interfaces.LocationDataTaskListener;
import com.imobile3.taylor.imobile3_weather_app.interfaces.WeatherDataTaskListener;
import com.imobile3.taylor.imobile3_weather_app.models.Location;
import com.imobile3.taylor.imobile3_weather_app.tasks.WeatherDataLookup;
import com.imobile3.taylor.imobile3_weather_app.tasks.LocationDataLookup;
import com.imobile3.taylor.imobile3_weather_app.utilities.Utils;

import java.util.ArrayList;
import java.util.Map;

/**
 * MainFragment is responsible for creating the location search page
 * it runs a background task to find location details and passes them to the
 * Simpleforecast activity/fragment.
 *
 * Should probably move some code to the activity level, I originally used
 * fragment to learn about them, but I dont think one should be used here,
 * except for if I convert the list to a recyclerview and recyclerfragment.
 *
 * @author Taylor Parrish
 * @since 8/29/2016
 */
public class MainFragment extends Fragment implements LocationDataTaskListener, WeatherDataTaskListener {
    private static final String CLASS_TAG = MainFragment.class.getSimpleName();
    private static final boolean DEBUG = true;

    private GPS gpsProvider;

    public final static String TAG_LOCATION_BUNDLE = "locationBundle";

    private boolean mIsTaskRunning = false;
    private ProgressDialog mProgressDialog;

    private SharedPreferences mSharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (DEBUG) Log.d(CLASS_TAG, "onCreate(Bundle)");
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (DEBUG) Log.d(CLASS_TAG, "onCreateView(LayoutInflater, ViewGroup, Bundle)");

        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (DEBUG) Log.d(CLASS_TAG, "onActivityCreated(Bundle)");
        super.onActivityCreated(savedInstanceState);

        gpsProvider  = GPS.sharedInstance(getActivity());

        if (mIsTaskRunning) {
            mProgressDialog = ProgressDialog.show(getActivity(), "Downloading data", "Please wait...");
        }
    }

    @Override
    public void onStart() {
        if (DEBUG) Log.d(CLASS_TAG, "onStart()");
        super.onStart();
    }

    @Override
    public void onResume() {
        if (DEBUG) Log.d(CLASS_TAG, "onResume()");
        super.onResume();
        refreshPastLocations();
    }

    @Override
    public void onDetach() {
        if (DEBUG) Log.d(CLASS_TAG, "onDetach()");
        super.onDetach();

        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onLocationDataTaskStarted() {
        if (DEBUG) Log.d(CLASS_TAG, "onLocationDataTaskStarted()");
        mIsTaskRunning = true;
        mProgressDialog = ProgressDialog.show(getActivity(), "Downloading data", "Please wait...");
    }

    @Override
    public void onLocationDataTaskFailed(String failureType) {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        mIsTaskRunning = false;

        if(failureType.equals("IOException")) {
            Utils.showToast(getActivity(), "Failed to connect to host. Please check that you have an internet connection");
        }
        else {
            Utils.showToast(getActivity(), "The location you entered could not be found");
        }
    }

    @Override
    public void onLocationDataTaskFinished(Location location) {
        if (DEBUG) Log.d(CLASS_TAG, "onLocationDataTaskFinished()");

        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        mIsTaskRunning = false;
        setupLocationValidationDialog(location);
    }

    @Override
    public void onWeatherDataTaskStarted() {
        if (DEBUG) Log.d(CLASS_TAG, "onLocationDataTaskStarted()");
        mIsTaskRunning = true;
        mProgressDialog = ProgressDialog.show(getActivity(), "Downloading data", "Please wait...");
    }

    @Override
    public void onWeatherDataTaskFailed(String failureType) {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }

        if(failureType.equals("IOException")) {
            Utils.showToast(getActivity(), "Failed to connect to host. Please check that you have an internet connection");
        }
        else {
            Utils.showToast(getActivity(), "The weather data could not be found. Please try again later");
        }
    }

    @Override
    public void onWeatherDataTaskFinished(Location location) {
        if (DEBUG) Log.d(CLASS_TAG, "onLocationDataTaskFinished()");

        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        mIsTaskRunning = false;

        Gson gson = new Gson();
        String locationJSON = gson.toJson(location);
        mSharedPreferences.edit().putString(location.getCoordinates(), locationJSON).apply();
        refreshPastLocations();
    }

    private void refreshPastLocations() {
        if(mSharedPreferences.getAll() != null) {
            ArrayList<Location> locations = new ArrayList<>();

            Map<String, ?> keys = mSharedPreferences.getAll();
            for (Map.Entry<String, ?> entry : keys.entrySet()) {
                Location tempLocation = new Gson().fromJson(entry.getValue().toString(), Location.class);
                locations.add(tempLocation);
            }
            setUpPastLocationListView(locations);
        }
    }

    private void setUpPastLocationListView(final ArrayList<Location> locations) {
        final ListView previousLocationsList = (ListView) getActivity().findViewById(R.id.pastLocationsList);

        previousLocationsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                startForecastActivity(locations.get(position));
            }
        });
        /* Delete on hold */
        previousLocationsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Location tempLocation = locations.get(i);
                final String locationText = tempLocation.getCity() + ", " + tempLocation.getState();

                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setCancelable(false);
                dialog.setTitle(R.string.deleteListItemWarning);
                dialog.setMessage(locationText);
                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        //The key is the coordinates now, not the city. This doesn't work.
                        mSharedPreferences.edit().remove(tempLocation.getCoordinates()).apply();
                        refreshPastLocations();
                    }
                });
                dialog.setNegativeButton(getActivity().getString(R.string.Cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        //User cancelled delete
                    }
                });
                dialog.show();
                return false;
            }
        });

        previousLocationsList.setAdapter(
                new PastLocationsAdapter(getActivity(), mSharedPreferences.getAll()));
    }

    private void startForecastActivity(Location locationToDisplay) {
        Intent mSimpleForecastIntent = new Intent(getActivity(), WeatherForecastActivity.class);
        Bundle locationBundle = new Bundle();

        locationBundle.putParcelable(TAG_LOCATION_BUNDLE, locationToDisplay);
        mSimpleForecastIntent.putExtras(locationBundle);
        startActivity(mSimpleForecastIntent);
    }

    private void setupLocationValidationDialog(final Location location) {
        Bundle locationAddressBundle = new Bundle();
        String locationJSON =  new Gson().toJson(location);

        locationAddressBundle.putString("locationJSON", locationJSON);

        LocationDialogFragment locationDialogFragment = new LocationDialogFragment();
        locationDialogFragment.setArguments(locationAddressBundle);
        locationDialogFragment.show(getActivity().getSupportFragmentManager(), "tag");
    }

    public void executeWeatherDataLookup(Location location){
        new WeatherDataLookup(MainFragment.this, getContext()).execute(location);
    }

    public boolean checkLocationProviderEnabled() {
        if (!gpsProvider.canGetLocation()){
            gpsProvider.showSettingsAlert();
            return false;
        }
        return true;
    }

    public void lookupLocation(String location){
        new LocationDataLookup(MainFragment.this).execute(location);
    }

    public void addLocationByGPS(){
        android.location.Location loc = gpsProvider.getLastKnownLocation();

        if(loc != null) {
            String location = loc.getLatitude() + "," + loc.getLongitude();
            new LocationDataLookup(MainFragment.this).execute(location);
        }
        else{
            //This can be more specific and let the user know if the app has location permissions
            Utils.showToast(getActivity(), "Failed to find GPS location");
        }
    }
}