package com.imobile3.taylor.imobile3_weather_app.activities;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.imobile3.taylor.imobile3_weather_app.R;
import com.imobile3.taylor.imobile3_weather_app.adapters.SimpleForecastAdapter;
import com.imobile3.taylor.imobile3_weather_app.adapters.ViewPagerAdapter;
import com.imobile3.taylor.imobile3_weather_app.fragments.WeatherForecastFragment;
import com.imobile3.taylor.imobile3_weather_app.models.DailyDetailedWeatherItem;
import com.imobile3.taylor.imobile3_weather_app.models.Location;

import java.util.ArrayList;

/**
 * Created by Taylor Parrish on 8/23/2016.
 *
 * WeatherForecastActivity is responsible for loading its corresponding fragment
 * as well as initializes the toolbar and its menus.
 *
 * Issue 1: Toolbar is showing on all activities but menu options are not?
 * Possible Solutions: Unsure where problem is coming from. Further testing required.
 */
public class WeatherForecastActivity extends AppCompatActivity {
    private static final String CLASS_TAG = WeatherForecastActivity.class.getSimpleName();
    private static final boolean DEBUG = true;

    private static final String TAG_SIMPLE_FRAGMENT = "simple_fragment";
    public final static String TAG_LOCATION_BUNDLE = "locationBundle";
    public final static String TAG_EXTRA_DETAIL_ITEMS = "detailWeatherItems";

    private WeatherForecastFragment mWeatherForecastFragment;

    private Location mLocation;

    private ViewPager mPager;
    private ViewPagerAdapter mPagerAdapter;

    TextView currentTempText;
    TextView currentWeatherDescrText;
    TextView currentPressureText;
    TextView currentHumidityText;
    TextView currentWindSpeedText;
    TextView sunriseText;
    TextView sunsetText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (DEBUG) Log.i(CLASS_TAG, "onCreate(Bundle)");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_forecast);

        mLocation =
                getIntent().getParcelableExtra(TAG_LOCATION_BUNDLE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.forecast_toolbar);
        if (toolbar != null)
        {
            this.setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(mLocation.getCity() + ", " + mLocation.getState());
        }

        currentTempText = (TextView) findViewById(R.id.currentTemperature);
        currentWeatherDescrText = (TextView) findViewById(R.id.currentWeatherDescription);
        currentPressureText = (TextView) findViewById(R.id.currentPressure);
        currentHumidityText = (TextView) findViewById(R.id.currentHumidity);
        currentWindSpeedText = (TextView) findViewById(R.id.currentWind);
        sunriseText = (TextView) findViewById(R.id.todaySunrise);
        sunsetText = (TextView) findViewById(R.id.todaySunset);

        String currentTemp = mLocation.getCurrentWeatherForecast().getTemp_F() + " °F";
        String currentWeatherDescr = mLocation.getCurrentWeatherForecast().getWeather_description();
        String currentPressure = "1016.0 hPa";
        String currentHumidity = mLocation.getCurrentWeatherForecast().getHumidity();
        Double currentWindSpeed = mLocation.getCurrentWeatherForecast().getWind_mph();

        currentTempText.setText(currentTemp);
        currentWeatherDescrText.setText(currentWeatherDescr);
        currentWindSpeedText.setText("Wind: " + currentWindSpeed + " Mph");
        currentPressureText.setText("Pressure: " + currentPressure);
        currentHumidityText.setText("Humidity: " + currentHumidity);
        sunriseText.setText("Sunrise: " + "5:42am");
        sunsetText.setText("Sunset: " + "8:00pm");
        setupWeatherItemListView(mLocation);
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
                //Do something
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

    private void setupWeatherItemListView(final Location location) {
        ListView simpleForecastListview = (ListView) findViewById(R.id.forecastListView);
        simpleForecastListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                Intent detailedforecastIntent = new Intent(view.getContext(), DetailedWeatherForecastActivity.class);
                ArrayList<DailyDetailedWeatherItem> detailItems = location.getDay(position).getWeatherForecast().getDetailWeatherItems();

                detailedforecastIntent.putParcelableArrayListExtra(TAG_EXTRA_DETAIL_ITEMS, detailItems);
                startActivity(detailedforecastIntent);
            }
        });
        SimpleForecastAdapter adapter = new SimpleForecastAdapter(this, location);
        simpleForecastListview.setAdapter(adapter);
    }
}



