package org.reyan.weatherwear.service;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.reyan.weatherwear.activity.MainActivity;
import org.reyan.weatherwear.domain.Dressing;
import org.reyan.weatherwear.domain.Weather;

/**
 * Created by reyan on 11/4/15.
 */
public class Algorithm {

    public static void dress(MainActivity mainActivity) {
        // update dressing based on setting and weather
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mainActivity);
        settings.getBoolean("gender", true);
    }

}
