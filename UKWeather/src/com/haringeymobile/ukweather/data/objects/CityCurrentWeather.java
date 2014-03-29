package com.haringeymobile.ukweather.data.objects;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class CityCurrentWeather {

	@SerializedName("clouds")
	private Clouds clouds;

	@SerializedName("coord")
	private Coordinates coordinates;

	@SerializedName("dt")
	private long date;

	@SerializedName("id")
	private int cityId;

	@SerializedName("main")
	private NumericParameters numericParameters;

	@SerializedName("name")
	private String cityName;

	@SerializedName("rain")
	private Rain rain;

	@SerializedName("sys")
	private SystemParameters systemParameters;

	@SerializedName("weather")
	private List<Weather> weather;

	@SerializedName("wind")
	private Wind wind;

	public Clouds getClouds() {
		return clouds;
	}

	public void setClouds(Clouds clouds) {
		this.clouds = clouds;
	}

	public Coordinates getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(Coordinates coordinates) {
		this.coordinates = coordinates;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public NumericParameters getNumericParameters() {
		return numericParameters;
	}

	public void setNumericParameters(NumericParameters numericParameters) {
		this.numericParameters = numericParameters;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public Rain getRain() {
		return rain;
	}

	public void setRain(Rain rain) {
		this.rain = rain;
	}

	public SystemParameters getSystemParameters() {
		return systemParameters;
	}

	public void setSystemParameters(SystemParameters systemParameters) {
		this.systemParameters = systemParameters;
	}

	public List<Weather> getWeather() {
		return weather;
	}

	public void setWeather(List<Weather> weather) {
		this.weather = weather;
	}

	public Wind getWind() {
		return wind;
	}

	public void setWind(Wind wind) {
		this.wind = wind;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CityCurrentWeather [clouds=").append(clouds)
				.append(", coordinates=").append(coordinates).append(", date=")
				.append(date).append(", cityId=").append(cityId)
				.append(", numericParameters=").append(numericParameters)
				.append(", cityName=").append(cityName).append(", rain=")
				.append(rain).append(", systemParameters=")
				.append(systemParameters).append(", weather=").append(weather)
				.append(", wind=").append(wind).append("]");
		return builder.toString();
	}

}
