package org.reyan.weatherwear.service;

import org.json.JSONObject;
import org.reyan.weatherwear.activity.MainActivity;
import org.reyan.weatherwear.domain.Weather;
import org.reyan.weatherwear.utility.IconURL2IconCodeMap;

/**
 * Created by reyan on 11/10/15.
 */
public class WeatherUpdateService {

    public static boolean updateWeatherById(Weather weather, JSONObject jsonObject) {
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

    public static boolean updateWeatherByCoordinate(Weather weather, JSONObject jsonObject) {
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

            tempF = mainJSON.getDouble("temp") * 1.8 - 459.67;
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
        return IconURL2IconCodeMap.ICON_URL_2_ICON_CODE_MAP.get(array[array.length - 1]);
    }
}
