package com.imobile3.taylor.imobile3_weather_app;

import android.content.Context;
import android.support.annotation.NonNull;

import com.imobile3.taylor.imobile3_weather_app.models.CurrentWeatherForecast;
import com.imobile3.taylor.imobile3_weather_app.models.HourlyWeatherForecast;
import com.imobile3.taylor.imobile3_weather_app.models.Location;
import com.imobile3.taylor.imobile3_weather_app.utilities.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by taylorp on 7/18/2017.
 *
 * @author Taylor Parrish
 * @since 7/18/2017
 */

public class WeatherDataParser {
    private Context mContext;
    Location mLocation;

    public WeatherDataParser(Location location, Context context) {
        this.mLocation = location;
        this.mContext = context;
    }

    public Location parseJSONWeatherData(HashMap<String, JSONObject> weatherData) {
        JSONObject hourly10DAYWeatherForecastData = weatherData.get("hourly10day");
        JSONObject currentWeatherForecastData = weatherData.get("conditions");
        JSONObject astronomyWeatherForecastData = weatherData.get("astronomy");

        try {
            HashMap<String, ArrayList<HourlyWeatherForecast>> hourlyWeatherForecasts =
                    parseHourlyForecastData(hourly10DAYWeatherForecastData);
            CurrentWeatherForecast currentWeatherForecast
                    = parseCurrentWeatherForecast(currentWeatherForecastData, astronomyWeatherForecastData);

            if(hourlyWeatherForecasts != null) {
                mLocation.setHourlyWeatherForecastsToday(hourlyWeatherForecasts.get("today"));
                mLocation.setHourlyWeatherForecastsTomorrow(hourlyWeatherForecasts.get("tomorrow"));
                mLocation.setHourlyWeatherForecastsLater(hourlyWeatherForecasts.get("later"));
            }
            if(currentWeatherForecast != null) {
                mLocation.setCurrentWeatherForecast(currentWeatherForecast);
            }
        }
        catch (ParseException | JSONException e) {
            e.printStackTrace();
        }
        return mLocation;
    }

    private HashMap<String, ArrayList<HourlyWeatherForecast>> parseHourlyForecastData(JSONObject hourly10DAYWeatherForecastData) throws JSONException, ParseException {
        HashMap<String, ArrayList<HourlyWeatherForecast>> hourlyWeatherForecasts = new HashMap<>();
        ArrayList<HourlyWeatherForecast> hourlyWeatherForecastsToday = new ArrayList<>();
        ArrayList<HourlyWeatherForecast> hourlyWeatherForecastsTomorrow = new ArrayList<>();
        ArrayList<HourlyWeatherForecast> hourlyWeatherForecastsLater = new ArrayList<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a z  MMMMM dd, yyyy", Locale.US);
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

        CurrentWeatherForecast currentWeatherForecast =
                new CurrentWeatherForecast(weatherIcon, weatherDescr, tempText, tempF,
                        tempC, humidity, windText, windDir,
                        windDegree, pressureMB, pressureIN, windMPH, windGustMPH,
                        windKPH, windGustKPH, sunriseTime, sunsetTime);

        return currentWeatherForecast;
    }

    private String getIcon(String icon) {
        String iconString;

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