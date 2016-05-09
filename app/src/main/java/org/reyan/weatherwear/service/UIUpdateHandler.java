package org.reyan.weatherwear.service;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.widget.TextView;

import com.github.pwittchen.weathericonview.WeatherIconView;

import org.reyan.weatherwear.R;
import org.reyan.weatherwear.activity.MainActivity;
import org.reyan.weatherwear.domain.Dressing;
import org.reyan.weatherwear.domain.Weather;

/**
 * Created by reyan on 11/10/15.
 */
public class UIUpdateHandler extends Handler {

    public static final int STATUS_SUCCESS = 0;
    public static final int STATUS_NETWORK_PROBLEM = 1;

    private MainActivity mainActivity;

    private Weather weather;
    private Dressing dressing;

    private WeatherIconView weatherIconViewWeather;
    private TextView textViewTemperature;
    private TextView textViewCityName;
    private TextView textViewHumidity;
    private TextView textViewPressure;
    private TextView textViewWind;
    private TextView textViewRecommender;

    public UIUpdateHandler(MainActivity mainActivity) {
        this.mainActivity = mainActivity;

        weather = mainActivity.getWeather();
        dressing = mainActivity.getDressing();

        weatherIconViewWeather = mainActivity.getWeatherIconViewWeather();
        textViewTemperature = mainActivity.getTextViewTemperature();
        textViewCityName = mainActivity.getTextViewCityName();
        textViewHumidity = mainActivity.getTextViewHumidity();
        textViewPressure = mainActivity.getTextViewPressure();
        textViewWind = mainActivity.getTextViewWind();
        textViewRecommender = mainActivity.getTextViewRecommender();
    }

    @Override
    public void handleMessage(Message msg) {
        if (msg.what == STATUS_SUCCESS) {
            /*
                For weather icon, later we can combine wind and temperature
                to choose a better one
            */
            String iconCode = mainActivity.getWeather().getIconCode();
            switch (iconCode) {
                case "01d":
                    weatherIconViewWeather.setIconResource(
                            mainActivity.getString(R.string.wi_day_sunny));
                    break;
                case "01n":
                    weatherIconViewWeather.setIconResource(
                            mainActivity.getString(R.string.wi_night_clear));
                    break;
                case "02d":
                    weatherIconViewWeather.setIconResource(
                            mainActivity.getString(R.string.wi_day_cloudy));
                    break;
                case "02n":
                    weatherIconViewWeather.setIconResource(
                            mainActivity.getString(R.string.wi_night_cloudy));
                    break;
                case "03d":
                    weatherIconViewWeather.setIconResource(
                            mainActivity.getString(R.string.wi_day_cloudy_high));
                    break;
                case "03n":
                    weatherIconViewWeather.setIconResource(
                            mainActivity.getString(R.string.wi_night_cloudy_high));
                    break;
                case "04d":
                    weatherIconViewWeather.setIconResource(
                            mainActivity.getString(R.string.wi_day_cloudy_windy));
                    break;
                case "04n":
                    weatherIconViewWeather.setIconResource(
                            mainActivity.getString(R.string.wi_night_cloudy_windy));
                    break;
                case "09d":
                    weatherIconViewWeather.setIconResource(
                            mainActivity.getString(R.string.wi_day_showers));
                    break;
                case "09n":
                    weatherIconViewWeather.setIconResource(
                            mainActivity.getString(R.string.wi_night_showers));
                    break;
                case "10d":
                    weatherIconViewWeather.setIconResource(
                            mainActivity.getString(R.string.wi_day_rain));
                    break;
                case "10n":
                    weatherIconViewWeather.setIconResource(
                            mainActivity.getString(R.string.wi_night_rain));
                    break;
                case "11d":
                    weatherIconViewWeather.setIconResource(
                            mainActivity.getString(R.string.wi_day_thunderstorm));
                    break;
                case "11n":
                    weatherIconViewWeather.setIconResource(
                            mainActivity.getString(R.string.wi_night_thunderstorm));
                    break;
                case "13d":
                    weatherIconViewWeather.setIconResource(
                            mainActivity.getString(R.string.wi_day_snow));
                    break;
                case "13n":
                    weatherIconViewWeather.setIconResource(
                            mainActivity.getString(R.string.wi_night_snow));
                    break;
                case "50d":
                    weatherIconViewWeather.setIconResource(
                            mainActivity.getString(R.string.wi_day_fog));
                    break;
                case "50n":
                    weatherIconViewWeather.setIconResource(
                            mainActivity.getString(R.string.wi_night_fog));
                    break;
                default:
                    break;
            }
            SharedPreferences settings =
                    PreferenceManager.getDefaultSharedPreferences(mainActivity);
            boolean temperature = settings.getBoolean("temperature", true);
            boolean wind_speed = settings.getBoolean("wind_speed", true);
            if (temperature) {
                textViewTemperature.setText(
                        String.format("%.1f", weather.getTempF()) + "\u2109");
            } else {
                textViewTemperature.setText(
                        String.format("%.1f", weather.getTempC()) + "\u2103");
            }
            textViewCityName.setText(weather.getCityName());
            textViewHumidity.setText(String.format("%.0f", weather.getHumidity()) + "%");
            textViewPressure.setText(String.format("%.2f", weather.getPressure()) + "kPa");
            if (wind_speed) {
                textViewWind.setText(String.format("%.2f", weather.getWindSpeedMPH()) + "MPH");
            } else {
                textViewWind.setText(String.format("%.2f", weather.getWindSpeedKPH()) + "KPH");
            }
            textViewRecommender.setText(dressing.toString());

        } else if (msg.what == STATUS_NETWORK_PROBLEM) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
            builder.setMessage("Please go to settings to ensure your network is turned on")
                    .setTitle("Network Problem");
            builder.setPositiveButton("Go Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mainActivity.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.create().show();
        }
    }
}
