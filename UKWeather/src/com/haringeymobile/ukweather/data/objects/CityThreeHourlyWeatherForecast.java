package com.haringeymobile.ukweather.data.objects;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class CityThreeHourlyWeatherForecast implements WeatherInformation {

	@SerializedName("dt")
	private long date;

	@SerializedName("main")
	private NumericParameters numericParameters;

	@SerializedName("weather")
	private List<Weather> weather;

	@SerializedName("clouds")
	private Clouds clouds;

	@SerializedName("wind")
	private Wind wind;

	@SerializedName("dt_txt")
	private String dateText;

	@Override
	public String getDescription() {
		return weather.get(0).getDescription();
	}

	@Override
	public String getType() {
		return weather.get(0).getType();
	}

	@Override
	public String getIconName() {
		return weather.get(0).getIcon();
	}

	@Override
	public double getDayTemperature() {
		return numericParameters.getTemperature();
	}

	@Override
	public double getHumidity() {
		return numericParameters.getHumidity();
	}

	@Override
	public double getPressure() {
		return numericParameters.getPressure();
	}

	@Override
	public Wind getWind() {
		return wind;
	}

	public long getDate() {
		return date;
	}

}
