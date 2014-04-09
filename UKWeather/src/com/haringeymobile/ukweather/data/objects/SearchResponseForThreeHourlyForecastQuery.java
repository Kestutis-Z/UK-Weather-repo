package com.haringeymobile.ukweather.data.objects;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class SearchResponseForThreeHourlyForecastQuery {

	@SerializedName("cod")
	private int code;

	@SerializedName("message")
	private String message;

	@SerializedName("city")
	private CityInfo cityInfo;

	@SerializedName("cnt")
	private int forecastCount;

	@SerializedName("list")
	private List<CityThreeHourlyWeatherForecast> threeHourlyWeatherForecasts;

	public CityInfo getCityInfo() {
		return cityInfo;
	}

	public List<CityThreeHourlyWeatherForecast> getThreeHourlyWeatherForecasts() {
		return threeHourlyWeatherForecasts;
	}

}
