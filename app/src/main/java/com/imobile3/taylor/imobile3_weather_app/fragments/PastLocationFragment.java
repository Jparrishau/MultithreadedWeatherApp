package com.imobile3.taylor.imobile3_weather_app.fragments;

import android.Manifest;
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
import android.support.v4.app.Fragment;
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

import com.google.gson.Gson;
import com.imobile3.taylor.imobile3_weather_app.HttpJSONRequest;
import com.imobile3.taylor.imobile3_weather_app.LocationLookup;
import com.imobile3.taylor.imobile3_weather_app.ForecastLocationListener;
import com.imobile3.taylor.imobile3_weather_app.R;
import com.imobile3.taylor.imobile3_weather_app.activities.MainActivity;
import com.imobile3.taylor.imobile3_weather_app.activities.WeatherForecastActivity;
import com.imobile3.taylor.imobile3_weather_app.adapters.PastLocationsAdapter;
import com.imobile3.taylor.imobile3_weather_app.interfaces.LocationDataTaskListener;
import com.imobile3.taylor.imobile3_weather_app.interfaces.WeatherDataTaskListener;
import com.imobile3.taylor.imobile3_weather_app.models.CurrentWeatherForecast;
import com.imobile3.taylor.imobile3_weather_app.models.Day;
import com.imobile3.taylor.imobile3_weather_app.models.DailyDetailedWeatherItem;
import com.imobile3.taylor.imobile3_weather_app.models.Location;
import com.imobile3.taylor.imobile3_weather_app.models.DailyWeatherForecast;
import com.imobile3.taylor.imobile3_weather_app.utilities.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Taylor Parrish on 8/29/2016.
 *
 * PastLocationFragment is responsible for creating the location search page
 * it runs a background task to find location details and passes them to the
 * Simpleforecast activity/fragment.
 *
 * Issue 1: Need to get sharedpreference on change listener updating pastLocationsListView without refresh.
 * Possible Solutions: Still researching. Will add current temperature as well in future.
 * Issue 2: If Fragment is recreated after failed location lookup sharedpreferences is erased.
 * Possible Solutions: Need to handle exception better. RevertLocation method probably the issue.
 */
public class PastLocationFragment extends Fragment implements LocationDataTaskListener, WeatherDataTaskListener {
    private static final String CLASS_TAG = PastLocationFragment.class.getSimpleName();
    private static final boolean DEBUG = true;

    public final static String TAG_LOCATION_BUNDLE = "locationBundle";

    private boolean mIsTaskRunning = false;
    private LocationManager mLocationManager;
    private ProgressDialog mProgressDialog;
    private SharedPreferences mSharedPreferences;

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

        View root = inflater.inflate(R.layout.fragment_main, container, false);

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
        refreshPastLocations();
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
    public void onTaskFinished(Location location) {
        if (DEBUG) Log.d(CLASS_TAG, "onTaskFinished()");

        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        mIsTaskRunning = false;
        setupLocationValidationDialog(location);
    }

    @Override
    public void onWeatherDataTaskFinished(Location location) {
        if (DEBUG) Log.d(CLASS_TAG, "onTaskFinished()");

        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        mIsTaskRunning = false;

        Gson gson = new Gson();
        String locationJSON = gson.toJson(location);
        mSharedPreferences.edit().putString(location.getCoordinates(), locationJSON).apply();
        refreshPastLocations();
    }

    private void refreshPastLocations() {
        if(mSharedPreferences.getAll() != null) {
            ArrayList<Location> locations = new ArrayList<>();

            Map<String, ?> keys = mSharedPreferences.getAll();
            for (Map.Entry<String, ?> entry : keys.entrySet()) {
                Location tempLocation = new Gson().fromJson(entry.getValue().toString(), Location.class);
                locations.add(tempLocation);
            }
            setUpPastLocationListView(locations);
        }
    }

