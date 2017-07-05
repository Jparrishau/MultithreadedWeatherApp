package com.imobile3.taylor.imobile3_weather_app.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.imobile3.taylor.imobile3_weather_app.R;
import com.imobile3.taylor.imobile3_weather_app.fragments.CurrentForecastFragment;
import com.imobile3.taylor.imobile3_weather_app.fragments.WeatherForecastFragment;

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
    private CurrentForecastFragment mCurrentForecastFragment;
    private WeatherForecastFragment mWeatherForecastFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (DEBUG) Log.i(CLASS_TAG, "onCreate(Bundle)");
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            mCurrentForecastFragment = new CurrentForecastFragment();
            mWeatherForecastFragment = new WeatherForecastFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(android.R.id.content, mCurrentForecastFragment)
                    .commit();
        } else {
            mCurrentForecastFragment = (CurrentForecastFragment) getSupportFragmentManager().findFragmentByTag(TAG_SIMPLE_FRAGMENT);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.forecast_toolbar);
        if (toolbar != null)
        {
            this.setSupportActionBar(toolbar);//To display toolbar
            getSupportActionBar().setElevation(2); // or other...
        }
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
}



