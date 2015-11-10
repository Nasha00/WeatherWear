package org.reyan.weatherwear.service;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by reyan on 11/4/15.
 */
public class JSONService {

    public static final int ACQUIRE_WEATHER_BY_ID = 0;
    public static final int ACQUIRE_WEATHER_BY_COORDINATE = 1;
    public static final int SEARCH_CITY = 2;

    // Acquire weather by id: HEAD + id + TAIL
    private static final String HEAD_URL_FOR_ACQUIRING_WEATHER_BY_ID =
            "http://api.wunderground.com/api/fd8aad1999895464/conditions";
    private static final String TAIL_URL_FOR_ACQUIRING_WEATHER_BY_ID = ".json";

    // Acquire weather by coordinate: HEAD + ordinate + TAIL
    private static final String BASE_URL_FOR_ACQUIRING_WEATHER_BY_COORDINATE =
            "http://api.openweathermap.org/data/2.5/weather?";
    private static final String TAIL_URL_FOR_ACQUIRING_WEATHER_BY_COORDINATE =
            "&appid=2cc497cafac898b5b9aae020a2040dc2";

    // Search city: HEAD + keyword
    private static final String BASE_URL_FOR_SEARCHING_CITY =
            "http://autocomplete.wunderground.com/aq?query=";

    public static JSONObject getJSONObject(String key, int type) throws JSONException {
        HttpURLConnection con = null;
        InputStream is = null;

        try {
            switch (type) {
                case ACQUIRE_WEATHER_BY_ID:
                    con = (HttpURLConnection) new URL(HEAD_URL_FOR_ACQUIRING_WEATHER_BY_ID
                            + key
                            + TAIL_URL_FOR_ACQUIRING_WEATHER_BY_ID).openConnection();
                    break;
                case ACQUIRE_WEATHER_BY_COORDINATE:
                    con = (HttpURLConnection) new URL(BASE_URL_FOR_ACQUIRING_WEATHER_BY_COORDINATE
                            + key
                            + TAIL_URL_FOR_ACQUIRING_WEATHER_BY_COORDINATE).openConnection();
                    break;
                case SEARCH_CITY:
                    con = (HttpURLConnection) new URL(BASE_URL_FOR_SEARCHING_CITY
                            + key).openConnection();
                    break;
                default:
                    return null;
            }
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            StringBuffer buffer = new StringBuffer();
            is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while ((line = br.readLine()) != null) {
                buffer.append(line + "\r\n");
            }
            is.close();
            con.disconnect();

            return new JSONObject(buffer.toString());
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            try { is.close(); } catch(Throwable t) {}
            try { con.disconnect(); } catch(Throwable t) {}
        }

        return null;
    }

}
