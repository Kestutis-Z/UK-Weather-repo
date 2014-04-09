package com.haringeymobile.ukweather.data.objects;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class SearchResponseForFindQuery {

	@SerializedName("cod")
	private int code;

	@SerializedName("count")
	private int count;

	@SerializedName("list")
	private List<CityCurrentWeather> cities;

	@SerializedName("message")
	private String message;

	public List<CityCurrentWeather> getCities() {
		return cities;
	}

	public int getCode() {
		return code;
	}

	public int getCount() {
		return count;
	}

}
