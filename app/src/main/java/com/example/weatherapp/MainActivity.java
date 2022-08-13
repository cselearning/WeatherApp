package com.example.weatherapp;

import android.app.ProgressDialog;
import android.os.Bundle;
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
    String jsonURL = "https://api.openweathermap.org/data/2.5/weather?lat=27.900383&lon=78.072281&units=metric&appid=cecfaf87466e5f6516ba6985d3dfef50";
    TextView cityName, temperature, weatherR, windSpeed, sunRise, sunSet;
    String cityname, temp, weatherr, windspeed;
    long sunrise, sunset;
    ImageView imageView;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityName = findViewById(R.id.cityName);
        temperature = findViewById(R.id.temperature);
        weatherR = findViewById(R.id.weather);
        windSpeed = findViewById(R.id.windSpeed);
        sunRise = findViewById(R.id.sunRise);
        sunSet = findViewById(R.id.sunSet);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait..");
        progressDialog.setMessage("Info uploading..");
        progressDialog.show();

        requestQueue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.GET, jsonURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);


                            JSONObject wind = (jsonObject.getJSONObject("wind"));
                            windspeed = wind.getString("speed");
                            windSpeed.setText(windspeed + " m/s");

                            JSONObject weather = jsonObject.getJSONArray("weather").getJSONObject(0);
                            weatherr = weather.getString("description");
                            weatherR.setText(weatherr);

                            JSONObject main = jsonObject.getJSONObject("main");
                            temp = main.getString("temp");
                            temperature.setText(temp + "°C");


                            JSONObject sys = jsonObject.getJSONObject("sys");
                            sunrise = sys.getLong("sunrise");
                            sunRise.setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunrise * 1000)));
                            sunset = sys.getLong("sunset");
                            sunSet.setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunset * 1000)));

                            cityname = jsonObject.getString("name");
                            cityName.setText(cityname + " , " + sys.getString("country"));

                            progressDialog.cancel();


                            String iconcode = weather.getString("icon").trim();
                            String url = "http://openweathermap.org/img/w/" + iconcode + ".png";


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