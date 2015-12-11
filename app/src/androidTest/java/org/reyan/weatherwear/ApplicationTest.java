package org.reyan.weatherwear;

import android.app.Application;
import android.test.ApplicationTestCase;

import org.json.JSONException;
import org.json.JSONObject;
import org.reyan.weatherwear.service.JSONService;

import java.net.URLEncoder;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void testAcquireWeatherById() {
        String key = "/q/zmw:92101.1.99999@San Diego, California, US";
        JSONObject json = null;
        try {
            json = JSONService.getJSONObject(key, JSONService.ACQUIRE_WEATHER_BY_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        assertNotNull(json);
    }

    public void testAcquireWeatherByCoordinate() {
        String key = "lat=32.7105&lon=117.1625";
        JSONObject json = null;
        try {
            json = JSONService.getJSONObject(key, JSONService.ACQUIRE_WEATHER_BY_COORDINATE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        assertNotNull(json);
    }

    public void testSearchCity() {
        String key = URLEncoder.encode("San Diego");
        JSONObject json = null;
        try {
            json = JSONService.getJSONObject(key, JSONService.SEARCH_CITY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        assertNotNull(json);
    }
}