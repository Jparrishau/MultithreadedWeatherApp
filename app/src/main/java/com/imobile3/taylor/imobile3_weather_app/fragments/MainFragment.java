package com.imobile3.taylor.imobile3_weather_app.fragments;

import android.Manifest;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.imobile3.taylor.imobile3_weather_app.HttpJSONRequest;
import com.imobile3.taylor.imobile3_weather_app.LocationLookup;
import com.imobile3.taylor.imobile3_weather_app.ForecastLocationListener;
import com.imobile3.taylor.imobile3_weather_app.R;
import com.imobile3.taylor.imobile3_weather_app.activities.MainActivity;
import com.imobile3.taylor.imobile3_weather_app.activities.SimpleForecastActivity;
import com.imobile3.taylor.imobile3_weather_app.adapters.PastLocationsAdapter;
import com.imobile3.taylor.imobile3_weather_app.interfaces.LocationDataTaskListener;
import com.imobile3.taylor.imobile3_weather_app.interfaces.WeatherDataTaskListener;
import com.imobile3.taylor.imobile3_weather_app.models.Day;
import com.imobile3.taylor.imobile3_weather_app.models.DetailedWeatherItem;
import com.imobile3.taylor.imobile3_weather_app.models.Location;
import com.imobile3.taylor.imobile3_weather_app.models.WeatherForecast;
import com.imobile3.taylor.imobile3_weather_app.utilities.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Taylor Parrish on 8/29/2016.
 *
 * MainFragment is responsible for creating the location search page
 * it runs a background task to find location details and passes them to the
 * Simpleforecast activity/fragment.
 *
 * Issue 1: Need to get sharedpreference on change listener updating pastLocationsListView without refresh.
 * Possible Solutions: Still researching. Will add current temperature as well in future.
 * Issue 2: If Fragment is recreated after failed location lookup sharedpreferences is erased.
 * Possible Solutions: Need to handle exception better. RevertLocation method probably the issue.
 */
public class MainFragment extends Fragment implements LocationDataTaskListener, WeatherDataTaskListener {
    private static final String CLASS_TAG = MainFragment.class.getSimpleName();
    private static final boolean DEBUG = true;

    public final static String TAG_EXTRA_LOCATION = "location";

    private boolean mIsTaskRunning = false;
    private LocationManager mLocationManager;
    private ProgressDialog mProgressDialog;
    private SharedPreferences mSharedPreferences;
    private Location mLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (DEBUG) Log.d(CLASS_TAG, "onCreate(Bundle)");
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        mLocationManager = (LocationManager)
                getActivity().getSystemService(Context.LOCATION_SERVICE);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (DEBUG) Log.d(CLASS_TAG, "onCreateView(LayoutInflater, ViewGroup, Bundle)");

        View root = inflater.inflate(R.layout.main_menu, container, false);

        Button fetchDataButton = (Button) root.findViewById(R.id.fetchDataButton);
        fetchDataButton.setOnClickListener(fetchDataButtonListener);
        fetchDataButton.setTransformationMethod(null);

        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (DEBUG) Log.d(CLASS_TAG, "onActivityCreated(Bundle)");
        super.onActivityCreated(savedInstanceState);

