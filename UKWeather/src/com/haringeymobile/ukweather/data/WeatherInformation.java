package com.haringeymobile.ukweather.data;

public class WeatherInformation {

	private String cityName;
	private LocationCoordinates coordinates;
	private WeatherConditions weatherConditions;
	private WeatherNumericParameters weatherNumericParameters;

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public LocationCoordinates getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(LocationCoordinates coordinates) {
		this.coordinates = coordinates;
	}

	public WeatherConditions getWeatherConditions() {
		return weatherConditions;
	}

	public void setWeatherConditions(WeatherConditions weatherConditions) {
		this.weatherConditions = weatherConditions;
	}

	public WeatherNumericParameters getWeatherNumericParameters() {
		return weatherNumericParameters;
	}

	public void setWeatherNumericParameters(
			WeatherNumericParameters weatherNumericParameters) {
		this.weatherNumericParameters = weatherNumericParameters;
	}

}
