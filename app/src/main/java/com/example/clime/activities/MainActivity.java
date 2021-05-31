package com.example.clime.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        FirebaseApp.initializeApp(this);
        firebaseAuth = FirebaseAuth.getInstance();




        EditText dataInput;
        ListView weatherReportsListView;
        Button btnCityId, btnWeatherById, btnWeatherByName, btnSignOut;

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
        btnSignOut = findViewById(R.id.btn_sign_out);

        progressDialog = new ProgressDialog(this);

        WeatherService weatherService = new WeatherService(MainActivity.this);

        btnCityId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Loading Please Wait...");
                progressDialog.show();

                //String cityID = weatherService.getCityID(cityName);
                weatherService.getCityID(dataInput.getText().toString().trim(), new VolleyResponseListener() {
                    @Override
                    public void onResponse(String cityID) {
                        Toast.makeText(MainActivity.this, "Returned an ID of: " + cityID, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                    @Override
                    public void onError(String error) {
                        Toast.makeText(MainActivity.this, "Something went wrong:(", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
            }
        });
        btnWeatherById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Loading Please Wait...");
                progressDialog.show();
//                weatherService.getWeatherReportByID("44418");
                weatherService.getWeatherReportByID(dataInput.getText().toString().trim(), new WeatherByIDResponseListener() {
                    @Override
                    public void onResponse(List<WeatherReportModel> weatherReportModels) {
//                        Toast.makeText(MainActivity.this,  weatherReportModels.toString(), Toast.LENGTH_SHORT).show();
//                        ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, weatherReportModels);
//                        weatherReportsListView.setAdapter(arrayAdapter);
                        //=======================
                        //Specify an adapter
                        mAdapter = new WeatherAdapter(weatherReportModels, MainActivity.this);
                        recyclerView.setAdapter(mAdapter);
                        progressDialog.dismiss();

                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(MainActivity.this, "Something went wrong:(", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
            }
        });
        btnWeatherByName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Loading Please Wait...");
                progressDialog.show();
                // weatherService.getWeatherReportByID("44418");
                weatherService.getWeatherReportByCityName(dataInput.getText().toString().trim(), new WeatherByNameResponseListener() {
                    @Override
                    public void onResponse(List<WeatherReportModel> weatherReportModels) {
//                        Toast.makeText(MainActivity.this,  weatherReportModels.toString(), Toast.LENGTH_SHORT).show();
//                        ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, weatherReportModels);
//                        weatherReportsListView.setAdapter(arrayAdapter);
                        //Specify an adapter
                        mAdapter = new WeatherAdapter(weatherReportModels, MainActivity.this);
                        recyclerView.setAdapter(mAdapter);
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(MainActivity.this, "Something went wrong:(", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
//                Toast.makeText(MainActivity.this, "You clicked Me:)", Toast.LENGTH_SHORT).show();

            }
        });
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Signing out...");
                progressDialog.show();
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                progressDialog.dismiss();


            }
        });

    }

}