package com.imobile3.taylor.imobile3_weather_app.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.google.gson.Gson;
import com.imobile3.taylor.imobile3_weather_app.GPS;
import com.imobile3.taylor.imobile3_weather_app.R;
import com.imobile3.taylor.imobile3_weather_app.fragments.LocationDialogFragment;
import com.imobile3.taylor.imobile3_weather_app.fragments.MainFragment;
import com.imobile3.taylor.imobile3_weather_app.interfaces.LocationDataTaskListener;
import com.imobile3.taylor.imobile3_weather_app.interfaces.WeatherDataTaskListener;
import com.imobile3.taylor.imobile3_weather_app.models.Location;
import com.imobile3.taylor.imobile3_weather_app.tasks.LocationDataLookup;
import com.imobile3.taylor.imobile3_weather_app.tasks.WeatherDataLookup;
import com.imobile3.taylor.imobile3_weather_app.utilities.Utils;

/**
 * MainActvity is responsible for loading its corresponding fragment
 * as well as initializes the toolbar and its menus.
 *
 * @author Taylor Parrish
 * @since 8/23/2016
 */
public class MainActivity extends AppCompatActivity implements LocationDialogFragment.SubmitCancelListener,
        WeatherDataTaskListener, LocationDataTaskListener, GPS.LocationPermissionResponseListener {
    private static final String CLASS_TAG = MainActivity.class.getSimpleName();
    private static final boolean DEBUG = true;

    private GPS gpsProvider;

    private static final String TAG_MAIN_FRAGMENT = "main_fragment";
    private MainFragment mMainFragment;

    private boolean mIsTaskRunning = false;
    private ProgressDialog mProgressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (DEBUG) Log.d(CLASS_TAG, "onCreate(Bundle)");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gpsProvider  = GPS.sharedInstance(this);
        gpsProvider.requestLocationPermission(this);

        if (savedInstanceState == null) {
            mMainFragment = new MainFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.pastLocationContent, mMainFragment, TAG_MAIN_FRAGMENT)
                    .commit();
        } else {
            mMainFragment = (MainFragment) getSupportFragmentManager().findFragmentByTag(TAG_MAIN_FRAGMENT);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        if (toolbar != null)
        {
            this.setSupportActionBar(toolbar);
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Location Dialog
                searchCitiesDialog();
            }
        });

        if (mIsTaskRunning) {
            mProgressDialog = ProgressDialog.show(this, "Downloading data", "Please wait...");
        }

    }

    @Override
    public void onLocationDataTaskStarted() {
        if (DEBUG) Log.d(CLASS_TAG, "onLocationDataTaskStarted()");
        mIsTaskRunning = true;
        mProgressDialog = ProgressDialog.show(this, "Downloading data", "Please wait...");
    }

    @Override
    public void onLocationDataTaskFailed(String failureType) {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        mIsTaskRunning = false;

        if(failureType.equals("IOException")) {
            Utils.showToast(this, "Failed to connect to host. Please check that you have an internet connection");
        }
        else {
            Utils.showToast(this, "The location you entered could not be found");
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
        mProgressDialog = ProgressDialog.show(this, "Downloading data", "Please wait...");
    }

    @Override
    public void onWeatherDataTaskFailed(String failureType) {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }

        if(failureType.equals("IOException")) {
            Utils.showToast(this, "Failed to connect to host. Please check that you have an internet connection");
        }
        else {
            Utils.showToast(this, "The weather data could not be found. Please try again later");
        }
    }

    @Override
    public void onWeatherDataTaskFinished(Location location) {
        if (DEBUG) Log.d(CLASS_TAG, "onLocationDataTaskFinished()");

        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        mIsTaskRunning = false;

        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        location.saveLocationData(sharedPreferences);
        mMainFragment.refreshPastLocations();
    }

    @Override
    public void onLocationValidationDialogSubmit(Location location) {
        if (DEBUG) Log.d(CLASS_TAG, "onLocationValidationDialogSubmit()");
        executeWeatherDataLookup(location);
    }

    @Override
    public void onLocationValidationDialogCancel() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_location:
                 if(checkLocationProviderEnabled()) {
                     addLocationByGPS();
                 }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void searchCitiesDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setMaxLines(1);
        input.setHint("City, State or Zipcode");
        input.setSingleLine();
        FrameLayout container = new FrameLayout(this);
        FrameLayout.LayoutParams params =
                new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        input.setLayoutParams(params);
        container.addView(input);
        alertDialog.setTitle("Search for a location");
        alertDialog.setView(container);
        alertDialog.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String result = input.getText().toString();
                if (!result.isEmpty()) {
                    lookupLocation(result);
                }
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Cancelled
            }
        });
        AlertDialog dialog = alertDialog.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();
    }

    public void executeWeatherDataLookup(Location location){
        new WeatherDataLookup(this, this).execute(location);
    }

    public void lookupLocation(String location){
        new LocationDataLookup(this).execute(location);
    }

    public void addLocationByGPS(){
        android.location.Location loc = gpsProvider.getLastKnownLocation();

        if(loc != null) {
            String location = loc.getLatitude() + "," + loc.getLongitude();
            new LocationDataLookup(this).execute(location);
        }
        else if(!gpsProvider.isPermissionEnabled()){
            gpsProvider.requestLocationPermission(this);
        }
        else{
            Utils.showToast(this, "Failed to find Location.");
        }
    }

    private void setupLocationValidationDialog(final Location location) {
        Bundle locationAddressBundle = new Bundle();
        String locationJSON =  new Gson().toJson(location);

        locationAddressBundle.putString("locationJSON", locationJSON);

        LocationDialogFragment locationDialogFragment = new LocationDialogFragment();
        locationDialogFragment.setArguments(locationAddressBundle);
        locationDialogFragment.show(getSupportFragmentManager(), "tag");
    }

    public boolean checkLocationProviderEnabled() {
        if (!gpsProvider.canGetLocation()){
            gpsProvider.showSettingsAlert();
            return false;
        }
        return true;
    }

    @Override
    public void onResponse(Boolean permissionGranted) {
    }

}