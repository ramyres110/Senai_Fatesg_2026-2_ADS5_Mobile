package com.ramyres.appsampleview;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Handler;
import android.os.Looper;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WeatherService {

    public interface WeatherCallback {
        void onSuccess(WeatherData data);
        void onError(Exception e);
    }

    public static class WeatherData {
        public final double temperature;
        public final double windSpeed;
        public final int weatherCode;
        public final String cityName;

        public WeatherData(double temperature, double windSpeed, int weatherCode, String cityName) {
            this.temperature = temperature;
            this.windSpeed = windSpeed;
            this.weatherCode = weatherCode;
            this.cityName = cityName;
        }
    }

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public void fetchWeather(Context context, double lat, double lon, WeatherCallback callback) {
        executor.execute(() -> {
            try {
                // 1. Fetch Weather Data
                String urlString = String.format(Locale.US,
                        "https://api.open-meteo.com/v1/forecast?latitude=%f&longitude=%f&current_weather=true",
                        lat, lon);
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JSONObject json = new JSONObject(response.toString());
                JSONObject current = json.getJSONObject("current_weather");

                // 2. Resolve City Name using Geocoder
                String cityName = "Unknown Location";
                try {
                    Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                    List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
                    if (addresses != null && !addresses.isEmpty()) {
                        Address address = addresses.get(0);
                        cityName = address.getLocality();
                        if (cityName == null) {
                            cityName = address.getSubAdminArea();
                        }
                        if (cityName == null) {
                            cityName = address.getAdminArea();
                        }
                    }
                } catch (Exception ge) {
                    // Ignore geocoding errors, fallback to "Unknown"
                }

                WeatherData data = new WeatherData(
                        current.getDouble("temperature"),
                        current.getDouble("windspeed"),
                        current.getInt("weathercode"),
                        cityName
                );

                mainHandler.post(() -> callback.onSuccess(data));

            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }

    public static int getWeatherIconResource(int code) {
        if (code == 0) return android.R.drawable.ic_menu_day;
        if (code <= 3) return android.R.drawable.ic_menu_report_image;
        if (code >= 51 && code <= 67) return android.R.drawable.ic_menu_view;
        if (code >= 95) return android.R.drawable.ic_delete;
        return android.R.drawable.ic_menu_help;
    }
}