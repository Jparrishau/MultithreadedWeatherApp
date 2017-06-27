package com.imobile3.taylor.imobile3_weather_app.fragments;

import android.Manifest;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
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
import android.widget.TextView;

import com.imobile3.taylor.imobile3_weather_app.LocationLookup;
import com.imobile3.taylor.imobile3_weather_app.ForecastLocationListener;
import com.imobile3.taylor.imobile3_weather_app.R;
import com.imobile3.taylor.imobile3_weather_app.activities.SimpleForecastActivity;
import com.imobile3.taylor.imobile3_weather_app.adapters.PastLocationsAdapter;
import com.imobile3.taylor.imobile3_weather_app.interfaces.LocationDataTaskListener;
import com.imobile3.taylor.imobile3_weather_app.models.LocationData;

import java.util.HashSet;

/**
 * Created by Taylor Parrish on 8/29/2016.
 *
 * MainFragment is responsible for creating the location search page
 * it runs a background task to find location details and passes them to the
 * Simpleforecast activity/fragment.
 *
 * Issue 1: Need to get sharedpreference on change listener updating pastLocationsListView without refresh.
 * Possible Solutions: Still researching. Will add current temperature as well in future.
 * Issue 2: If Fragment is recreated after failed location lookup sharedpreferences is erased.
 * Possible Solutions: Need to handle exception better. RevertLocation method probably the issue.
 */
public class MainFragment extends Fragment implements LocationDataTaskListener {
    private static final String CLASS_TAG = MainFragment.class.getSimpleName();
    private static final boolean DEBUG = true;

    public final static String TAG_EXTRA_LOCATION = "location";

    private boolean mIsTaskRunning = false;
    private LocationManager mLocationManager;
    private ProgressDialog mProgressDialog;
    private SharedPreferences mSharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (DEBUG) Log.d(CLASS_TAG, "onCreate(Bundle)");
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        mLocationManager = (LocationManager)
                getActivity().getSystemService(Context.LOCATION_SERVICE);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (DEBUG) Log.d(CLASS_TAG, "onCreateView(LayoutInflater, ViewGroup, Bundle)");

        View root = inflater.inflate(R.layout.main_menu, container, false);

        Button fetchDataButton = (Button) root.findViewById(R.id.fetchDataButton);
        fetchDataButton.setOnClickListener(fetchDataButtonListener);
        fetchDataButton.setTransformationMethod(null);

        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (DEBUG) Log.d(CLASS_TAG, "onActivityCreated(Bundle)");
        super.onActivityCreated(savedInstanceState);

