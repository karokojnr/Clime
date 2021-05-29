package com.example.clime.interfaces;

import com.example.clime.models.WeatherReportModel;

import java.util.List;

public interface WeatherByNameResponseListener {
    void onResponse(List<WeatherReportModel> weatherReportModels);
    void onError(String error);
}
