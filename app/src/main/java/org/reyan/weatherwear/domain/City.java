package org.reyan.weatherwear.domain;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.reyan.weatherwear.service.JSONService;

/**
 * Created by reyan on 11/7/15.
 */
public class City {

    private String id;

    private double latitude;
    private double longitude;

    private String cityName;
    private String countryName;

    public City(String id, double latitude, double longitude,
                String cityName, String countryName) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.cityName = cityName;
        this.countryName = countryName;
    }

    public String getId() { return id; }
    public String getCityName() { return cityName; }
    public String getCountryName() { return countryName; }

    @Override
    public String toString() {
        return cityName + ", " + countryName;
    }
}
