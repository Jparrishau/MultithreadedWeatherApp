package com.imobile3.taylor.imobile3_weather_app.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.imobile3.taylor.imobile3_weather_app.R;
import com.imobile3.taylor.imobile3_weather_app.fragments.PastLocationFragment;

/**
 * Created by Taylor Parrish on 8/23/2016.
 *
 * MainActvity is responsible for loading its corresponding fragment
 * as well as initializes the toolbar and its menus.
 *
 * Issue 1: Toolbar is showing on all activities but menu options are not?
 * Possible Solutions: Unsure where problem is coming from. Further testing required.
 */
public class MainActivity extends AppCompatActivity {
    private static final String CLASS_TAG = MainActivity.class.getSimpleName();
    private static final boolean DEBUG = true;

    private static final String TAG_MAIN_FRAGMENT = "main_fragment";
    private PastLocationFragment mPastLocationFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (DEBUG) Log.d(CLASS_TAG, "onCreate(Bundle)");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            mPastLocationFragment = new PastLocationFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.pastLocationContent, mPastLocationFragment, TAG_MAIN_FRAGMENT)
                    .commit();
        } else {
            mPastLocationFragment = (PastLocationFragment) getSupportFragmentManager().findFragmentByTag(TAG_MAIN_FRAGMENT);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        if (toolbar != null)
        {
            this.setSupportActionBar(toolbar);
        }
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
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


