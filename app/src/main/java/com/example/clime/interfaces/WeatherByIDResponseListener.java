package com.example.clime.interfaces;

import com.example.clime.models.WeatherReportModel;

import java.util.List;

public interface WeatherByIDResponseListener {
    void onResponse(List<WeatherReportModel> weatherReportModel);
    void onError(String error);
}
