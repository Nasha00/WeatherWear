package org.reyan.weatherwear.activity;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.pwittchen.weathericonview.WeatherIconView;

import org.reyan.weatherwear.R;
import org.reyan.weatherwear.domain.Dressing;
import org.reyan.weatherwear.domain.Weather;
import org.reyan.weatherwear.service.UIUpdateHandler;
import org.reyan.weatherwear.thread.AutoUpdateThread;
import org.reyan.weatherwear.thread.ManualUpdateThread;

public class MainActivity extends AppCompatActivity {

    private static final long MIN_TIME = 1000;
    private static final float MIN_DISTANCE = 1000;

    // registered services
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

    private Handler handler;

    // data associated with main UI
    private Weather weather = new Weather();
    private Dressing dressing = new Dressing();

    // UI references
    private WeatherIconView weatherIconViewWeather;
    private TextView textViewTemperature;
    private TextView textViewCityName;
    private TextView textViewHumidity;
    private TextView textViewPressure;
    private TextView textViewWind;
    private TextView textViewRecommender;

    // getters
    public Handler getHandler() { return handler; }

    public Weather getWeather() { return weather; }
    public Dressing getDressing() { return dressing; }

    public WeatherIconView getWeatherIconViewWeather() { return weatherIconViewWeather; }
    public TextView getTextViewTemperature() { return textViewTemperature; }
    public TextView getTextViewCityName() { return textViewCityName; }
    public TextView getTextViewHumidity() { return textViewHumidity; }
    public TextView getTextViewPressure() { return textViewPressure; }
    public TextView getTextViewWind() { return textViewWind; }
    public TextView getTextViewRecommender() { return textViewRecommender; }

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

        handler = new UIUpdateHandler(this);
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
