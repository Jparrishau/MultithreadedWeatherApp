package com.imobile3.taylor.imobile3_weather_app.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.imobile3.taylor.imobile3_weather_app.HttpJSONRequest;
import com.imobile3.taylor.imobile3_weather_app.R;
import com.imobile3.taylor.imobile3_weather_app.activities.DetailedForecastActivity;
import com.imobile3.taylor.imobile3_weather_app.activities.MainActivity;
import com.imobile3.taylor.imobile3_weather_app.adapters.ForecastAdapter;
import com.imobile3.taylor.imobile3_weather_app.interfaces.WeatherDataTaskListener;
import com.imobile3.taylor.imobile3_weather_app.models.DetailedWeatherItem;
import com.imobile3.taylor.imobile3_weather_app.models.WeatherItem;
import com.imobile3.taylor.imobile3_weather_app.utilities.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Taylor Parrish on 8/29/2016
 *
 * SimpleForecastFragment runs a background task using the users location
 * data that searches for relevant weather details to then be displayed to the user.
 */
public class SimpleForecastFragment extends Fragment implements WeatherDataTaskListener {
    private static final String CLASS_TAG = SimpleForecastFragment.class.getSimpleName();
    private static final boolean DEBUG = true;
    private static final boolean TEST = true;


    private boolean mIsTaskRunning = false;
    private String mLocation;

    private Bundle saveWeatherItems;
    private ProgressDialog mProgressDialog;
    public final static String TAG_EXTRA_DETAIL_ITEMS = "detailWeatherItems";
    public final static String TAG_WEATHER_ITEM_BUNDLE = "weatherBundle";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (DEBUG) Log.d(CLASS_TAG, "onCreate(Bundle)");
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        saveWeatherItems = new Bundle();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (DEBUG) Log.d(CLASS_TAG, "onCreateView(LayoutInflater, ViewGroup, Bundle)");