        if (mIsTaskRunning) {
            mProgressDialog = ProgressDialog.show(getActivity(), "Fetching data", "Please wait...");
        }
    }

    @Override
    public void onStart() {
        if (DEBUG) Log.d(CLASS_TAG, "onStart()");
        super.onStart();
        checkLocationProvidersEnabled();
    }

    @Override
    public void onResume() {
        if (DEBUG) Log.d(CLASS_TAG, "onResume()");
        super.onResume();

    }

    @Override
    public void onDetach() {
        if (DEBUG) Log.d(CLASS_TAG, "onDetach()");

        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        super.onDetach();
    }

    @Override
    public void onTaskStarted() {
        if (DEBUG) Log.d(CLASS_TAG, "onTaskStarted()");
        mIsTaskRunning = true;
        mProgressDialog = ProgressDialog.show(getActivity(), "Fetching data", "Please wait...");
    }

    @Override
    public void onTaskFinished(ArrayList<WeatherForecast> weatherForecasts) {
        if (DEBUG) Log.d(CLASS_TAG, "onTaskFinished()");

        String test = weatherForecasts.get(0).toString();
        Log.i("TEST", test);

        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        mIsTaskRunning = false;
        setUpPastLocationListView(getView());

        //saveWeatherItems.putParcelableArrayList(TAG_WEATHER_ITEM_BUNDLE, weatherForecasts);

        //Sets items in the WeatherForecast listviews on click function
        //setupWeatherItemListView(weatherForecasts);
    }

    @Override
    public void onTaskFinished(Location location) {
        if (DEBUG) Log.d(CLASS_TAG, "onTaskFinished()");

        //This is a temporary fix.
        //Find out how to avoid global class later.
        mLocation = location;

        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        mIsTaskRunning = false;
        setupLocationValidationDialog(location);
    }

    private void setupLocationValidationDialog(Location location) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                new JSONParser(MainFragment.this).execute(mLocation);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog. Exit dialog.
            }
        });

        AlertDialog locationConfirmationDialog = builder.create();
        locationConfirmationDialog.setTitle("Is the information below correct?");
        locationConfirmationDialog.setMessage(location.getFormatted_address());
        locationConfirmationDialog.show();
    }

    private void setUpPastLocationListView(View root) {
        final ListView previousLocationsList = (ListView) root.findViewById(R.id.pastLocationsList);
        setupForecastOnItemClickListener(previousLocationsList);
        setupDeleteItemOnLongClickListener(previousLocationsList);

        previousLocationsList.setAdapter(
                new PastLocationsAdapter(getActivity(), mSharedPreferences.getAll()));
    }

    private void setupForecastOnItemClickListener(ListView previousLocationsList) {
        previousLocationsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                TextView textView = (TextView) view.findViewById(R.id.cityText);
                String locationText = textView.getText().toString();

                Intent mSimpleForecastIntent = new Intent(getActivity(), SimpleForecastActivity.class);
                mSimpleForecastIntent.putExtra(TAG_EXTRA_LOCATION,
                        mSharedPreferences.getString(locationText, null));
                startActivity(mSimpleForecastIntent);
            }
        });
    }

    private void setupDeleteItemOnLongClickListener(ListView previousLocationsList) {
        previousLocationsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView = (TextView) view.findViewById(R.id.cityText);
                final String locationText = textView.getText().toString();

                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setCancelable(false);
                dialog.setTitle(R.string.deletListItemWarning);
                dialog.setMessage(locationText);
                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        mSharedPreferences.edit().remove(locationText).apply();
                    }
                });
                dialog.setNegativeButton(getActivity().getString(R.string.Cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        //User cancelled delete
                    }
                });
                dialog.show();
                return false;
            }
        });
    }

    private void checkLocationProvidersEnabled() {
        boolean gps_enabled =
                mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!gps_enabled) {
            enableGPSDialog();
        }
    }

    private void enableGPSDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setCancelable(false);
        dialog.setTitle(getActivity().getResources().getString(R.string.gps_warning_title));
        dialog.setMessage(getActivity().getResources().getString(R.string.gps_not_enabled));
        dialog.setPositiveButton(getActivity().getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                getActivity().startActivity(myIntent);
            }
        });
        dialog.setNegativeButton(getActivity().getString(R.string.Cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                getActivity().finish();
                System.exit(0);
            }
        });
        dialog.show();
    }

    View.OnClickListener fetchDataButtonListener = new View.OnClickListener() {
        public void onClick(View view) {
            EditText locationText = (EditText) getActivity().findViewById(R.id.locationField);
            String location = locationText.getText().toString();

            //If locationField is empty, use GPS location instead
            if (location.matches("")) {
                LocationListener locationListener = new ForecastLocationListener();

                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mLocationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener, null);
                android.location.Location loc = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                location = loc.getLatitude() + "," + loc.getLongitude();
            }
            new LocationResponse(MainFragment.this).execute(location);
        }
    };

    // Using the user input location string to verify and respond with more detailed location data
    private class LocationResponse extends AsyncTask<String, String, Location> {
        private final LocationDataTaskListener listener;

        public LocationResponse(LocationDataTaskListener mListener) {
            this.listener = mListener;
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
            listener.onTaskStarted();
        }

        @Override
        public Location doInBackground(String... args) {
            //Use user set location string and return geocode location data object model
            String locationString = args[0];
            return new LocationLookup(locationString).invoke();
        }

        @Override
        public void onPostExecute(Location location) {
            listener.onTaskFinished(location);
        }
    }

    private class JSONParser extends AsyncTask<Location, String, JSONObject> {
        private WeatherDataTaskListener mListener;
        Location location;

        public JSONParser(WeatherDataTaskListener listener) {
            this.mListener = listener;
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
            mListener.onTaskStarted();
        }

        @Override
        public JSONObject doInBackground(Location... args) {
            location = args[0];

            //Put this somewhere else later3
            final String WUNDERGROUND_API_KEY = "20a88f5fc4c597d7";

            //URL for WUnderground API Call
            String url = "http://api.wunderground.com/api/" + WUNDERGROUND_API_KEY
                    + "/forecast/q/" + location.getCoordinates() + ".json";

            try {
                return new HttpJSONRequest().getJSONFromUrl(url);
            } catch (IOException | JSONException e) {
                //Need to handle this properly. Network may not be enabled/working.
                e.printStackTrace();
                revertToMainPage();
            }
            //This causes app to crash. Find a way to handle this.
            //Need to add new exception above?
            return null;
        }

        @Override
        public void onPostExecute(JSONObject JSONWeatherData) {
            ArrayList<WeatherForecast> weatherForecasts = parseJSONWeatherData(JSONWeatherData);
            mListener.onTaskFinished(weatherForecasts);
        }

        //Parses JSON Data into its respective model objects
        private ArrayList<WeatherForecast> parseJSONWeatherData(JSONObject JSONWeatherData) {
            ArrayList<WeatherForecast> weatherForecasts = new ArrayList<>();
            ArrayList<DetailedWeatherItem> detailWeatherItems = new ArrayList<>();

            try {
                // Getting JSON Wunderground Simpleforecast data
                JSONArray simpleforecastData = JSONWeatherData.getJSONObject("forecast")
                        .getJSONObject("simpleforecast").getJSONArray("forecastday");
                // Getting JSON Wunderground detail forecast data
                JSONArray detailforecastData = JSONWeatherData.getJSONObject("forecast")
                        .getJSONObject("txt_forecast").getJSONArray("forecastday");

                //Parse the corresponding data into WeatherForecast object model
                parseSimpleForecastDataModel(weatherForecasts, simpleforecastData);
                //Parse the corresponding data into WeatherForecast object model
                parseDetailForecastDataModel(detailWeatherItems, detailforecastData);
                //Set List of DetailWeatherItems to corresponding WeatherForecast model
                setWeatherItemDetails(weatherForecasts, detailWeatherItems);
            } catch (JSONException e) {
                e.printStackTrace();
                revertToMainPage();
            }
            return weatherForecasts;
        }

        // This should be done inside of weatherforecast?
        private void setWeatherItemDetails(ArrayList<WeatherForecast> weatherForecasts,
                                           ArrayList<DetailedWeatherItem> detailWeatherItems) {
           /* //Find a more efficient way to do this?
            for (int i = 0; i < weatherForecasts.size(); i++) {
                for (int j = 0; j < detailWeatherItems.size(); j++) {
                    String weekday = Utils.getFirstWord(detailWeatherItems.get(j).getWeekday());
                    if (weatherForecasts.get(i).getWeekday().equals(weekday)) {
                        weatherForecasts.get(i).addDetailWeatherItem(detailWeatherItems.get(j));
                    }
                }
            }*/
        }

        private void parseDetailForecastDataModel(ArrayList<DetailedWeatherItem> detailWeatherItems,
                                                  JSONArray detailforecastData) throws JSONException {
            JSONObject detailData;
            for (int i = 0; i < detailforecastData.length(); i++) {
                detailData = detailforecastData.getJSONObject(i);

                String weekday = detailData.getString("title");
                String description = detailData.getString("fcttext");
                String pop = detailData.getString("pop");

                detailWeatherItems.add(new DetailedWeatherItem(weekday, description, pop));
            }
        }

        private void parseSimpleForecastDataModel(ArrayList<WeatherForecast> weatherForecasts,
                                                  JSONArray simpleforecastData) throws JSONException {
            JSONObject simpleData;
            ArrayList<Day> days = new ArrayList<>();
            for (int i = 0; i < simpleforecastData.length(); i++) {
                simpleData = simpleforecastData.getJSONObject(i);

                String textDay = simpleData.getJSONObject("date").getString("weekday");
                int day = Integer.parseInt(simpleData.getJSONObject("date").getString("day"));
                int month = Integer.parseInt(simpleData.getJSONObject("date").getString("month"));
                int year = Integer.parseInt(simpleData.getJSONObject("date").getString("year"));
                long time = Integer.parseInt(simpleData.getJSONObject("date").getString("epoch"));

                String conditions = simpleData.getString("conditions");
                String high = simpleData.getJSONObject("high").getString("fahrenheit") + "˚ F";
                String low = simpleData.getJSONObject("low").getString("fahrenheit") + "˚ F";
                WeatherForecast weatherForecast = new WeatherForecast(conditions, high, low);

                Day currentDay = new Day(day, month, year, time, textDay, weatherForecast);
                days.add(i, currentDay);
                //weatherForecasts.add(new WeatherForecast(weekday, conditions, high, low));
            }
            location.setDays(days);
        }

        private void revertToMainPage() {
            Utils.showToast(getActivity(),
                    "Something went wrong, try another location. Make sure you have internet connection.");
            startActivity(new Intent(getActivity(), MainActivity.class));
        }
    }
}