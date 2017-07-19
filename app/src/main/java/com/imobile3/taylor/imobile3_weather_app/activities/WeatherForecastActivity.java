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
 * Issue 1: Toolbar is showing on all activities but menu options are not?
 * Possible Solutions: Unsure where problem is coming from. Further testing required.
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
            getSupportActionBar().setTitle(mLocation.getCity() + ", " + mLocation.getState());
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
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onWeatherDataTaskStarted() {
        mProgressDialog = ProgressDialog.show(this, "Downloading data", "Please wait...");
    }

    @Override
    public void onWeatherDataTaskFailed() {
        Utils.showToast(this, "Could not lookup data. Please check that you have an internet connection");

        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onWeatherDataTaskFinished(Location location) {
        location.updateTimeStamp();
        refreshUI(location);

        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    private void refreshUI(Location location) {
        mLocation = location;
        setupForecastPager();
        setupCurrentForecastBar();
        saveLocationData(location);
    }

    private void setupForecastPager() {
        // Instantiate a ViewPager and a PagerAdapter.
        ViewPager pager = (ViewPager) findViewById(R.id.viewPager);
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        String locationJSON = new Gson().toJson(mLocation);

        ForecastRecyclerFragment todayFragment = new ForecastRecyclerFragment();
        Bundle bundleLocation = new Bundle();
        bundleLocation.putString("type", "today");
        bundleLocation.putString("locationData", locationJSON);
        todayFragment.setArguments(bundleLocation);

        ForecastRecyclerFragment tomorrowFragment = new ForecastRecyclerFragment();
        Bundle bundleLocation2 = new Bundle();
        bundleLocation2.putString("type", "tomorrow");
        bundleLocation2.putString("locationData", locationJSON);
        tomorrowFragment.setArguments(bundleLocation2);

        ForecastRecyclerFragment laterFragment = new ForecastRecyclerFragment();
        Bundle bundleLocation3 = new Bundle();
        bundleLocation3.putString("type", "later");
        bundleLocation3.putString("locationData", locationJSON);
        laterFragment.setArguments(bundleLocation3);

        pagerAdapter.addFragment(todayFragment, "Today");
        pagerAdapter.addFragment(tomorrowFragment, "Tomorrow");
        pagerAdapter.addFragment(laterFragment, "Later");
        pager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);
    }

    private void setupCurrentForecastBar() {
        TextView lastUpdate1 = (TextView) findViewById(R.id.lastUpdate);
        TextView currentWeatherIconText = (TextView) findViewById(R.id.currentWeatherIcon);
        TextView currentTempText = (TextView) findViewById(R.id.currentTemperature);
        TextView currentWeatherDescrText = (TextView) findViewById(R.id.currentWeatherDescription);
        TextView currentPressureText = (TextView) findViewById(R.id.currentPressure);
        TextView currentHumidityText = (TextView) findViewById(R.id.currentHumidity);
        TextView currentWindSpeedText = (TextView) findViewById(R.id.currentWind);
        TextView sunriseText = (TextView) findViewById(R.id.todaySunrise);
        TextView sunsetText = (TextView) findViewById(R.id.todaySunset);

        String lastUpdate = Utils.getStandardTime(mLocation.getTimeStamp());
        String currentWeatherIcon = mLocation.getCurrentWeatherForecast().getWeatherIcon();
        String currentTemp = mLocation.getCurrentWeatherForecast().getTemp_F() + " °F";
        String currentWeatherDescr = mLocation.getCurrentWeatherForecast().getWeather_description();
        String currentPressure = mLocation.getCurrentWeatherForecast().getPressure_mb() + " hPa";
        String currentHumidity = mLocation.getCurrentWeatherForecast().getHumidity();
        String sunriseTime = mLocation.getCurrentWeatherForecast().getSunriseTime();
        String sunsetTime = mLocation.getCurrentWeatherForecast().getSunsetTime();
        String currentWindSpeed = mLocation.getCurrentWeatherForecast().getWind_mph() + " Mph";

        Typeface weatherFont = Typeface.createFromAsset(getAssets(), "font/weathericons.ttf");
        currentWeatherIconText.setTypeface(weatherFont);
        currentWeatherIconText.setText(currentWeatherIcon);
        lastUpdate1.setText("Last Update: \n" + lastUpdate);
        currentTempText.setText(currentTemp);
        currentWeatherDescrText.setText(currentWeatherDescr);
        currentWindSpeedText.setText("Wind: " + currentWindSpeed);
        currentPressureText.setText("Pressure: " + currentPressure);
        currentHumidityText.setText("Humidity: " + currentHumidity);
        sunriseText.setText("Sunrise: " + sunriseTime);
        sunsetText.setText("Sunset: " + sunsetTime);
    }

    private void saveLocationData(Location location) {
        Gson gson = new Gson();
        String locationJSON = gson.toJson(location);
        mSharedPreferences.edit().putString(location.getCoordinates(), locationJSON).apply();
    }

    private void isTimeToUpdate(long elapsedTime){
        long timeElapsed =
                (Calendar.getInstance().getTimeInMillis() - mLocation.getTimeStamp().getTimeInMillis());
        if(timeElapsed >= elapsedTime){
            new WeatherDataLookup(this, this).execute(mLocation);
        }
    }
}