    private void setUpPastLocationListView(final ArrayList<Location> locations) {
        final ListView previousLocationsList = (ListView) getView().findViewById(R.id.pastLocationsList);

        /*Go to on press*/
        previousLocationsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                startForecastActivity(locations.get(position));
            }
        });
        /* Delete on hold */
        previousLocationsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Location tempLocation = locations.get(i);
                //I should add a function in Location model that does this for me.
                final String locationText = tempLocation.getCity() + ", " + tempLocation.getState();

                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setCancelable(false);
                dialog.setTitle(R.string.deleteListItemWarning);
                dialog.setMessage(locationText);
                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        //The key is the coordinates now, not the city. This doesn't work.
                        mSharedPreferences.edit().remove(tempLocation.getCoordinates()).apply();
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

        previousLocationsList.setAdapter(
                new PastLocationsAdapter(getActivity(), mSharedPreferences.getAll()));
    }

    private void startForecastActivity(Location locationToDisplay) {
        Intent mSimpleForecastIntent = new Intent(getActivity(), WeatherForecastActivity.class);
        Bundle locationBundle = new Bundle();

        locationBundle.putParcelable(TAG_LOCATION_BUNDLE, locationToDisplay);
        mSimpleForecastIntent.putExtras(locationBundle);
        startActivity(mSimpleForecastIntent);
    }

    private void setupLocationValidationDialog(final Location location) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                new JSONParser(PastLocationFragment.this).execute(location);
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
            new LocationResponse(PastLocationFragment.this).execute(location);
        }
    };

    /*
        Using the user input location string to verify and respond with more detailed location data

        Note: Hopefully move this to an outer class for better readability in future.
    */
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

    /*
        This needs to return the Location object with its respective counterparts now set.
        How to have two separate onTaskFinished() functions? Do we even need two asynch task?
        Possibly combine them.
    */
    private class JSONParser extends AsyncTask<Location, String, HashMap<String, JSONObject> > {
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
        public HashMap<String, JSONObject> doInBackground(Location... args) {
            HashMap<String, JSONObject> weatherData = new HashMap<>();
            location = args[0];

            //Put these somewhere else later
            final String WUNDERGROUND_API_KEY = "20a88f5fc4c597d7";

            /*
                Do this some cleaner way later, possibly with loop.
            */
            //URL for WUnderground API Call
            String weatherURL_10DAY = "http://api.wunderground.com/api/" + WUNDERGROUND_API_KEY
                    + "/" + "forecast10day" + "/q/" + location.getCoordinates() + ".json";
            //URL for WUnderground API Call
            String weatherURL_Conditions = "http://api.wunderground.com/api/" + WUNDERGROUND_API_KEY
                    + "/" + "conditions" + "/q/" + location.getCoordinates() + ".json";
            //URL for WUnderground API Call
            String weatherURL_Astronomy = "http://api.wunderground.com/api/" + WUNDERGROUND_API_KEY
                    + "/" + "astronomy" + "/q/" + location.getCoordinates() + ".json";

            try {
                weatherData.put("FORECAST_10DAY", new HttpJSONRequest().getJSONFromUrl(weatherURL_10DAY));
                weatherData.put("FORECAST_OBSERVATION_CURR", new HttpJSONRequest().getJSONFromUrl(weatherURL_Conditions));
                weatherData.put("FORECAST_ASTRONOMY", new HttpJSONRequest().getJSONFromUrl(weatherURL_Astronomy));
                return weatherData;
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
        public void onPostExecute(HashMap<String, JSONObject> weatherData) {
            Location location = parseJSONWeatherData(weatherData);
            mListener.onWeatherDataTaskFinished(location);
        }

        //Parses JSON Data into its respective model objects
        private Location parseJSONWeatherData(HashMap<String, JSONObject> weatherData) {
            JSONObject dailyWeatherForecastData = weatherData.get("FORECAST_10DAY");
            JSONObject currentWeatherForecastData = weatherData.get("FORECAST_OBSERVATION_CURR");
            JSONObject astronomyWeatherForecastData = weatherData.get("FORECAST_ASTRONOMY");

            try {
                ArrayList<Day> days =
                        parseSimpleForecastData(dailyWeatherForecastData);
                CurrentWeatherForecast currentWeatherForecast
                        = parseCurrentWeatherForecast(currentWeatherForecastData, astronomyWeatherForecastData);

                if(days != null) {
                    location.setDays(days);
                }
                if(currentWeatherForecast != null) {
                    location.setCurrentWeatherForecast(currentWeatherForecast);
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
                revertToMainPage();
            }
            return location;
        }

        private CurrentWeatherForecast parseCurrentWeatherForecast(JSONObject currentWeatherForecastData, JSONObject astronomyWeatherForecastData) {
            // Getting JSON Wunderground Simpleforecast data
            CurrentWeatherForecast currentWeatherForecast = null;
            try {
                JSONObject observation_data = currentWeatherForecastData.getJSONObject("current_observation");
                JSONObject astronomy_data = astronomyWeatherForecastData.getJSONObject("sun_phase");
                JSONObject sunrise = astronomy_data.getJSONObject("sunrise");
                JSONObject sunset = astronomy_data.getJSONObject("sunset");

                String weatherDescr = observation_data.getString("weather");
                String tempText = observation_data.getString("temperature_string");
                double tempF = observation_data.getDouble("temp_f");
                double tempC = observation_data.getDouble("temp_c");
                String humidity = observation_data.getString("relative_humidity");
                String windText = observation_data.getString("wind_string");
                String windDir = observation_data.getString("wind_dir");
                int windDegree = observation_data.getInt("wind_degrees");
                double windMPH = observation_data.getDouble("wind_mph");
                double windGustMPH = observation_data.getDouble("wind_gust_mph");
                double windKPH = observation_data.getDouble("wind_kph");
                double windGustKPH = observation_data.getDouble("wind_gust_kph");


                currentWeatherForecast =
                        new CurrentWeatherForecast(weatherDescr, tempText, tempF,
                        tempC, humidity, windText, windDir,
                        windDegree, windMPH, windGustMPH,
                        windKPH, windGustKPH);
            }
            catch (JSONException e) {
                e.printStackTrace();
                revertToMainPage();
            }
            return currentWeatherForecast;
        }

        private ArrayList<Day> parseSimpleForecastData(JSONObject dailyWeatherForecastData) throws JSONException {
            JSONObject simpleData;
            ArrayList<Day> days = new ArrayList<>();

            // Getting JSON Wunderground Simpleforecast data
            JSONArray simpleforecastData = dailyWeatherForecastData.getJSONObject("forecast")
                    .getJSONObject("simpleforecast").getJSONArray("forecastday");
            // Getting JSON Wunderground detail forecast data
            JSONArray detailforecastData = dailyWeatherForecastData.getJSONObject("forecast")
                    .getJSONObject("txt_forecast").getJSONArray("forecastday");

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
                DailyWeatherForecast dailyWeatherForecast = new DailyWeatherForecast(conditions, high, low);

                Day currentDay = new Day(day, month, year, time, textDay, dailyWeatherForecast);
                days.add(i, currentDay);
            }

            //Parse the corresponding data into DailyWeatherForecast object model
            days = parseDetailForecastDataModel(detailforecastData, days);
            return days;
        }

        private ArrayList<Day> parseDetailForecastDataModel(JSONArray detailForecastData, ArrayList<Day> days) throws JSONException {
            JSONObject detailData;

            int j = 0;
            int iterationPairCounter = 0;
            for (int i = 0; i < days.size(); i++) {
                while(j < detailForecastData.length()){
                    detailData = detailForecastData.getJSONObject(j);

                    String weekday = detailData.getString("title");
                    String description = detailData.getString("fcttext");
                    String pop = detailData.getString("pop");

                    days.get(i).getWeatherForecast().getDetailWeatherItems().add(new DailyDetailedWeatherItem(weekday, description, pop));

                    iterationPairCounter++;
                    j++;
                    if(iterationPairCounter == 2){
                        iterationPairCounter = 0;
                        break;
                    }
                }
            }
            return days;
        }

        private void revertToMainPage() {
            Utils.showToast(getActivity(),
                    "Something went wrong, try another location. Make sure you have internet connection.");
            startActivity(new Intent(getActivity(), MainActivity.class));
        }
    }
}