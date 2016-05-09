package org.reyan.weatherwear.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;
import org.reyan.weatherwear.activity.MainActivity;

/**
 * Created by reyan on 11/4/15.
 */
public class UpdateService {

    public static void update(MainActivity mainActivity) {
        SharedPreferences settings =
                PreferenceManager.getDefaultSharedPreferences(mainActivity);
        boolean specifyLocation = settings.getBoolean("specify_location", false);

        String key = null;
        if (specifyLocation) {
            key = settings.getString("location",
                    "/q/zmw:92101.1.99999@San Diego, California, US").split("@", 2)[0];
        } else {
            Location location = LocationServices
                    .FusedLocationApi
                    .getLastLocation(mainActivity.getGoogleApiClient());
            if (location != null) {
                key = "lat=" + location.getLatitude() + "&lon=" + location.getLongitude();
            }
        }

        if (key == null) {
            // Most likely to the be network problem
            mainActivity.getHandler().sendEmptyMessage(UIUpdateHandler.STATUS_NETWORK_PROBLEM);
        } else {
            // Get JSONObject
            JSONObject json = null;
            try {
                if (specifyLocation) {
                    json = JSONService.getJSONObject(key, JSONService.ACQUIRE_WEATHER_BY_ID);
                } else {
                    json = JSONService.getJSONObject(key, JSONService.ACQUIRE_WEATHER_BY_COORDINATE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Update Weather Object
            if (json == null) {
                // Most likely to be the network problem
                mainActivity.getHandler().sendEmptyMessage(UIUpdateHandler.STATUS_NETWORK_PROBLEM);
            } else {
                boolean isWeatherUpdated;
                if (specifyLocation) {
                    isWeatherUpdated = WeatherUpdateService
                            .updateWeatherById(mainActivity.getWeather(), json);

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
                    isWeatherUpdated = WeatherUpdateService
                            .updateWeatherByCoordinate(mainActivity.getWeather(), json);

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

                    mainActivity.getHandler().sendEmptyMessage(UIUpdateHandler.STATUS_SUCCESS);
                }
                // isWeatherUpdated == false:
                // JSONObject Problem (very rare, ignored)
            }
        }
    }

}
