package com.haringeymobile.ukweather.data.objects;

import com.google.gson.annotations.SerializedName;

public class CityInfo {

	@SerializedName("coord")
	private Coordinates coordinates;

	@SerializedName("country")
	private String country;

	@SerializedName("id")
	private int cityId;

	@SerializedName("name")
	private String cityName;

	public Coordinates getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(Coordinates coordinates) {
		this.coordinates = coordinates;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

}
