package com.imobile3.taylor.imobile3_weather_app;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created an HTTPConnection from URL String
 * Returns a JSONObject containing requested data
 *
 * Issues: If no internet is enabled will throw exception. UnknownHostException probably.
 * Solution: Send exception back to calling method and present warning and option to turn on internet.
 *
 * @author Taylor Parrish
 * @since 8/23/2016
 */
public class HttpJSONRequest {

    HttpURLConnection urlConnection;

    //Returns JSONObject response via HTTP
    public JSONObject getJSONFromUrl(String urlIn) throws IOException, JSONException {
        StringBuilder result = new StringBuilder();

        URL url;
        url = new URL(urlIn);
        urlConnection = (HttpURLConnection) url.openConnection();
        InputStream in = new BufferedInputStream(urlConnection.getInputStream());

        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        urlConnection.disconnect();

        return new JSONObject(result.toString());
    }
}
