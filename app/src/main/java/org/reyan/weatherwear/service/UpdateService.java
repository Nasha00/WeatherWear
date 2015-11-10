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
import org.reyan.weatherwear.domain.Weather;
import org.reyan.weatherwear.utility.IconCodeMap;

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
                isWeatherUpdated = updateWeatherById(mainActivity.getWeather(), json);

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
                isWeatherUpdated = updateWeatherByCoordinate(mainActivity.getWeather(), json);

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
                // update Algorithm
            }
            // ??? synchonized(this) {}
            // update weather based on json
            // update dressing using algorithm
            return true;
        }

        return false;
    }

    private static boolean updateWeatherById(Weather weather, JSONObject jsonObject) {
        JSONObject currentObservation = null;
        JSONObject displayLocation = null;

        String cityName = null;
        String countryName = null;

        String iconCode = null;

        double tempF = 0.0;
        double tempC = 0.0;

        double windDegrees = 0.0;
        double windSpeedKPH = 0.0;
        double windSpeedMPH = 0.0;

        double humidity = 0.0;
        double pressure = 0.0;

        try {
            currentObservation = jsonObject.getJSONObject("current_observation");
            displayLocation = currentObservation.getJSONObject("display_location");

            cityName = displayLocation.getString("city");
            countryName = displayLocation.getString("country");

            iconCode = convertIconURL2IconCode(currentObservation.getString("icon_url"));

            tempF = currentObservation.getDouble("temp_f");
            tempC = currentObservation.getDouble("temp_c");

            windDegrees = currentObservation.getDouble("wind_degrees");
            windSpeedKPH = currentObservation.getDouble("wind_kph");
            windSpeedMPH = currentObservation.getDouble("wind_mph");

            String relative_humidity = currentObservation.getString("relative_humidity");
            humidity = Double.parseDouble(relative_humidity.split("%")[0]);
            pressure = currentObservation.getDouble("pressure_mb") / 10;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        weather.setCityName(cityName);
        weather.setCountryName(countryName);
        weather.setIconCode(iconCode);
        weather.setTempF(tempF);
        weather.setTempC(tempC);
        weather.setWindDegrees(windDegrees);
        weather.setWindSpeedKPH(windSpeedKPH);
        weather.setWindSpeedMPH(windSpeedMPH);
        weather.setHumidity(humidity);
        weather.setPressure(pressure);

        return true;
    }

    private static boolean updateWeatherByCoordinate(Weather weather, JSONObject jsonObject) {
        JSONObject weatherJSON = null;
        JSONObject mainJSON = null;
        JSONObject windJSON = null;
        JSONObject sysJSON = null;

        String cityName = null;
        String countryName = null;

        String iconCode = null;

        double tempF = 0.0;
        double tempC = 0.0;

        double windDegrees = 0.0;
        double windSpeedKPH = 0.0;
        double windSpeedMPH = 0.0;

        double humidity = 0.0;
        double pressure = 0.0;

        try {
            weatherJSON = jsonObject.getJSONArray("weather").getJSONObject(0);
            mainJSON = jsonObject.getJSONObject("main");
            windJSON = jsonObject.getJSONObject("wind");
            sysJSON = jsonObject.getJSONObject("sys");

            cityName = jsonObject.getString("name");
            countryName = sysJSON.getString("country");

            iconCode = weatherJSON.getString("icon");

            tempF = mainJSON.getDouble("temp") * 1.4 - 459.67;
            tempC = mainJSON.getDouble("temp") - 273.15;

            windDegrees = windJSON.getDouble("deg");
            windSpeedKPH = windJSON.getDouble("speed") * 3.6;
            windSpeedMPH = windJSON.getDouble("speed") / 0.44704;

            humidity = mainJSON.getDouble("humidity");
            pressure = mainJSON.getDouble("pressure") / 10;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        weather.setCityName(cityName);
        weather.setCountryName(countryName);
        weather.setIconCode(iconCode);
        weather.setTempF(tempF);
        weather.setTempC(tempC);
        weather.setWindDegrees(windDegrees);
        weather.setWindSpeedKPH(windSpeedKPH);
        weather.setWindSpeedMPH(windSpeedMPH);
        weather.setHumidity(humidity);
        weather.setPressure(pressure);

        return true;
    }

    private static String convertIconURL2IconCode(String iconURL) {
        // Sample: "http://icons.wxug.com/i/c/k/nt_partlycloudy.gif"
        String[] array = iconURL.split("/");
        return IconCodeMap.iconCodeMap.get(array[array.length - 1]);
    }

}
