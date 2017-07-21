package com.imobile3.taylor.imobile3_weather_app.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.imobile3.taylor.imobile3_weather_app.R;
import com.imobile3.taylor.imobile3_weather_app.fragments.LocationDialogFragment;
import com.imobile3.taylor.imobile3_weather_app.fragments.MainFragment;
import com.imobile3.taylor.imobile3_weather_app.models.Location;

/**
 * MainActvity is responsible for loading its corresponding fragment
 * as well as initializes the toolbar and its menus.
 *
 * @author Taylor Parrish
 * @since 8/23/2016
 */
public class MainActivity extends AppCompatActivity implements LocationDialogFragment.SubmitCancelListener {
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
                //Location Dialog
                searchCitiesDialog();
            }
        });

    }

    @Override
    public void onLocationValidationDialogSubmit(Location location) {
        if (DEBUG) Log.d(CLASS_TAG, "onLocationValidationDialogSubmit()");
        mMainFragment.executeWeatherDataLookup(location);
    }

    @Override
    public void onLocationValidationDialogCancel() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_location:
                 if(mMainFragment.checkLocationProviderEnabled()) {
                     mMainFragment.addLocationByGPS();
                 }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void searchCitiesDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setMaxLines(1);
        input.setHint("City, State or Zipcode");
        input.setSingleLine();
        FrameLayout container = new FrameLayout(this);
        FrameLayout.LayoutParams params =
                new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        input.setLayoutParams(params);
        container.addView(input);
        alertDialog.setTitle("Search for a location");
        alertDialog.setView(container);
        alertDialog.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String result = input.getText().toString();
                if (!result.isEmpty()) {
                    mMainFragment.lookupLocation(result);
                }
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Cancelled
            }
        });
        AlertDialog dialog = alertDialog.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();
    }
}