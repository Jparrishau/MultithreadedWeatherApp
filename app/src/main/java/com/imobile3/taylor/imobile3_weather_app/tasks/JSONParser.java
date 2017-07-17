package com.imobile3.taylor.imobile3_weather_app.tasks;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.imobile3.taylor.imobile3_weather_app.HttpJSONRequest;
import com.imobile3.taylor.imobile3_weather_app.R;
import com.imobile3.taylor.imobile3_weather_app.activities.MainActivity;
import com.imobile3.taylor.imobile3_weather_app.interfaces.WeatherDataTaskListener;
import com.imobile3.taylor.imobile3_weather_app.models.CurrentWeatherForecast;
import com.imobile3.taylor.imobile3_weather_app.models.DailyDetailedWeatherItem;
import com.imobile3.taylor.imobile3_weather_app.models.DailyWeatherForecast;
import com.imobile3.taylor.imobile3_weather_app.models.Day;
import com.imobile3.taylor.imobile3_weather_app.models.HourlyWeatherForecast;
import com.imobile3.taylor.imobile3_weather_app.models.Location;
import com.imobile3.taylor.imobile3_weather_app.utilities.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by taylorp on 7/17/2017.
 */
public class JSONParser extends AsyncTask<Location, String, HashMap<String, JSONObject> > {
    private WeatherDataTaskListener mListener;
    private Context mContext;
    Location location;

