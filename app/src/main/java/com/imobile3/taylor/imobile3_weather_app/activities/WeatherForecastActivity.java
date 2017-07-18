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
import com.imobile3.taylor.imobile3_weather_app.fragments.RecyclerViewFragment;
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

    private SharedPreferences mSharedPreferences;
    private boolean mIsTaskRunning = false;
    private ProgressDialog mProgressDialog;

    private Location mLocation;
    private ViewPager mPager;
    private ViewPagerAdapter mPagerAdapter;

    private TextView mLastUpdate;
    private TextView mCurrentWeatherIconText;
    private TextView mCurrentTempText;
    private TextView mCurrentWeatherDescrText;
    private TextView mCurrentPressureText;
    private TextView mCurrentHumidityText;
    private TextView mCurrentWindSpeedText;
    private TextView mSunriseText;
    TextView mSunsetText;

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
        isTimeToUpdate();
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
            case R.id.action_search:
                //Do something
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
    public void onTaskStarted() {
        mIsTaskRunning = true;
        mProgressDialog = ProgressDialog.show(this, "Fetching data", "Please wait...");
    }

    @Override
    public void onWeatherDataTaskFinished(Location location) {
        location.updateTimeStamp();
        refreshUI(location);

        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        mIsTaskRunning = false;
    }

    private void refreshUI(Location location) {
        mLocation = location;
        setupForecastPager();
        setupCurrentForecastBar();
        saveLocationData(location);
    }

    private void setupForecastPager() {
        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.viewPager);
        mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        String locationJSON = new Gson().toJson(mLocation);

        RecyclerViewFragment todayFragment = new RecyclerViewFragment();
        Bundle bundleLocation = new Bundle();
        bundleLocation.putString("type", "today");
        bundleLocation.putString("locationData", locationJSON);
        todayFragment.setArguments(bundleLocation);

        RecyclerViewFragment tomorrowFragment = new RecyclerViewFragment();
        Bundle bundleLocation2 = new Bundle();
        bundleLocation2.putString("type", "tomorrow");
        bundleLocation2.putString("locationData", locationJSON);
        tomorrowFragment.setArguments(bundleLocation2);

        RecyclerViewFragment laterFragment = new RecyclerViewFragment();
        Bundle bundleLocation3 = new Bundle();
        bundleLocation3.putString("type", "later");
        bundleLocation3.putString("locationData", locationJSON);
        laterFragment.setArguments(bundleLocation3);

        mPagerAdapter.addFragment(todayFragment, "Today");
        mPagerAdapter.addFragment(tomorrowFragment, "Tomorrow");
        mPagerAdapter.addFragment(laterFragment, "Later");
        mPager.setAdapter(mPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mPager);
    }

    private void setupCurrentForecastBar() {
        mLastUpdate = (TextView) findViewById(R.id.lastUpdate);
        mCurrentWeatherIconText = (TextView) findViewById(R.id.currentWeatherIcon);
        mCurrentTempText = (TextView) findViewById(R.id.currentTemperature);
        mCurrentWeatherDescrText = (TextView) findViewById(R.id.currentWeatherDescription);
        mCurrentPressureText = (TextView) findViewById(R.id.currentPressure);
        mCurrentHumidityText = (TextView) findViewById(R.id.currentHumidity);
        mCurrentWindSpeedText = (TextView) findViewById(R.id.currentWind);
        mSunriseText = (TextView) findViewById(R.id.todaySunrise);
        mSunsetText = (TextView) findViewById(R.id.todaySunset);

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
        mCurrentWeatherIconText.setTypeface(weatherFont);
        mCurrentWeatherIconText.setText(currentWeatherIcon);
        mLastUpdate.setText("Last Update: \n" + lastUpdate);
        mCurrentTempText.setText(currentTemp);
        mCurrentWeatherDescrText.setText(currentWeatherDescr);
        mCurrentWindSpeedText.setText("Wind: " + currentWindSpeed);
        mCurrentPressureText.setText("Pressure: " + currentPressure);
        mCurrentHumidityText.setText("Humidity: " + currentHumidity);
        mSunriseText.setText("Sunrise: " + sunriseTime);
        mSunsetText.setText("Sunset: " + sunsetTime);
    }

    private void saveLocationData(Location location) {
        Gson gson = new Gson();
        String locationJSON = gson.toJson(location);
        mSharedPreferences.edit().putString(location.getCoordinates(), locationJSON).apply();
    }

    private void isTimeToUpdate(){
        long timeElapsed =
                (Calendar.getInstance().getTimeInMillis() - mLocation.getTimeStamp().getTimeInMillis());
        if(timeElapsed >= 3600000){
            new WeatherDataLookup(this, this).execute(mLocation);
        }
    }
}