        return inflater.inflate(R.layout.forecast_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (DEBUG) Log.d(CLASS_TAG, "onActivityCreated(Bundle)");
        super.onActivityCreated(savedInstanceState);

        //Retrieve location input from user in MainFragment
        mLocation = getActivity().getIntent().getStringExtra(MainFragment.TAG_EXTRA_LOCATION);

        if (mIsTaskRunning) {
            mProgressDialog = ProgressDialog.show(getActivity(), "Fetching data", "Please wait...");
        }

        if (!saveWeatherItems.containsKey(TAG_WEATHER_ITEM_BUNDLE)) {
            new JSONParser(SimpleForecastFragment.this).execute();
        } else {
            ArrayList<WeatherItem> weatherItems =
                    saveWeatherItems.getParcelableArrayList(TAG_WEATHER_ITEM_BUNDLE);
            setupWeatherItemListView(weatherItems);
        }
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
    public void onTaskFinished(ArrayList<WeatherItem> weatherItems) {
        if (DEBUG) Log.d(CLASS_TAG, "onTaskFinished()");

        String test = weatherItems.get(0).toString();
        Log.i("TEST", test);

        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        mIsTaskRunning = false;
        saveWeatherItems.putParcelableArrayList(TAG_WEATHER_ITEM_BUNDLE, weatherItems);

        //Sets items in the WeatherItem listviews on click function
        setupWeatherItemListView(weatherItems);
    }

    private void setupWeatherItemListView(final ArrayList<WeatherItem> weatherItems) {
        ListView simpleForecastListview = (ListView) getActivity().findViewById(R.id.forecastListView);
        simpleForecastListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                Intent detailedforecastIntent = new Intent(getActivity(), DetailedForecastActivity.class);
                ArrayList<DetailedWeatherItem> detailItems = weatherItems.get(position).getDetailWeatherItems();

                detailedforecastIntent.putParcelableArrayListExtra(TAG_EXTRA_DETAIL_ITEMS, detailItems);
                startActivity(detailedforecastIntent);
            }
        });
        ForecastAdapter adapter = new ForecastAdapter(getActivity(), weatherItems);
        simpleForecastListview.setAdapter(adapter);
    }

    private class JSONParser extends AsyncTask<String, String, JSONObject> {
        private WeatherDataTaskListener mListener;

        public JSONParser(WeatherDataTaskListener listener) {
            this.mListener = listener;
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
            mListener.onTaskStarted();
        }

        @Override
        public JSONObject doInBackground(String... args) {
            final String WUNDERGROUND_API_KEY = "20a88f5fc4c597d7";

            //URL for WUnderground API Call
            String url = "http://api.wunderground.com/api/" + WUNDERGROUND_API_KEY
                    + "/forecast/q/" + mLocation + ".json";

            try {
                return new HttpJSONRequest().getJSONFromUrl(url);
            } catch (IOException | JSONException e) {
                //Need to handle this properly. Network may not be enabled/working.
                e.printStackTrace();
                revertToMainPage();
            }
            return null;
        }

        @Override
        public void onPostExecute(JSONObject JSONWeatherData) {
            ArrayList<WeatherItem> weatherItems = parseJSONWeatherData(JSONWeatherData);
            mListener.onTaskFinished(weatherItems);
        }

        //Parses JSON Data into its respective model objects
        private ArrayList<WeatherItem> parseJSONWeatherData(JSONObject JSONWeatherData) {
            ArrayList<WeatherItem> weatherItems = new ArrayList<>();
            ArrayList<DetailedWeatherItem> detailWeatherItems = new ArrayList<>();

            try {
                // Getting JSON Wunderground Simpleforecast data
                JSONArray simpleforecastData = JSONWeatherData.getJSONObject("forecast")
                        .getJSONObject("simpleforecast").getJSONArray("forecastday");
                // Getting JSON Wunderground detail forecast data
                JSONArray detailforecastData = JSONWeatherData.getJSONObject("forecast")
                        .getJSONObject("txt_forecast").getJSONArray("forecastday");

                //Parse the corresponding data into WeatherItem object model
                parseSimpleForecastDataModel(weatherItems, simpleforecastData);
                //Parse the corresponding data into WeatherItem object model
                parseDetailForecastDataModel(detailWeatherItems, detailforecastData);
                //Set List of DetailWeatherItems to corresponding WeatherItem model
                setWeatherItemDetails(weatherItems, detailWeatherItems);
            } catch (JSONException e) {
                e.printStackTrace();
                revertToMainPage();
            }
            return weatherItems;
        }

        private void setWeatherItemDetails(ArrayList<WeatherItem> weatherItems,
                                           ArrayList<DetailedWeatherItem> detailWeatherItems) {
            //Find a more efficient way to do this?
            for (int i = 0; i < weatherItems.size(); i++) {
                for (int j = 0; j < detailWeatherItems.size(); j++) {
                    String weekday = Utils.getFirstWord(detailWeatherItems.get(j).getWeekday());
                    if (weatherItems.get(i).getWeekday().equals(weekday)) {
                        weatherItems.get(i).addDetailWeatherItem(detailWeatherItems.get(j));
                    }
                }
            }
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

        private void parseSimpleForecastDataModel(ArrayList<WeatherItem> weatherItems,
                                                  JSONArray simpleforecastData) throws JSONException {
            JSONObject simpleData;
            for (int i = 0; i < simpleforecastData.length(); i++) {
                simpleData = simpleforecastData.getJSONObject(i);

                String weekday = simpleData.getJSONObject("date").getString("weekday");
                String conditions = simpleData.getString("conditions");
                String high = simpleData.getJSONObject("high").getString("fahrenheit") + "˚ F";
                String low = simpleData.getJSONObject("low").getString("fahrenheit") + "˚ F";

                weatherItems.add(new WeatherItem(weekday, conditions, high, low));
            }
        }

        private void revertToMainPage() {
            Utils.showToast(getActivity(),
                    "Something went wrong, try another location. Make sure you have internet connection.");
            startActivity(new Intent(getActivity(), MainActivity.class));
        }
    }
}
