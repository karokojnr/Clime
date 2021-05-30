package com.example.clime.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.clime.R;
import com.example.clime.models.WeatherReportModel;

import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {
    List<WeatherReportModel> weatherReportModelList;
    Context ctx;

    public WeatherAdapter(List<WeatherReportModel> weatherReportModelList, Context ctx) {
        this.weatherReportModelList = weatherReportModelList;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_layout, parent, false);
        WeatherViewHolder weatherViewHolder = new WeatherViewHolder(view);
        return weatherViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherAdapter.WeatherViewHolder holder, int position) {

        holder.tv_weather_state_name.setText(weatherReportModelList.get(position).getWeather_state_name());
        holder.tv_weather_applicable_date.setText(weatherReportModelList.get(position).getApplicable_date());
//        Glide.with(ctx).load(weatherReportModelList.get(position).getImageURL).into(holder.weather_icon);
        Glide.with(this.ctx).load("https://image.flaticon.com/icons/png/512/4812/4812819.png").into(holder.weather_icon);

    }

    @Override
    public int getItemCount() {
        return weatherReportModelList.size();
    }


    public class WeatherViewHolder extends RecyclerView.ViewHolder {
        ImageView weather_icon;
        TextView tv_weather_state_name, tv_weather_applicable_date;

        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            weather_icon = itemView.findViewById(R.id.weather_icon);
            tv_weather_state_name = itemView.findViewById(R.id.weather_state_name);
            tv_weather_applicable_date = itemView.findViewById(R.id.weather_applicable_date);
        }
    }
}
