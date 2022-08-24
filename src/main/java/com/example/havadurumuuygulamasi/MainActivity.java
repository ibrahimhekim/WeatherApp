package com.example.havadurumuuygulamasi;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {


     EditText sehirAdi;
     TextView sonucMesaji;

     final String url ="https://api.openweathermap.org/data/2.5/weather";
     final String appid = "ba33d5d5d4e1bec00b1a748d2726721f";

    DecimalFormat decimalFormat=new DecimalFormat("#.##");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sehirAdi = (EditText)findViewById(R.id.txtInputAd);
        sonucMesaji =(TextView)findViewById(R.id.textViewSonuc);
    }

    public void btnGiris(View v){
        thread t1 = new thread();
        t1.start();

    }

//-----------------------------------------------------------------------------------------


    public class thread extends Thread {
        @Override
        public synchronized void run() {


            String tempUrl = "";
            String city = sehirAdi.getText().toString().trim();
            if (city.equals("")) {
                sonucMesaji.setText("Şehir alanını girmediniz");
            } else {
            }
            {
                tempUrl = url + "?q=" + city + "&appid=" + appid;
            }

            StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String output = "";
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                        JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);

                        JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                        double temp = jsonObjectMain.getDouble("temp") - 273.15;
                        double feelsLike = jsonObjectMain.getDouble("feels_like") - 273.15;
                        JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                        String countryName = jsonObjectSys.getString("country");
                        String cityName = jsonResponse.getString("name");
                        sonucMesaji.setTextColor(Color.rgb(68, 134, 199));
                        output += cityName + " için Günlük Hava Durumu " + " (" + countryName + ")"
                                + "\n Sıcaklık : " + decimalFormat.format(temp) + " °C"
                                + "\n Hissedilen : " + decimalFormat.format(feelsLike) + " °C";

                        sonucMesaji.setText(output);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }
}


