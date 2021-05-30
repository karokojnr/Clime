package com.example.clime.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.clime.R;
import com.example.clime.adapters.WeatherAdapter;
import com.example.clime.interfaces.VolleyResponseListener;
import com.example.clime.interfaces.WeatherByIDResponseListener;
import com.example.clime.interfaces.WeatherByNameResponseListener;
import com.example.clime.models.WeatherReportModel;
import com.example.clime.services.WeatherService;
import com.example.clime.services.WeatherServiceSingleton;

import org.json.JSONArray;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        EditText dataInput;
        ListView weatherReportsListView;
        Button btnCityId, btnWeatherById, btnWeatherByName;

        //EditText
        dataInput = findViewById(R.id.et_input);

        //ListView
//        weatherReportsListView = findViewById(R.id.lv_weather_reports);
        //Recycler view
        recyclerView = findViewById(R.id.lv_weather_reports);
        recyclerView.setHasFixedSize(true);
        //Use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
//        //Specify an adapter
//        mAdapter = new WeatherAdapter(myDataset);
//        recyclerView.setAdapter(mAdapter);

        //Buttons
        btnCityId = findViewById(R.id.btn_city_by_id);
        btnWeatherById = findViewById(R.id.btn_weather_by_id);
        btnWeatherByName = findViewById(R.id.btn_weather_by_name);

        WeatherService weatherService = new WeatherService(MainActivity.this);

        btnCityId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //String cityID = weatherService.getCityID(cityName);
                weatherService.getCityID(dataInput.getText().toString(), new VolleyResponseListener() {
                    @Override
                    public void onResponse(String cityID) {
                        Toast.makeText(MainActivity.this, "Returned an ID of: " + cityID, Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onError(String error) {
                        Toast.makeText(MainActivity.this, "Something went wrong:(", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        btnWeatherById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                weatherService.getWeatherReportByID("44418");
                weatherService.getWeatherReportByID(dataInput.getText().toString(), new WeatherByIDResponseListener() {
                    @Override
                    public void onResponse(List<WeatherReportModel> weatherReportModels) {
//                        Toast.makeText(MainActivity.this,  weatherReportModels.toString(), Toast.LENGTH_SHORT).show();
//                        ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, weatherReportModels);
//                        weatherReportsListView.setAdapter(arrayAdapter);
                        //=======================
                        //Specify an adapter
                        mAdapter = new WeatherAdapter(weatherReportModels, MainActivity.this);
                        recyclerView.setAdapter(mAdapter);

                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(MainActivity.this, "Something went wrong:(", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        btnWeatherByName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                weatherService.getWeatherReportByID("44418");
                weatherService.getWeatherReportByCityName(dataInput.getText().toString(), new WeatherByNameResponseListener() {
                    @Override
                    public void onResponse(List<WeatherReportModel> weatherReportModels) {
//                        Toast.makeText(MainActivity.this,  weatherReportModels.toString(), Toast.LENGTH_SHORT).show();
//                        ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, weatherReportModels);
//                        weatherReportsListView.setAdapter(arrayAdapter);
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(MainActivity.this, "Something went wrong:(", Toast.LENGTH_SHORT).show();
                    }
                });
//                Toast.makeText(MainActivity.this, "You clicked Me:)", Toast.LENGTH_SHORT).show();

            }
        });


    }
}