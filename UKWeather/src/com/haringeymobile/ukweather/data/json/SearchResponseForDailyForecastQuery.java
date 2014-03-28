package com.haringeymobile.ukweather.data.json;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class SearchResponseForDailyForecastQuery {

	@SerializedName("city")
	private CityInfo cityInfo;

	@SerializedName("cnt")
	private int dayCount;

	@SerializedName("cod")
	private int code;

	@SerializedName("list")
	private List<CityWeatherForecast> dailyWeatherForecasts;

	@SerializedName("message")
	private String message;

	public CityInfo getCityInfo() {
		return cityInfo;
	}

	public void setCityInfo(CityInfo cityInfo) {
		this.cityInfo = cityInfo;
	}

	public int getDayCount() {
		return dayCount;
	}

	public void setDayCount(int dayCount) {
		this.dayCount = dayCount;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public List<CityWeatherForecast> getDailyWeatherForecasts() {
		return dailyWeatherForecasts;
	}

	public void setDailyWeatherForecasts(
			List<CityWeatherForecast> dailyWeatherForecasts) {
		this.dailyWeatherForecasts = dailyWeatherForecasts;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
