package com.ramyres.appsampleview;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    private TaskAdapter adapter;
    private TextView textOpenCount;
    private TextView textDoneCount;

    // Weather UI
    private ImageView imageWeatherIcon;
    private TextView textTemperature;
    private TextView textCity;
    private TextView textWind;
    private TextView textWeatherStatus;

    private WeatherService weatherService;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textOpenCount = findViewById(R.id.text_open_count);
        textDoneCount = findViewById(R.id.text_done_count);

        // Weather UI init
        imageWeatherIcon = findViewById(R.id.image_weather_icon);
        textTemperature = findViewById(R.id.text_temperature);
        textCity = findViewById(R.id.text_city);
        textWind = findViewById(R.id.text_wind);
        textWeatherStatus = findViewById(R.id.text_weather_status);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TaskAdapter(this, TaskRepository.getInstance().getTasks(), this::updateStatistics);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab_add);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
            startActivity(intent);
        });

        weatherService = new WeatherService();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        updateStatistics();
        checkLocationPermission();
    }

    private void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getLocationAndFetchWeather();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocationAndFetchWeather();
            } else {
                textWeatherStatus.setText("Permission Denied");
            }
        }
    }

    private void getLocationAndFetchWeather() {
        //lat -16.6868491
        //lon -49.2707899
        fetchWeather(-16.6868491,-49.2707899);

        try {
            // Try Network Provider first (usually faster/better indoors)
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            
            // If null, try GPS
            if (location == null) {
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }

            if (location != null) {
                fetchWeather(location.getLatitude(), location.getLongitude());
            } else {
                // Request single update if no last known location
                String provider = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ? 
                        LocationManager.NETWORK_PROVIDER : LocationManager.GPS_PROVIDER;
                
                locationManager.requestLocationUpdates(provider, 0, 0, new LocationListener() {
                    @Override
                    public void onLocationChanged(@NonNull Location loc) {
                        fetchWeather(loc.getLatitude(), loc.getLongitude());
                        locationManager.removeUpdates(this);
                    }
                    @Override public void onStatusChanged(String provider, int status, Bundle extras) {}
                    @Override public void onProviderEnabled(@NonNull String provider) {}
                    @Override public void onProviderDisabled(@NonNull String provider) {}
                });
            }
        } catch (SecurityException e) {
            textWeatherStatus.setText("Security Error");
        }
    }

    private void fetchWeather(double lat, double lon) {
        textWeatherStatus.setText("Loading...");
        weatherService.fetchWeather(this, lat, lon, new WeatherService.WeatherCallback() {
            @Override
            public void onSuccess(WeatherService.WeatherData data) {
                textTemperature.setText(String.format(Locale.getDefault(), "%.1f°C", data.temperature));
                textCity.setText(data.cityName != null ? data.cityName : "Unknown City");
                textWind.setText(String.format(Locale.getDefault(), "Wind: %.1f km/h", data.windSpeed));
                imageWeatherIcon.setImageResource(WeatherService.getWeatherIconResource(data.weatherCode));
                textWeatherStatus.setText("Updated");
            }

            @Override
            public void onError(Exception e) {
                textWeatherStatus.setText("Error");
                Toast.makeText(MainActivity.this, "Weather error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateStatistics() {
        List<Task> tasks = TaskRepository.getInstance().getTasks();
        int openTasks = 0;
        int doneTasks = 0;

        for (Task task : tasks) {
            if (task.isCompleted()) {
                doneTasks++;
            } else {
                openTasks++;
            }
        }

        textOpenCount.setText(String.valueOf(openTasks));
        textDoneCount.setText(String.valueOf(doneTasks));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
            updateStatistics();
        }
    }
}