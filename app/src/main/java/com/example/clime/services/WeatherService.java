package com.example.clime.services;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.clime.interfaces.VolleyResponseListener;
import com.example.clime.interfaces.WeatherByIDResponseListener;
import com.example.clime.interfaces.WeatherByNameResponseListener;
import com.example.clime.models.WeatherReportModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeatherService {
    Context ctx;
    public WeatherService(Context ctx) {
        this.ctx = ctx;
    }
    String cityID = "";
    public void getCityID(String cityName, VolleyResponseListener volleyResponseListener){
        String url = "https://www.metaweather.com/api/location/search/?query=" + cityName;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    JSONObject cityInformation = response.getJSONObject(0);
                    cityID = cityInformation.getString("woeid");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                volleyResponseListener.onResponse(cityID);
//                Toast.makeText(ctx, response.toString(), Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(ctx, error.toString(), Toast.LENGTH_SHORT).show();
                volleyResponseListener.onError("Something went wrong:(");
            }
        });

        // Add the request to the RequestQueue.
        WeatherServiceSingleton.getInstance(ctx).addToRequestQueue(jsonArrayRequest);
    }
    public void getWeatherReportByID (String cityID, WeatherByIDResponseListener weatherByIDResponseListener){
        List<WeatherReportModel> weatherReportModelList = new ArrayList<>();
        String url = "https://www.metaweather.com/api/location/" + cityID;

        // 1. Get the JSON Object
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // 2. Get the property -> "consolidated_weather"
                try {
//                    Toast.makeText(ctx, response.toString(), Toast.LENGTH_SHORT).show();
                    JSONArray consolidated_weather_list = response.getJSONArray("consolidated_weather");

                    /// 3. Get first item in the array

                    for (int index = 0; index< consolidated_weather_list.length(); index++){
                        WeatherReportModel weatherReportModel = new WeatherReportModel();
                        JSONObject current_day = (JSONObject) consolidated_weather_list.get(index);

                        weatherReportModel.setId(current_day.getInt("id"));
                        weatherReportModel.setWeather_state_name(current_day.getString("weather_state_name"));
                        weatherReportModel.setWeather_state_abbr(current_day.getString("weather_state_abbr"));
                        weatherReportModel.setWind_direction_compass(current_day.getString("wind_direction_compass"));
                        weatherReportModel.setCreated(current_day.getString("created"));
                        weatherReportModel.setApplicable_date(current_day.getString("applicable_date"));
                        weatherReportModel.setMin_temp(current_day.getLong("min_temp"));
                        weatherReportModel.setMax_temp(current_day.getLong("max_temp"));
                        weatherReportModel.setThe_temp(current_day.getLong("the_temp"));
                        weatherReportModel.setWind_speed(current_day.getLong("wind_speed"));
                        weatherReportModel.setWind_direction(current_day.getLong("wind_direction"));
                        weatherReportModel.setAir_pressure(current_day.getInt("air_pressure"));
                        weatherReportModel.setHumidity(current_day.getInt("humidity"));
                        weatherReportModel.setVisibility(current_day.getLong("visibility"));
                        weatherReportModel.setPredictability(current_day.getInt("predictability"));
                        weatherReportModelList.add(weatherReportModel);

                    }
                    weatherByIDResponseListener.onResponse(weatherReportModelList);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(ctx, error.toString(), Toast.LENGTH_SHORT).show();
                weatherByIDResponseListener.onError("Something went wrong:(");            }
        });
        // Add the request to the RequestQueue.
        WeatherServiceSingleton.getInstance(ctx).addToRequestQueue(jsonObjectRequest);
    }
    public void getWeatherReportByCityName(String cityName, WeatherByNameResponseListener weatherByNameResponseListener){
        getCityID(cityName, new VolleyResponseListener() {
            @Override
            public void onResponse(String cityID) {
                getWeatherReportByID(cityID, new WeatherByIDResponseListener() {
                    @Override
                    public void onResponse(List<WeatherReportModel> weatherReportModel) {
                        weatherByNameResponseListener.onResponse(weatherReportModel);

                    }

                    @Override
                    public void onError(String error) {

                    }
                });
            }
            @Override
            public void onError(String error) {

            }
        });
    }
}
