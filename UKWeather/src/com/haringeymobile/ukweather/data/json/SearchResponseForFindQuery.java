package com.haringeymobile.ukweather.data.json;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class SearchResponseForFindQuery {

	@SerializedName("cod")
	private int code;

	private int count;

	private List<CityCurrentWeather> cities;

	private String message;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<CityCurrentWeather> getCities() {
		return cities;
	}

	public void setCities(List<CityCurrentWeather> cities) {
		this.cities = cities;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
