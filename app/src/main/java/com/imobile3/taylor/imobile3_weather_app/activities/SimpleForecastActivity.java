package com.imobile3.taylor.imobile3_weather_app.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.imobile3.taylor.imobile3_weather_app.R;
import com.imobile3.taylor.imobile3_weather_app.fragments.SimpleForecastFragment;


/**
 * Created by Taylor Parrish on 8/23/2016.
 *
 * SimpleForecastActivity is responsible for loading its corresponding fragment
 * as well as initializes the toolbar and its menus.
 *
 * Issue 1: Toolbar is showing on all activities but menu options are not?
 * Possible Solutions: Unsure where problem is coming from. Further testing required.
 */
public class SimpleForecastActivity extends FragmentActivity {
    private static final String CLASS_TAG = SimpleForecastActivity.class.getSimpleName();
    private static final boolean DEBUG = true;

    private static final String TAG_SIMPLE_FRAGMENT = "simple_fragment";
    private SimpleForecastFragment mSimpleFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (DEBUG) Log.i(CLASS_TAG, "onCreate(Bundle)");
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            mSimpleFragment = new SimpleForecastFragment();

            getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.content, mSimpleFragment, TAG_SIMPLE_FRAGMENT)
                    .commit();
        } else {
            mSimpleFragment = (SimpleForecastFragment) getSupportFragmentManager().findFragmentByTag(TAG_SIMPLE_FRAGMENT);
        }
    }

    //@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    //@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }
}



