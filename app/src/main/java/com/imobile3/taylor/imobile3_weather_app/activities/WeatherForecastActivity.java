package com.imobile3.taylor.imobile3_weather_app.activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.TextView;

import com.google.gson.Gson;
import com.imobile3.taylor.imobile3_weather_app.R;
import com.imobile3.taylor.imobile3_weather_app.adapters.ViewPagerAdapter;
import com.imobile3.taylor.imobile3_weather_app.fragments.ForecastRecyclerFragment;
import com.imobile3.taylor.imobile3_weather_app.interfaces.WeatherDataTaskListener;
import com.imobile3.taylor.imobile3_weather_app.models.Location;
import com.imobile3.taylor.imobile3_weather_app.tasks.WeatherDataLookup;
import com.imobile3.taylor.imobile3_weather_app.utilities.Utils;

import java.util.Calendar;

/**
 * WeatherForecastActivity is responsible for loading its corresponding fragment
 * as well as initializes the toolbar and its menus.
 *
 * @author Taylor Parrish
 * @since 8/23/2016
 */
public class WeatherForecastActivity extends AppCompatActivity implements WeatherDataTaskListener {
    private static final String CLASS_TAG = WeatherForecastActivity.class.getSimpleName();
    private static final boolean DEBUG = true;

    public final static String TAG_LOCATION_BUNDLE = "locationBundle";
    private final static long TIME_TO_UPDATE = 3600000;

    private SharedPreferences mSharedPreferences;
    private ProgressDialog mProgressDialog;

    private Location mLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (DEBUG) Log.i(CLASS_TAG, "onCreate(Bundle)");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_forecast);

        mLocation =
                getIntent().getParcelableExtra(TAG_LOCATION_BUNDLE);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.forecast_toolbar);
        if (toolbar != null)
        {
            this.setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(mLocation.getCityState());
        }
        setupForecastPager();
        setupCurrentForecastBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isTimeToUpdate(TIME_TO_UPDATE);
    }

    //@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_forecast, menu);
        return true;
    }

    //@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                //Do something
                return true;
            case R.id.action_refresh:
                new WeatherDataLookup(this, this).execute(mLocation);
                return true;
            case R.id.action_location:
                //Do something
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onWeatherDataTaskStarted() {
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
        refreshUI(mLocation);

        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    private void refreshUI(Location location) {
        mLocation = location;
        mLocation.updateTimeStamp();
        location.saveLocationData(mSharedPreferences);
        setupForecastPager();
        setupCurrentForecastBar();
    }

    private void setupForecastPager() {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        ViewPager pager = (ViewPager) findViewById(R.id.viewPager);

        ViewPagerAdapter pagerAdapter =
                new ViewPagerAdapter(getSupportFragmentManager());
        String locationJSON = new Gson().toJson(mLocation);
        String[] weatherTypes = {"today", "tomorrow", "later"};

        for(String type : weatherTypes){
            addFragment(pagerAdapter, locationJSON, type);
        }
        pager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(pager);
    }

    private void addFragment(ViewPagerAdapter pagerAdapter, String locationJSON, String weatherType) {
        Bundle dataBundle = new Bundle();
        dataBundle.putString("type", weatherType);
        dataBundle.putString("locationData", locationJSON);
        ForecastRecyclerFragment recyclerFragment = new ForecastRecyclerFragment();
        recyclerFragment.setArguments(dataBundle);
        pagerAdapter.addFragment(recyclerFragment, weatherType);
    }

    private void setupCurrentForecastBar() {
        TextView lastUpdateText = (TextView) findViewById(R.id.lastUpdate);
        TextView currentWeatherIconText = (TextView) findViewById(R.id.currentWeatherIcon);
        TextView currentTempText = (TextView) findViewById(R.id.currentTemperature);
        TextView currentWeatherDescrText = (TextView) findViewById(R.id.currentWeatherDescription);
        TextView currentPressureText = (TextView) findViewById(R.id.currentPressure);
        TextView currentHumidityText = (TextView) findViewById(R.id.currentHumidity);
        TextView currentWindSpeedText = (TextView) findViewById(R.id.currentWind);
        TextView sunriseText = (TextView) findViewById(R.id.todaySunrise);
        TextView sunsetText = (TextView) findViewById(R.id.todaySunset);

        String lastUpdate = "Last Update: " + "\n" + mLocation.getUpdateTime();
        String currentWeatherIcon = mLocation.getCurrentWeatherForecast().getWeatherIcon();
        String currentTemp = mLocation.getCurrentWeatherForecast().getTemp_F() + " °F";
        String currentWeatherDescr = mLocation.getCurrentWeatherForecast().getWeather_description();
        String currentPressure = "Pressure: " + mLocation.getCurrentWeatherForecast().getPressure_mb() + " hPa";
        String currentHumidity = "Humidity: " +  mLocation.getCurrentWeatherForecast().getHumidity();
        String sunriseTime = "Sunrise: " + mLocation.getCurrentWeatherForecast().getSunriseTime();
        String sunsetTime = "Sunset: " + mLocation.getCurrentWeatherForecast().getSunsetTime();
        String currentWindSpeed = "Wind: " + mLocation.getCurrentWeatherForecast().getWind_mph() + " Mph";

        Typeface weatherFont = Typeface.createFromAsset(getAssets(), "font/weathericons.ttf");
        currentWeatherIconText.setTypeface(weatherFont);
        currentWeatherIconText.setText(currentWeatherIcon);
        lastUpdateText.setText(lastUpdate);
        currentTempText.setText(currentTemp);
        currentWeatherDescrText.setText(currentWeatherDescr);
        currentWindSpeedText.setText(currentWindSpeed);
        currentPressureText.setText(currentPressure);
        currentHumidityText.setText(currentHumidity);
        sunriseText.setText(sunriseTime);
        sunsetText.setText(sunsetTime);
    }

    private void isTimeToUpdate(long elapsedTime){
        long timeElapsed =
                (Calendar.getInstance().getTimeInMillis() - mLocation.getTimeStamp().getTimeInMillis());
        if(timeElapsed >= elapsedTime){
            new WeatherDataLookup(this, this).execute(mLocation);
        }
    }
}