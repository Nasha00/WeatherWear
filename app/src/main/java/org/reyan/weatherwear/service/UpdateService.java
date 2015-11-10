package org.reyan.weatherwear.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.reyan.weatherwear.activity.MainActivity;

/**
 * Created by reyan on 11/4/15.
 */
public class UpdateService {

    public static boolean update(MainActivity mainActivity) {
        SharedPreferences settings =
                PreferenceManager.getDefaultSharedPreferences(mainActivity);
        Boolean specify_location = settings.getBoolean("specify_location", false);

        String key = null;
        if (specify_location) {
            key = settings.getString("location",
                    "/q/zmw:92101.1.99999@San Diego, California, US").split("@", 2)[0];
        } else {
            LocationManager locationManager =
                    (LocationManager) mainActivity.getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, true);
            Location location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                key = "lat=" + location.getLatitude() + "&lon=" + location.getLongitude();
            }
        }

        if (key == null) {
            return false;
        }
        //TODO
            /* several steps:
            1. get json object, if null, not update
            2. else update weather based on json
            3. and call algorithm to update dressing
             */
        // need synchronized here for domain?
        JSONObject json = null;
        try {
            if (specify_location) {
                json = JSONService.getJSONObject(key, JSONService.ACQUIRE_WEATHER_BY_ID);
            } else {
                json = JSONService.getJSONObject(key, JSONService.ACQUIRE_WEATHER_BY_COORDINATE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (json != null) {
            Log.d("UPDATE", "running");

            boolean isWeatherUpdated = false;
            if (specify_location) {
                isWeatherUpdated =
                        WeatherUpdateService.updateWeatherById(mainActivity.getWeather(), json);

                Log.d("Update Weather", mainActivity.getWeather().getCityName());
                Log.d("Update Weather", mainActivity.getWeather().getCountryName());
                Log.d("Update Weather", mainActivity.getWeather().getIconCode());
                Log.d("Update Weather", String.valueOf(mainActivity.getWeather().getTempF()));
                Log.d("Update Weather", String.valueOf(mainActivity.getWeather().getTempC()));
                Log.d("Update Weather", String.valueOf(mainActivity.getWeather().getWindDegrees()));
                Log.d("Update Weather", String.valueOf(mainActivity.getWeather().getWindSpeedKPH()));
                Log.d("Update Weather", String.valueOf(mainActivity.getWeather().getWindSpeedMPH()));
                Log.d("Update Weather", String.valueOf(mainActivity.getWeather().getHumidity()));
                Log.d("Update Weather", String.valueOf(mainActivity.getWeather().getPressure()));
            } else {
                isWeatherUpdated =
                        WeatherUpdateService.updateWeatherByCoordinate(mainActivity.getWeather(), json);

                Log.d("Update Weather", mainActivity.getWeather().getCityName());
                Log.d("Update Weather", mainActivity.getWeather().getCountryName());
                Log.d("Update Weather", mainActivity.getWeather().getIconCode());
                Log.d("Update Weather", String.valueOf(mainActivity.getWeather().getTempF()));
                Log.d("Update Weather", String.valueOf(mainActivity.getWeather().getTempC()));
                Log.d("Update Weather", String.valueOf(mainActivity.getWeather().getWindDegrees()));
                Log.d("Update Weather", String.valueOf(mainActivity.getWeather().getWindSpeedKPH()));
                Log.d("Update Weather", String.valueOf(mainActivity.getWeather().getWindSpeedMPH()));
                Log.d("Update Weather", String.valueOf(mainActivity.getWeather().getHumidity()));
                Log.d("Update Weather", String.valueOf(mainActivity.getWeather().getPressure()));
            }

            if (isWeatherUpdated) {
                DressingUpdateService.updateDressing(mainActivity);
                Log.d("dressing test", mainActivity.getDressing().toString());
            }
            // ??? synchonized(this) {}
            // update weather based on json
            // update dressing using algorithm
            return true;
        }

        return false;
    }

}
