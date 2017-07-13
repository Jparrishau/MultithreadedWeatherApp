package com.imobile3.taylor.imobile3_weather_app.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.TextView;

import com.google.gson.Gson;
import com.imobile3.taylor.imobile3_weather_app.R;
import com.imobile3.taylor.imobile3_weather_app.adapters.ViewPagerAdapter;
import com.imobile3.taylor.imobile3_weather_app.fragments.RecyclerViewFragment;
import com.imobile3.taylor.imobile3_weather_app.models.Location;

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
public class WeatherForecastActivity extends AppCompatActivity {
    private static final String CLASS_TAG = WeatherForecastActivity.class.getSimpleName();
    private static final boolean DEBUG = true;

    private static final String TAG_SIMPLE_FRAGMENT = "simple_fragment";
    public final static String TAG_LOCATION_BUNDLE = "locationBundle";
    public final static String TAG_EXTRA_DETAIL_ITEMS = "detailWeatherItems";

    private Location mLocation;
    private ViewPager mPager;
    private ViewPagerAdapter mPagerAdapter;

    TextView mCurrentWeatherIconText;
    TextView mCurrentTempText;
    TextView mCurrentWeatherDescrText;
    TextView mCurrentPressureText;
    TextView mCurrentHumidityText;
    TextView mCurrentWindSpeedText;
    TextView mSunriseText;
    TextView mSunsetText;

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



        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.viewPager);
        mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        RecyclerViewFragment recyclerViewFragment = new RecyclerViewFragment();
        Bundle bundleLocation = new Bundle();
        String locationJSON = new Gson().toJson(mLocation);
        bundleLocation.putString("location", locationJSON);
        recyclerViewFragment.setArguments(bundleLocation);

        RecyclerViewFragment recyclerViewFragment2 = new RecyclerViewFragment();
        Bundle bundleLocation2 = new Bundle();
        String locationJSON2 = new Gson().toJson(mLocation);
        bundleLocation2.putString("location", locationJSON2);
        recyclerViewFragment2.setArguments(bundleLocation2);

        mPagerAdapter.addFragment(recyclerViewFragment, "Today");
        mPagerAdapter.addFragment(recyclerViewFragment2, "Tomorrow");

        mPager.setAdapter(mPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mPager);

        mCurrentWeatherIconText = (TextView) findViewById(R.id.currentWeatherIcon);
        mCurrentTempText = (TextView) findViewById(R.id.currentTemperature);
        mCurrentWeatherDescrText = (TextView) findViewById(R.id.currentWeatherDescription);
        mCurrentPressureText = (TextView) findViewById(R.id.currentPressure);
        mCurrentHumidityText = (TextView) findViewById(R.id.currentHumidity);
        mCurrentWindSpeedText = (TextView) findViewById(R.id.currentWind);
        mSunriseText = (TextView) findViewById(R.id.todaySunrise);
        mSunsetText = (TextView) findViewById(R.id.todaySunset);

        String currentWeatherIcon = mLocation.getCurrentWeatherForecast().getWeatherIcon();
        String currentTemp = mLocation.getCurrentWeatherForecast().getTemp_F() + " °F";
        String currentWeatherDescr = mLocation.getCurrentWeatherForecast().getWeather_description();
        String currentPressure = "1016.0 hPa";
        String currentHumidity = mLocation.getCurrentWeatherForecast().getHumidity();
        Double currentWindSpeed = mLocation.getCurrentWeatherForecast().getWind_mph();

        Typeface weatherFont = Typeface.createFromAsset(getAssets(), "font/weathericons.ttf");
        mCurrentWeatherIconText.setTypeface(weatherFont);
        mCurrentWeatherIconText.setText(currentWeatherIcon);
        mCurrentTempText.setText(currentTemp);
        mCurrentWeatherDescrText.setText(currentWeatherDescr);
        mCurrentWindSpeedText.setText("Wind: " + currentWindSpeed + " Mph");
        mCurrentPressureText.setText("Pressure: " + currentPressure);
        mCurrentHumidityText.setText("Humidity: " + currentHumidity);
        mSunriseText.setText("Sunrise: " + "5:42am");
        mSunsetText.setText("Sunset: " + "8:00pm");
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

    /*private void setupWeatherItemListView(final Location location) {
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
    }*/
}



