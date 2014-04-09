package com.haringeymobile.ukweather;

import java.util.Date;

import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.haringeymobile.ukweather.data.objects.CityDailyWeatherForecast;
import com.haringeymobile.ukweather.data.objects.Temperature;
import com.haringeymobile.ukweather.data.objects.WeatherInformation;
import com.haringeymobile.ukweather.utils.MiscMethods;

public class WeatherDailyWeatherForecastChildFragment extends
		WeatherInfoFragment {

	private TextView extraTemperaturesTextView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_daily_weather_forecast,
				container, false);
		initializeCommonViews(view);
		extraTemperaturesTextView = (TextView) view
				.findViewById(R.id.night_morning_evening_temperatures_text_view);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Bundle args = getArguments();
		String jsonString = args.getString(WeatherInfoFragment.JSON_STRING);
		Gson gson = new Gson();
		CityDailyWeatherForecast cityWeatherForecast = gson.fromJson(
				jsonString, CityDailyWeatherForecast.class);
		displayWeather(cityWeatherForecast);
	}

	@Override
	protected void displayExtraInfo(WeatherInformation weatherInformation) {
		CityDailyWeatherForecast cityDailyWeatherForecast = (CityDailyWeatherForecast) weatherInformation;
		displayTemperatureText(cityDailyWeatherForecast);
		Context context = getActivity();
		Date date = new Date(cityDailyWeatherForecast.getDate() * 1000);
		String dateString = DateFormat.getLongDateFormat(context).format(date);
		String timeString = DateFormat.getTimeFormat(context).format(date);
		extraInfoTextView.setText(dateString + "\n" + timeString + "\n"
				+ getArguments().getString(CITY_NAME));
	}

	private void displayTemperatureText(
			CityDailyWeatherForecast weatherInformation) {
		String degree = res.getString(R.string.weather_info_degree_celcius);
		CityDailyWeatherForecast weatherForecast = (CityDailyWeatherForecast) weatherInformation;
		Temperature temperature = weatherForecast.getTemperature();
		String temperatureInfo = MiscMethods.formatDoubleValue(temperature
				.getNightTemperature()) + degree;
		temperatureInfo += "\n"
				+ MiscMethods.formatDoubleValue(temperature
						.getMorningTemperature()) + degree;
		temperatureInfo += "\n"
				+ MiscMethods.formatDoubleValue(temperature
						.getEveningTemperature()) + degree;
		extraTemperaturesTextView.setText(temperatureInfo);
	}

}
