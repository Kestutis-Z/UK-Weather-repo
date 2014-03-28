package com.haringeymobile.ukweather.data.json;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class CityWeatherForecast {

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

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public int getCloudinessPercentage() {
		return cloudinessPercentage;
	}

	public void setCloudinessPercentage(int cloudinessPercentage) {
		this.cloudinessPercentage = cloudinessPercentage;
	}

	public int getWindDirectionInDegrees() {
		return windDirectionInDegrees;
	}

	public void setWindDirectionInDegrees(int windDirectionInDegrees) {
		this.windDirectionInDegrees = windDirectionInDegrees;
	}

	public double getWindSpeed() {
		return windSpeed;
	}

	public void setWindSpeed(double windSpeed) {
		this.windSpeed = windSpeed;
	}

	public double getHumidity() {
		return humidity;
	}

	public void setHumidity(double humidity) {
		this.humidity = humidity;
	}

	public double getPressure() {
		return pressure;
	}

	public void setPressure(double pressure) {
		this.pressure = pressure;
	}

	public Temperature getTemperature() {
		return temperature;
	}

	public void setTemperature(Temperature temperature) {
		this.temperature = temperature;
	}

	public List<Weather> getWeather() {
		return weather;
	}

	public void setWeather(List<Weather> weather) {
		this.weather = weather;
	}

}
