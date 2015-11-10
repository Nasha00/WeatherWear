package org.reyan.weatherwear.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.github.pwittchen.weathericonview.WeatherIconView;

import org.reyan.weatherwear.R;
import org.reyan.weatherwear.domain.Dressing;
import org.reyan.weatherwear.domain.Weather;
import org.reyan.weatherwear.thread.AutoUpdateThread;
import org.reyan.weatherwear.thread.ManualUpdateThread;

public class MainActivity extends AppCompatActivity {

    private static final long MIN_TIME = 1000;
    private static final float MIN_DISTANCE = 1000;

    private LocationManager locationManager;
    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            new ManualUpdateThread(MainActivity.this).start();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    private AutoUpdateThread autoUpdateThread;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                /*
                 For weather icon, later we can combine wind and temperature
                 to choose a better one
                  */
                String iconCode = weather.getIconCode();
                switch (iconCode) {
                    case "01d":
                        weatherIconViewWeather.setIconResource(getString(R.string.wi_day_sunny));
                        break;
                    case "01n":
                        weatherIconViewWeather.setIconResource(getString(R.string.wi_night_clear));
                        break;
                    case "02d":
                        weatherIconViewWeather.setIconResource(getString(R.string.wi_day_cloudy));
                        break;
                    case "02n":
                        weatherIconViewWeather.setIconResource(getString(R.string.wi_night_cloudy));
                        break;
                    case "03d":
                        weatherIconViewWeather.setIconResource(getString(R.string.wi_day_cloudy_high));
                        break;
                    case "03n":
                        weatherIconViewWeather.setIconResource(getString(R.string.wi_night_cloudy_high));
                        break;
                    case "04d":
                        weatherIconViewWeather.setIconResource(getString(R.string.wi_day_cloudy_windy));
                        break;
                    case "04n":
                        weatherIconViewWeather.setIconResource(getString(R.string.wi_night_cloudy_windy));
                        break;
                    case "09d":
                        weatherIconViewWeather.setIconResource(getString(R.string.wi_day_showers));
                        break;
                    case "09n":
                        weatherIconViewWeather.setIconResource(getString(R.string.wi_night_showers));
                        break;
                    case "10d":
                        weatherIconViewWeather.setIconResource(getString(R.string.wi_day_rain));
                        break;
                    case "10n":
                        weatherIconViewWeather.setIconResource(getString(R.string.wi_night_rain));
                        break;
                    case "11d":
                        weatherIconViewWeather.setIconResource(getString(R.string.wi_day_thunderstorm));
                        break;
                    case "11n":
                        weatherIconViewWeather.setIconResource(getString(R.string.wi_night_thunderstorm));
                        break;
                    case "13d":
                        weatherIconViewWeather.setIconResource(getString(R.string.wi_day_snow));
                        break;
                    case "13n":
                        weatherIconViewWeather.setIconResource(getString(R.string.wi_night_snow));
                        break;
                    case "50d":
                        weatherIconViewWeather.setIconResource(getString(R.string.wi_day_fog));
                        break;
                    case "50n":
                        weatherIconViewWeather.setIconResource(getString(R.string.wi_night_fog));
                        break;
                    default:
                        break;
                }
                SharedPreferences settings =
                        PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
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
            }
        }

    };

    private Weather weather = new Weather();
    private Dressing dressing = new Dressing();

    public Handler getHandler() { return handler; }
    public Weather getWeather() { return weather; }
    public Dressing getDressing() { return dressing; }

    private WeatherIconView weatherIconViewWeather;
    private TextView textViewTemperature;
    private TextView textViewCityName;
    private TextView textViewHumidity;
    private TextView textViewPressure;
    private TextView textViewWind;
    private TextView textViewRecommender;

    private Button testButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        weatherIconViewWeather = (WeatherIconView) findViewById(R.id.weather);
        textViewTemperature = (TextView) findViewById(R.id.temperature);
        textViewCityName = (TextView) findViewById(R.id.cityName);
        textViewHumidity = (TextView) findViewById(R.id.humidity);
        textViewPressure = (TextView) findViewById(R.id.pressure);
        textViewWind = (TextView) findViewById(R.id.wind);
        textViewRecommender = (TextView) findViewById(R.id.recommender);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        autoUpdateThread = new AutoUpdateThread(this);
        autoUpdateThread.start();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        locationManager.requestLocationUpdates(provider, MIN_TIME, MIN_DISTANCE, locationListener);
    }

    @Override
    protected void onPause() {
        super.onPause();

        autoUpdateThread.close();
        locationManager.removeUpdates(locationListener);
    }

}
