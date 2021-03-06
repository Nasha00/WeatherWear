package org.reyan.weatherwear.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.pwittchen.weathericonview.WeatherIconView;
import com.github.xizzhu.simpletooltip.ToolTip;
import com.github.xizzhu.simpletooltip.ToolTipView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.reyan.weatherwear.R;
import org.reyan.weatherwear.domain.Dressing;
import org.reyan.weatherwear.domain.Weather;
import org.reyan.weatherwear.service.UIUpdateHandler;
import org.reyan.weatherwear.thread.AutoUpdateThread;
import org.reyan.weatherwear.thread.ManualUpdateThread;

public class MainActivity extends AppCompatActivity
        implements ConnectionCallbacks, OnConnectionFailedListener, LocationListener {

    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 300000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 60000;

    // When location setting is local and google map service is firstly connected,
    // both autoUpdateThread and manualUpdateThread will execute(needless).
    // By using this flag can we prevent manualUpdateThread to execute
    boolean isFirstConnected;

    // registered services
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;

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

    private ImageButton imageButtonClothes;
    private ImageButton imageButtonTrousers;
    private ImageButton imageButtonHead;
    private ImageButton imageButtonFeet;

    private ToolTipView toolTipViewClothes;
    private ToolTipView toolTipViewTrousers;
    private ToolTipView toolTipViewHead;
    private ToolTipView toolTipViewFeet;

    // getters
    public GoogleApiClient getGoogleApiClient() { return googleApiClient; }
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
        imageButtonClothes = (ImageButton) findViewById(R.id.clothes);
        imageButtonTrousers  = (ImageButton) findViewById(R.id.trousers);
        imageButtonHead = (ImageButton) findViewById(R.id.head);
        imageButtonFeet = (ImageButton) findViewById(R.id.feet);

        handler = new UIUpdateHandler(this);

        buildGoogleApiClient();
    }

    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }

    protected void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                googleApiClient, locationRequest, this);
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                googleApiClient, this);
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
    protected void onStart() {
        super.onStart();

        setImageButtonOnClickListener();

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        boolean specifyLocation = settings.getBoolean("specify_location", false);

        if (!specifyLocation) {
            isFirstConnected = true;
            googleApiClient.connect();
        } else {
            autoUpdateThread = new AutoUpdateThread(this);
            autoUpdateThread.start();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (autoUpdateThread != null) {
            autoUpdateThread.close();
        }

        if (googleApiClient.isConnected()) {
            stopLocationUpdates();
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d("Google Play Services", "onConnected is called");

        startLocationUpdates();
        autoUpdateThread = new AutoUpdateThread(this);
        autoUpdateThread.start();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("Google Play Services", "onConnectionSuspended is called");

        stopLocationUpdates();
        autoUpdateThread.close();
        googleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (isFirstConnected) {
            isFirstConnected = false;
        } else {
            new ManualUpdateThread(this).start();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("Google Play Services", "onConnectionFailed is called");

        // need to handler
        // refers to:
        // https://developers.google.com/android/guides/api-client#handle_connection_failures
    }

    private void setImageButtonOnClickListener() {
        imageButtonClothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toolTipViewClothes != null) {
                    toolTipViewClothes.remove();
                }
                String upper = dressing.getUpper();
                if ("".equals(upper)) {
                    upper = "No clothes";
                }
                ToolTip toolTip = new ToolTip.Builder()
                        .withText(upper)
                        .withTextSize(50)
                        .withTextColor(Color.MAGENTA)
                        .withBackgroundColor(Color.CYAN)
                        .withPadding(15, 15, 10, 10)
                        .build();
                toolTipViewClothes = new ToolTipView.Builder(MainActivity.this)
                        .withAnchor(v)
                        .withToolTip(toolTip)
                        .withGravity(Gravity.LEFT)
                        .build();
                toolTipViewClothes.show();
            }
        });

        imageButtonTrousers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toolTipViewTrousers != null) {
                    toolTipViewTrousers.remove();
                }
                String lower = dressing.getLower();
                if ("".equals(lower)) {
                    lower = "No trousers";
                }
                ToolTip toolTip = new ToolTip.Builder()
                        .withText(lower)
                        .withTextSize(50)
                        .withTextColor(Color.RED)
                        .withBackgroundColor(Color.YELLOW)
                        .withPadding(15, 15, 10, 10)
                        .build();
                toolTipViewTrousers = new ToolTipView.Builder(MainActivity.this)
                        .withAnchor(v)
                        .withToolTip(toolTip)
                        .withGravity(Gravity.LEFT)
                        .build();
                toolTipViewTrousers.show();
            }
        });

        imageButtonHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toolTipViewHead != null) {
                    toolTipViewHead.remove();
                }
                String hat = dressing.getHat();
                if ("".equals(hat)) {
                    hat = "No hat";
                }
                ToolTip toolTip = new ToolTip.Builder()
                        .withText(hat)
                        .withTextSize(50)
                        .withTextColor(Color.YELLOW)
                        .withBackgroundColor(Color.RED)
                        .withPadding(15, 15, 10, 10)
                        .build();
                toolTipViewHead = new ToolTipView.Builder(MainActivity.this)
                        .withAnchor(v)
                        .withToolTip(toolTip)
                        .withGravity(Gravity.TOP)
                        .build();
                toolTipViewHead.show();
            }
        });

        imageButtonFeet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toolTipViewFeet != null) {
                    toolTipViewFeet.remove();
                }
                String shoe = dressing.getShoes();
                if ("".equals(shoe)) {
                    shoe = "No shoe";
                }
                ToolTip toolTip = new ToolTip.Builder()
                        .withText(shoe)
                        .withTextSize(50)
                        .withTextColor(Color.BLUE)
                        .withBackgroundColor(Color.GREEN)
                        .withPadding(15, 15, 10, 10)
                        .build();
                toolTipViewFeet = new ToolTipView.Builder(MainActivity.this)
                        .withAnchor(v)
                        .withToolTip(toolTip)
                        .withGravity(Gravity.BOTTOM)
                        .build();
                toolTipViewFeet.show();
            }
        });
    }
}