        if (mIsTaskRunning) {
            mProgressDialog = ProgressDialog.show(getActivity(), "Fetching data", "Please wait...");
        }
    }

    @Override
    public void onStart() {
        if (DEBUG) Log.d(CLASS_TAG, "onStart()");
        super.onStart();
        checkLocationProvidersEnabled();
    }

    @Override
    public void onResume() {
        if (DEBUG) Log.d(CLASS_TAG, "onResume()");
        super.onResume();
        setUpPastLocationListView(getView());
    }

    @Override
    public void onDetach() {
        if (DEBUG) Log.d(CLASS_TAG, "onDetach()");

        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        super.onDetach();
    }

    @Override
    public void onTaskStarted() {
        if (DEBUG) Log.d(CLASS_TAG, "onTaskStarted()");
        mIsTaskRunning = true;
        mProgressDialog = ProgressDialog.show(getActivity(), "Fetching data", "Please wait...");
    }

    @Override
    public void onTaskFinished(LocationData locationData) {
        if (DEBUG) Log.d(CLASS_TAG, "onTaskFinished()");

        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        mIsTaskRunning = false;
        setupLocationValidationDialog(locationData);
    }

    private void setupLocationValidationDialog(LocationData locationData) {
        final String latitude = locationData.getLatitude();
        final String longitude = locationData.getLongitude();
        final String formatted_address = locationData.getFormattedAddress();
        final String city = locationData.getCity();
        final String location = latitude + "," + longitude;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent mSimpleForecastIntent = new Intent(getActivity(), SimpleForecastActivity.class);
                mSimpleForecastIntent.putExtra(TAG_EXTRA_LOCATION, location);
                mSharedPreferences.edit().putString(city, location).apply();
                startActivity(mSimpleForecastIntent);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog. Exit dialog.
            }
        });

        AlertDialog locationConfirmationDialog = builder.create();
        locationConfirmationDialog.setTitle("Is the information below correct?");
        locationConfirmationDialog.setMessage(formatted_address);
        locationConfirmationDialog.show();
    }

    private void setUpPastLocationListView(View root) {
        final ListView previousLocationsList = (ListView) root.findViewById(R.id.pastLocationsList);
        setupForecastOnItemClickListener(previousLocationsList);
        setupDeleteItemOnLongClickListener(previousLocationsList);

        previousLocationsList.setAdapter(
                new PastLocationsAdapter(getActivity(), mSharedPreferences.getAll()));
    }

    private void setupForecastOnItemClickListener(ListView previousLocationsList) {
        previousLocationsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                TextView textView = (TextView) view.findViewById(R.id.cityText);
                String locationText = textView.getText().toString();

                Intent mSimpleForecastIntent = new Intent(getActivity(), SimpleForecastActivity.class);
                mSimpleForecastIntent.putExtra(TAG_EXTRA_LOCATION,
                        mSharedPreferences.getString(locationText, null));
                startActivity(mSimpleForecastIntent);
            }
        });
    }

    private void setupDeleteItemOnLongClickListener(ListView previousLocationsList) {
        previousLocationsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView = (TextView) view.findViewById(R.id.cityText);
                final String locationText = textView.getText().toString();

                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setCancelable(false);
                dialog.setTitle(R.string.deletListItemWarning);
                dialog.setMessage(locationText);
                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        mSharedPreferences.edit().remove(locationText).apply();
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
    }

    private void checkLocationProvidersEnabled() {
        boolean gps_enabled =
                mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!gps_enabled) {
            enableGPSDialog();
        }
    }

    private void enableGPSDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setCancelable(false);
        dialog.setTitle(getActivity().getResources().getString(R.string.gps_warning_title));
        dialog.setMessage(getActivity().getResources().getString(R.string.gps_not_enabled));
        dialog.setPositiveButton(getActivity().getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                getActivity().startActivity(myIntent);
            }
        });
        dialog.setNegativeButton(getActivity().getString(R.string.Cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                getActivity().finish();
                System.exit(0);
            }
        });
        dialog.show();
    }

    View.OnClickListener fetchDataButtonListener = new View.OnClickListener() {
        public void onClick(View view) {
            EditText locationText = (EditText) getActivity().findViewById(R.id.locationField);
            String location = locationText.getText().toString();

            //If locationField is empty, use GPS location instead
            if (location.matches("")) {
                LocationListener locationListener = new ForecastLocationListener();

                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mLocationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener, null);
                Location loc = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                location = loc.getLatitude() + "," + loc.getLongitude();
            }
            new LocationResponse(MainFragment.this).execute(location);
        }
    };

    // Using the user input location string to verify and respond with more detailed location data
    private class LocationResponse extends AsyncTask<String, String, LocationData> {
        private final LocationDataTaskListener listener;

        public LocationResponse(LocationDataTaskListener mListener) {
            this.listener = mListener;
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
            listener.onTaskStarted();
        }

        @Override
        public LocationData doInBackground(String... args) {
            //Use user set location string and return geocode location data object model
            String locationString = args[0];
            return new LocationLookup(locationString).invoke();
        }

        @Override
        public void onPostExecute(LocationData locationData) {
            listener.onTaskFinished(locationData);
        }
    }
}