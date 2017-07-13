package com.imobile3.taylor.imobile3_weather_app.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.imobile3.taylor.imobile3_weather_app.R;
import com.imobile3.taylor.imobile3_weather_app.fragments.DetailedForecastFragment;

/**
 * DetailedWeatherForecastActivity is responsible for loading the corresponding fragment
 * as well as initializing the toolbar and its menus.
 *
 * Issue 1: Toolbar is showing on all activities but menu options are not?
 * Possible Solutions: Unsure where problem is coming from. Further testing required.
 *
 * @author Taylor Parrish
 * @since 8/23/2016
 */
public class DetailedWeatherForecastActivity extends AppCompatActivity {
    private static final String CLASS_TAG = DetailedWeatherForecastActivity.class.getSimpleName();

    private static final boolean DEBUG = true;
    private static final boolean DEBUG1 = true;

    private static final String TAG_DETAIL_FRAGMENT = "detail_fragment";
    private DetailedForecastFragment mDetailedForecastFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (DEBUG) Log.d(CLASS_TAG, "onCreate(Bundle)");
        super.onCreate(savedInstanceState);

        mDetailedForecastFragment = new DetailedForecastFragment();

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.content, mDetailedForecastFragment, TAG_DETAIL_FRAGMENT)
                    .commit();
        }else{
            mDetailedForecastFragment = (DetailedForecastFragment) getSupportFragmentManager().findFragmentByTag(TAG_DETAIL_FRAGMENT);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }
}
