package com.example.weatherapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    RequestQueue requestQueue;
    EditText your_city;
    String jsonURL;
    TextView time, cityName,forecast, country, windSpeed, sunRise, sunSet, temperature, humidity,maxTemp, minTemp, pressure;
    String CITY, CityName, Country, Temperature, Forecast, WindSpeed,Humidity,MaxTemp, MinTemp, Pressure;
    long SunRise, SunSet, updatedAt;
    ImageView search;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityName = findViewById(R.id.cityName);
        time= findViewById(R.id.time);
        country = findViewById(R.id.country);
        forecast = findViewById(R.id.forecast);
        windSpeed = findViewById(R.id.wind_speed);
        humidity = findViewById(R.id.humidity);
        sunRise = findViewById(R.id.sunrises);
        temperature = findViewById(R.id.temperature);
        minTemp = findViewById(R.id.min_temp);
        maxTemp = findViewById(R.id.max_temp);
        pressure =findViewById(R.id.pressure);
        sunSet = findViewById(R.id.sunsets);
        search = findViewById(R.id.search);
        your_city = findViewById(R.id.your_city);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CITY = your_city.getText().toString().trim();
                jsonURL = "https://api.openweathermap.org/data/2.5/weather?q="+CITY+"&units=metric&appid=cecfaf87466e5f6516ba6985d3dfef50";
                weatherInfo();
                your_city.setText("");
            }
        });

    }

    public void weatherInfo(){

        requestQueue = Volley.newRequestQueue(MainActivity.this);

        StringRequest request = new StringRequest(Request.Method.GET, jsonURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog = new ProgressDialog(MainActivity.this);
                        progressDialog.setTitle("Please Wait..");
                        progressDialog.setMessage("Info uploading..");
                        progressDialog.show();

                        try {
                            JSONObject jsonObject = new JSONObject(response);


                            JSONObject wind = (jsonObject.getJSONObject("wind"));
                            WindSpeed = wind.getString("speed");
                            windSpeed.setText(WindSpeed + " m/s");

                            JSONObject weather = jsonObject.getJSONArray("weather").getJSONObject(0);
                            Forecast = weather.getString("description");
                            forecast.setText(Forecast);

                            JSONObject main = jsonObject.getJSONObject("main");
                            Temperature = main.getString("temp");
                            temperature.setText(Temperature + "°C");

                            Pressure = main.getString("pressure");
                            pressure.setText(Pressure);

                            MaxTemp = main.getString("temp_max");
                            maxTemp.setText(MaxTemp);

                            MinTemp = main.getString("temp_min");
                            minTemp.setText(MinTemp);

                            Humidity = main.getString("humidity");
                            humidity.setText(Humidity);

                            JSONObject sys = jsonObject.getJSONObject("sys");
                            SunRise = sys.getLong("sunrise");
                            sunRise.setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(SunRise * 1000)));

                            SunSet = sys.getLong("sunset");
                            sunSet.setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(SunSet * 1000)));

                            Country = sys.getString("country");
                            country.setText(Country);

                            CityName = jsonObject.getString("name");
                            cityName.setText(CityName);

                            updatedAt = jsonObject.getLong("dt");
                            String updatedAtText = "Last Updated at: " + new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(new Date(updatedAt * 1000));
                            time.setText(updatedAtText);


                            progressDialog.cancel();

                        } catch (JSONException e) {

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this.getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(request);
    }
}