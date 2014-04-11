package com.haringeymobile.ukweather.data.objects;

import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.haringeymobile.ukweather.data.objects.Temperature.TemperatureScale;

public class CityDailyWeatherForecast implements WeatherInformation {

	@SerializedName("dt")
	private long date;

	@SerializedName("clouds")
	private int cloudinessPercentage;

	@SerializedName("deg")
	private int windDirectionInDegrees;

	@SerializedName("speed")
	private double windSpeed;

	@SerializedName("humidity")
	private double humidity;

	@SerializedName("pressure")
	private double pressure;

	@SerializedName("temp")
	private Temperature temperature;

	@SerializedName("weather")
	private List<Weather> weather;

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
	public double getDayTemperature(TemperatureScale temperatureScale) {
		return temperature.getDayTemperature(temperatureScale);
	}

	@Override
	public double getHumidity() {
		return humidity;
	}

	@Override
	public double getPressure() {
		return pressure;
	}

	@Override
	public Wind getWind() {
		Wind wind = new Wind();
		wind.setDirectionInDegrees(windDirectionInDegrees);
		wind.setSpeed(windSpeed);
		return wind;
	}

	public long getDate() {
		return date;
	}

	public Temperature getTemperature() {
		return temperature;
	}

}
