package com.imobile3.taylor.imobile3_weather_app.activities;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.imobile3.taylor.imobile3_weather_app.ForecastLocationListener;
import com.imobile3.taylor.imobile3_weather_app.R;
import com.imobile3.taylor.imobile3_weather_app.fragments.MainFragment;
import com.imobile3.taylor.imobile3_weather_app.tasks.LocationDataLookup;

import static com.imobile3.taylor.imobile3_weather_app.R.string.location;

/**
 * MainActvity is responsible for loading its corresponding fragment
 * as well as initializes the toolbar and its menus.
 *
 * Issue 1: Toolbar is showing on all activities but menu options are not?
 * Possible Solutions: Unsure where problem is coming from. Further testing required.
 *
 * @author Taylor Parrish
 * @since 8/23/2016
 */
public class MainActivity extends AppCompatActivity {
    private static final String CLASS_TAG = MainActivity.class.getSimpleName();
    private static final boolean DEBUG = true;

    private static final String TAG_MAIN_FRAGMENT = "main_fragment";
    private MainFragment mMainFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (DEBUG) Log.d(CLASS_TAG, "onCreate(Bundle)");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    //@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                //Do something
                return true;
            case R.id.action_location:
                mMainFragment.addLocationByGPS();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}