    public JSONParser(WeatherDataTaskListener listener, Context context) {
        this.mListener = listener;
        this.mContext = context;
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
        //URL for WUnderground API Call
        String weatherURL_Hourly10Day = "http://api.wunderground.com/api/" + WUNDERGROUND_API_KEY
                + "/" + "hourly10day" + "/q/" + location.getCoordinates() + ".json";

        try {
            weatherData.put("FORECAST_10DAY", new HttpJSONRequest().getJSONFromUrl(weatherURL_10DAY));
            weatherData.put("FORECAST_OBSERVATION_CURR", new HttpJSONRequest().getJSONFromUrl(weatherURL_Conditions));
            weatherData.put("FORECAST_ASTRONOMY", new HttpJSONRequest().getJSONFromUrl(weatherURL_Astronomy));
            weatherData.put("FORECAST_HOURLY_10DAY", new HttpJSONRequest().getJSONFromUrl(weatherURL_Hourly10Day));
            return weatherData;
        } catch (IOException | JSONException e) {
            //Need to handle this properly. Network may not be enabled/working.
            e.printStackTrace();
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
        JSONObject hourly10DAYWeatherForecastData = weatherData.get("FORECAST_HOURLY_10DAY");
        JSONObject currentWeatherForecastData = weatherData.get("FORECAST_OBSERVATION_CURR");
        JSONObject astronomyWeatherForecastData = weatherData.get("FORECAST_ASTRONOMY");

        try {
            ArrayList<Day> days =
                    parseSimpleForecastData(dailyWeatherForecastData);
            HashMap<String, ArrayList<HourlyWeatherForecast>> hourlyWeatherForecasts =
                    parseHourlyForecastData(hourly10DAYWeatherForecastData);
            CurrentWeatherForecast currentWeatherForecast
                    = parseCurrentWeatherForecast(currentWeatherForecastData, astronomyWeatherForecastData);

            if(days != null) {
                location.setDays(days);
            }
            if(hourlyWeatherForecasts != null) {
                location.setHourlyWeatherForecastsToday(hourlyWeatherForecasts.get("today"));
                location.setHourlyWeatherForecastsTomorrow(hourlyWeatherForecasts.get("tomorrow"));
                location.setHourlyWeatherForecastsLater(hourlyWeatherForecasts.get("later"));
            }
            if(currentWeatherForecast != null) {
                location.setCurrentWeatherForecast(currentWeatherForecast);
            }
        }
        catch (ParseException | JSONException e) {
            e.printStackTrace();
        }
        return location;
    }

    private HashMap<String, ArrayList<HourlyWeatherForecast>> parseHourlyForecastData(JSONObject hourly10DAYWeatherForecastData) throws JSONException, ParseException {
        HashMap<String, ArrayList<HourlyWeatherForecast>> hourlyWeatherForecasts = new HashMap<>();
        ArrayList<HourlyWeatherForecast> hourlyWeatherForecastsToday = new ArrayList<>();
        ArrayList<HourlyWeatherForecast> hourlyWeatherForecastsTomorrow = new ArrayList<>();
        ArrayList<HourlyWeatherForecast> hourlyWeatherForecastsLater = new ArrayList<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a z  MMMMM dd, yyyy");
        JSONArray hourly_forecast_data = hourly10DAYWeatherForecastData.getJSONArray("hourly_forecast");
        for (int i = 0; i < hourly_forecast_data.length(); i++) {
            JSONObject hourly_forecast = hourly_forecast_data.getJSONObject(i);
            JSONObject time_data = hourly_forecast.getJSONObject("FCTTIME");

            Calendar calendar = Calendar.getInstance();
            String timeToConvert = time_data.getString("pretty").replace("on", "");
            calendar.setTime(dateFormat.parse(timeToConvert));

            String condition = hourly_forecast.getString("condition");
            String temperature = hourly_forecast.getJSONObject("temp").getString("english");
            String feelslike = hourly_forecast.getJSONObject("feelslike").getString("english");
            String humidity = hourly_forecast.getString("humidity");
            String icon = getIcon(hourly_forecast.getString("icon"));


            int currentDayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
            int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
            if(currentDayOfYear == dayOfYear) {
                hourlyWeatherForecastsToday.add(
                        new HourlyWeatherForecast(condition, temperature, feelslike, humidity, icon, calendar));
            }
            else if((currentDayOfYear + 1) == dayOfYear){
                hourlyWeatherForecastsTomorrow.add(
                        new HourlyWeatherForecast(condition, temperature, feelslike, humidity, icon, calendar));
            }
            else if ((currentDayOfYear + 2) == dayOfYear || (currentDayOfYear + 3) == dayOfYear){
                hourlyWeatherForecastsLater.add(
                        new HourlyWeatherForecast(condition, temperature, feelslike, humidity, icon, calendar));
            }

        }
        hourlyWeatherForecasts.put("today", hourlyWeatherForecastsToday);
        hourlyWeatherForecasts.put("tomorrow", hourlyWeatherForecastsTomorrow);
        hourlyWeatherForecasts.put("later", hourlyWeatherForecastsLater);
        return hourlyWeatherForecasts;
    }

    private CurrentWeatherForecast parseCurrentWeatherForecast(JSONObject currentWeatherForecastData,
                                                               JSONObject astronomyWeatherForecastData) throws JSONException, ParseException {
        // Getting JSON Wunderground Simpleforecast data
        CurrentWeatherForecast currentWeatherForecast;

        JSONObject observation_data = currentWeatherForecastData.getJSONObject("current_observation");
        JSONObject astronomy_data = astronomyWeatherForecastData.getJSONObject("sun_phase");
        JSONObject sunrise = astronomy_data.getJSONObject("sunrise");
        JSONObject sunset = astronomy_data.getJSONObject("sunset");
        String sunriseTime = Utils.militaryToStandard(sunrise.getString("hour") + sunrise.getString("minute"));
        String sunsetTime = Utils.militaryToStandard(sunset.getString("hour") + sunset.getString("minute"));

        String weatherIcon = getIcon(observation_data.getString("icon"));
        String weatherDescr = observation_data.getString("weather");
        String tempText = observation_data.getString("temperature_string");
        double tempF = observation_data.getDouble("temp_f");
        double tempC = observation_data.getDouble("temp_c");
        String humidity = observation_data.getString("relative_humidity");
        String windText = observation_data.getString("wind_string");
        String windDir = observation_data.getString("wind_dir");
        int windDegree = observation_data.getInt("wind_degrees");
        double pressureMB = observation_data.getInt("pressure_mb");
        double pressureIN = observation_data.getInt("pressure_in");
        double windMPH = observation_data.getDouble("wind_mph");
        double windGustMPH = observation_data.getDouble("wind_gust_mph");
        double windKPH = observation_data.getDouble("wind_kph");
        double windGustKPH = observation_data.getDouble("wind_gust_kph");

        currentWeatherForecast =
                new CurrentWeatherForecast(weatherIcon, weatherDescr, tempText, tempF,
                        tempC, humidity, windText, windDir,
                        windDegree, pressureMB, pressureIN, windMPH, windGustMPH,
                        windKPH, windGustKPH, sunriseTime, sunsetTime);

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
            String humidity = simpleData.getString("avehumidity");
            String icon = getIcon(simpleData.getString("icon"));

            DailyWeatherForecast dailyWeatherForecast = new DailyWeatherForecast(conditions, high, low, humidity, icon);

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

    private String getIcon(String icon) {
        String iconString = "";

        switch (icon) {
            case "chanceflurries":
                iconString = mContext.getResources().getString(R.string.wi_wu_chanceflurries);
                break;
            case "chancerain":
                iconString = mContext.getResources().getString(R.string.wi_wu_chancerain);
                break;
            case "chancesleat":
                iconString = mContext.getResources().getString(R.string.wi_wu_chancesleat);
                break;
            case "chancesnow":
                iconString = mContext.getResources().getString(R.string.wi_wu_chancesnow);
                break;
            case "chancetstorms":
                iconString = mContext.getResources().getString(R.string.wi_wu_chancetstorms);
                break;
            case "clear":
                iconString = mContext.getResources().getString(R.string.wi_wu_clear);
                break;
            case "cloudy":
                iconString = mContext.getResources().getString(R.string.wi_wu_cloudy);
                break;
            case "flurries":
                iconString = mContext.getResources().getString(R.string.wi_wu_flurries);
                break;
            case "hazy":
                iconString = mContext.getResources().getString(R.string.wi_wu_hazy);
                break;
            case "mostlycloudy":
                iconString = mContext.getResources().getString(R.string.wi_wu_mostlycloudy);
                break;
            case "mostlysunny":
                iconString = mContext.getResources().getString(R.string.wi_wu_mostlysunny);
                break;
            case "partlycloudy":
                iconString = mContext.getResources().getString(R.string.wi_wu_partlycloudy);
                break;
            case "partlysunny":
                iconString = mContext.getResources().getString(R.string.wi_wu_partlysunny);
                break;
            case "rain":
                iconString = mContext.getResources().getString(R.string.wi_wu_rain);
                break;
            case "sleat":
                iconString = mContext.getResources().getString(R.string.wi_wu_sleat);
                break;
            case "snow":
                iconString = mContext.getResources().getString(R.string.wi_wu_snow);
                break;
            case "sunny":
                iconString = mContext.getResources().getString(R.string.wi_wu_sunny);
                break;
            case "tstorms":
                iconString = mContext.getResources().getString(R.string.wi_wu_tstorms);
                break;
            default:
                iconString = mContext.getResources().getString(R.string.wi_wu_unknown);
                break;
        }
        return iconString;
    }
}
