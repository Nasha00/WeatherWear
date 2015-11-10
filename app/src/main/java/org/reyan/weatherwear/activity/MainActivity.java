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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

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
                // ui update here
                temperature.setText(String.valueOf(weather.getTemperature()));
                cityName.setText(weather.getCityName());
            }
        }

    };

    private Weather weather = Weather.mock();
    private Dressing dressing = Dressing.mock();

    public Handler getHandler() { return handler; }
    public Weather getWeather() { return weather; }
    public Dressing getDressing() { return dressing; }

    private Button testButton;

    private TextView temperature;
    private TextView cityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // For test
        final SharedPreferences settings =
                PreferenceManager.getDefaultSharedPreferences(this);
        testButton = (Button) findViewById(R.id.testButton);
        testButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean gender = settings.getBoolean("gender", true);
                Integer temperature_preference =
                        Integer.parseInt(settings.getString("temperature_preference", "-1"));
                Integer dressing_style =
                        Integer.parseInt(settings.getString("dressing_style", "-1"));
                Boolean specify_location = settings.getBoolean("specify_location", false);
                String location = settings.getString("location",
                        "/q/zmw:92101.1.99999@San Diego, California, US");
                Boolean temperature = settings.getBoolean("temperature", true);
                Boolean wind_speed = settings.getBoolean("wind_speed", true);

                Log.d("gender", gender.toString());
                Log.d("temperature_preference", temperature_preference.toString());
                Log.d("dressing_style", dressing_style.toString());
                Log.d("specify_location", specify_location.toString());
                Log.d("location", location);
                Log.d("temperature", temperature.toString());
                Log.d("wind_speed", wind_speed.toString());
            }
        });

        temperature = (TextView) findViewById(R.id.temperature);
        cityName = (TextView) findViewById(R.id.cityName);
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
